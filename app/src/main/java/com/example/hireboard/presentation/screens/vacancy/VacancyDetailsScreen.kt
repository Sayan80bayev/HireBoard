package com.example.hireboard.presentation.screens.vacancy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.viewmodels.VacancyState
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailsScreen(
    id: Long,
    viewModel: VacancyViewModel,
    onBackClick: () -> Unit,
    onUpdateClick: (Long) -> Unit,
    onDeleteSuccess: () -> Unit,
    onViewApplicationsClick: (Long) -> Unit, // New parameter
    modifier: Modifier = Modifier
) {
    viewModel.getVacancyDetails(id)

    val vacancyState by viewModel.vacancyState.collectAsState()
    val selectedVacancy by viewModel.selectedVacancy.collectAsState()

    LaunchedEffect(vacancyState) {
        when (vacancyState) {
            is VacancyState.VacancyDeleted -> onDeleteSuccess()
            else -> {}
        }
    }

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
        },
        floatingActionButton = {
            selectedVacancy?.let { vacancy ->
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionButton(
                        icon = Icons.Default.Edit,
                        contentDescription = "Update",
                        onClick = { onUpdateClick(vacancy.id) }
                    )
                    ActionButton(
                        icon = Icons.Default.Delete,
                        contentDescription = "Delete",
                        onClick = {
                            viewModel.deleteVacancy(vacancy.id)
                            onDeleteSuccess()
                        }
                    )
                }
            }
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
                    VacancyDetailsContent(
                        vacancy = selectedVacancy!!,
                        onViewApplicationsClick = { onViewApplicationsClick(selectedVacancy!!.id) }
                    )
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
private fun VacancyDetailsContent(
    vacancy: Vacancy,
    onViewApplicationsClick: () -> Unit
) {
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

        Section(title = "Status") {
            Text(text = if (vacancy.isActive) "Active" else "Inactive")
        }

        Section(title = "Applications") {
            HireBoardButton(
                text = "View Applications",
                onClick = onViewApplicationsClick
            )
        }
    }
}

@Composable
fun Section(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(56.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}