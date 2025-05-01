package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.ui.theme.HireBoardTheme

@Composable
fun RoleSelectionScreen(onRoleSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Кто вы?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        HireBoardButton(
            text = "Работник",
            icon = Icons.Default.Person,
            contentDescription = "Иконка работника",
            onClick = { onRoleSelected("employee") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HireBoardButton(
            text = "Работодатель",
            icon = Icons.Default.Business,
            contentDescription = "Иконка работодателя",
            onClick = { onRoleSelected("employer") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoleSelectionScreenPreview() {
    HireBoardTheme(darkTheme = true) {
        RoleSelectionScreen(onRoleSelected = {})
    }
}