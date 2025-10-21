package com.fnndev.pocky.data.local.repository.login

import com.fnndev.pocky.data.local.database.dao.LoginDao
import com.fnndev.pocky.data.local.models.User
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginDao: LoginDao) : LoginRepository {
    override suspend fun getUser(
        userName: String,
        password: String
    ): User? {
        return loginDao.getUser(userName, password)
    }

    override suspend fun getUserById(userId: Int): User {
        return loginDao.getUserById(userId)
    }

    override suspend fun addNewUser(user: User) {
        loginDao.addNewUser(user)
    }
}