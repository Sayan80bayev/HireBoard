package com.example.hireboard.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xFF006E6D)
val OnPrimaryColor = Color(0xFFFFFFFF)
val SecondaryColor = Color(0xFF4ECDC4)
val OnSecondaryColor = Color(0xFF000000)
val BackgroundColor = Color(0xFFF0F0F0)
val SurfaceColor = Color(0xFFFFFFFF)
val ErrorColor = Color(0xFFB00020)

val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    error = ErrorColor
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = ErrorColor
)