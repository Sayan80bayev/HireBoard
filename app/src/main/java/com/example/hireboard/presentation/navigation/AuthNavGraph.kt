package com.example.hireboard.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import com.example.hireboard.presentation.screens.auth.*
import com.example.hireboard.presentation.viewmodels.AuthViewModel
import com.example.hireboard.presentation.viewmodels.factory.AuthViewModelFactory

object AuthScreens {
    const val Login = "login"
    const val RoleSelection = "role_selection"
    const val RegisterEmployer = "register_employer"
    const val RegisterEmployee = "register_employee"
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginUseCase: LoginUseCase,
    registerEmployeeUseCase: RegisterEmployeeUseCase,
    registerEmployerUseCase: RegisterEmployerUseCase,
    onAuthSuccess: () -> Unit
) {
    val authViewModelFactory = AuthViewModelFactory(
        loginUseCase,
        registerEmployeeUseCase,
        registerEmployerUseCase
    )

    composable(AuthScreens.Login) {
        val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

        LoginScreen(
            viewModel = authViewModel,
            onLoginSuccess = onAuthSuccess,
            onSignUpClick = {
                navController.navigate(AuthScreens.RoleSelection)
            }
        )
    }

    composable(AuthScreens.RoleSelection) {
        RoleSelectionScreen(
            onRoleSelected = { role ->
                when (role.lowercase()) {
                    "employer" -> navController.navigate(AuthScreens.RegisterEmployer)
                    "employee" -> navController.navigate(AuthScreens.RegisterEmployee)
                }
            }
        )
    }

    composable(AuthScreens.RegisterEmployer) {
        val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

        RegisterEmployerScreen(
            viewModel = authViewModel,
            onRegisterSuccess = onAuthSuccess
        )
    }

    composable(AuthScreens.RegisterEmployee) {
        val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

        RegisterEmployeeScreen(
            viewModel = authViewModel,
            onRegisterSuccess = onAuthSuccess
        )
    }
}