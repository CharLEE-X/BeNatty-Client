package theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

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
    sysColorPrimary = this.primary.rgb(),
    sysColorOnPrimary = this.onPrimary.rgb(),
    sysColorInversePrimary = this.inversePrimary.rgb(),
    sysColorPrimaryContainer = this.primaryContainer.rgb(),
    sysColorOnPrimaryContainer = this.onPrimaryContainer.rgb(),
    sysColorSecondary = this.secondary.rgb(),
    sysColorOnSecondary = this.onSecondary.rgb(),
    sysColorSecondaryContainer = this.secondaryContainer.rgb(),
    sysColorOnSecondaryContainer = this.onSecondaryContainer.rgb(),
    sysColorTertiary = this.tertiary.rgb(),
    sysColorOnTertiary = this.onTertiary.rgb(),
    sysColorTertiaryContainer = this.tertiaryContainer.rgb(),
    sysColorOnTertiaryContainer = this.onTertiaryContainer.rgb(),
    sysColorError = this.error.rgb(),
    sysColorErrorContainer = this.errorContainer.rgb(),
    sysColorOnError = this.onError.rgb(),
    sysColorOnErrorContainer = this.onErrorContainer.rgb(),
    sysColorBackground = this.background.rgb(),
    sysColorOnBackground = this.onBackground.rgb(),
    sysColorSurface = this.surface.rgb(),
    sysColorOnSurface = this.onSurface.rgb(),

    sysColorSurfaceBright = sysColorSurfaceBright.rgb(),
    sysColorSurfaceDim = sysColorSurfaceDim.rgb(),

    sysColorSurfaceVariant = this.surfaceVariant.rgb(),
    sysColorOnSurfaceVariant = this.onSurfaceVariant.rgb(),
    sysColorInverseSurface = this.inverseSurface.rgb(),
    sysColorInverseOnSurface = this.inverseOnSurface.rgb(),
    sysColorSurfaceTint = this.surfaceTint.rgb(),

    sysColorSurfaceContainerLowest = sysColorSurfaceContainerLowest.rgb(),
    sysColorSurfaceContainerLow = sysColorSurfaceContainerLow.rgb(),
    sysColorSurfaceContainer = sysColorSurfaceContainer.rgb(),
    sysColorSurfaceContainerHigh = sysColorSurfaceContainerHigh.rgb(),
    sysColorSurfaceContainerHighest = sysColorSurfaceContainerHighest.rgb(),

    sysColorOutline = this.outline.rgb(),
    sysColorOutlineVariant = this.outlineVariant.rgb(),
    sysColorShadow = md_theme_light_shadow.rgb(),
    sysColorScrim = this.scrim.rgb()
)

fun Color.rgb(): com.varabyte.kobweb.compose.ui.graphics.Color =
    com.varabyte.kobweb.compose.ui.graphics.Color.rgb(red, green, blue)
