package com.fnndev.pocky.data.local.backup

import android.content.Context
import java.io.File

class DatabaseBackupManager(private val context: Context) {

    private val dbFile = context.getDatabasePath("pocky_db.db")
    private val backupDir = File(context.filesDir, "backup")
    private val backupFileName = "Backup_Database.db"
    private val backupFile = File(backupDir, backupFileName)
    private val walFile = File(dbFile.parent, "${dbFile.name}-wal")
    private val shmFile = File(dbFile.parent, "${dbFile.name}-shm")
    private val backupWalFile = File(backupDir, walFile.name)
    private val backupShmFile = File(backupDir, shmFile.name)

    fun backup(): Boolean {
        return try {
            if (!backupDir.exists()) {
                backupDir.mkdir()
            }

            dbFile.copyTo(backupFile, overwrite = true)

            if (walFile.exists()) {
                walFile.copyTo(backupWalFile, overwrite = true)
            }
            if (shmFile.exists()) {
                shmFile.copyTo(backupShmFile, overwrite = true)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun restore(): Boolean {
        return try {
            if (!backupFile.exists()) {
                return false
            }

            // IMPORTANT: The database connection must be closed BEFORE calling this method.

            backupFile.copyTo(dbFile, overwrite = true)

            if (backupWalFile.exists()) {
                backupWalFile.copyTo(walFile, overwrite = true)
            }
            if (backupShmFile.exists()) {
                backupShmFile.copyTo(shmFile, overwrite = true)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}