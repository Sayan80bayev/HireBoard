package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextArea
import com.example.hireboard.presentation.components.HireBoardTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import com.example.hireboard.ui.theme.HireBoardTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmployerScreen(onRegister: (String, String, String, String, String) -> Unit) {
    var step by remember { mutableIntStateOf(0) }

    var companyName by remember { mutableStateOf("") }
    val companyDescription: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var contactName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (step > 0) {
                TopAppBar(
                    title = { Text("Регистрация Работодателя") },
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
                text = "Регистрация Работодателя (Шаг ${step + 1} из 3)",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp)) // Optional spacer to control button position

            when (step) {
                0 -> {
                    HireBoardTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = "Название компании",
                        isRequired = true
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Optional spacer to control button position

                    HireBoardTextArea(
                        value = companyDescription,
                        onValueChange = { companyDescription.value = it },
                        label = "Описание компании (необязательно)",
                        isRequired = false
                    )
                }
                1 -> {
                    HireBoardTextField(
                        value = contactName,
                        onValueChange = { contactName = it },
                        label = "Имя контактного лица",
                        isRequired = true
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Optional spacer to control button position

                    HireBoardTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Телефон",
                        isRequired = true
                    )
                }
                2 -> {
                    HireBoardTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        isRequired = true
                    )
                    Spacer(modifier = Modifier.height(16.dp)) // Optional spacer to control button position

                    HireBoardTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Пароль",
                        isPassword = true,
                        isRequired = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Optional spacer to control button position

            HireBoardButton(
                text = if (step == 2) "Зарегистрироваться" else "Далее",
                onClick = {
                    if (step < 2) {
                        step++
                    } else {
                        onRegister(companyName, companyDescription.value.text, email, password, phone)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun RegisterEmployerStep1Preview() {
    HireBoardTheme(
        darkTheme = true // This will set the theme to dark mode
    ) {
        Surface {
            RegisterEmployerScreen(
                onRegister = { _, _, _, _, _ -> }
            )
        }
    }
}