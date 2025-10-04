package com.fnndev.pocky.ui.screens.bank_account

import com.fnndev.pocky.data.local.models.BankAccount

data class AccountUiState(
    val bankAccounts: List<BankAccount> = emptyList(),
    val selectedBankAccountId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)