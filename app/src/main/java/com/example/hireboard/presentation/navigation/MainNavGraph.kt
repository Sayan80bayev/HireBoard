package com.example.hireboard.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.usecase.ApplyForVacancyUseCase
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.DeleteVacancyUseCase
import com.example.hireboard.domain.usecase.GetAllActiveVacanciesUseCase
import com.example.hireboard.domain.usecase.GetEmployeeApplicationsUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.domain.usecase.GetUserUseCase
import com.example.hireboard.domain.usecase.GetVacancyApplicationsUseCase
import com.example.hireboard.domain.usecase.GetVacancyUseCase
import com.example.hireboard.domain.usecase.UpdateApplicationStatusUseCase
import com.example.hireboard.domain.usecase.UpdateEmployeeProfileUseCase
import com.example.hireboard.domain.usecase.UpdateEmployerProfileUseCase
import com.example.hireboard.domain.usecase.UpdateVacancyUseCase
import com.example.hireboard.domain.usecase.WithdrawApplicationUseCase
import com.example.hireboard.presentation.screens.application.ApplicantScreen
import com.example.hireboard.presentation.screens.application.ApplicationEmployeeListScreen
import com.example.hireboard.presentation.screens.application.ApplicationListScreen
import com.example.hireboard.presentation.screens.main.MainScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyCreationScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyDetailsEmployeeScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyDetailsScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyUpdateScreen
import com.example.hireboard.presentation.viewmodels.ApplicationViewModel
import com.example.hireboard.presentation.viewmodels.UserProfileViewModel
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
    getAllActiveVacanciesUseCase: GetAllActiveVacanciesUseCase,
    applyForVacancyUseCase: ApplyForVacancyUseCase,
    getEmployeeApplicationsUseCase: GetEmployeeApplicationsUseCase,
    getVacancyApplicationsUseCase: GetVacancyApplicationsUseCase,
    updateApplicationStatusUseCase: UpdateApplicationStatusUseCase,
    withdrawApplicationUseCase: WithdrawApplicationUseCase,
    getUserUseCase: GetUserUseCase,
    updateEmployeeProfileUseCase: UpdateEmployeeProfileUseCase,
    updateEmployerProfileUseCase: UpdateEmployerProfileUseCase
) {
    navigation(
        startDestination = "main_screen",
        route = AppRoutes.Main
    ) {
        composable("main_screen") {
            if (user != null) {
                val userProfileViewModel = remember {
                    UserProfileViewModel(
                        currentUser = user,
                        getUserUseCase = getUserUseCase,
                        updateEmployeeProfileUseCase = updateEmployeeProfileUseCase,
                        updateEmployerProfileUseCase = updateEmployerProfileUseCase
                    )
                }
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
                val applicationViewModel = remember {
                    ApplicationViewModel(
                        currentUser = user,
                        applyForVacancyUseCase = applyForVacancyUseCase,
                        getEmployeeApplicationsUseCase = getEmployeeApplicationsUseCase,
                        getVacancyApplicationsUseCase = getVacancyApplicationsUseCase,
                        updateApplicationStatusUseCase = updateApplicationStatusUseCase,
                        withdrawApplicationUseCase = withdrawApplicationUseCase
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
                    applicationViewModel = applicationViewModel,
                    userProfileViewModel = userProfileViewModel,
                    vacancyViewModel = vacancyViewModel,
                    onCreateVacancyClick = {
                        navController.navigate(VacancyRoutes.VacancyCreation)
                    },
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
                val applicationViewModel = remember {
                    ApplicationViewModel(
                        currentUser = user,
                        applyForVacancyUseCase = applyForVacancyUseCase,
                        getEmployeeApplicationsUseCase = getEmployeeApplicationsUseCase,
                        getVacancyApplicationsUseCase = getVacancyApplicationsUseCase,
                        updateApplicationStatusUseCase = updateApplicationStatusUseCase,
                        withdrawApplicationUseCase = withdrawApplicationUseCase
                    )
                }

                VacancyDetailsEmployeeScreen(
                    id = id,
                    viewModel = vacancyViewModel,
                    applicationViewModel = applicationViewModel,
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
                    },
                    onViewApplicationsClick = { vacancyId ->
                        navController.navigate(ApplicationRoutes.applicationList(vacancyId))
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
        // Add this composable within the mainNavGraph function
        composable(ApplicationRoutes.ApplicationList) { backStackEntry ->
            val vacancyId = backStackEntry.arguments?.getString("vacancyId")?.toLongOrNull()
            if (vacancyId != null && user is User.Employer) {
                val applicationViewModel = remember {
                    ApplicationViewModel(
                        currentUser = user,
                        applyForVacancyUseCase = applyForVacancyUseCase,
                        getEmployeeApplicationsUseCase = getEmployeeApplicationsUseCase,
                        getVacancyApplicationsUseCase = getVacancyApplicationsUseCase,
                        updateApplicationStatusUseCase = updateApplicationStatusUseCase,
                        withdrawApplicationUseCase = withdrawApplicationUseCase
                    )
                }
                val userProfileViewModel = remember {
                    UserProfileViewModel(
                        currentUser = user,
                        getUserUseCase = getUserUseCase,
                        updateEmployeeProfileUseCase = updateEmployeeProfileUseCase,
                        updateEmployerProfileUseCase = updateEmployerProfileUseCase
                    )
                }
                ApplicationListScreen(
                    vacancyId = vacancyId,
                    applicationViewModel = applicationViewModel,
                    userProfileViewModel = userProfileViewModel,
                    onBackClick = { navController.popBackStack() },
                    onApplicationClick = {employeeId, applicationId -> navController.navigate(ApplicationRoutes.applicantScreen(employeeId, applicationId))}
                )
            }
        }
        composable(ApplicationRoutes.ApplicantScreen) { backStackEntry ->
            val employeeId = backStackEntry.arguments?.getString("employeeId")?.toLongOrNull()
            val applicationId = backStackEntry.arguments?.getString("applicationId")?.toLongOrNull()
            if (employeeId!= null && user is User.Employer && applicationId != null) {
                val applicationViewModel = remember {
                    ApplicationViewModel(
                        currentUser = user,
                        applyForVacancyUseCase = applyForVacancyUseCase,
                        getEmployeeApplicationsUseCase = getEmployeeApplicationsUseCase,
                        getVacancyApplicationsUseCase = getVacancyApplicationsUseCase,
                        updateApplicationStatusUseCase = updateApplicationStatusUseCase,
                        withdrawApplicationUseCase = withdrawApplicationUseCase
                    )
                }

                val userProfileViewModel = remember {
                    UserProfileViewModel(
                        currentUser = user,
                        getUserUseCase = getUserUseCase,
                        updateEmployeeProfileUseCase = updateEmployeeProfileUseCase,
                        updateEmployerProfileUseCase = updateEmployerProfileUseCase
                    )
                }
                ApplicantScreen(
                    applicationId,
                    employeeId,
                    applicationViewModel,
                    userProfileViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
