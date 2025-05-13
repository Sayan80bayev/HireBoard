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
import com.example.hireboard.presentation.screens.vacancy.VacancyListScreen
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
                    VacancyListScreen(
                        vacancies = vacancies,
                        onVacancyClick = onVacancyClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }
    }
}
