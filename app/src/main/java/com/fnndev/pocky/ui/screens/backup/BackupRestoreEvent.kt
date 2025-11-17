package com.fnndev.pocky.ui.screens.backup

sealed class BackupRestoreEvent {
    object Backup : BackupRestoreEvent()
    object Restore : BackupRestoreEvent()
}