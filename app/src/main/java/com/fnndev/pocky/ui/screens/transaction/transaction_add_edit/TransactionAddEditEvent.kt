package com.fnndev.pocky.ui.screens.transaction.transaction_add_edit

sealed class TransactionAddEditEvent {
    data class OnDateChange(val date: String) : TransactionAddEditEvent()
    data class OnAmountChange(val amount: String) : TransactionAddEditEvent()
    data class OnDescriptionChange(val description: String) : TransactionAddEditEvent()
    object OnSaveClicked : TransactionAddEditEvent()
}