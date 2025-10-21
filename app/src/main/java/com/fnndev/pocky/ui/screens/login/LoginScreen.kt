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
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fnndev.pocky.R
import com.fnndev.pocky.ui.theme.VazirFont

@Composable
fun LoginScreen() {

    val lotteComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))
    val lotteProgress by animateLottieCompositionAsState(
        composition = lotteComposition,
        iterations = LottieConstants.IterateForever
    )

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
                Card(modifier = Modifier.fillMaxWidth(0.95f)) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
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
                            value = "",
                            onValueChange = {},
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
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick={}
                        ){
                            Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = "")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(R.string.str_login))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Image(imageVector = Icons.Default.Fingerprint,contentDescription = "", modifier = Modifier.size(100.dp).padding(16.dp))
                    }
                }
            }
        }
    }
}