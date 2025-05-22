package com.example.hireboard.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.presentation.components.BottomNavBar
import com.example.hireboard.presentation.screens.application.ApplicationEmployeeListScreen
import com.example.hireboard.presentation.screens.auth.LogoutScreen
import com.example.hireboard.presentation.screens.profile.EmployeeProfileScreen
import com.example.hireboard.presentation.screens.profile.EmployerProfileScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyListScreen
import com.example.hireboard.presentation.screens.vacancy.VacancyScreen
import com.example.hireboard.presentation.viewmodels.ApplicationViewModel
import com.example.hireboard.presentation.viewmodels.AuthViewModel
import com.example.hireboard.presentation.viewmodels.UserProfileViewModel
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

@Composable
fun MainScreen(
    user: User,
    vacancies: List<Vacancy> = emptyList(),
    applicationViewModel: ApplicationViewModel,
    userProfileViewModel: UserProfileViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    vacancyViewModel: VacancyViewModel,
    onVacancyClick: (Long) -> Unit = {},
    onCreateVacancyClick: () -> Unit = {},
    onNavItemSelected: (Int) -> Unit = {},
) {
    val isEmployer = user is User.Employer
    var selectedItem by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { index ->
                    selectedItem = index
                    onNavItemSelected(index)
                },
                isEmployer = isEmployer
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            when {
                selectedItem == 0 -> {
                        VacancyListScreen(
                            vacancies = vacancies,
                            onVacancyClick = onVacancyClick,
                            modifier = Modifier.fillMaxSize()
                        )
                }
                isEmployer && selectedItem == 1 -> {
                    VacancyScreen(
                        vacancies = vacancies,
                        onVacancyClick = onVacancyClick,
                        onCreateVacancyClick = onCreateVacancyClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                isEmployer && selectedItem == 2 -> {
                    EmployerProfileScreen(
                        modifier = Modifier.fillMaxSize(),
                        userProfileViewModel = userProfileViewModel
                    )
                }
                !isEmployer && selectedItem == 1 -> {
                    ApplicationEmployeeListScreen(
                        employeeId = (user as User.Employee).id,
                        applicationViewModel = applicationViewModel,
                        userProfileViewModel = userProfileViewModel,
                        vacancyViewModel = vacancyViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                !isEmployer && selectedItem == 2 -> {
                    EmployeeProfileScreen(
                        modifier = Modifier.fillMaxSize(),
                        userProfileViewModel = userProfileViewModel
                    )
                }
                else -> {
                    LogoutScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}