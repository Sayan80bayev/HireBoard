package com.example.hireboard.presentation.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.presentation.viewmodels.AuthState
import com.example.hireboard.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            onClick = { viewModel.login(email, password) },
            textStyle = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Нет аккаунта? Зарегистрироваться",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onSignUpClick() }
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
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
                    onLoginSuccess()
//                    viewModel.resetState()
                }
            }
            else -> {}
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFF121212)
//@Composable
//fun LoginScreenPreview() {
//    HireBoardTheme(darkTheme = true) {
//        // Preview doesn't include real ViewModel, so stub callbacks used
//        LoginScreen(
//            loginUseCase = { _, _ -> Result.failure(Exception("Preview")) },
//            registerEmployeeUseCase = { Result.success(1L) },
//            registerEmployerUseCase = { Result.success(1L) },
//            onLoginSuccess = {},
//            onSignUpClick = {}
//        )
//    }
//}