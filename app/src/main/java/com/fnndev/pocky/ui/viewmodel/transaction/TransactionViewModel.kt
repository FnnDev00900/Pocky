package com.fnndev.pocky.ui.viewmodel.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.repository.AccountRepository
import com.fnndev.pocky.ui.screens.transaction.transaction_list.TransactionUiState
import com.fnndev.pocky.ui.utils.UiEvent
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

    init {
        val accountId = savedStateHandle.get<Int>("bankId")
        if (accountId != -1 && accountId != null) {
            observeTransactionsByBankAccountId(accountId)
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
                        isLoading = false
                    )
                }
        }
    }
}