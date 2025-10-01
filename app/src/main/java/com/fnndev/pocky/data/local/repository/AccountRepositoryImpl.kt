package com.fnndev.pocky.data.local.repository

import com.fnndev.pocky.data.local.database.dao.BankAccountDao
import com.fnndev.pocky.data.local.database.dao.TransactionDao
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val bankAccountDao: BankAccountDao,
    private val transactionDao: TransactionDao
) : AccountRepository {
    override fun getAllBanks(): Flow<List<BankAccount>> {
        return bankAccountDao.getAllBankAccounts()
    }

    override fun getBankById(id: Int): Flow<BankAccount?> {
        return bankAccountDao.getBankAccountById(id)
    }

    override suspend fun updateBank(bank: BankAccount) {
        bankAccountDao.updateBankAccount(bank)
    }

    override suspend fun deleteBank(bank: BankAccount) {
        bankAccountDao.deleteBankAccount(bank)
    }

    override suspend fun insertBank(bank: BankAccount) {
        bankAccountDao.insertBankAccount(bank)
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    override fun getTransactionById(id: Int): Flow<Transaction?> {
        return transactionDao.getTransactionById(id)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
}