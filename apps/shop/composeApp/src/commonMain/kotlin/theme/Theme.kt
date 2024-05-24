package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Colors.primary,
    primaryVariant = Colors.primary,
    onPrimary = Color.Black,

    secondary = Colors.secondary,
    secondaryVariant = Colors.secondary,
    onSecondary = Color.Black,

    background = Colors.white,
    onBackground = Color.Black,

    surface = Colors.lightGray,
    onSurface = Colors.darkGray,

    error = Colors.red,
)

private val DarkColorPalette = darkColors(
    primary = Colors.primary,
    primaryVariant = Colors.primary,
    onPrimary = Color.Black,

    secondary = Colors.secondary,
    secondaryVariant = Colors.secondary,
    onSecondary = Color.Black,

    background = Color.Black,
    onBackground = Colors.white,

    surface = Colors.darkGray,
    onSurface = Colors.white,

    error = Colors.red,
)

@Composable
internal fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (!isDarkTheme) {
        LightColorPalette
    } else {
        DarkColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = shapes,
        typography = typography(),
        content = content,
    )
}
