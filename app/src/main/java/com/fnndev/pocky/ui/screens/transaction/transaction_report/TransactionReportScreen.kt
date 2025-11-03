package com.fnndev.pocky.ui.screens.transaction.transaction_report

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.models.TransactionType
import com.fnndev.pocky.ui.theme.BackgroundWhite
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.PastelGreen
import com.fnndev.pocky.ui.theme.PastelRed
import com.fnndev.pocky.ui.theme.TextPrimary
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.utils.PdfCreator
import com.fnndev.pocky.ui.viewmodel.transaction.TransactionReportViewModel
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import com.razaghimahdi.compose_persian_date.dialog.PersianLinearDatePickerDialog
import kotlinx.coroutines.launch
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionReportScreen(viewModel: TransactionReportViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val state = viewModel.state.collectAsState()
    val listFilterTransaction = state.value.transactionList
    val bankAccounts = state.value.bankAccounts
    val selectedBank = state.value.selectedBank

    val coroutine = rememberCoroutineScope()
    val rememberPersianDialogDatePicker = rememberDialogDatePicker()
    val rememberPersianBottomSheetDatePickerController = rememberDialogDatePicker()
    val showDialogStartDate = remember { mutableStateOf(false) }
    val showDialogEndDate = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    var isExpanded by remember { mutableStateOf(false) }


    var startDate: String? = null
    var endDate: String? = null

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            uri?.let {
                val pdfCreator = PdfCreator()
                pdfCreator.generatePdf(
                    context = context,
                    listTransaction = listFilterTransaction,
                    uri = it
                )
                Toast.makeText(
                    context,
                    "فایل با موفقیت ذخیره شد",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    if (showDialogStartDate.value) {
        PersianLinearDatePickerDialog(
            rememberPersianDialogDatePicker,
            Modifier.fillMaxWidth(),
            onDismissRequest = { showDialogStartDate.value = false },
            onDateChanged = { year, month, day ->
                val formattedMonth = month.toString().padStart(2, '0')
                val formattedDay = day.toString().padStart(2, '0')
                val selectedDate = "$year-$formattedMonth-$formattedDay"
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
                val formattedMonth = month.toString().padStart(2, '0')
                val formattedDay = day.toString().padStart(2, '0')
                val selectedDate = "$year-$formattedMonth-$formattedDay"
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
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

                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedBank?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "انتخاب بانک", fontFamily = VazirFont) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        bankAccounts.forEach { bank ->
                            DropdownMenuItem(
                                text = { Text(text = bank.name, fontFamily = VazirFont) },
                                onClick = {
                                    viewModel.onEvent(TransactionReportEvent.OnBankSelected(bank))
                                    isExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        selectedBank?.let {
                            viewModel.onEvent(TransactionReportEvent.OnShowReportClick(bankId = it.id))
                        }
                    }
                ) {
                    Text(text = "نمایش گزارش", fontFamily = VazirFont)
                }
                Button(
                    onClick = {
                        createDocumentLauncher.launch("transaction_report.pdf")
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
                        TransactionReportItem(transaction = transaction)
                    }
                }

            }
        }
    }
}

@Composable
fun TransactionReportItem(transaction: Transaction) {
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundWhite)
                .padding(vertical = 2.dp, horizontal = 4.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                colors = CardDefaults.cardColors(if (transaction.type == TransactionType.INCOME) PastelGreen else PastelRed),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = transaction.type.title,
                                fontFamily = VazirFont,
                                fontSize = 18.sp
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "تاریخ:", fontFamily = VazirFont, fontSize = 18.sp)
                            Text(text = transaction.date, fontFamily = KoodakFont, fontSize = 18.sp)
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(TextPrimary)
                    ) { }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = "مبلغ:", fontFamily = VazirFont)
                        Text(
                            text = NumberFormat.getInstance().format(transaction.amount),
                            fontFamily = KoodakFont
                        )
                        Text(text = " ریال", fontFamily = VazirFont)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(TextPrimary)
                    ) { }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = "شرح:", fontFamily = VazirFont)
                        Text(text = transaction.description, fontFamily = VazirFont)
                    }
                }
            }
        }
    }
}
