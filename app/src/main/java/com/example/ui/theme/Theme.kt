package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PlayerAccentBlue,
    secondary = PlayerAccentLightBlue,
    tertiary = PlayerAccentBlue,
    background = PlayerBlack,
    surface = PlayerSurface,
    onPrimary = PlayerWhite,
    onSecondary = PlayerWhite,
    onBackground = PlayerWhite,
    onSurface = PlayerWhite
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
