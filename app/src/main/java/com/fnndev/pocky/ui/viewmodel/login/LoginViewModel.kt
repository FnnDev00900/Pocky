package com.fnndev.pocky.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import com.fnndev.pocky.ui.screens.login.LoginScreenEvent
import com.fnndev.pocky.ui.screens.login.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState = _loginState.asStateFlow()

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            LoginScreenEvent.OnLoginClicked -> TODO()
            is LoginScreenEvent.OnPasswordChanged -> {
                _loginState.value = _loginState.value.copy(password = event.password)
            }

            LoginScreenEvent.OnRegisterClicked -> TODO()
            is LoginScreenEvent.OnUserNameChanged -> {
                _loginState.value = _loginState.value.copy(username = event.userName)
            }
        }
    }

    private fun loginToApp(){

    }
}