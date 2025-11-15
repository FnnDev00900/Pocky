package com.fnndev.pocky.data.local.workers

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File
import java.time.LocalDateTime

class BackupDatabaseWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val dbFile = applicationContext.getDatabasePath("pocky_db.db")
            val backupDir = File(applicationContext.filesDir, "backup")

            if (!backupDir.exists()) backupDir.mkdir()

            val backupFileName = "Backup_Database.db"
            val backupFile = File(backupDir, backupFileName)

            dbFile.copyTo(backupFile,overwrite = true)

            val walFile = File(dbFile.parent,"${dbFile.name}-wal")
            val shmFile = File(dbFile.parent,"${dbFile.name}-shm")

            if (walFile.exists()) walFile.copyTo(File(backupDir, walFile.name), overwrite = true)
            if (shmFile.exists()) shmFile.copyTo(File(backupDir, shmFile.name), overwrite = true)

            Result.success()
        }catch (e: Exception){
            e.printStackTrace()
            Result.failure()
        }
    }
}