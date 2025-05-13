package com.example.hireboard.presentation.screens.vacancy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.presentation.components.VacancyItemCard

@Composable
fun VacancyListScreen(
    vacancies: List<Vacancy>,
    onVacancyClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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