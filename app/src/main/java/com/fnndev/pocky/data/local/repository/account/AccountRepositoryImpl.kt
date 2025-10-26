package com.fnndev.pocky.data.local.repository.account

import com.fnndev.pocky.data.local.database.dao.BankAccountDao
import com.fnndev.pocky.data.local.database.dao.TransactionDao
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.models.TransactionType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val bankAccountDao: BankAccountDao,
    private val transactionDao: TransactionDao
) : AccountRepository {
    override fun getAllBanks(): Flow<List<BankAccount>> {
        return bankAccountDao.getAllBankAccounts()
    }

    override suspend fun getBankById(id: Int): BankAccount {
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

    override fun getTransactionsByBankAccountId(bankAccountId: Int): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByBankAccountId(bankAccountId)
    }

    override suspend fun getTransactionById(id: Int): Transaction? {
        return transactionDao.getTransactionById(id)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val oldTransaction = transactionDao.getTransactionById(transaction.id)

        if (oldTransaction!!.bankAccountId == transaction.bankAccountId) {
            // Account has not changed
            val account = bankAccountDao.getBankAccountById(transaction.bankAccountId)

            // Revert old transaction effect
            val balanceAfterRevert = when (oldTransaction.type) {
                TransactionType.INCOME -> account.balance - oldTransaction.amount
                TransactionType.EXPENSE -> account.balance + oldTransaction.amount
            }

            // Apply new transaction effect
            val finalBalance = when (transaction.type) {
                TransactionType.INCOME -> balanceAfterRevert + transaction.amount
                TransactionType.EXPENSE -> balanceAfterRevert - transaction.amount
            }

            bankAccountDao.updateBankAccount(account.copy(balance = finalBalance))
        } else {
            // Account has changed
            // Revert from old account
            val oldAccount = bankAccountDao.getBankAccountById(oldTransaction.bankAccountId)
            val oldAccountNewBalance = when (oldTransaction.type) {
                TransactionType.INCOME -> oldAccount.balance - oldTransaction.amount
                TransactionType.EXPENSE -> oldAccount.balance + oldTransaction.amount
            }
            bankAccountDao.updateBankAccount(oldAccount.copy(balance = oldAccountNewBalance))

            // Apply to new account
            val newAccount = bankAccountDao.getBankAccountById(transaction.bankAccountId)
            val newAccountNewBalance = when (transaction.type) {
                TransactionType.INCOME -> newAccount.balance + transaction.amount
                TransactionType.EXPENSE -> newAccount.balance - transaction.amount
            }
            bankAccountDao.updateBankAccount(newAccount.copy(balance = newAccountNewBalance))
        }

        transactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        val account = bankAccountDao.getBankAccountById(transaction.bankAccountId)
        val newBalance = when (transaction.type) {
            TransactionType.INCOME -> account.balance - transaction.amount
            TransactionType.EXPENSE -> account.balance + transaction.amount
        }
        bankAccountDao.updateBankAccount(account.copy(balance = newBalance))
        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun insertTransaction(transaction: Transaction): Boolean {
        val account = bankAccountDao.getBankAccountById(transaction.bankAccountId)

        if (transaction.type == TransactionType.EXPENSE && account.balance < transaction.amount) {
            // Not enough balance, do not register transaction.
            return false
        }

        transactionDao.insertTransaction(transaction)
        val newBalance = when (transaction.type) {
            TransactionType.INCOME -> account.balance + transaction.amount
            TransactionType.EXPENSE -> account.balance - transaction.amount
        }
        bankAccountDao.updateBankAccount(account.copy(balance = newBalance))
        return true
    }

    override suspend fun deleteTransactionsByBankAccountId(bankAccountId: Int) {
        transactionDao.deleteTransactionsByBankId(bankAccountId)
    }
}
