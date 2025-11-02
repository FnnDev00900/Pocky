package com.fnndev.pocky.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fnndev.pocky.data.local.models.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE bankAccountId = :bankAccountId ORDER BY date DESC")
    fun getTransactionsByBankAccountId(bankAccountId: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): Transaction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions WHERE bankAccountId = :bankAccountId")
    suspend fun deleteTransactionsByBankId(bankAccountId: Int)

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate And bankAccountId = :bankAccountId ORDER BY date DESC")
    fun getTransactionsByDate(startDate: String,endDate: String,bankAccountId: Int): Flow<List<Transaction>>

}