package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GoldBright,
    onPrimary = NavyPrimary,
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = GoldAccent,
    onSecondary = NavyPrimary,
    secondaryContainer = NavySecondary,
    onSecondaryContainer = Color.White,
    tertiary = BlueInfo,
    onTertiary = Color.White,
    background = DeepDarkBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurfaceCard,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = RedUrgent,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavySecondary,
    onPrimaryContainer = GoldBright,
    secondary = GoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF7DD),
    onSecondaryContainer = Color(0xFF5E4300),
    tertiary = BlueInfo,
    onTertiary = Color.White,
    background = ParchmentLight,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = SurfaceContainerLight,
    onSurfaceVariant = TextMuted,
    error = RedUrgent,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep tailored luxury legal palette by default
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
