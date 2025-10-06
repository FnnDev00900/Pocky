package com.fnndev.pocky.ui.screens.bank_account

import com.fnndev.pocky.data.local.models.BankAccount

sealed class BankAccountUiEvent {
    data class SearchQueryChange(val query: String) : BankAccountUiEvent()
    data class DeleteBankAccount(val bankAccount: BankAccount) : BankAccountUiEvent()
    data class BankAccountSelected(val bankAccount: BankAccount) : BankAccountUiEvent()
    object ObserveAccounts : BankAccountUiEvent()
    object AddNewBankAccountClicked : BankAccountUiEvent()
    object OnUndoDeleteClick : BankAccountUiEvent()
}