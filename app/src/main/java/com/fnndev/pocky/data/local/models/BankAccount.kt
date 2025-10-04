package com.fnndev.pocky.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_accounts")
data class BankAccount(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val balance: Double = 0.0,
)

