package com.fnndev.pocky.ui.screens.transaction.transaction_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.models.TransactionType
import com.fnndev.pocky.ui.theme.BackgroundGray
import com.fnndev.pocky.ui.theme.ExpenseRed
import com.fnndev.pocky.ui.theme.ExpenseRedLight
import com.fnndev.pocky.ui.theme.IncomeGreen
import com.fnndev.pocky.ui.theme.IncomeGreenLight
import com.fnndev.pocky.ui.theme.KoodakFont
import com.fnndev.pocky.ui.theme.TextPrimary
import com.fnndev.pocky.ui.theme.VazirFont
import java.text.NumberFormat

@Composable
fun TransactionScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

        }
    }
}

@Composable
fun TransactionListItem(transaction: Transaction) {
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 4.dp),
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
                            .format(transaction.amount) + "+ ريال" else transaction.amount.toString() + "- ريال",
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
            }
        }
    }
}


@Preview
@Composable
fun TransactionScreenPreview() {
    val transaction = Transaction(
        id = 1,
        bankAccountId = 1,
        date = "1404/07/15",
        amount = 2000000,
        description = "خرید مرغ برای گردش",
        type = TransactionType.EXPENSE
    )
    TransactionListItem(transaction)
}