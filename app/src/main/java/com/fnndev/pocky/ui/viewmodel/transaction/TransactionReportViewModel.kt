package com.fnndev.pocky.ui.viewmodel.transaction

import androidx.lifecycle.ViewModel
import com.fnndev.pocky.data.local.repository.account.AccountRepository
import com.fnndev.pocky.ui.screens.transaction.transaction_report.TransactionReportEvent
import com.fnndev.pocky.ui.screens.transaction.transaction_report.TransactionReportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionReportViewModel @Inject constructor(private val repository: AccountRepository) :
    ViewModel() {
    private val _state = MutableStateFlow(TransactionReportState())
    val state = _state.asStateFlow()

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
        }
    }
}
