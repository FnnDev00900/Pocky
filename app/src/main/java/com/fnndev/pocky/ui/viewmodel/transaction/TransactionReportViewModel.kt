package com.fnndev.pocky.ui.viewmodel.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.repository.account.AccountRepository
import com.fnndev.pocky.ui.screens.transaction.transaction_report.TransactionReportEvent
import com.fnndev.pocky.ui.screens.transaction.transaction_report.TransactionReportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionReportViewModel @Inject constructor(
    private val repository: AccountRepository,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val _state = MutableStateFlow(TransactionReportState())
    val state = _state.asStateFlow()

    init {
        getAllBanks()
    }

    fun onEvent(event: TransactionReportEvent) {
        when (event) {
            is TransactionReportEvent.OnStartDateChange -> {
                _state.value = _state.value.copy(startDate = event.startDate)
            }

            is TransactionReportEvent.OnEndDateChange -> {
                _state.value = _state.value.copy(endDate = event.endDate)
            }

            is TransactionReportEvent.OnCreatePdfClick -> {

            }

            is TransactionReportEvent.OnShowReportClick -> {
                getTransactionsByDate(
                    startDate = _state.value.startDate,
                    endDate = _state.value.endDate,
                    bankId = event.bankId
                )
            }
            is TransactionReportEvent.OnBankSelected -> {
                _state.value = _state.value.copy(selectedBank = event.bank)
            }
        }
    }

    private fun getAllBanks() {
        viewModelScope.launch {
            repository.getAllBanks()
                .onStart {
                    _state.value = _state.value.copy(isLoading = true)
                }
                .catch {
                    _state.value =
                        _state.value.copy(isLoading = false, error = it.message.toString())
                }
                .collect {
                    _state.value = _state.value.copy(
                        bankAccounts = it,
                        isLoading = false
                    )
                }
        }
    }

    private fun getTransactionsByDate(startDate: String, endDate: String, bankId: Int) {
        if (startDate.isNotEmpty() && endDate.isNotEmpty() && bankId != -1) {
            viewModelScope.launch {
                repository.getTransactionsByDate(
                    startDate = startDate,
                    endDate = endDate,
                    bankAccountId = bankId
                )
                    .onStart {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    .catch {
                        _state.value =
                            _state.value.copy(isLoading = false, error = it.message.toString())
                    }
                    .collect {
                        _state.value = _state.value.copy(
                            transactionList = it,
                            isSuccess = true,
                            isLoading = false
                        )
                    }
            }
        }
    }
}