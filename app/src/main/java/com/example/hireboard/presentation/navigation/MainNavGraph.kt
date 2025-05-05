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
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.presentation.screens.main.MainScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyCreationScreen
import com.example.hireboard.presentation.viewmodels.VacancyState
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    user: User?,
    createVacancyUseCase: CreateVacancyUseCase,
    getEmployerVacanciesUseCase: GetEmployerVacanciesUseCase
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
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase
                    )
                }

                val vacancies by vacancyViewModel.vacancies.collectAsState()

                MainScreen(
                    user = user,
                    vacancies = vacancies,
                    onVacancyClick = { vacancyId ->
                        navController.navigate(VacancyRoutes.vacancyDetails(vacancyId))
                    },
                    onCreateVacancyClick = {
                        navController.navigate(VacancyRoutes.VacancyCreation)
                    }
                )
            } else {
                println("There's no User")
            }
        }

        // Vacancy creation screen
        composable(VacancyRoutes.VacancyCreation) {
            if (user is User.Employer) {
                val vacancyViewModel = remember {
                    VacancyViewModel(
                        currentUser = user,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase
                    )
                }

                VacancyCreationScreen(
                    viewModel = vacancyViewModel,
                    onBackClick = { navController.popBackStack() },
                    onCreateClick = { vacancy ->
                        vacancyViewModel.createVacancy(vacancy)
                    },
                    onCreationSuccess = {
                        navController.popBackStack() // First pop
                    }
                )
            }
        }

        // Vacancy details screen
//        composable(VacancyRoutes.VacancyDetails) { backStackEntry ->
//            val vacancyId = backStackEntry.arguments?.getString("vacancyId")?.toLongOrNull()
//            // Here you would fetch the specific vacancy details
//            // For now just show a simple screen
//            if (vacancyId != null) {
//                VacancyDetailsScreen(
//                    vacancyId = vacancyId,
//                    onBackClick = { navController.popBackStack() }
//                )
//            }
//        }
    }
}
