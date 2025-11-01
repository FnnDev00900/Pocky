package com.fnndev.pocky.ui.screens.transaction.transaction_report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fnndev.pocky.ui.screens.transaction.transaction_list.TransactionListItem
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.viewmodel.transaction.TransactionReportViewModel
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import com.razaghimahdi.compose_persian_date.dialog.PersianLinearDatePickerDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionReportScreen(viewModel: TransactionReportViewModel = hiltViewModel()) {

    val state = viewModel.state.collectAsState()
    val listFilterTransaction = state.value.transactionList

    val coroutine = rememberCoroutineScope()
    val rememberPersianDialogDatePicker = rememberDialogDatePicker()
    val rememberPersianBottomSheetDatePickerController = rememberDialogDatePicker()
    val showDialogStartDate = remember { mutableStateOf(false) }
    val showDialogEndDate = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    var startDate: String? = null
    var endDate: String? = null

    if (showDialogStartDate.value) {
        PersianLinearDatePickerDialog(
            rememberPersianDialogDatePicker,
            Modifier.fillMaxWidth(),
            onDismissRequest = { showDialogStartDate.value = false },
            onDateChanged = { year, month, day ->
                val selectedDate = "$year/$month/$day"
                startDate = selectedDate
                viewModel.onEvent(TransactionReportEvent.OnStartDateChange(startDate = startDate))
            })
    }

    if (showDialogEndDate.value) {
        PersianLinearDatePickerDialog(
            rememberPersianDialogDatePicker,
            Modifier.fillMaxWidth(),
            onDismissRequest = { showDialogEndDate.value = false },
            onDateChanged = { year, month, day ->
                val selectedDate = "$year/$month/$day"
                endDate = selectedDate
                viewModel.onEvent(TransactionReportEvent.OnEndDateChange(endDate = endDate))
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

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val interactionSourceStartDate = remember { MutableInteractionSource() }
                val interactionSourceEndDate = remember { MutableInteractionSource() }
                val isStartDateFocused by interactionSourceStartDate.collectIsFocusedAsState()
                val isEndDateFocused by interactionSourceEndDate.collectIsFocusedAsState()

                if (isStartDateFocused) {
                    LaunchedEffect(Unit) {
                        showDialogStartDate.value = true
                    }
                }
                if (isEndDateFocused) {
                    LaunchedEffect(Unit) {
                        showDialogEndDate.value = true
                    }
                }

                Text(
                    text = "گزارش",
                    fontFamily = VazirFont,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .clickable(onClick = {
                                showDialogStartDate.value = true
                            }),
                        value = state.value.startDate,
                        onValueChange = {

                        },
                        label = {
                            Text(
                                text = "از تاریخ", fontFamily = VazirFont,
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            showDialogStartDate.value = true
                                        }
                                    ),
                            )
                        },
                        interactionSource = interactionSourceStartDate,
                        textStyle = TextStyle(fontFamily = KoodakFont),
                        readOnly = true
                    )
                    Spacer(Modifier.width(4.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .clickable(onClick = {
                                showDialogEndDate.value = true
                            }),
                        value = state.value.endDate,
                        onValueChange = {},
                        label = {
                            Text(
                                text = "تا تاریخ",
                                fontFamily = VazirFont,
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            showDialogEndDate.value = true
                                        }
                                    ),
                            )
                        },
                        interactionSource = interactionSourceEndDate,
                        textStyle = TextStyle(fontFamily = KoodakFont),
                        readOnly = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        val sDate = state.value.startDate
                        val eDate = state.value.endDate
                        viewModel.getTransactionsByDate(startDate = sDate, endDate = eDate)
                    }
                ) {
                    Text(text = "نمایش گزارش", fontFamily = VazirFont)
                }
                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "ذخیره در فایل PDF", fontFamily = VazirFont)
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(listFilterTransaction) { transaction ->
                        TransactionListItem(
                            transaction = transaction,
                            onTransactionItemClick = {},
                            onTransactionDeleteClick = {}
                        )
                    }
                }

            }
        }
    }
}