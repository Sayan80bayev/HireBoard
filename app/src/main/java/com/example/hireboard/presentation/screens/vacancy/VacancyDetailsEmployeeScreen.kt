package com.example.hireboard.presentation.screens.vacancy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.viewmodels.VacancyState
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailsEmployeeScreen(
    id: Long,
    viewModel: VacancyViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.getVacancyDetails(id)

    val vacancyState by viewModel.vacancyState.collectAsState()
    val selectedVacancy by viewModel.selectedVacancy.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Vacancy Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                selectedVacancy != null -> {
                    VacancyDetailsContentEmployee(vacancy = selectedVacancy!!)
                }
                vacancyState is VacancyState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                vacancyState is VacancyState.Error -> {
                    Text(
                        text = (vacancyState as VacancyState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun VacancyDetailsContentEmployee(vacancy: Vacancy) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = vacancy.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Section(title = "Description") {
            Text(text = vacancy.description)
        }

        Section(title = "Location") {
            Text(text = vacancy.location)
        }

        Section(title = "Salary") {
            Text(text = vacancy.salary)
        }

        Section(title = "Experience Required") {
            Text(text = vacancy.experienceRequired)
        }

        Section(title = "Skills Required") {
            Text(text = vacancy.skillsRequired)
        }

        HireBoardButton(
            text = "Apply for vacancy",
            onClick = { /* TODO: Add application logic */ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}