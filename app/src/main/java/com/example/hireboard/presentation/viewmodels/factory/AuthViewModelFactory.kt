package com.example.hireboard.presentation.viewmodels.factory

import com.example.hireboard.presentation.viewmodels.AuthViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase

class AuthViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val registerEmployeeUseCase: RegisterEmployeeUseCase,
    private val registerEmployerUseCase: RegisterEmployerUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(
            loginUseCase,
            registerEmployeeUseCase,
            registerEmployerUseCase
        ) as T
    }
}