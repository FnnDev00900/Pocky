package com.fnndev.pocky.data.local.repository.login

import com.fnndev.pocky.data.local.models.User
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun getUser(userName: String, password: String): User?

    suspend fun getUserById(userId: Int): User

    suspend fun addNewUser(user: User)

    suspend fun updateUser(user: User)

    fun getAllUsers(): Flow<List<User>>
}
