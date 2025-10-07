package com.fnndev.pocky.ui.screens.bank.bank_add_edit

sealed class BankAddEditUiEvent {
    data class OnNameChange(val name: String) : BankAddEditUiEvent()
    data class OnBalanceChange(val balance: String) : BankAddEditUiEvent()
    object OnSaveClicked : BankAddEditUiEvent()
}