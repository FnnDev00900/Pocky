package com.fnndev.pocky.ui.screens.login.register

data class RegisterState (
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isFingerprintEnabled: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false
)