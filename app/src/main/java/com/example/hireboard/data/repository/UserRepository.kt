package com.example.hireboard.data.repository

import com.example.hireboard.data.local.dao.UserDao
import com.example.hireboard.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    suspend fun registerEmployee(employee: User.Employee): Long = withContext(Dispatchers.IO) {
        userDao.insertUser(employee)
    }

    suspend fun registerEmployer(employer: User.Employer): Long = withContext(Dispatchers.IO) {
        userDao.insertUser(employer)
    }

    suspend fun getUserById(id: Long): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(id)
    }

    suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)
    }

    suspend fun updateUser(user: User): Int = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(id: Long): Int = withContext(Dispatchers.IO) {
        userDao.deleteUser(id)
    }
}