package com.fnndev.pocky.ui.screens.bank_account

sealed class AccountUiEvent {
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : AccountUiEvent()
    object NavigateToBankAddEditScreen : AccountUiEvent()
}