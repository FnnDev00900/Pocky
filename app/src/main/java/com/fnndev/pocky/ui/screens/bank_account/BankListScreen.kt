package com.fnndev.pocky.ui.screens.bank_account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.navigation.ScreenRoute
import com.fnndev.pocky.ui.theme.VazirFont
import com.fnndev.pocky.ui.viewmodel.bank_account.BankAccountViewModel

@Composable
fun BankListScreen(
    navController: NavController,
    viewModel: BankAccountViewModel = hiltViewModel()

) {
    val uiState by viewModel.accountUiState.collectAsState()

    when {
        uiState.isLoading -> {
            CircularProgressIndicator()
        }

        uiState.error != null -> {
            Text(text = uiState.error!!)
        }

        else -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(ScreenRoute.AddEditBankScreen.route + "/-1")
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            value = uiState.searchQuery,
                            onValueChange = viewModel::onSearchQueryChanged,
                            label = { Text("جست‌وجوی حساب") }
                        )
                    }

                    items(uiState.filteredBankList) {
                        BankItem(
                            bank = it,
                            onClick = {
                                viewModel.selectAccount(it.id)
                                navController.navigate(ScreenRoute.AddEditBankScreen.route + "/${it.id}")
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun BankItem(bank: BankAccount, onClick: () -> Unit) {
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = bank.name, fontFamily = VazirFont, fontSize = 24.sp)
                Text(text = bank.balance.toString(), fontFamily = VazirFont, fontSize = 24.sp)
            }
        }
    }
}