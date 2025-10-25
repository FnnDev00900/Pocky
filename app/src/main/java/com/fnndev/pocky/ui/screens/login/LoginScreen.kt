package com.fnndev.pocky.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fnndev.pocky.R
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.utils.UiEvent
import com.fnndev.pocky.ui.viewmodel.login.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {

    val state = loginViewModel.loginState.collectAsState()

    val listUsers = loginViewModel.listUsers.collectAsState(emptyList()).value

    val lotteComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    val lotteProgress by animateLottieCompositionAsState(
        composition = lotteComposition,
        iterations = LottieConstants.IterateForever
    )

    RegisterSheet()

    LaunchedEffect(true) {
        loginViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navController.navigate(event.route){
                        event.popUpTo?.let {popupToRoute->
                            popUpTo(popupToRoute){
                                inclusive = event.inclusive
                            }
                        }
                    }
                }

                is UiEvent.ShowSnackBar -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = lotteComposition,
                progress = { lotteProgress },
                modifier = Modifier.size(300.dp)
            )
            CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
                var passwordVisible by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth(0.95f)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = state.value.username,
                            onValueChange = {
                                loginViewModel.onEvent(
                                    LoginScreenEvent.OnUserNameChanged(
                                        it
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(0.95f),
                            label = {
                                Text(
                                    text = stringResource(R.string.str_username),
                                    fontFamily = VazirFont
                                )
                            },
                            singleLine = true, maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Person, contentDescription = "")
                            }
                        )

                        OutlinedTextField(
                            value = state.value.password,
                            onValueChange = {
                                loginViewModel.onEvent(LoginScreenEvent.OnPasswordChanged(it))
                            },
                            modifier = Modifier.fillMaxWidth(0.95f),
                            label = {
                                Text(
                                    text = stringResource(R.string.str_password),
                                    fontFamily = VazirFont
                                )
                            },
                            singleLine = true, maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Password, contentDescription = "")
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                val description = if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = {passwordVisible = !passwordVisible}){
                                    Icon(imageVector  = image, contentDescription = description)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (listUsers.isNotEmpty()){
                                    loginViewModel.onEvent(LoginScreenEvent.OnLoginClicked)
                                }
                                else{
                                    loginViewModel.onEvent(LoginScreenEvent.OnRegisterClicked)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Login,
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(R.string.str_login))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (state.value.errorMessage != null) {
                            Text(
                                text = state.value.errorMessage!!,
                                fontFamily = VazirFont,
                                color = ExpenseRed
                            )
                        }
                    }
                }
            }
        }
    }
}