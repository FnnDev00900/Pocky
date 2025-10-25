package com.fnndev.pocky.ui.screens.login.register

sealed class RegisterEvent {
    data class OnUserNameChange(val userName: String): RegisterEvent()
    data class OnPasswordChange(val password: String): RegisterEvent()
    data class OnConfirmPasswordChange(val password: String): RegisterEvent()

    object OnRegisterClick: RegisterEvent()
}