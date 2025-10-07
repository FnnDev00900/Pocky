package com.fnndev.pocky.ui.screens.bank_add_edit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fnndev.pocky.R
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.viewmodel.bank_account.BankAddEditViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BankAddEditScreen(
    viewModel: BankAddEditViewModel = hiltViewModel(),
    onBankSaved: () -> Unit
) {
    val lotteComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.payment))
    val lotteProgress by animateLottieCompositionAsState(
        composition = lotteComposition,
        iterations = LottieConstants.IterateForever
    )

    val state by viewModel.addEditState.collectAsState()

    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = lotteComposition,
                progress = { lotteProgress },
                modifier = Modifier.size(300.dp)
            )

            Text(
                text = if (state.id == null) "افزودن بانک" else "ویرایش بانک",
                fontFamily = VazirFont
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(BankAddEditUiEvent.OnNameChange(it)) },
                label = {
                    Text(text = "نام بانک", fontFamily = VazirFont)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = TextFieldValue(
                    text = NumberFormat.getNumberInstance(Locale.US).format(state.balance.toLongOrNull() ?: 0),
                    selection = TextRange(NumberFormat.getNumberInstance(Locale.US).format(state.balance.toLongOrNull() ?: 0).length)
                ),
                onValueChange = {input ->
                    val convertedInput = convertPersianToEnglishNumbers(input.text)
                    val digits = convertedInput.filter { it.isDigit() }
                    Log.d("00900", "BankAddEditScreen: ValueChange $convertedInput")
                    viewModel.onEvent(BankAddEditUiEvent.OnBalanceChange(digits))
                },
                label = {
                    Text(text = "موجودی", fontFamily = VazirFont)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Button(
                onClick = {
                    viewModel.onEvent(BankAddEditUiEvent.OnSaveClicked)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (state.id == null) "ثبت بانک" else "ویرایش بانک",
                    fontFamily = VazirFont
                )
            }

            if (state.error != null) {
                Text(text = state.error!!, fontFamily = VazirFont, color = ExpenseRed)
            }

            if (state.isSuccess) {
                LaunchedEffect(key1 = Unit) { onBankSaved() }
            }
        }
    }
}

private fun convertPersianToEnglishNumbers(input: String): String {
    return input
        .replace('۰', '0')
        .replace('۱', '1')
        .replace('۲', '2')
        .replace('۳', '3')
        .replace('۴', '4')
        .replace('۵', '5')
        .replace('۶', '6')
        .replace('۷', '7')
        .replace('۸', '8')
        .replace('۹', '9')
}