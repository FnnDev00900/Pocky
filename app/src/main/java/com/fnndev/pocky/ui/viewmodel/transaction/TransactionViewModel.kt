package com.fnndev.pocky.ui.viewmodel.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.repository.account.AccountRepository
import com.fnndev.pocky.navigation.ScreenRoute
import com.fnndev.pocky.ui.screens.transaction.transaction_list.TransactionEvent
import com.fnndev.pocky.ui.screens.transaction.transaction_list.TransactionUiState
import com.fnndev.pocky.ui.utils.UiEvent
import com.fnndev.pocky.ui.utils.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: AccountRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val _transactionState = MutableStateFlow(TransactionUiState())
    val transactionState: StateFlow<TransactionUiState> = _transactionState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var bankId = savedStateHandle.get<Int>("bankId")

    private var deletedTransaction: Transaction? = null

    init {
        if (bankId != -1 && bankId != null) {
            observeTransactionsByBankAccountId(bankId!!)
        }
    }

    fun onEvent(event: TransactionEvent) {
        when (event) {
            TransactionEvent.OnAddReceiptClicked -> {
                sendUiEvent(Navigate(ScreenRoute.AddEditTransactionScreen.route + "/$bankId"))
            }

            is TransactionEvent.OnTransactionClicked -> {
                sendUiEvent(
                    Navigate(
                        ScreenRoute.AddEditTransactionScreen.route +
                                "/$bankId?transactionId=${event.transaction.id}"
                    )
                )
            }

            is TransactionEvent.OnTransactionDeleteClicked -> {
                viewModelScope.launch {
                    try {
                        deletedTransaction = event.transaction
                        repository.deleteTransaction(event.transaction)
                        sendUiEvent(ShowSnackBar(message = "رسید حذف شد", action = "بازگردانی"))
                    } catch (e: Exception) {
                        _transactionState.value = _transactionState.value.copy(error = e.message)
                    }
                }
            }

            is TransactionEvent.OnSearchQueryChanged -> {
                _transactionState.value = _transactionState.value.copy(
                    searchQuery = event.query,
                    filteredTransactionList = _transactionState.value.listTransaction.filter {
                        it.date.contains(event.query, ignoreCase = true)
                    }
                )
            }

            TransactionEvent.OnUndoDeleteClick -> {
                if (deletedTransaction != null) {
                    viewModelScope.launch {
                        repository.insertTransaction(deletedTransaction!!)
                        deletedTransaction = null
                    }
                }
            }

            TransactionEvent.OnReportClick -> {
                sendUiEvent(Navigate(ScreenRoute.ReportTransactionScreen.route))
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = _transactionState.value.listTransaction.filter {
            it.date.contains(query, ignoreCase = true)
        }
        _transactionState.value = _transactionState.value.copy(
            searchQuery = query,
            filteredTransactionList = filtered
        )
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun observeTransactionsByBankAccountId(accountId: Int) {
        viewModelScope.launch {
            repository.getTransactionsByBankAccountId(accountId)
                .onStart {
                    _transactionState.value = _transactionState.value.copy(isLoading = true)
                }
                .catch { e ->
                    _transactionState.value = _transactionState.value.copy(error = e.message)
                }
                .collect { list ->
                    _transactionState.value = _transactionState.value.copy(
                        listTransaction = list,
                        filteredTransactionList = list,
                        isLoading = false
                    )
                }
        }
    }
}