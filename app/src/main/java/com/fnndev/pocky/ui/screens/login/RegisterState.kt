package com.fnndev.pocky.ui.screens.login

data class RegisterState (
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String = "",
    val isSuccess: Boolean = false
)