package com.example.hireboard.presentation.screens.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.ApplicationStatus
import com.example.hireboard.domain.model.User
import com.example.hireboard.presentation.viewmodels.ApplicationState
import com.example.hireboard.presentation.viewmodels.ApplicationViewModel
import com.example.hireboard.presentation.viewmodels.UserProfileState
import com.example.hireboard.presentation.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantScreen(
    applicationId: Long,
    employeeId: Long,
    applicationViewModel: ApplicationViewModel,
    userProfileViewModel: UserProfileViewModel,
    onBackClick: () -> Unit
) {
    val userProfile by userProfileViewModel.userProfile.collectAsState()
    val profileState by userProfileViewModel.profileState.collectAsState()
    val appState by applicationViewModel.applicationState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val currentUser = applicationViewModel.currentUser

    // Introduce a refresh trigger
    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(employeeId, refreshTrigger) {
        userProfileViewModel.loadUserProfile(employeeId)
    }

    LaunchedEffect(Unit) {
        // Increment refreshTrigger to force reload when screen is composed
        refreshTrigger++
    }

    LaunchedEffect(appState) {
        when (appState) {
            is ApplicationState.StatusUpdated,
            is ApplicationState.ApplicationWithdrawn -> {
                applicationViewModel.resetState()
                onBackClick()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Applicant Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (currentUser is User.Employee) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (profileState) {
                is UserProfileState.Loading -> CircularProgressIndicator()
                is UserProfileState.Error -> Text(
                    text = (profileState as UserProfileState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                else -> userProfile?.let { user ->
                    if (user is User.Employee) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Name: ${user.name}", style = MaterialTheme.typography.titleLarge)
                                Text("Email: ${user.email}")
                                Text("Phone: ${user.phone}")
                                Text("Skills: ${user.skills}")
                                Text("Experience: ${user.experience}")
                                Text("Education: ${user.education}")

                                if (currentUser is User.Employer) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Button(
                                            onClick = {
                                                applicationViewModel.updateApplicationStatus(
                                                    applicationId,
                                                    ApplicationStatus.REJECTED
                                                )
                                            }
                                        ) { Text("Reject") }
                                        Button(
                                            onClick = {
                                                applicationViewModel.updateApplicationStatus(
                                                    applicationId,
                                                    ApplicationStatus.ACCEPTED
                                                )
                                            }
                                        ) { Text("Accept") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Application") },
            text = { Text("Are you sure you want to delete this application?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        applicationViewModel.withdrawApplication(applicationId)
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) { Text("Cancel") }
            }
        )
    }
}