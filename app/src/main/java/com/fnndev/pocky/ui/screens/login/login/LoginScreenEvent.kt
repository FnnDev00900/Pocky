package com.fnndev.pocky.ui.screens.login.login

sealed class LoginScreenEvent {
    data class OnUserNameChanged(val userName: String) : LoginScreenEvent()
    data class OnPasswordChanged(val password: String) : LoginScreenEvent()
    object OnLoginClicked : LoginScreenEvent()
    object OnRegisterClicked : LoginScreenEvent()
    object OnFingerprintLoginClicked : LoginScreenEvent()
    data class RegisterSheet(val isShow: Boolean) : LoginScreenEvent()

}


