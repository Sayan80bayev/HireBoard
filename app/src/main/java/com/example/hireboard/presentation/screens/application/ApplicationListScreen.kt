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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.Application
import com.example.hireboard.presentation.viewmodels.ApplicationState
import com.example.hireboard.presentation.viewmodels.ApplicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationListScreen(
    vacancyId: Long,
    viewModel: ApplicationViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val applications by viewModel.applications.collectAsState()
    val state by viewModel.applicationState.collectAsState()

    viewModel.loadVacancyApplications(vacancyId)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Applications") },
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
            when (state) {
                is ApplicationState.Loading -> {
                    CircularProgressIndicator()
                }
                is ApplicationState.Error -> {
                    Text(
                        text = (state as ApplicationState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(applications) { application ->
                            ApplicationCard(application = application)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationCard(application: Application) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Cover Letter: ${application.coverLetter}")
            Text(text = "Status: ${application.status}")
        }
    }
}