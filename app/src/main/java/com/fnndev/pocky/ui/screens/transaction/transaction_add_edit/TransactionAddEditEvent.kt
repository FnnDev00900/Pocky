package com.fnndev.pocky.ui.screens.transaction.transaction_add_edit

import com.fnndev.pocky.data.local.models.TransactionType

sealed class TransactionAddEditEvent {
    data class OnDateChange(val date: String) : TransactionAddEditEvent()
    data class OnAmountChange(val amount: String) : TransactionAddEditEvent()
    data class OnDescriptionChange(val description: String) : TransactionAddEditEvent()
    data class OnTransactionTypeChange(val transactionType: TransactionType) : TransactionAddEditEvent()
    object OnSaveClicked : TransactionAddEditEvent()
}