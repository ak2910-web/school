package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = AccentBlueBg,
    onPrimaryContainer = BlueSecondary,
    secondary = BlueSecondary,
    onSecondary = Color.White,
    tertiary = BlueTertiary,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = BorderLight,
    onSurfaceVariant = TextMuted,
    error = AccentDanger,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueTertiary,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFF93C5FD),
    secondary = BluePrimary,
    onSecondary = Color.White,
    tertiary = AccentCyan,
    onTertiary = Color.Black,
    background = Color(0xFF0F172A),  // Slate 900
    onBackground = Color(0xFFF9FAFB),
    surface = Color(0xFF1E293B),     // Slate 800
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    error = Color(0xFFF87171),
    onError = Color.Black
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
