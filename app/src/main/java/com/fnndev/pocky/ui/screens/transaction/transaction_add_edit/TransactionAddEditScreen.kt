package com.fnndev.pocky.ui.screens.transaction.transaction_add_edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fnndev.pocky.R
import com.fnndev.pocky.data.local.models.TransactionType
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.TextPrimary
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.utils.ConvertNumbers
import com.fnndev.pocky.ui.viewmodel.transaction.TransactionAddEditView
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import com.razaghimahdi.compose_persian_date.dialog.PersianLinearDatePickerDialog
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionAddEditScreen(
    viewModel: TransactionAddEditView = hiltViewModel(),
    onSaveClick: () -> Unit
) {
    val state = viewModel.addEditTransactionState.collectAsState()

    val focusManager = LocalFocusManager.current

    val coroutine = rememberCoroutineScope()
    val rememberPersianDialogDatePicker = rememberDialogDatePicker()
    val rememberPersianBottomSheetDatePickerController = rememberDialogDatePicker()
    val showDialog = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (showDialog.value) {
        PersianLinearDatePickerDialog(
            rememberPersianDialogDatePicker,
            Modifier.fillMaxWidth(),
            onDismissRequest = { showDialog.value = false },
            onDateChanged = { year, month, day ->
                val selectedDate = "$year/$month/$day"
                viewModel.onEvent(TransactionAddEditEvent.OnDateChange(selectedDate))
                focusManager.clearFocus()
            })
    }

    if (bottomSheetState.isVisible) {
        DatePickerLinearModalBottomSheet(
            modifier = Modifier
                .fillMaxSize(),
            sheetState = bottomSheetState,
            controller = rememberPersianBottomSheetDatePickerController,
            onDismissRequest = {
                coroutine.launch {
                    bottomSheetState.hide()
                }
            }
        )
    }

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
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isFocused by interactionSource.collectIsFocusedAsState()

                        if (isFocused) {
                            LaunchedEffect(Unit) {
                                showDialog.value = true
                            }
                        }

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = {
                                    showDialog.value = true
                                }),
                            value = state.value.transactionDate,
                            onValueChange = {},
                            label = {
                                Text(
                                    text = stringResource(R.string.str_date_transaction),
                                    fontFamily = KoodakFont
                                )
                            },
                            interactionSource = interactionSource,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 1,
                            singleLine = true,
                            readOnly = true,
                            textStyle = TextStyle(fontFamily = KoodakFont)
                        )
                        TransactionAmountField(
                            state = state.value,
                            onAmountChange = {
                                viewModel.onEvent(TransactionAddEditEvent.OnAmountChange(it))
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.value.transactionDescription,
                            onValueChange = {
                                viewModel.onEvent(TransactionAddEditEvent.OnDescriptionChange(it))
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.str_description),
                                    fontFamily = KoodakFont
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 5,
                            singleLine = false,
                            textStyle = TextStyle(fontFamily = KoodakFont)
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
                            Text(
                                text = if (state.value.transactionId != null) stringResource(R.string.str_edit) else stringResource(
                                    R.string.str_add
                                )
                            )
                        }
                        if (state.value.error != null) {
                            Text(
                                text = state.value.error!!,
                                fontFamily = VazirFont,
                                color = ExpenseRed
                            )
                        }
                    }
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

@Composable
fun TransactionAmountField(
    state: TransactionAddEditState,
    onAmountChange: (String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(state.transactionAmount) {
        val formatted = NumberFormat.getNumberInstance(Locale.US)
            .format(state.transactionAmount.toLongOrNull() ?: 0)

        if (formatted != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        }
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            val english = ConvertNumbers.convertPersianToEnglishNumbers(newValue.text)
            val digits = english.filter { it.isDigit() }

            if (digits.isEmpty()) {
                textFieldValue = TextFieldValue("", TextRange(0))
                onAmountChange("")
                return@OutlinedTextField
            }

            val formatted = NumberFormat.getNumberInstance(Locale.US).format(digits.toLong())
            val diff = formatted.length - digits.length
            val newSelection = (newValue.selection.end + diff).coerceIn(0, formatted.length)

            textFieldValue = TextFieldValue(
                text = formatted,
                selection = TextRange(newSelection)
            )
            onAmountChange(digits)
        },
        label = { Text(stringResource(R.string.str_amount), fontFamily = KoodakFont) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        textStyle = TextStyle(fontFamily = KoodakFont)
    )
}