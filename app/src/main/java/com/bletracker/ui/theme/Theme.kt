package com.bletracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    secondary = BlueSecondary,
    onSecondary = White,
    tertiary = BlueTertiary,
    onTertiary = White,
    background = GrayLight,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    error = RedError,
    onError = White
)

private val DarkColors = darkColorScheme(
    primary = BlueLight,
    onPrimary = Black,
    secondary = BlueAccent,
    onSecondary = Black,
    tertiary = BlueTertiary,
    onTertiary = White,
    background = DarkGray,
    onBackground = White,
    surface = GrayDark,
    onSurface = White,
    error = RedError,
    onError = White
)

@Composable
fun BleTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
