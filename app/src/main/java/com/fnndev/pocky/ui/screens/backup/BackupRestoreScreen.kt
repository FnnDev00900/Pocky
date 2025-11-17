package com.fnndev.pocky.ui.screens.backup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.fnndev.pocky.ui.theme.BackgroundWhite
import com.fnndev.pocky.ui.theme.VazirFont
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BackupRestoreScreen(viewModel: BackupRestoreViewModel = hiltViewModel()) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is BackupRestoreViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.onEvent(BackupRestoreEvent.Backup) }) {
            Text(text = "پشتیبان گیری", fontFamily = VazirFont)
        }
        Button(onClick = { viewModel.onEvent(BackupRestoreEvent.Restore) }) {
            Text(text = "بازگردانی", fontFamily = VazirFont)
        }
    }
}