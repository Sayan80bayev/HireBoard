package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextArea
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.presentation.viewmodels.AuthState
import com.example.hireboard.presentation.viewmodels.AuthViewModel
import com.example.hireboard.presentation.viewmodels.factory.AuthViewModelFactory
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import com.example.hireboard.ui.theme.HireBoardTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmployerScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    var step by remember { mutableIntStateOf(0) }

    var companyName by remember { mutableStateOf("") }
    val companyDescription = remember { mutableStateOf(TextFieldValue("")) }
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
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Назад")
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
                text = "Регистрация Работодателя (Шаг ${step + 1} из 3)",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                0 -> {
                    HireBoardTextField(
                        value = companyName,
                        onValueChange = { companyName = it },
                        label = "Название компании",
                        isRequired = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                    Spacer(modifier = Modifier.height(16.dp))
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
                    Spacer(modifier = Modifier.height(16.dp))
                    HireBoardTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Пароль",
                        isPassword = true,
                        isRequired = true
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
                        viewModel.registerEmployer(
                            companyName = companyName,
                            companyDescription = companyDescription.value.text,
                            email = email,
                            password = password,
                            phone = phone,
                            contactName = contactName
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
//fun RegisterEmployerScreenPreview() {
//    HireBoardTheme(darkTheme = true) {
//        RegisterEmployerScreen(
//            loginUseCase = { _, _ -> Result.failure(Exception("Preview")) },
//            registerEmployeeUseCase = { Result.success(1L) },
//            registerEmployerUseCase = { Result.success(1L) },
//            onRegisterSuccess = {}
//        )
//    }
//}