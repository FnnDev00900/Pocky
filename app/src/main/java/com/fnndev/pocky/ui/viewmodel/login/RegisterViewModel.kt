package com.fnndev.pocky.ui.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.models.User
import com.fnndev.pocky.data.local.repository.login.LoginRepository
import com.fnndev.pocky.ui.screens.login.LoginScreenState
import com.fnndev.pocky.ui.screens.login.RegisterEvent
import com.fnndev.pocky.ui.screens.login.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()
    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.OnConfirmPasswordChange -> _registerState.value =
                _registerState.value.copy(confirmPassword = event.password)

            is RegisterEvent.OnPasswordChange -> _registerState.value =
                _registerState.value.copy(password = event.password)

            is RegisterEvent.OnUserNameChange -> _registerState.value =
                _registerState.value.copy(userName = event.userName)

            RegisterEvent.OnRegisterClick -> {
                if (isValidateRegister()) {
                    val newUser = User(
                        userName = _registerState.value.userName,
                        password = _registerState.value.password
                    )
                    registerUser(newUser)
                    _registerState.value = _registerState.value.copy(
                        userName = "",
                        password = "",
                        confirmPassword = "",
                        errorMessage = "",
                        isSuccess = true
                    )
                }
            }
        }
    }

    private fun registerUser(user: User) {
        viewModelScope.launch {
            repository.addNewUser(user)
        }
    }

    private fun isValidateRegister(): Boolean {
        val userName = _registerState.value.userName
        val password = _registerState.value.password
        val confirmPassword = _registerState.value.confirmPassword

        if (userName.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                return true
            } else {
                _registerState.value = _registerState.value.copy(
                    errorMessage = "رمز وارد شده یکسان نمی باشد"
                    , isSuccess = false
                )
                return false
            }
        } else {
            _registerState.value = _registerState.value.copy(
                errorMessage = "اطلاعات را وارد کنید"
                , isSuccess = false
            )
            return false
        }
    }
}