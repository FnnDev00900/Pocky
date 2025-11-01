package com.fnndev.pocky.ui.screens.transaction.transaction_report

import com.fnndev.pocky.data.local.models.Transaction

data class TransactionReportState(
    val startDate: String = "",
    val endDate: String = "",
    val transactionList: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
