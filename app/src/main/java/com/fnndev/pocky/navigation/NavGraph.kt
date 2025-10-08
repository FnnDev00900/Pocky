package com.fnndev.pocky.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fnndev.pocky.ui.screens.bank.bank_account.BankListScreen
import com.fnndev.pocky.ui.screens.bank.bank_add_edit.BankAddEditScreen
import com.fnndev.pocky.ui.screens.transaction.transaction_list.TransactionScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, ScreenRoute.ListBankScreen.route) {
        composable(route = ScreenRoute.ListBankScreen.route) {
            BankListScreen(navController)
        }
        composable(
            route = ScreenRoute.AddEditBankScreen.route + "/{bankId}",
            arguments = listOf(
                navArgument(name = "bankId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            BankAddEditScreen(onBankSaved = { navController.popBackStack() })
        }

        composable(
            route = ScreenRoute.ListTransactionScreen.route + "/{bankId}",
            arguments = listOf(
                navArgument(name = "bankId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            TransactionScreen()
        }
    }
}