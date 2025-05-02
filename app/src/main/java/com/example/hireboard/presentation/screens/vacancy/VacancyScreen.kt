package com.example.hireboard.presentation.screens.vacancy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.R
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.presentation.components.VacancyItemCard
import com.example.hireboard.ui.theme.HireBoardTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyScreen(
    vacancies: List<Vacancy>,
    onVacancyClick: (Long) -> Unit,
    onCreateVacancyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateVacancyClick,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Создать вакансию"
                )
            }
        }
    ) { innerPadding ->
        if (vacancies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Вакансии отсутствуют",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(vacancies) { vacancy ->
                    VacancyItemCard(
                        vacancy = vacancy,
                        onClick = { onVacancyClick(vacancy.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VacancyScreenPreviewWithItems() {
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
        ),
        Vacancy(
            id = 2L,
            employerId = 1L,
            title = "Backend Engineer",
            description = "Node.js developer needed",
            salary = "300,000 KZT",
            experienceRequired = "5+ years",
            skillsRequired = "Node.js, PostgreSQL",
            location = "Remote",
            postDate = System.currentTimeMillis() - 86400000,
            isActive = true
        )
    )

    HireBoardTheme(darkTheme = true) {
        VacancyScreen(
            vacancies = testVacancies,
            onVacancyClick = {},
            onCreateVacancyClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VacancyScreenPreviewEmpty() {
    HireBoardTheme(darkTheme = true) {
        VacancyScreen(
            vacancies = emptyList(),
            onVacancyClick = {},
            onCreateVacancyClick = {}
        )
    }
}