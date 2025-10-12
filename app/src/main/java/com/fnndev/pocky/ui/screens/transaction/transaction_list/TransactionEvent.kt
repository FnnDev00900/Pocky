package com.fnndev.pocky.ui.screens.transaction.transaction_list

sealed class TransactionEvent {
    object OnAddReceiptClicked : TransactionEvent()
}