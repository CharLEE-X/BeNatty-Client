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
            backgroundColor(CSSMaterialTheme.colors.surfaceContainerLowest.value())
            color(CSSMaterialTheme.colors.onBackground.value())
        }

        universal style {
            CSSMaterialTheme.colors.primary(sysColorPrimary)
            CSSMaterialTheme.colors.primaryContainer(sysColorPrimaryContainer)
            CSSMaterialTheme.colors.onPrimary(sysColorOnPrimary)
            CSSMaterialTheme.colors.onPrimaryContainer(sysColorOnPrimaryContainer)
            CSSMaterialTheme.colors.secondary(sysColorSecondary)
            CSSMaterialTheme.colors.secondaryContainer(sysColorSecondaryContainer)
            CSSMaterialTheme.colors.onSecondary(sysColorOnSecondary)
            CSSMaterialTheme.colors.onSecondaryContainer(sysColorOnSecondaryContainer)
            CSSMaterialTheme.colors.tertiary(sysColorTertiary)
            CSSMaterialTheme.colors.tertiaryContainer(sysColorTertiaryContainer)
            CSSMaterialTheme.colors.onTertiary(sysColorOnTertiary)
            CSSMaterialTheme.colors.onTertiaryContainer(sysColorOnTertiaryContainer)
            CSSColors.Red(sysColorError)
            CSSColors.RedContainer(sysColorErrorContainer)
            CSSMaterialTheme.colors.onError(sysColorOnError)
            CSSMaterialTheme.colors.onErrorContainer(sysColorOnErrorContainer)
            CSSMaterialTheme.colors.outline(sysColorOutline)
            CSSMaterialTheme.colors.background(sysColorBackground)
            CSSMaterialTheme.colors.onBackground(sysColorOnBackground)
            CSSMaterialTheme.colors.surface(sysColorSurface)
            CSSColorMode.current.toPalette().color(sysColorOnSurface)
            CSSMaterialTheme.colors.surfaceDim(sysColorSurfaceDim)
            CSSMaterialTheme.colors.surfaceBright(sysColorSurfaceBright)
            CSSMaterialTheme.colors.surfaceContainerLowest(sysColorSurfaceContainerLowest)
            CSSMaterialTheme.colors.surfaceContainerLow(sysColorSurfaceContainerLow)
            CSSMaterialTheme.colors.surfaceContainer(sysColorSurfaceContainer)
            CSSMaterialTheme.colors.surfaceContainerHigh(sysColorSurfaceContainerHigh)
            CSSMaterialTheme.colors.surfaceContainerHighest(sysColorSurfaceContainerHighest)
            CSSMaterialTheme.colors.surfaceVariant(sysColorSurfaceVariant)
            CSSColorMode.current.toPalette().colorVariant(sysColorOnSurfaceVariant)
            CSSMaterialTheme.colors.inverseSurface(sysColorInverseSurface)
            CSSMaterialTheme.colors.inverseOnSurface(sysColorInverseOnSurface)
            CSSMaterialTheme.colors.inversePrimary(sysColorInversePrimary)
            CSSMaterialTheme.colors.shadow(sysColorShadow)
            CSSMaterialTheme.colors.surfaceTint(sysColorSurfaceTint)
            CSSMaterialTheme.colors.outlineVariant(sysColorOutlineVariant)
            CSSMaterialTheme.colors.scrim(sysColorScrim)
        }
    }
}
