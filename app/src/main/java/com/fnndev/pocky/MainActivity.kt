package com.fnndev.pocky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fnndev.pocky.ui.screens.bank_account.BankListScreen
import com.fnndev.pocky.ui.theme.CustomPockyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomPockyTheme {
                BankListScreen()
            }
        }
    }
}
