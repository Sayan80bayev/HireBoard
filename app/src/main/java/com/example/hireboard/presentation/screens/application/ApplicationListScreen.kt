package com.example.hireboard.presentation.screens.application

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.User
import com.example.hireboard.presentation.viewmodels.ApplicationState
import com.example.hireboard.presentation.viewmodels.ApplicationViewModel
import com.example.hireboard.presentation.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationListScreen(
    vacancyId: Long,
    applicationViewModel: ApplicationViewModel,
    userProfileViewModel: UserProfileViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val applications by applicationViewModel.applications.collectAsState()
    val appState by applicationViewModel.applicationState.collectAsState()
    val currentUser by userProfileViewModel.userProfile.collectAsState()

    LaunchedEffect(vacancyId) {
        applicationViewModel.loadVacancyApplications(vacancyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Applications") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
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
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(applications) { application ->
                        val employee = remember { mutableStateOf<User.Employee?>(null) }

                        LaunchedEffect(application.employeeId) {
                            userProfileViewModel.loadUserProfile(application.employeeId)
                        }

                        userProfileViewModel.userProfile
                            .collectAsState().value?.let { user ->
                                if (user is User.Employee) {
                                    employee.value = user
                                }
                            }

                        ApplicationCard(
                            application = application,
                            employee = employee.value
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationCard(
    application: Application,
    employee: User.Employee?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            employee?.let {
                Text("Applicant: ${it.name}", style = MaterialTheme.typography.titleMedium)
                Text("Email: ${it.email}")
            }
            Text("Cover Letter: ${application.coverLetter}")
            Text("Status: ${application.status}")
        }
    }
}