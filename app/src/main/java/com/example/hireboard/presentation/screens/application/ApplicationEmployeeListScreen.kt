package com.example.hireboard.presentation.screens.application

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.User
import com.example.hireboard.presentation.viewmodels.ApplicationState
import com.example.hireboard.presentation.viewmodels.ApplicationViewModel
import com.example.hireboard.presentation.viewmodels.UserProfileViewModel
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationEmployeeListScreen(
    employeeId: Long,
    applicationViewModel: ApplicationViewModel,
    userProfileViewModel: UserProfileViewModel,
    vacancyViewModel: VacancyViewModel,
    modifier: Modifier = Modifier
) {
    val applications by applicationViewModel.applications.collectAsState()
    val appState by applicationViewModel.applicationState.collectAsState()
    val currentUser by userProfileViewModel.userProfile.collectAsState()
    val vacancies by vacancyViewModel.vacancies.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(employeeId) {
        applicationViewModel.loadEmployeeApplications(employeeId)
    }

    LaunchedEffect(appState) {
        when (appState) {
            is ApplicationState.ApplicationWithdrawn -> {
                Toast.makeText(context, "Application withdrawn successfully", Toast.LENGTH_SHORT).show()
                applicationViewModel.resetState()
                applicationViewModel.loadEmployeeApplications(employeeId)
            }
            is ApplicationState.Error -> {
                Toast.makeText(context, (appState as ApplicationState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when (appState) {
                is ApplicationState.Loading -> CircularProgressIndicator()
                is ApplicationState.Error -> Text(
                    text = (appState as ApplicationState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(applications) { application ->
                        var showDeleteDialog by remember { mutableStateOf(false) }

                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeleteDialog = false },
                                title = { Text("Withdraw Application") },
                                text = { Text("Are you sure you want to withdraw this application?") },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            showDeleteDialog = false
                                            applicationViewModel.withdrawApplication(application.id)
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDeleteDialog = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                },
                                properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
                            )
                        }

                        ApplicationEmployeeCard(
                            application = application,
                            employee = currentUser as? User.Employee,
                            onDeleteClick = { showDeleteDialog = true },
                            vacancyViewModel = vacancyViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationEmployeeCard(
    application: Application,
    employee: User.Employee?,
    onDeleteClick: () -> Unit,
    vacancyViewModel: VacancyViewModel
) {
    val vacancies by vacancyViewModel.vacancies.collectAsState()
    val vacancyTitle = remember(vacancies, application.vacancyId) {
        vacancies.find { it.id == application.vacancyId }?.title ?: "Unknown Position"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Position: $vacancyTitle",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Application"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            employee?.let {
                Text("Email: ${it.email}")
            }

            Text("Cover Letter: ${application.coverLetter}")
            Text("Status: ${application.status}")
        }
    }
}