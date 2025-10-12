package com.fnndev.pocky.ui.screens.transaction.transaction_add_edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fnndev.pocky.data.local.models.TransactionType
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.TextPrimary
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.viewmodel.transaction.TransactionAddEditView

@Composable
fun TransactionAddEditScreen(
    viewModel: TransactionAddEditView = hiltViewModel(),
    onSaveClick: () -> Unit
) {
    val state = viewModel.addEditTransactionState.collectAsState()

    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(width = 1.dp, color = TextPrimary)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.value.transactionDate,
                            onValueChange = {
                                viewModel.onEvent(TransactionAddEditEvent.OnDateChange(it))
                            },
                            label = {
                                Text(text = "تاریخ", fontFamily = KoodakFont)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 1,
                            singleLine = true
                        )
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.value.transactionAmount,
                            onValueChange = {
                                viewModel.onEvent(TransactionAddEditEvent.OnAmountChange(it))
                            },
                            label = {
                                Text(text = "مبلغ", fontFamily = KoodakFont)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 1,
                            singleLine = true
                        )
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.value.transactionDescription,
                            onValueChange = {
                                viewModel.onEvent(TransactionAddEditEvent.OnDescriptionChange(it))
                            },
                            label = {
                                Text(text = "شرح", fontFamily = KoodakFont)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 5,
                            singleLine = false
                        )
                        TransactionTypeSelector(
                            selectedType = state.value.transactionType,
                            onTypeSelected = {
                                viewModel.onEvent(TransactionAddEditEvent.OnTransactionTypeChange(it))
                            }
                        )

                        Button(onClick = {
                            viewModel.onEvent(TransactionAddEditEvent.OnSaveClicked)
                        }) {
                            Text(text = "ذخیره")
                        }
                    }
                }
                if (state.value.error != null) {
                    Text(text = state.value.error!!, fontFamily = VazirFont)
                }
                if (state.value.isSuccess) {
                    onSaveClick()
                }
            }
        }
    }
}

@Composable
fun TransactionTypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TransactionType.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .clickable(onClick = { onTypeSelected(option) })
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                RadioButton(
                    selected = option == selectedType,
                    onClick = { onTypeSelected(option) }
                )
                Text(text = option.title)
            }
        }
    }
}