package com.example.hireboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerEmployeeUseCase: RegisterEmployeeUseCase,
    private val registerEmployerUseCase: RegisterEmployerUseCase
) : ViewModel() {

    // Existing state flow for UI
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // New direct user storage
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = loginUseCase(email, password)
            _authState.value = result.fold(
                onSuccess = { user ->
                    _currentUser.value = user  // Store the user
                    println("Login success! User: $user")
                    AuthState.Success(user)
                },
                onFailure = {
                    println("Login error: ${it.message}")
                    AuthState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    fun registerEmployee(
        name: String,
        email: String,
        password: String,
        phone: String,
        skills: String,
        experience: String,
        education: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val employee = User.Employee(
                id = 0L,
                name = name,
                email = email,
                passwordHash = password,
                phone = phone,
                skills = skills,
                experience = experience,
                education = education
            )
            val result = registerEmployeeUseCase(employee)
            _authState.value = result.fold(
                onSuccess = { newId ->
                    val savedEmployee = employee.copy(id = newId)
                    _currentUser.value = savedEmployee  // Store the user
                    println("Employee registered! User: $savedEmployee")
                    AuthState.Success(savedEmployee)
                },
                onFailure = {
                    println("Registration error: ${it.message}")
                    AuthState.Error(it.message ?: "Registration failed")
                }
            )
        }
    }

    fun registerEmployer(
        companyName: String,
        companyDescription: String,
        email: String,
        password: String,
        phone: String,
        contactName: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val employer = User.Employer(
                id = 0L,
                name = contactName,
                email = email,
                passwordHash = password,
                phone = phone,
                companyName = companyName,
                companyDescription = companyDescription
            )
            val result = registerEmployerUseCase(employer)
            _authState.value = result.fold(
                onSuccess = { newId ->
                    val savedEmployer = employer.copy(id = newId)
                    _currentUser.value = savedEmployer  // Store the user
                    println("Employer registered! User: $savedEmployer")
                    AuthState.Success(savedEmployer)
                },
                onFailure = {
                    println("Registration error: ${it.message}")
                    AuthState.Error(it.message ?: "Registration failed")
                }
            )
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
        // Don't reset currentUser here - it should persist until logout
    }

    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}