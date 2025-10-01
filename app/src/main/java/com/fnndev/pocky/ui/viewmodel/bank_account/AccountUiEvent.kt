package com.fnndev.pocky.ui.viewmodel.bank_account

sealed class AccountUiEvent {
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : AccountUiEvent()
    object ShowAddTransActionDialog : AccountUiEvent()
    object HideAddTransActionDialog : AccountUiEvent()
}