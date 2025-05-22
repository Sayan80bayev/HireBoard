package com.example.hireboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.example.hireboard.domain.usecase.ApplyForVacancyUseCase
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.DeleteVacancyUseCase
import com.example.hireboard.domain.usecase.GetAllActiveVacanciesUseCase
import com.example.hireboard.domain.usecase.GetEmployeeApplicationsUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.domain.usecase.GetUserUseCase
import com.example.hireboard.domain.usecase.GetVacancyApplicationsUseCase
import com.example.hireboard.domain.usecase.GetVacancyUseCase
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import com.example.hireboard.domain.usecase.UpdateApplicationStatusUseCase
import com.example.hireboard.domain.usecase.UpdateEmployeeProfileUseCase
import com.example.hireboard.domain.usecase.UpdateEmployerProfileUseCase
import com.example.hireboard.domain.usecase.UpdateVacancyUseCase
import com.example.hireboard.domain.usecase.WithdrawApplicationUseCase
import com.example.hireboard.presentation.viewmodels.AuthState
import com.example.hireboard.presentation.viewmodels.AuthViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginUseCase: LoginUseCase,
    registerEmployeeUseCase: RegisterEmployeeUseCase,
    registerEmployerUseCase: RegisterEmployerUseCase,
    createVacancyUseCase: CreateVacancyUseCase,
    getEmployerVacanciesUseCase: GetEmployerVacanciesUseCase,
    getVacancyUseCase: GetVacancyUseCase,
    deleteVacancyUseCase: DeleteVacancyUseCase,
    updateVacancyUseCase: UpdateVacancyUseCase,
    getAllActiveVacanciesUseCase: GetAllActiveVacanciesUseCase,
    applyForVacancyUseCase: ApplyForVacancyUseCase,
    getEmployeeApplicationsUseCase: GetEmployeeApplicationsUseCase,
    getVacancyApplicationsUseCase: GetVacancyApplicationsUseCase,
    updateApplicationStatusUseCase: UpdateApplicationStatusUseCase,
    withdrawApplicationUseCase: WithdrawApplicationUseCase,
    getUserUseCase: GetUserUseCase,
    updateEmployeeProfileUseCase: UpdateEmployeeProfileUseCase,
    updateEmployerProfileUseCase: UpdateEmployerProfileUseCase,
    onAuthSuccess: () -> Unit = {}
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
            user = currentUser,
            createVacancyUseCase = createVacancyUseCase,
            getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
            navController = navController,
            getVacancyUseCase = getVacancyUseCase,
            deleteVacancyUseCase = deleteVacancyUseCase,
            updateVacancyUseCase = updateVacancyUseCase,
            getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase,
            applyForVacancyUseCase = applyForVacancyUseCase,
            getEmployeeApplicationsUseCase = getEmployeeApplicationsUseCase,
            getVacancyApplicationsUseCase = getVacancyApplicationsUseCase,
            updateApplicationStatusUseCase = updateApplicationStatusUseCase,
            withdrawApplicationUseCase = withdrawApplicationUseCase,
            getUserUseCase = getUserUseCase,
            updateEmployeeProfileUseCase = updateEmployeeProfileUseCase,
            updateEmployerProfileUseCase = updateEmployerProfileUseCase
        )
    }
}