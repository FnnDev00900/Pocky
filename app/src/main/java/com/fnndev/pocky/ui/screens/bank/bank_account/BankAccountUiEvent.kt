package com.fnndev.pocky.ui.screens.bank.bank_account

import com.fnndev.pocky.data.local.models.BankAccount

sealed class BankAccountUiEvent {
    data class SearchQueryChange(val query: String) : BankAccountUiEvent()
    data class DeleteBankAccount(val bankAccount: BankAccount) : BankAccountUiEvent()
    data class BankAccountSelected(val bankAccount: BankAccount) : BankAccountUiEvent()
    data class OnReceiptClicked(val bankAccountId: Int) : BankAccountUiEvent()
    data class ChangePassword(val currentPass: String, val newPass: String) : BankAccountUiEvent()
    class OnFingerprintClick : BankAccountUiEvent()
    object ObserveAccounts : BankAccountUiEvent()
    object AddNewBankAccountClicked : BankAccountUiEvent()
    object OnUndoDeleteClick : BankAccountUiEvent()
}