package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.ui.theme.HireBoardTheme

@Composable
fun RegisterEmployerScreen(onRegister: (String, String, String) -> Unit) {
    var companyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Регистрация Работодателя",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        HireBoardTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = "Название компании"
        )

        Spacer(modifier = Modifier.height(16.dp))

        HireBoardTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        HireBoardTextField(
            value = password,
            onValueChange = { password = it },
            label = "Пароль",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        HireBoardButton(
            text = "Зарегистрироваться",
            onClick = { onRegister(companyName, email, password) }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun RegisterEmployerScreenPreview() {
    HireBoardTheme(darkTheme = true) {
        RegisterEmployerScreen(onRegister = { _, _, _ -> })
    }
}