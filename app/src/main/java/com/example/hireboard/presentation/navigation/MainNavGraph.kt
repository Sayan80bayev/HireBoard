package com.example.hireboard.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.DeleteVacancyUseCase
import com.example.hireboard.domain.usecase.GetAllActiveVacanciesUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.domain.usecase.GetVacancyUseCase
import com.example.hireboard.domain.usecase.UpdateVacancyUseCase
import com.example.hireboard.presentation.screens.main.MainScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyCreationScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyDetailsEmployeeScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyDetailsScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyUpdateScreen
import com.example.hireboard.presentation.viewmodels.VacancyState
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    user: User?,
    createVacancyUseCase: CreateVacancyUseCase,
    getEmployerVacanciesUseCase: GetEmployerVacanciesUseCase,
    getVacancyUseCase: GetVacancyUseCase,
    deleteVacancyUseCase: DeleteVacancyUseCase,
    updateVacancyUseCase: UpdateVacancyUseCase,
    getAllActiveVacanciesUseCase: GetAllActiveVacanciesUseCase // Added
) {
    navigation(
        startDestination = "main_screen",
        route = AppRoutes.Main
    ) {
        composable("main_screen") {
            if (user != null) {
                val vacancyViewModel = remember {
                    VacancyViewModel(
                        currentUser = user,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
                        updateVacancyUseCase = updateVacancyUseCase,
                        getVacancyUseCase = getVacancyUseCase,
                        deleteVacancyUseCase = deleteVacancyUseCase,
                        getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase
                    )
                }

                val vacancies by vacancyViewModel.vacancies.collectAsState()

                MainScreen(
                    user = user,
                    vacancies = vacancies,
                    onVacancyClick = { vacancyId ->
                        if (user is User.Employer) {
                            navController.navigate(VacancyRoutes.vacancyDetails(vacancyId))
                        } else {
                            navController.navigate(VacancyRoutes.vacancyDetailsEmployee(vacancyId))
                        }
                    },
                    onCreateVacancyClick = {
                        navController.navigate(VacancyRoutes.VacancyCreation)
                    }
                )
            } else {
                println("There's no User")
            }
        }

        composable(VacancyRoutes.VacancyDetailsEmployee) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("vacancyId")?.toLongOrNull()
            if (id != null) {
                val vacancyViewModel = remember {
                    VacancyViewModel(
                        currentUser = user,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
                        updateVacancyUseCase = updateVacancyUseCase,
                        getVacancyUseCase = getVacancyUseCase,
                        deleteVacancyUseCase = deleteVacancyUseCase,
                        getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase
                    )
                }

                VacancyDetailsEmployeeScreen(
                    id = id,
                    viewModel = vacancyViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // Vacancy creation screen
        composable(VacancyRoutes.VacancyCreation) {
            if (user is User.Employer) {
                val vacancyViewModel = remember {
                    VacancyViewModel(
                        currentUser = user,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
                        updateVacancyUseCase = updateVacancyUseCase,
                        getVacancyUseCase = getVacancyUseCase,
                        deleteVacancyUseCase = deleteVacancyUseCase,
                        getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase
                    )
                }

                VacancyCreationScreen(
                    viewModel = vacancyViewModel,
                    onBackClick = { navController.popBackStack() },
                    onCreateClick = { vacancy ->
                        vacancyViewModel.createVacancy(vacancy)
                    },
                    onCreationSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(VacancyRoutes.VacancyDetails) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("vacancyId")?.toLongOrNull()
            if (id != null && user is User.Employer) {
                val vacancyViewModel = remember {
                    VacancyViewModel(
                        currentUser = user,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
                        updateVacancyUseCase = updateVacancyUseCase,
                        getVacancyUseCase = getVacancyUseCase,
                        deleteVacancyUseCase = deleteVacancyUseCase,
                        getAllActiveVacanciesUseCase =getAllActiveVacanciesUseCase
                    )
                }

                VacancyDetailsScreen(
                    id = id,
                    viewModel = vacancyViewModel,
                    onBackClick = { navController.popBackStack() },
                    onDeleteSuccess = {
                        navController.popBackStack()
                    },
                    onUpdateClick = { vacancyId ->
                        navController.navigate(VacancyRoutes.vacancyUpdate(vacancyId))
                    }
                )
            }
        }

        composable(VacancyRoutes.VacancyUpdate) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("vacancyId")?.toLongOrNull()
            if (id != null && user is User.Employer) {
                val vacancyViewModel = remember {
                    VacancyViewModel(
                        currentUser = user,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
                        updateVacancyUseCase = updateVacancyUseCase,
                        getVacancyUseCase = getVacancyUseCase,
                        deleteVacancyUseCase = deleteVacancyUseCase,
                        getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase
                    )
                }

                val updateState by vacancyViewModel.vacancyState.collectAsState()

                LaunchedEffect(updateState) {
                    if (updateState is VacancyState.VacancyUpdated) {
                        navController.popBackStack()
                    }
                }

                VacancyUpdateScreen(
                    id = id,
                    viewModel = vacancyViewModel,
                    onBackClick = { navController.popBackStack() },
                    onUpdateClick = { vacancy ->
                        vacancyViewModel.updateVacancy(vacancy)
                    },
                    onUpdateSuccess = {
                    }
                )
            }
        }

    }
}
