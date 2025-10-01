package com.fnndev.pocky.ui.viewmodel

sealed class AccountUiEvent {
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : AccountUiEvent()
    object ShowAddTransActionDialog : AccountUiEvent()
    object HideAddTransActionDialog : AccountUiEvent()
}