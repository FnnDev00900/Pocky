package com.fnndev.pocky.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bankAccountId: Int,
    val date: String,
    val amount: Double,
    val description: String,
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE
}