package com.fnndev.pocky.ui.screens.transaction.transaction_add_edit

import com.fnndev.pocky.data.local.models.TransactionType

data class TransactionAddEditState(
    val transactionDate: String = "",
    val transactionAmount: String = "",
    val transactionDescription: String = "",
    val transactionType: TransactionType = TransactionType.INCOME,
)