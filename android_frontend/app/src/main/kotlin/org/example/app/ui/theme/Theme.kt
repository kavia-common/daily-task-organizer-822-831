package org.example.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = OceanPrimary,
    onPrimary = OceanOnPrimary,
    secondary = OceanSecondary,
    onSecondary = OceanOnSecondary,
    error = OceanError,
    onError = OceanOnError,
    background = OceanBackground,
    onBackground = OceanOnBackground,
    surface = OceanSurface,
    onSurface = OceanOnSurface
)

private val DarkColors = darkColorScheme(
    primary = OceanPrimary,
    onPrimary = OceanOnPrimary,
    secondary = OceanSecondary,
    onSecondary = Color(0xFF0A0A0A),
    error = OceanError,
    onError = OceanOnError,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE5E7EB)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

// PUBLIC_INTERFACE
@Composable
@ReadOnlyComposable
fun headerGradient(): Brush {
    // Subtle gradient: primary with 10% alpha to near-surface
    return Brush.verticalGradient(
        colors = listOf(
            OceanPrimary.copy(alpha = 0.08f),
            Color.Transparent
        )
    )
}

// PUBLIC_INTERFACE
fun Modifier.headerGradientBackground(): Modifier = this.background(headerGradient())
