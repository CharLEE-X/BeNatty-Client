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
            backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainerLowest.value())
            color(MaterialTheme.colors.mdSysColorOnBackground.value())
        }

        universal style {
            MaterialTheme.colors.mdSysColorPrimary(sysColorPrimary)
            MaterialTheme.colors.mdSysColorPrimaryContainer(sysColorPrimaryContainer)
            MaterialTheme.colors.mdSysColorOnPrimary(sysColorOnPrimary)
            MaterialTheme.colors.mdSysColorOnPrimaryContainer(sysColorOnPrimaryContainer)
            MaterialTheme.colors.mdSysColorSecondary(sysColorSecondary)
            MaterialTheme.colors.mdSysColorSecondaryContainer(sysColorSecondaryContainer)
            MaterialTheme.colors.mdSysColorOnSecondary(sysColorOnSecondary)
            MaterialTheme.colors.mdSysColorOnSecondaryContainer(sysColorOnSecondaryContainer)
            MaterialTheme.colors.mdSysColorTertiary(sysColorTertiary)
            MaterialTheme.colors.mdSysColorTertiaryContainer(sysColorTertiaryContainer)
            MaterialTheme.colors.mdSysColorOnTertiary(sysColorOnTertiary)
            MaterialTheme.colors.mdSysColorOnTertiaryContainer(sysColorOnTertiaryContainer)
            MaterialTheme.colors.mdSysColorError(sysColorError)
            MaterialTheme.colors.mdSysColorErrorContainer(sysColorErrorContainer)
            MaterialTheme.colors.mdSysColorOnError(sysColorOnError)
            MaterialTheme.colors.mdSysColorOnErrorContainer(sysColorOnErrorContainer)
            MaterialTheme.colors.mdSysColorOutline(sysColorOutline)
            MaterialTheme.colors.mdSysColorBackground(sysColorBackground)
            MaterialTheme.colors.mdSysColorOnBackground(sysColorOnBackground)
            MaterialTheme.colors.mdSysColorSurface(sysColorSurface)
            MaterialTheme.colors.mdSysColorOnSurface(sysColorOnSurface)
            MaterialTheme.colors.mdSysColorSurfaceDim(sysColorSurfaceDim)
            MaterialTheme.colors.mdSysColorSurfaceBright(sysColorSurfaceBright)
            MaterialTheme.colors.mdSysColorSurfaceContainerLowest(sysColorSurfaceContainerLowest)
            MaterialTheme.colors.mdSysColorSurfaceContainerLow(sysColorSurfaceContainerLow)
            MaterialTheme.colors.mdSysColorSurfaceContainer(sysColorSurfaceContainer)
            MaterialTheme.colors.mdSysColorSurfaceContainerHigh(sysColorSurfaceContainerHigh)
            MaterialTheme.colors.mdSysColorSurfaceContainerHighest(sysColorSurfaceContainerHighest)
            MaterialTheme.colors.mdSysColorSurfaceVariant(sysColorSurfaceVariant)
            MaterialTheme.colors.mdSysColorOnSurfaceVariant(sysColorOnSurfaceVariant)
            MaterialTheme.colors.mdSysColorInverseSurface(sysColorInverseSurface)
            MaterialTheme.colors.mdSysColorInverseOnSurface(sysColorInverseOnSurface)
            MaterialTheme.colors.mdSysColorInversePrimary(sysColorInversePrimary)
            MaterialTheme.colors.mdSysColorShadow(sysColorShadow)
            MaterialTheme.colors.mdSysColorSurfaceTint(sysColorSurfaceTint)
            MaterialTheme.colors.mdSysColorOutlineVariant(sysColorOutlineVariant)
            MaterialTheme.colors.mdSysColorScrim(sysColorScrim)
        }
    }
}
