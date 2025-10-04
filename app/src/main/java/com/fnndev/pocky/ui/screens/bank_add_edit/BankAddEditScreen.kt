package com.fnndev.pocky.ui.screens.bank_add_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.viewmodel.bank_account.BankAddEditViewModel

@Composable
fun BankAddEditScreen(
    viewModel: BankAddEditViewModel = hiltViewModel(),
    onBankSaved: () -> Unit
) {
    val state by viewModel.addEditState.collectAsState()

    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                value = state.balance,
                onValueChange = { viewModel.onEvent(BankAddEditUiEvent.OnBalanceChange(it)) },
                label = {
                    Text(text = "موجودی", fontFamily = VazirFont)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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