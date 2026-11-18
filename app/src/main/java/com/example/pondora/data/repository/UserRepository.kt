package com.example.pondora.data.repository

import com.example.pondora.data.db.dao.UserDao
import com.example.pondora.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUser(): Flow<UserEntity?> =
        userDao.getUser()

    suspend fun insertUser(user: UserEntity) =
        userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) =
        userDao.updateUser(user)

    suspend fun addXp(amount: Int) =
        userDao.addXp(amount)

    suspend fun addDuck() =
        userDao.addDuck()

    suspend fun updateLevel(level: Int) =
        userDao.updateLevel(level)
}