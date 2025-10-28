package com.fnndev.pocky.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.repository.login.LoginRepository
import com.fnndev.pocky.navigation.ScreenRoute
import com.fnndev.pocky.ui.screens.login.login.LoginScreenEvent
import com.fnndev.pocky.ui.screens.login.login.LoginScreenState
import com.fnndev.pocky.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState = _loginState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _listUsers = repository.getAllUsers()
    val listUsers = _listUsers

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            LoginScreenEvent.OnLoginClicked -> {
                loginToApp()
            }

            is LoginScreenEvent.OnPasswordChanged -> {
                _loginState.value = _loginState.value.copy(password = event.password)
            }

            LoginScreenEvent.OnRegisterClicked -> {
                _loginState.value = _loginState.value.copy(registerSheet = true)
            }

            is LoginScreenEvent.OnUserNameChanged -> {
                _loginState.value = _loginState.value.copy(username = event.userName)
            }

            is LoginScreenEvent.RegisterSheet -> {
                _loginState.value = _loginState.value.copy(registerSheet = event.isShow)
            }

            LoginScreenEvent.OnFingerprintLoginClicked -> {
                viewModelScope.launch {
                    sendUiEvent(
                        UiEvent.Navigate(
                            route = ScreenRoute.ListBankScreen.route,
                            popUpTo = ScreenRoute.LoginScreen.route,
                            inclusive = true
                        )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    private fun loginToApp() {

        val userName = _loginState.value.username
        val password = _loginState.value.password

        if (userName.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                val user = repository.getUser(userName, password)
                if (user != null) {
                    _loginState.value = _loginState.value.copy(isSuccess = true)
                    sendUiEvent(
                        UiEvent.Navigate(
                            route = ScreenRoute.ListBankScreen.route,
                            popUpTo = ScreenRoute.LoginScreen.route,
                            inclusive = true
                        )
                    )
                } else {
                    _loginState.value = _loginState.value.copy(
                        isSuccess = false,
                        errorMessage = "نام کاربری یا رمز عبور اشتباه است"
                    )
                }
            }
        } else {
            _loginState.value = _loginState.value.copy(
                isSuccess = false,
                errorMessage = "نام کاربری و رمز عبور را وارد کنید"
            )
        }
    }
}