package com.example.hireboard.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hireboard.ui.theme.HireBoardTheme

data class BottomNavItem(val label: String, val icon: ImageVector, val badgeCount: Int = 0)

private val employeeItems = listOf(
    BottomNavItem("Главная", Icons.Filled.Home),
    BottomNavItem("Отклики", Icons.Filled.Mail, badgeCount = 2),
    BottomNavItem("Профиль", Icons.Filled.Person),
    BottomNavItem("Настройки", Icons.Filled.Settings)
)

private val employerItems = listOf(
    BottomNavItem("Отклики", Icons.Filled.ChatBubble),
    BottomNavItem("Вакансии", Icons.Filled.Work, badgeCount = 5),
    BottomNavItem("Профиль", Icons.Filled.Person),
    BottomNavItem("Настройки", Icons.Filled.Settings)
)

@Composable
fun BottomNavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    isEmployer: Boolean
) {
    val items = if (isEmployer) employerItems else employeeItems

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    if (item.badgeCount > 0) {
                        BadgedBox(badge = {
                            Badge {
                                Text(item.badgeCount.toString(), color = Color.White) // Set text color to white
                            }
                        }) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.label)
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarEmployeePreview() {
    HireBoardTheme {
        BottomNavBar(selectedItem = 0, onItemSelected = {}, isEmployer = false)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarEmployerPreview() {
    HireBoardTheme(darkTheme = true){
        BottomNavBar(selectedItem = 1, onItemSelected = {}, isEmployer = true)
    }
}