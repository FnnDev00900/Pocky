package com.fnndev.pocky.ui.screens.bank.bank_add_edit

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
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
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.utils.ConvertNumbers
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
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle(fontFamily = KoodakFont)
            )
            Spacer(modifier = Modifier.height(12.dp))

            BankBalanceField(
                state = state,
                onBalanceChange = {
                    viewModel.onEvent(BankAddEditUiEvent.OnBalanceChange(it))
                }
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

@Composable
fun BankBalanceField(
    state: BankAddEditState,
    onBalanceChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(state.balance) {
        val formatted = NumberFormat.getNumberInstance(Locale.US)
            .format(state.balance.toLongOrNull() ?: 0)

        if (formatted != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val english = ConvertNumbers.convertPersianToEnglishNumbers(newValue.text)
            val digits = english.filter { it.isDigit() }

            if (digits.isEmpty()) {
                textFieldValue = TextFieldValue("", TextRange(0))
                onBalanceChange("")
                return@OutlinedTextField
            }

            val formatted = NumberFormat.getNumberInstance(Locale.US).format(digits.toLong())
            val diff = formatted.length - digits.length
            val newSelection = (newValue.selection.end + diff).coerceIn(0, formatted.length)

            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(newSelection)
            )

            onBalanceChange(digits)
        },
        label = { Text("موجودی", fontFamily = VazirFont) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = TextStyle(fontFamily = KoodakFont)
    )
}