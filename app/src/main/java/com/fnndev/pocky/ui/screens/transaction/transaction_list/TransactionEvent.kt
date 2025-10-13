package com.fnndev.pocky.ui.screens.transaction.transaction_list

import com.fnndev.pocky.data.local.models.Transaction

sealed class TransactionEvent {
    object OnAddReceiptClicked : TransactionEvent()
    data class OnTransactionClicked(val transaction: Transaction) : TransactionEvent()
}