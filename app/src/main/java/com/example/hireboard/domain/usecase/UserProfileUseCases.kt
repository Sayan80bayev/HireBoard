package com.example.hireboard.domain.usecase

import com.example.hireboard.data.repository.UserRepository
import com.example.hireboard.domain.model.User

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Long): Result<User> = try {
        val user = userRepository.getUserById(userId)
        if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("User not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class UpdateEmployeeProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(employee: User.Employee): Result<Unit> = try {
        val rowsUpdated = userRepository.updateUser(employee)
        if (rowsUpdated > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to update profile"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class UpdateEmployerProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(employer: User.Employer): Result<Unit> = try {
        val rowsUpdated = userRepository.updateUser(employer)
        if (rowsUpdated > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to update profile"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}