package com.example.hireboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.usecase.GetUserUseCase
import com.example.hireboard.domain.usecase.UpdateEmployeeProfileUseCase
import com.example.hireboard.domain.usecase.UpdateEmployerProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val currentUser: User?,
    private val getUserUseCase: GetUserUseCase,
    private val updateEmployeeProfileUseCase: UpdateEmployeeProfileUseCase,
    private val updateEmployerProfileUseCase: UpdateEmployerProfileUseCase
) : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    private val _profileState = MutableStateFlow<UserProfileState>(UserProfileState.Idle)
    val profileState: StateFlow<UserProfileState> = _profileState

    init {
        currentUser?.id?.let { loadUserProfile(it) }
    }

    fun loadUserProfile(userId: Long) {  // Changed from currentUser.id
        viewModelScope.launch {
            _profileState.value = UserProfileState.Loading
            getUserUseCase(userId).fold(
                onSuccess = { user ->
                    _userProfile.value = user
                    _profileState.value = UserProfileState.Success
                },
                onFailure = {
                    _profileState.value = UserProfileState.Error(it.message ?: "Failed to load profile")
                }
            )
        }
    }

    fun updateEmployeeProfile(employee: User.Employee) {
        viewModelScope.launch {
            _profileState.value = UserProfileState.Loading
            updateEmployeeProfileUseCase(employee).fold(
                onSuccess = {
                    _userProfile.value = employee
                    _profileState.value = UserProfileState.ProfileUpdated
                },
                onFailure = {
                    _profileState.value = UserProfileState.Error(it.message ?: "Update failed")
                }
            )
        }
    }

    fun updateEmployerProfile(employer: User.Employer) {
        viewModelScope.launch {
            _profileState.value = UserProfileState.Loading
            updateEmployerProfileUseCase(employer).fold(
                onSuccess = {
                    _userProfile.value = employer
                    _profileState.value = UserProfileState.ProfileUpdated
                },
                onFailure = {
                    _profileState.value = UserProfileState.Error(it.message ?: "Update failed")
                    println("Update failed")
                }
            )
        }
    }

    fun resetState() {
        _profileState.value = UserProfileState.Idle
    }
}

sealed class UserProfileState {
    data object Idle : UserProfileState()
    data object Loading : UserProfileState()
    data object Success : UserProfileState()
    data object ProfileUpdated : UserProfileState()
    data class Error(val message: String) : UserProfileState()
}