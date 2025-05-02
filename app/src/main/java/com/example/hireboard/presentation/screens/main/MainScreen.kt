package com.example.hireboard.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.presentation.components.BottomNavBar
import com.example.hireboard.presentation.screens.vacancy.VacancyScreen
import com.example.hireboard.ui.theme.HireBoardTheme

@Composable
fun MainScreen(
    user: User,
    vacancies: List<Vacancy> = emptyList(),
    onVacancyClick: (Long) -> Unit = {},
    onCreateVacancyClick: () -> Unit = {},
    onNavItemSelected: (Int) -> Unit = {}
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
                // Employer viewing vacancies tab
                isEmployer && selectedItem == 1 -> {
                    VacancyScreen(
                        vacancies = vacancies,
                        onVacancyClick = onVacancyClick,
                        onCreateVacancyClick = onCreateVacancyClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                // Default welcome screen for other cases
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Добро пожаловать, ${if (isEmployer) "работодатель" else "работник"}!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenEmployeePreview() {
    HireBoardTheme {
        MainScreen(
            user = User.Employee(
                id = 1,
                name = "Test Employee",
                email = "employee@test.com",
                passwordHash = "",
                phone = "",
                skills = "",
                experience = "",
                education = ""
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenEmployerPreview() {
    HireBoardTheme(darkTheme = true) {
        MainScreen(
            user = User.Employer(
                id = 1,
                name = "Test Employer",
                email = "employer@test.com",
                passwordHash = "",
                phone = "",
                companyName = "Test Company",
                companyDescription = ""
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenEmployerVacanciesPreview() {
    val testVacancies = listOf(
        Vacancy(
            id = 1L,
            employerId = 1L,
            title = "Android Developer",
            description = "Looking for experienced Android developer",
            salary = "250,000 KZT",
            experienceRequired = "3+ years",
            skillsRequired = "Kotlin, Jetpack Compose",
            location = "Almaty",
            postDate = System.currentTimeMillis(),
            isActive = true
        )
    )

    HireBoardTheme(darkTheme = true) {
        MainScreen(
            user = User.Employer(
                id = 1,
                name = "Test Employer",
                email = "employer@test.com",
                passwordHash = "",
                phone = "",
                companyName = "Test Company",
                companyDescription = ""
            ),
            vacancies = testVacancies,
        )
    }
}