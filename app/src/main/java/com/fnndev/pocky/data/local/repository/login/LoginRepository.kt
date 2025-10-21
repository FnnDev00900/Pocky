package com.fnndev.pocky.data.local.repository.login

import com.fnndev.pocky.data.local.models.User

interface LoginRepository {
    suspend fun getUser(userName: String, password: String): User?

    suspend fun getUserById(userId: Int): User

    suspend fun addNewUser(user: User)
}

