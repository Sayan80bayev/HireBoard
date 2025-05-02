package com.example.hireboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginUseCase: LoginUseCase,
    registerEmployeeUseCase: RegisterEmployeeUseCase,
    registerEmployerUseCase: RegisterEmployerUseCase,
    onAuthSuccess: () -> Unit
) {
    NavHost(navController = navController, startDestination = AppRoutes.Auth) {
        navigation(
            startDestination = AuthScreens.Login,
            route = AppRoutes.Auth
        ) {
            authNavGraph(
                navController = navController,
                loginUseCase = loginUseCase,
                registerEmployeeUseCase = registerEmployeeUseCase,
                registerEmployerUseCase = registerEmployerUseCase,
                onAuthSuccess = onAuthSuccess
            )
        }
    }
}