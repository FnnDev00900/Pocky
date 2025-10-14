package com.fnndev.pocky.ui.screens.transaction.transaction_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.models.TransactionType
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.ExpenseRedLight
import com.fnndev.pocky.ui.theme.IncomeGreen
import com.fnndev.pocky.ui.theme.IncomeGreenLight
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.TextPrimary
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.utils.UiEvent
import com.fnndev.pocky.ui.viewmodel.transaction.TransactionViewModel
import java.text.NumberFormat

@Composable
fun TransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = hiltViewModel()
) {

    val uiState by viewModel.transactionState.collectAsState()
    val transactionList = uiState.filteredTransactionList
    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    focusManager.clearFocus()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TransactionEvent.OnAddReceiptClicked)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Receipt")
            }
        }
    ) { paddingValues ->
        CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                        label = { Text("جست‌وجوی تاریخ") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(transactionList) { transaction ->
                        TransactionListItem(
                            transaction = transaction,
                            onTransactionItemClick = {
                                viewModel.onEvent(TransactionEvent.OnTransactionClicked(it))
                            },
                            onTransactionDeleteClick = {
                                viewModel.onEvent(TransactionEvent.OnTransactionDeleteClicked(it))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(
    transaction: Transaction,
    onTransactionItemClick: (Transaction) -> Unit,
    onTransactionDeleteClick: (Transaction) -> Unit
) {
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 4.dp)
                .clickable(onClick = {
                    onTransactionItemClick(transaction)
                }),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(width = 2.dp, color = TextPrimary),
            color = if (transaction.type == TransactionType.INCOME) IncomeGreenLight else ExpenseRedLight,
            contentColor = TextPrimary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("تاریخ:", fontFamily = VazirFont)
                    Text(text = transaction.date, fontFamily = KoodakFont)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("مبلغ:", fontFamily = VazirFont)
                    Text(
                        text = if (transaction.type == TransactionType.INCOME) NumberFormat.getInstance()
                            .format(transaction.amount) + "+ ريال" else NumberFormat.getInstance()
                            .format(transaction.amount) + "- ريال",
                        color = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed,
                        fontFamily = KoodakFont
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("شرح:", fontFamily = VazirFont)
                    Text(text = transaction.description, fontFamily = VazirFont)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        onTransactionDeleteClick(transaction)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Receipt"
                        )
                    }
                }
            }
        }
    }
}