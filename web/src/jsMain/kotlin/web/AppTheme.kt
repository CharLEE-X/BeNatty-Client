package web

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.cssRem
import theme.FontConfig
import theme.SysFontScheme
import theme.appDarkColorScheme
import theme.appLightColorScheme
import theme.sp

const val HEADLINE_FONT = "Montserrat"
const val BODY_FONT = "Roboto"

fun colorScheme(colorMode: ColorMode) = when (colorMode) {
    ColorMode.LIGHT -> appLightColorScheme
    ColorMode.DARK -> appDarkColorScheme
}

val appFontScheme = SysFontScheme(
    displayLarge = FontConfig(HEADLINE_FONT, 57.sp, 64.sp, (-0.25 / 57).cssRem, "400"),
    displayMedium = FontConfig(HEADLINE_FONT, 45.sp, 52.sp, 0.cssRem, "400"),
    displaySmall = FontConfig(HEADLINE_FONT, 36.sp, 44.sp, 0.cssRem, "400"),
    headlineLarge = FontConfig(HEADLINE_FONT, 32.sp, 40.sp, 0.cssRem, "400"),
    headlineMedium = FontConfig(HEADLINE_FONT, 28.sp, 36.sp, 0.cssRem, "400"),
    headlineSmall = FontConfig(HEADLINE_FONT, 24.sp, 32.sp, 0.cssRem, "400"),
    titleLarge = FontConfig(BODY_FONT, 22.sp, 28.sp, 0.cssRem, "400"),
    titleMedium = FontConfig(BODY_FONT, 16.sp, 24.sp, (0.15 / 16).cssRem, "500"),
    titleSmall = FontConfig(BODY_FONT, 14.sp, 20.sp, (0.1 / 14).cssRem, "500"),
    bodyLarge = FontConfig(BODY_FONT, 16.sp, 24.sp, (0.5 / 16).cssRem, "400"),
    bodyMedium = FontConfig(BODY_FONT, 14.sp, 20.sp, (0.25 / 14).cssRem, "400"),
    bodySmall = FontConfig(BODY_FONT, 12.sp, 16.sp, (0.4 / 12).cssRem, "400"),
    labelLarge = FontConfig(BODY_FONT, 14.sp, 20.sp, (0.1 / 14).cssRem, "500"),
    labelMedium = FontConfig(BODY_FONT, 12.sp, 16.sp, (0.5 / 12).cssRem, "500"),
    labelSmall = FontConfig(BODY_FONT, 11.sp, 16.sp, (0.5 / 11).cssRem, "500"),
)

@Composable
fun shadow() = if (ColorMode.current.isLight) Colors.Black.copy(alpha = 25).toRgb()
else Colors.White.copy(alpha = 25).toRgb()
