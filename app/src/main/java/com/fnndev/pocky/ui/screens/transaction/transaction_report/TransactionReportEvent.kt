package com.fnndev.pocky.ui.screens.transaction.transaction_report

sealed class TransactionReportEvent {
    data class OnStartDateChange(val startDate: String) : TransactionReportEvent()
    data class OnEndDateChange(val endDate: String) : TransactionReportEvent()
    object OnCreatePdfClick : TransactionReportEvent()
}