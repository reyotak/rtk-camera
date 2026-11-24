package com.rtkcamera.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val RtkDarkColorScheme = darkColorScheme(
    primary = RtkColorTokens.AccentCyan,
    onPrimary = RtkColorTokens.TextOnAccent,
    primaryContainer = RtkColorTokens.SurfaceChipActiveCyan,
    onPrimaryContainer = RtkColorTokens.AccentCyan,
    secondary = RtkColorTokens.AccentAmber,
    onSecondary = RtkColorTokens.TextOnAccent,
    tertiary = RtkColorTokens.AccentMint,
    onTertiary = RtkColorTokens.TextOnAccent,
    background = RtkColorTokens.CanvasDeep,
    onBackground = RtkColorTokens.TextPrimary,
    surface = RtkColorTokens.CanvasDark,
    onSurface = RtkColorTokens.TextPrimary,
    surfaceVariant = RtkColorTokens.SurfaceCard,
    onSurfaceVariant = RtkColorTokens.TextMuted,
    outline = RtkColorTokens.BorderSubtle,
    error = RtkColorTokens.AccentError,
    onError = RtkColorTokens.TextPrimary
)

val LocalRtkColors = staticCompositionLocalOf { RtkExtendedColors() }

object RtkTheme {
    val colors: RtkExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalRtkColors.current
}

@Composable
fun RtkCameraTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalRtkColors provides RtkExtendedColors()) {
        MaterialTheme(
            colorScheme = RtkDarkColorScheme,
            typography = RtkTypography,
            content = content
        )
    }
}
