package com.fnndev.pocky.navigation

sealed class ScreenRoute(val route: String) {
    object LoginScreen : ScreenRoute("loginScreen")
    object ListBankScreen : ScreenRoute("list_bank_screen")
    object AddEditBankScreen : ScreenRoute("add_edit_bank_screen")

    object ListTransactionScreen : ScreenRoute("list_transaction_screen")
    object AddEditTransactionScreen : ScreenRoute("add_edit_transaction_screen")
}