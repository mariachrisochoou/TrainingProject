package com.magazinestore.app.ui.theme



import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = buttonColor,
    onPrimary = background,
    secondary = itemBackground1,
    onSecondary = background,
    background = background,
    onBackground = primaryText,
    surface = labelBackground,
    onSurface = primaryText,
    error = error,
    onError = primaryText
)


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = appTypography,
        content = content
    )
}