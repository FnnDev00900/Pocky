package com.fnndev.pocky.data.local.workers

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fnndev.pocky.data.local.database.AccountDatabase
import java.io.File

class RestoreDatabaseWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val dbFile = applicationContext.getDatabasePath("pocky_db.db")
            val backupDir = File(applicationContext.filesDir, "backup")
            val backupFile = File(backupDir, "Backup_Database.db")

            if (!backupFile.exists()) return Result.failure()

            // دیتابیس را ببند تا کرش نشود
            Room.databaseBuilder(applicationContext, AccountDatabase::class.java, "pocky_db")
                .build()
                .close()

            // جایگزینی فایل بکاپ
            backupFile.copyTo(dbFile, overwrite = true)

            // جایگزینی فایل‌های جانبی WAL
            val walFile = File(backupDir, "${dbFile.name}-wal")
            val shmFile = File(backupDir, "${dbFile.name}-shm")
            if (walFile.exists()) walFile.copyTo(File(dbFile.parent, walFile.name), overwrite = true)
            if (shmFile.exists()) shmFile.copyTo(File(dbFile.parent, shmFile.name), overwrite = true)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
