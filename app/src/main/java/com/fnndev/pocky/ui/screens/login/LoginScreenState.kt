package com.fnndev.pocky.ui.screens.login

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)