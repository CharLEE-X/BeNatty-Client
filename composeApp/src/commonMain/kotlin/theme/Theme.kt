package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Colors.yellow,
    primaryVariant = Colors.yellow,
    onPrimary = Color.Black,

    secondary = Colors.yellow,
    secondaryVariant = Colors.darkGreen,
    onSecondary = Color.Black,

    background = Color.White,
    onBackground = Color.DarkGray,

    surface = Colors.Surface.light,
    onSurface = Color.Black,

    error = Color.Red,
)

private val DarkColorPalette = darkColors(
    primary = Colors.yellow,
    primaryVariant = Colors.yellow,
    onPrimary = Color.Black,

    secondary = Colors.green,
    secondaryVariant = Colors.darkGreen,
    onSecondary = Color.Black,

    background = Color.Black,
    onBackground = Color.White,

    surface = Colors.Surface.dark,
    onSurface = Color.LightGray,

    error = Color.Red,
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
