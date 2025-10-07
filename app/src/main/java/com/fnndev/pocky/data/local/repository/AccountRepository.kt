package com.fnndev.pocky.data.local.repository

import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAllBanks(): Flow<List<BankAccount>>
    suspend fun getBankById(id: Int): BankAccount
    suspend fun updateBank(bank: BankAccount)
    suspend fun deleteBank(bank: BankAccount)
    suspend fun insertBank(bank: BankAccount)

    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByBankAccountId(bankAccountId: Int): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Int): Transaction
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun insertTransaction(transaction: Transaction)
}