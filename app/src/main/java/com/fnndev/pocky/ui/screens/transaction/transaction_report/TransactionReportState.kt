package com.fnndev.pocky.ui.screens.transaction.transaction_report

import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction

data class TransactionReportState(
    val startDate: String = "",
    val endDate: String = "",
    val bankAccounts: List<BankAccount> = emptyList(),
    val selectedBank: BankAccount? = null,
    val transactionList: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)