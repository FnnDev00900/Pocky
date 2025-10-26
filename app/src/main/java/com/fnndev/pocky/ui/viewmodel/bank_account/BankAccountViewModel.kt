package com.fnndev.pocky.ui.viewmodel.bank_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.repository.account.AccountRepository
import com.fnndev.pocky.data.local.repository.login.LoginRepository
import com.fnndev.pocky.navigation.ScreenRoute
import com.fnndev.pocky.ui.screens.bank.bank_account.AccountUiState
import com.fnndev.pocky.ui.screens.bank.bank_account.BankAccountUiEvent
import com.fnndev.pocky.ui.utils.UiEvent
import com.fnndev.pocky.ui.utils.UiEvent.Navigate
import com.fnndev.pocky.ui.utils.UiEvent.ShowSnackBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankAccountViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val loginRepository: LoginRepository
) :
    ViewModel() {

    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState: StateFlow<AccountUiState> = _accountUiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedBankAccount: BankAccount? = null
    private var listDeletedTransactions: List<Transaction> = emptyList()

    init {
        observeAccounts()
    }

    fun observeAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllBanks()
                .onStart { _accountUiState.update { state -> state.copy(isLoading = true) } }
                .catch { exception -> _accountUiState.update { state -> state.copy(error = exception.message) } }
                .collect { accountList ->
                    _accountUiState.update { state ->
                        state.copy(
                            bankAccounts = accountList,
                            filteredBankList = accountList,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onEvent(event: BankAccountUiEvent) {
        when (event) {
            BankAccountUiEvent.AddNewBankAccountClicked -> {
                sendUiEvent(Navigate(ScreenRoute.AddEditBankScreen.route + "/-1"))
            }

            is BankAccountUiEvent.BankAccountSelected -> {
                sendUiEvent(Navigate(ScreenRoute.AddEditBankScreen.route + "/${event.bankAccount.id}"))
            }

            is BankAccountUiEvent.DeleteBankAccount -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        deletedBankAccount = event.bankAccount
                        listDeletedTransactions = repository.getTransactionsByBankAccountId(event.bankAccount.id).first()
                        repository.deleteBank(event.bankAccount)
                        repository.deleteTransactionsByBankAccountId(event.bankAccount.id)
                        sendUiEvent(
                            ShowSnackBar(
                                message = "بانک حذف شد",
                                action = "بازگردانی"
                            )
                        )
                    } catch (e: Exception) {
                        _accountUiState.value = _accountUiState.value.copy(error = e.message)
                    }
                }
            }

            BankAccountUiEvent.ObserveAccounts -> {
                observeAccounts()
            }

            is BankAccountUiEvent.SearchQueryChange -> {
                _accountUiState.value = _accountUiState.value.copy(
                    searchQuery = event.query,
                    filteredBankList = _accountUiState.value.filteredBankList.filter {
                        it.name.contains(event.query, ignoreCase = true)
                    }
                )
            }

            BankAccountUiEvent.OnUndoDeleteClick -> {
                deletedBankAccount?.let { bank ->
                    viewModelScope.launch {
                        repository.insertBank(bank)
                        listDeletedTransactions.forEach {
                            repository.insertTransaction(it)
                        }
                        deletedBankAccount = null
                        listDeletedTransactions = emptyList()
                    }
                }
            }

            is BankAccountUiEvent.OnReceiptClicked -> {
                sendUiEvent(Navigate(ScreenRoute.ListTransactionScreen.route + "/${event.bankAccountId}"))
            }

            is BankAccountUiEvent.ChangePassword -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val user = loginRepository.getAllUsers().first().firstOrNull()

                    if (user == null) {
                        sendUiEvent(ShowSnackBar(message = "کاربری یافت نشد"))
                        return@launch
                    }

                    if (event.newPass.isBlank()) {
                        sendUiEvent(ShowSnackBar(message = "رمز عبور جدید نمی‌تواند خالی باشد"))
                        return@launch
                    }

                    if (user.password == event.currentPass) {
                        val updatedUser = user.copy(password = event.newPass)
                        loginRepository.updateUser(updatedUser)
                        sendUiEvent(ShowSnackBar(message = "رمز عبور با موفقیت تغییر کرد"))
                    } else {
                        sendUiEvent(ShowSnackBar(message = "رمز عبور فعلی اشتباه است"))
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = _accountUiState.value.bankAccounts.filter {
            it.name.contains(query.trim(), ignoreCase = true)
        }
        _accountUiState.value = _accountUiState.value.copy(
            searchQuery = query,
            filteredBankList = filtered
        )
    }
}