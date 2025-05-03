package com.example.hireboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.hireboard.presentation.screens.auth.LoginScreen
import com.example.hireboard.presentation.screens.auth.RegisterEmployeeScreen
import com.example.hireboard.presentation.screens.auth.RegisterEmployerScreen
import com.example.hireboard.presentation.screens.auth.RoleSelectionScreen
import com.example.hireboard.presentation.viewmodels.AuthViewModel

object AuthScreens {
    const val Login = "login"
    const val RoleSelection = "role_selection"
    const val RegisterEmployer = "register_employer"
    const val RegisterEmployee = "register_employee"
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    composable(AuthScreens.Login) {
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
        RegisterEmployerScreen(
            viewModel = authViewModel,
            onRegisterSuccess = onAuthSuccess
        )
    }

    composable(AuthScreens.RegisterEmployee) {
        RegisterEmployeeScreen(
            viewModel = authViewModel,
            onRegisterSuccess = onAuthSuccess
        )
    }
}