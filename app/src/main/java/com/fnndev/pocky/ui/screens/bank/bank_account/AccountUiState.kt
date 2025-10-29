package com.fnndev.pocky.ui.screens.bank.bank_account

import com.fnndev.pocky.data.local.models.BankAccount

data class AccountUiState(
    val bankAccounts: List<BankAccount> = emptyList(),
    val filteredBankList: List<BankAccount> = emptyList(),
    val searchQuery: String = "",
    val selectedBankAccountId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val fingerprint: Boolean = false
)