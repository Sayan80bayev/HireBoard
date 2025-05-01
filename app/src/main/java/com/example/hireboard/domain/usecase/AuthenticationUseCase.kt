package com.example.hireboard.domain.usecase

import com.example.hireboard.data.repository.UserRepository
import com.example.hireboard.domain.model.User

class RegisterEmployeeUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(employee: User.Employee): Result<Long> = try {
        val existingUser = userRepository.getUserByEmail(employee.email)
        if (existingUser != null) {
            Result.failure(Exception("Email already in use"))
        } else {
            val userId = userRepository.registerEmployee(employee)
            Result.success(userId)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class RegisterEmployerUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(employer: User.Employer): Result<Long> = try {
        val existingUser = userRepository.getUserByEmail(employer.email)
        if (existingUser != null) {
            Result.failure(Exception("Email already in use"))
        } else {
            val userId = userRepository.registerEmployer(employer)
            Result.success(userId)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, passwordHash: String): Result<User> = try {
        val user = userRepository.getUserByEmail(email)
        when {
            user == null -> Result.failure(Exception("User not found"))
            user.passwordHash != passwordHash -> Result.failure(Exception("Invalid password"))
            else -> Result.success(user)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}