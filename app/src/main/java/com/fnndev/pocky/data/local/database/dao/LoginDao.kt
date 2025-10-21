package com.fnndev.pocky.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fnndev.pocky.data.local.models.User

@Dao
interface LoginDao {
    @Query("SELECT * FROM user_table WHERE username = :userName AND password = :password")
    suspend fun getUser(userName: String, password: String): User?

    @Query("SELECT * FROM user_table WHERE id = :userId")
    suspend fun getUserById(userId: Int): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewUser(user: User)
}