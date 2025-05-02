package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.ui.theme.HireBoardTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmployeeScreen(onRegister: (String, String, String, String, String, String) -> Unit) {
    var step by remember { mutableIntStateOf(0) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (step > 0) {
                TopAppBar(
                    title = { Text("Регистрация Работника") },
                    navigationIcon = {
                        IconButton(onClick = { step-- }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Назад"
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        // Centering the form vertically and horizontally
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center, // Center the content vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
        ) {
            Text(
                text = "Регистрация Работника (Шаг ${step + 1} из 3)",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                0 -> {
                    HireBoardTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Имя",
                        isRequired = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HireBoardTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        isRequired = true
                    )
                }
                1 -> {
                    HireBoardTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Телефон",
                        isRequired = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HireBoardTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Пароль",
                        isPassword = true,
                        isRequired = true
                    )
                }
                2 -> {
                    HireBoardTextField(
                        value = skills,
                        onValueChange = { skills = it },
                        label = "Навыки",
                        isRequired = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HireBoardTextField(
                        value = experience,
                        onValueChange = { experience = it },
                        label = "Опыт работы",
                        isRequired = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    HireBoardTextField(
                        value = education,
                        onValueChange = { education = it },
                        label = "Образование",
                        isRequired = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HireBoardButton(
                text = if (step == 2) "Зарегистрироваться" else "Далее",
                onClick = {
                    if (step < 2) {
                        step++
                    } else {
                        onRegister(name, email, password, phone, skills, experience)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun RegisterEmployeeScreenPreview() {
    HireBoardTheme(darkTheme = true) {
        RegisterEmployeeScreen(onRegister = { _, _, _, _, _, _ -> })
    }
}