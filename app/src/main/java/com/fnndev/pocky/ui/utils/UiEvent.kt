package com.fnndev.pocky.ui.utils

sealed class UiEvent {
    data class ShowSnackBar(val message: String, val action: String? = null) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NavigateUp : UiEvent()
}