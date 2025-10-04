package com.fnndev.pocky.ui.screens.bank_add_edit

data class BankAddEditState(
    val id: Int? = null,
    val name: String = "",
    val balance: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)