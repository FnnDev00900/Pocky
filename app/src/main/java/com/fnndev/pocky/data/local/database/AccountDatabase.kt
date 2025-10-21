package com.fnndev.pocky.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fnndev.pocky.data.local.database.dao.BankAccountDao
import com.fnndev.pocky.data.local.database.dao.LoginDao
import com.fnndev.pocky.data.local.database.dao.TransactionDao
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction

@Database(
    entities = [BankAccount::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class AccountDatabase: RoomDatabase() {
    abstract val bankAccountDao: BankAccountDao
    abstract val transactionDao: TransactionDao

    abstract val loginDao: LoginDao
}