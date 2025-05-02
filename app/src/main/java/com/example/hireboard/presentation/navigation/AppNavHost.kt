package com.example.hireboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import com.example.hireboard.presentation.viewmodels.AuthState
import com.example.hireboard.presentation.viewmodels.AuthViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginUseCase: LoginUseCase,
    registerEmployeeUseCase: RegisterEmployeeUseCase,
    registerEmployerUseCase: RegisterEmployerUseCase,
    onAuthSuccess: () -> Unit = {} // Make optional with empty default
) {
    val authViewModel = remember {
        AuthViewModel(
            loginUseCase,
            registerEmployeeUseCase,
            registerEmployerUseCase
        )
    }

    val authState by authViewModel.authState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(AppRoutes.Main) {
                    popUpTo(AppRoutes.Auth) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Auth
    ) {
        navigation(
            startDestination = AuthScreens.Login,
            route = AppRoutes.Auth
        ) {
            authNavGraph(
                navController = navController,
                authViewModel = authViewModel,
                onAuthSuccess = onAuthSuccess
            )
        }

        mainNavGraph(
            user = currentUser // Use the stored user
        )
    }
}