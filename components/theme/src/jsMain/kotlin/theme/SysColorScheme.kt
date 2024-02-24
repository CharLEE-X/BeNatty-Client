package theme

import com.varabyte.kobweb.compose.ui.graphics.Color
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.value

data class SysColorScheme(
    val sysColorPrimary: Color,
    val sysColorPrimaryContainer: Color,
    val sysColorOnPrimary: Color,
    val sysColorOnPrimaryContainer: Color,
    val sysColorSecondary: Color,
    val sysColorSecondaryContainer: Color,
    val sysColorOnSecondary: Color,
    val sysColorOnSecondaryContainer: Color,
    val sysColorTertiary: Color,
    val sysColorTertiaryContainer: Color,
    val sysColorOnTertiary: Color,
    val sysColorOnTertiaryContainer: Color,
    val sysColorError: Color,
    val sysColorErrorContainer: Color,
    val sysColorOnError: Color,
    val sysColorOnErrorContainer: Color,
    val sysColorOutline: Color,
    val sysColorBackground: Color,
    val sysColorOnBackground: Color,
    val sysColorSurface: Color,
    val sysColorOnSurface: Color,
    val sysColorSurfaceDim: Color, // = Color("#141218"),
    val sysColorSurfaceBright: Color, // = Color("#3B383E"),
    val sysColorSurfaceContainerLowest: Color,
    val sysColorSurfaceContainerLow: Color, // = Color("#1D1B20"),
    val sysColorSurfaceContainer: Color, // = Color("#211F26"),
    val sysColorSurfaceContainerHigh: Color, // = Color("#2B2930"),
    val sysColorSurfaceContainerHighest: Color, // = Color("#36343B"),
    val sysColorSurfaceVariant: Color,
    val sysColorOnSurfaceVariant: Color,
    val sysColorInverseSurface: Color,
    val sysColorInverseOnSurface: Color,
    val sysColorInversePrimary: Color,
    val sysColorShadow: Color,
    val sysColorSurfaceTint: Color,
    val sysColorOutlineVariant: Color,
    val sysColorScrim: Color
) {
    fun asStylesheet(): StyleSheet = StyleSheet().apply {
        root style {
            backgroundColor(MaterialTheme.colors.surfaceContainerLowest.value())
            color(MaterialTheme.colors.onBackground.value())
        }

        universal style {
            MaterialTheme.colors.primary(sysColorPrimary)
            MaterialTheme.colors.primaryContainer(sysColorPrimaryContainer)
            MaterialTheme.colors.onPrimary(sysColorOnPrimary)
            MaterialTheme.colors.onPrimaryContainer(sysColorOnPrimaryContainer)
            MaterialTheme.colors.secondary(sysColorSecondary)
            MaterialTheme.colors.secondaryContainer(sysColorSecondaryContainer)
            MaterialTheme.colors.onSecondary(sysColorOnSecondary)
            MaterialTheme.colors.onSecondaryContainer(sysColorOnSecondaryContainer)
            MaterialTheme.colors.tertiary(sysColorTertiary)
            MaterialTheme.colors.tertiaryContainer(sysColorTertiaryContainer)
            MaterialTheme.colors.onTertiary(sysColorOnTertiary)
            MaterialTheme.colors.onTertiaryContainer(sysColorOnTertiaryContainer)
            MaterialTheme.colors.error(sysColorError)
            MaterialTheme.colors.errorContainer(sysColorErrorContainer)
            MaterialTheme.colors.onError(sysColorOnError)
            MaterialTheme.colors.onErrorContainer(sysColorOnErrorContainer)
            MaterialTheme.colors.outline(sysColorOutline)
            MaterialTheme.colors.background(sysColorBackground)
            MaterialTheme.colors.onBackground(sysColorOnBackground)
            MaterialTheme.colors.surface(sysColorSurface)
            MaterialTheme.colors.onSurface(sysColorOnSurface)
            MaterialTheme.colors.surfaceDim(sysColorSurfaceDim)
            MaterialTheme.colors.surfaceBright(sysColorSurfaceBright)
            MaterialTheme.colors.surfaceContainerLowest(sysColorSurfaceContainerLowest)
            MaterialTheme.colors.surfaceContainerLow(sysColorSurfaceContainerLow)
            MaterialTheme.colors.surfaceContainer(sysColorSurfaceContainer)
            MaterialTheme.colors.surfaceContainerHigh(sysColorSurfaceContainerHigh)
            MaterialTheme.colors.surfaceContainerHighest(sysColorSurfaceContainerHighest)
            MaterialTheme.colors.surfaceVariant(sysColorSurfaceVariant)
            MaterialTheme.colors.onSurfaceVariant(sysColorOnSurfaceVariant)
            MaterialTheme.colors.inverseSurface(sysColorInverseSurface)
            MaterialTheme.colors.inverseOnSurface(sysColorInverseOnSurface)
            MaterialTheme.colors.inversePrimary(sysColorInversePrimary)
            MaterialTheme.colors.shadow(sysColorShadow)
            MaterialTheme.colors.surfaceTint(sysColorSurfaceTint)
            MaterialTheme.colors.outlineVariant(sysColorOutlineVariant)
            MaterialTheme.colors.scrim(sysColorScrim)
        }
    }
}
