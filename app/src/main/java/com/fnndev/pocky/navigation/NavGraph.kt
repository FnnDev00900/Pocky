package com.fnndev.pocky.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fnndev.pocky.ui.screens.bank.bank_account.BankListScreen
import com.fnndev.pocky.ui.screens.bank.bank_add_edit.BankAddEditScreen
import com.fnndev.pocky.ui.screens.login.login.LoginScreen
import com.fnndev.pocky.ui.screens.transaction.transaction_add_edit.TransactionAddEditScreen
import com.fnndev.pocky.ui.screens.transaction.transaction_list.TransactionScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, ScreenRoute.LoginScreen.route) {

        composable(route = ScreenRoute.LoginScreen.route) {
            LoginScreen(navController)
        }

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
            TransactionScreen(navController)
        }

        composable(
            route = ScreenRoute.AddEditTransactionScreen.route + "/{bankId}?transactionId={transactionId}",
            arguments = listOf(
                navArgument(name = "transactionId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument(name = "bankId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            TransactionAddEditScreen(onSaveClick = { navController.popBackStack()})
        }
    }
}