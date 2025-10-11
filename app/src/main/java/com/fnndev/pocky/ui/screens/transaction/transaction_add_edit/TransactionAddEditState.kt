package com.fnndev.pocky.ui.screens.transaction.transaction_add_edit

import com.fnndev.pocky.data.local.models.TransactionType

data class TransactionAddEditState(
    val transactionId: Int? = null,
    val bankId: Int? = null,
    val transactionDate: String = "",
    val transactionAmount: String = "",
    val transactionDescription: String = "",
    val transactionType: TransactionType = TransactionType.INCOME,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)