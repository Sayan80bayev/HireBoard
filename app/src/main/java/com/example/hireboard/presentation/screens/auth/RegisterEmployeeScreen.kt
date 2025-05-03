package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.presentation.viewmodels.AuthState
import com.example.hireboard.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmployeeScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                        label = "Навыки"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HireBoardTextField(
                        value = experience,
                        onValueChange = { experience = it },
                        label = "Опыт работы"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HireBoardTextField(
                        value = education,
                        onValueChange = { education = it },
                        label = "Образование"
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
                        viewModel.registerEmployee(
                            name = name,
                            email = email,
                            password = password,
                            phone = phone,
                            skills = skills,
                            experience = experience,
                            education = education
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = authState) {
                is AuthState.Loading -> CircularProgressIndicator()
                is AuthState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                is AuthState.Success -> {
                    LaunchedEffect(Unit) {
                        onRegisterSuccess()
                        viewModel.resetState()
                    }
                }
                else -> {}
            }
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFF121212)
//@Composable
//fun RegisterEmployeeScreenPreview() {
//    HireBoardTheme(darkTheme = true) {
//        RegisterEmployeeScreen(
//            loginUseCase = { _, _ -> Result.failure(Exception("Preview")) },
//            registerEmployeeUseCase = {
//                Result.success(1L)
//            },
//            registerEmployerUseCase = {
//                Result.success(1L)
//            },
//            onRegisterSuccess = {}
//        )
//    }
//}