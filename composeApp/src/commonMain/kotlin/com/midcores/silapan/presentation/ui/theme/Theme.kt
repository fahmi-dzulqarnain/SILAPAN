package com.midcores.silapan.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Inside Theme.kt
@Composable
fun SilapanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColorScheme = lightColorScheme(
        primary = Primary,
        onPrimary = OnPrimary,
        primaryContainer = PrimaryContainer,
        onPrimaryContainer = OnPrimaryContainer,

        secondary = Secondary,
        onSecondary = OnSecondary,
        secondaryContainer = SecondaryContainer,
        onSecondaryContainer = OnSecondaryContainer,

        tertiary = Tertiary,
        onTertiary = OnTertiary,
        tertiaryContainer = TertiaryContainer,
        onTertiaryContainer = OnTertiaryContainer,

        background = Background,
        onBackground = OnBackground,
        surface = Surface,
        onSurface = OnSurface,
        surfaceVariant = SurfaceVariant,
        onSurfaceVariant = OnSurfaceVariant,
        outline = Outline
    )

    val darkColorScheme = darkColorScheme(
        primary = PrimaryContainer,
        onPrimary = OnPrimaryContainer,
        primaryContainer = Primary,
        onPrimaryContainer = OnPrimary,

        secondary = SecondaryContainer,
        onSecondary = OnSecondaryContainer,
        secondaryContainer = Secondary,
        onSecondaryContainer = OnSecondary,

        tertiary = TertiaryContainer,
        onTertiary = OnTertiaryContainer,
        tertiaryContainer = Tertiary,
        onTertiaryContainer = OnTertiary,

        background = Color(0xFF191C1B),
        onBackground = Color(0xFFE0E3E1),
        surface = Color(0xFF191C1B),
        onSurface = Color(0xFFE0E3E1),
        surfaceVariant = Color(0xFF3F4947),
        onSurfaceVariant = Color(0xFFBFC9C7),
        outline = Color(0xFF899391)
    )

    val colorScheme = if (darkTheme) {
        darkColorScheme
    } else {
        lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography(),
        content = content
    )
}