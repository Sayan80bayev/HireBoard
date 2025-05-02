package com.example.hireboard.presentation.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hireboard.domain.model.User
import com.example.hireboard.presentation.screens.main.MainScreen

fun NavGraphBuilder.mainNavGraph(user: User?) {
    navigation(
        startDestination = "main_screen",
        route = AppRoutes.Main
    ) {
        composable("main_screen") {
            if (user != null) {
                MainScreen(user)
            } else {
                println("There's no User")
            }
        }
    }
}