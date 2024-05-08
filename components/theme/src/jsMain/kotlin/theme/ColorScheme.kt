package theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

object JsPalette {
    object Light {
        val background = Color.White.rgba()
        val onBackground = Color(0x111111).rgba()
        val surface = Color(0xC4C4C4).rgba()
        val onSurface = Color(0x111111).rgba()
        val primary = Color(0x9A7F6A).rgba()
        val onPrimary = Color.White.rgba()
        val accent = Color(0xCEDFDC).rgba()
        val onAccent = Color(0x111111).rgba()
    }

    object Dark {
        val background = Color(0x111111).rgba()
        val onBackground = Color.White.rgba()
        val surface = Color(0x262528).rgba()
        val onSurface = Color.White.rgba()
        val primary = Color(0x9A7F6AFF).rgba()
        val onPrimary = Color.White.rgba()
        val accent = Color(0x384946).rgba()
        val onAccent = Color(0x111111).rgba()
    }
}

val appLightColorScheme: SysColorScheme = lightScheme.colorScheme(
    sysColorSurfaceBright = surfaceBrightLight,
    sysColorSurfaceDim = surfaceDimLight,
    sysColorSurfaceContainerLowest = surfaceContainerLowestLight,
    sysColorSurfaceContainerLow = surfaceContainerLowLight,
    sysColorSurfaceContainer = surfaceContainerLight,
    sysColorSurfaceContainerHigh = surfaceContainerHighLight,
    sysColorSurfaceContainerHighest = surfaceContainerHighestLight,
)
val appDarkColorScheme: SysColorScheme = darkScheme.colorScheme(
    sysColorSurfaceBright = surfaceBrightDark,
    sysColorSurfaceDim = surfaceDimDark,
    sysColorSurfaceContainerLowest = surfaceContainerLowestDark,
    sysColorSurfaceContainerLow = surfaceContainerLowDark,
    sysColorSurfaceContainer = surfaceContainerDark,
    sysColorSurfaceContainerHigh = surfaceContainerHighDark,
    sysColorSurfaceContainerHighest = surfaceContainerHighestDark,
)

private fun ColorScheme.colorScheme(
    sysColorSurfaceBright: Color,
    sysColorSurfaceDim: Color,
    sysColorSurfaceContainerLowest: Color,
    sysColorSurfaceContainerLow: Color,
    sysColorSurfaceContainer: Color,
    sysColorSurfaceContainerHigh: Color,
    sysColorSurfaceContainerHighest: Color,
) = SysColorScheme(
    sysColorPrimary = this.primary.rgba(),
    sysColorOnPrimary = this.onPrimary.rgba(),
    sysColorInversePrimary = this.inversePrimary.rgba(),
    sysColorPrimaryContainer = this.primaryContainer.rgba(),
    sysColorOnPrimaryContainer = this.onPrimaryContainer.rgba(),
    sysColorSecondary = this.secondary.rgba(),
    sysColorOnSecondary = this.onSecondary.rgba(),
    sysColorSecondaryContainer = this.secondaryContainer.rgba(),
    sysColorOnSecondaryContainer = this.onSecondaryContainer.rgba(),
    sysColorTertiary = this.tertiary.rgba(),
    sysColorOnTertiary = this.onTertiary.rgba(),
    sysColorTertiaryContainer = this.tertiaryContainer.rgba(),
    sysColorOnTertiaryContainer = this.onTertiaryContainer.rgba(),
    sysColorError = this.error.rgba(),
    sysColorErrorContainer = this.errorContainer.rgba(),
    sysColorOnError = this.onError.rgba(),
    sysColorOnErrorContainer = this.onErrorContainer.rgba(),
    sysColorBackground = this.background.rgba(),
    sysColorOnBackground = this.onBackground.rgba(),
    sysColorSurface = this.surface.rgba(),
    sysColorOnSurface = this.onSurface.rgba(),

    sysColorSurfaceBright = sysColorSurfaceBright.rgba(),
    sysColorSurfaceDim = sysColorSurfaceDim.rgba(),

    sysColorSurfaceVariant = this.surfaceVariant.rgba(),
    sysColorOnSurfaceVariant = this.onSurfaceVariant.rgba(),
    sysColorInverseSurface = this.inverseSurface.rgba(),
    sysColorInverseOnSurface = this.inverseOnSurface.rgba(),
    sysColorSurfaceTint = this.surfaceTint.rgba(),

    sysColorSurfaceContainerLowest = sysColorSurfaceContainerLowest.rgba(),
    sysColorSurfaceContainerLow = sysColorSurfaceContainerLow.rgba(),
    sysColorSurfaceContainer = sysColorSurfaceContainer.rgba(),
    sysColorSurfaceContainerHigh = sysColorSurfaceContainerHigh.rgba(),
    sysColorSurfaceContainerHighest = sysColorSurfaceContainerHighest.rgba(),

    sysColorOutline = this.outline.rgba(),
    sysColorOutlineVariant = this.outlineVariant.rgba(),
    sysColorShadow = md_theme_light_shadow.rgba(),
    sysColorScrim = this.scrim.rgba()
)

fun Color.rgba(): com.varabyte.kobweb.compose.ui.graphics.Color =
    com.varabyte.kobweb.compose.ui.graphics.Color.rgba(
        r = red,
        g = green,
        b = blue,
        a = alpha,
    )
