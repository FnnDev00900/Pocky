package com.fnndev.pocky.ui.screens.transaction.transaction_list

import com.fnndev.pocky.data.local.models.Transaction

data class TransactionUiState (
    val listTransaction: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


