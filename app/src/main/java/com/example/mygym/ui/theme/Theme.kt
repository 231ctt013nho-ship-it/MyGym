package com.example.mygym.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MyGymDarkColorScheme = darkColorScheme(

    primary = GymAccent,
    onPrimary = GymBackground,

    secondary = GymAccentLight,
    onSecondary = GymBackground,

    tertiary = GymAccentDark,
    onTertiary = GymWhite,

    background = GymBackground,
    onBackground = GymWhite,

    surface = GymSurface,
    onSurface = GymWhite,

    surfaceVariant = GymCard,
    onSurfaceVariant = GymGray,

    outline = GymDivider
)

@Composable
fun MyGymTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MyGymDarkColorScheme,
        typography = Typography,
        content = content
    )
}