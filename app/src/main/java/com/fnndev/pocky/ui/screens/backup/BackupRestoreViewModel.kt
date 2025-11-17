package com.fnndev.pocky.ui.screens.backup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.backup.DatabaseBackupManager
import com.fnndev.pocky.data.local.database.AccountDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupRestoreViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: AccountDatabase
) : ViewModel() {

    private val backupManager = DatabaseBackupManager(context)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: BackupRestoreEvent) {
        when (event) {
            BackupRestoreEvent.Backup -> backupDatabase()
            BackupRestoreEvent.Restore -> restoreDatabase()
        }
    }

    private fun backupDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val success = backupManager.backup()
            if (success) {
                _eventFlow.emit(UiEvent.ShowToast("پشتیبان گیری با موفقیت انجام شد"))
            } else {
                _eventFlow.emit(UiEvent.ShowToast("خطا در پشتیبان گیری"))
            }
        }
    }

    private fun restoreDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            if (db.isOpen) {
                db.close()
            }
            val success = backupManager.restore()
            if (success) {
                _eventFlow.emit(UiEvent.ShowToast("بازیابی با موفقیت انجام شد. لطفاً برنامه را مجدداً راه اندازی کنید."))
            } else {
                _eventFlow.emit(UiEvent.ShowToast("خطا در بازیابی"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }
}