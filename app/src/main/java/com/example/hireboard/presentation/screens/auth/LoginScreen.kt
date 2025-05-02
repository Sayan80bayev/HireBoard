package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.presentation.components.HireBoardButton
import androidx.compose.material3.MaterialTheme
import com.example.hireboard.ui.theme.HireBoardTheme

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        HireBoardTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            labelStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(16.dp))

        HireBoardTextField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            isPassword = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            labelStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(24.dp))

        HireBoardButton(
            text = "Войти",
            onClick = { onLogin(email, password) },
            textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun LoginScreenPreview() {
    HireBoardTheme(darkTheme = true) {
        LoginScreen(onLogin = { _, _ -> })
    }
}