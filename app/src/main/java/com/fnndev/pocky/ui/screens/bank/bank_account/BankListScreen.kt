package com.fnndev.pocky.ui.screens.bank.bank_account

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fnndev.pocky.R
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.navigation.ScreenRoute
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.SurfaceWhite
import com.fnndev.pocky.ui.theme.TextSecondary
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.utils.UiEvent
import com.fnndev.pocky.ui.viewmodel.bank_account.BankAccountViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat

@SuppressLint("UnrememberedMutableState")
@Composable
fun BankListScreen(
    navController: NavController,
    viewModel: BankAccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.accountUiState.collectAsState()
    val bankList = uiState.filteredBankList

    val snackBarHostState = remember { SnackbarHostState() }
    val scopeSnackBar = rememberCoroutineScope()

    val lotteComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bank_list))
    val lotteProgress by animateLottieCompositionAsState(
        composition = lotteComposition,
        iterations = LottieConstants.IterateForever
    )

    val focusManager = LocalFocusManager.current

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navController.navigate(event.route)
                is UiEvent.ShowSnackBar -> {
                    scopeSnackBar.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        val result = snackBarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(BankAccountUiEvent.OnUndoDeleteClick)
                        }
                    }
                }
            }
        }
    }


    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Text(text = uiState.error!!)
        }

        else -> {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = { focusManager.clearFocus() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(ScreenRoute.AddEditBankScreen.route + "/-1")
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }, snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
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
                                label = { Text("جست‌وجوی حساب") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LottieAnimation(
                                composition = lotteComposition,
                                progress = { lotteProgress },
                                modifier = Modifier.size(300.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(text = "لیست حساب ها", fontSize = 22.sp, fontFamily = VazirFont)
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                        ) {
                            items(bankList) { bank ->
                                BankItem(
                                    bank = bank,
                                    onClick = {
                                        viewModel.onEvent(
                                            BankAccountUiEvent.BankAccountSelected(
                                                bank
                                            )
                                        )
                                    },
                                    onDelete = {
                                        viewModel.onEvent(BankAccountUiEvent.DeleteBankAccount(bank))
                                    },
                                    onReceipt = {
                                        viewModel.onEvent(
                                            BankAccountUiEvent.OnReceiptClicked(
                                                it
                                            )
                                        )
                                        Log.d("00900", "BankListScreen: Item =${it}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BankItem(bank: BankAccount, onClick: () -> Unit, onDelete: () -> Unit, onReceipt: (bankId: Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete()
                            showDialog = false
                        }
                    ) {
                        Text(text = "آره", fontFamily = VazirFont, color = ExpenseRed)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = "نه", fontFamily = VazirFont)
                    }
                },
                title = {
                    Text(text = "حذف حساب", fontFamily = VazirFont)
                },
                text = {
                    Text("آیا حساب ${bank.name} حذف شود", fontFamily = VazirFont)
                }
            )
        }
    }

    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            onClick = onClick,
            colors = CardDefaults.cardColors(SurfaceWhite)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .weight(0.7f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = "حساب:" + bank.name, fontFamily = VazirFont, fontSize = 20.sp)
                        Text(
                            text = "موجودی:" + NumberFormat.getInstance()
                                .format(bank.balance) + " ریال",
                            fontFamily = KoodakFont,
                            fontSize = 18.sp,
                            color = TextSecondary
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ExpenseRed,
                        modifier = Modifier
                            .clickable(onClick = { showDialog = true })
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = { onReceipt(bank.id) }
                    ) {
                        Icon(imageVector = Icons.Default.Add, "Add")
                        Text(text = "افزودن رسید", fontFamily = VazirFont)
                    }
                }
            }
        }
    }
}

