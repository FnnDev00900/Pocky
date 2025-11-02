package com.fnndev.pocky.ui.screens.transaction.transaction_report

import com.fnndev.pocky.data.local.models.BankAccount

sealed class TransactionReportEvent {
    data class OnStartDateChange(val startDate: String) : TransactionReportEvent()
    data class OnEndDateChange(val endDate: String) : TransactionReportEvent()
    data class OnShowReportClick(val bankId: Int) : TransactionReportEvent()
    data class OnBankSelected(val bank: BankAccount) : TransactionReportEvent()
    object OnCreatePdfClick : TransactionReportEvent()
}