package com.fnndev.pocky.ui.viewmodel

import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction

data class AccountUiState(
    val bankAccounts: List<BankAccount> = emptyList(),
    val selectedBankAccountId: Int? = null,
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)