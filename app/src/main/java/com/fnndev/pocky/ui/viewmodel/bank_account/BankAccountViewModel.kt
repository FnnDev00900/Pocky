package com.fnndev.pocky.ui.viewmodel.bank_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.repository.AccountRepository
import com.fnndev.pocky.ui.screens.bank_account.AccountUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankAccountViewModel @Inject constructor(private val repository: AccountRepository) :
    ViewModel() {

    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState: StateFlow<AccountUiState> = _accountUiState.asStateFlow()

    init {
        observeAccounts()
    }

    fun observeAccounts() {
        viewModelScope.launch {
            repository.getAllBanks()
                .onStart { _accountUiState.update { state -> state.copy(isLoading = true) } }
                .catch { exception -> _accountUiState.update { state -> state.copy(error = exception.message) } }
                .collect { accountList ->
                    _accountUiState.update { state ->
                        state.copy(
                            bankAccounts = accountList,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun selectAccount(accountId: Int) {
        _accountUiState.update { state -> state.copy(selectedBankAccountId = accountId) }
    }

    fun clearError() {
        _accountUiState.update { state -> state.copy(error = null) }
    }
}