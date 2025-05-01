package com.example.hireboard.data.local.dao

import com.example.hireboard.domain.model.User

interface UserDao {
    fun insertUser(user: User): Long
    fun getUserById(id: Long): User?
    fun getUserByEmail(email: String): User?
    fun updateUser(user: User): Int
    fun deleteUser(id: Long): Int
}