package com.example.hireboard.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.BottomNavBar
import com.example.hireboard.ui.theme.HireBoardTheme

@Composable
fun MainScreen(isEmployer: Boolean) {
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavBar(selectedItem = selectedItem, onItemSelected = { selectedItem = it }, isEmployer = isEmployer)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Добро пожаловать, ${if (isEmployer) "работодатель" else "работник"}!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenEmployeePreview() {
    HireBoardTheme(darkTheme = false) {
        MainScreen(isEmployer = false)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenEmployerPreview() {
    HireBoardTheme(darkTheme = true) {
        MainScreen(isEmployer = true)
    }
}