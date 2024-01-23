package theme

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import org.jetbrains.compose.web.css.CSSStyleVariable
import theme.TypeScaleTokens.Companion.applyStyle

fun ColorMode.toColors(): SysColorScheme {
    return when (this) {
        ColorMode.LIGHT -> appLightColorScheme
        ColorMode.DARK -> appDarkColorScheme
    }
}

@InitSilk
fun initTheme(ctx: InitSilkContext) {
    ctx.theme.palettes.light.background = appLightColorScheme.sysColorBackground
    ctx.theme.palettes.light.color = appLightColorScheme.sysColorOnBackground
    ctx.theme.palettes.dark.background = appDarkColorScheme.sysColorBackground
    ctx.theme.palettes.dark.color = appDarkColorScheme.sysColorOnBackground
}

fun Modifier.roleStyle(role: TypeScaleTokens.Role): Modifier = Modifier.styleModifier {
    this.applyStyle(role)
}

object MaterialTheme {
    @Suppress("ClassName")
    object colors {
        val mdSysColorPrimary = CSSStyleVariable<Color>("md-sys-color-primary")
        val mdSysColorPrimaryContainer = CSSStyleVariable<Color>("md-sys-color-primary-container")
        val mdSysColorOnPrimary = CSSStyleVariable<Color>("md-sys-color-on-primary")
        val mdSysColorOnPrimaryContainer = CSSStyleVariable<Color>("md-sys-color-on-primary-container")
        val mdSysColorSecondary = CSSStyleVariable<Color>("md-sys-color-secondary")
        val mdSysColorSecondaryContainer = CSSStyleVariable<Color>("md-sys-color-secondary-container")
        val mdSysColorOnSecondary = CSSStyleVariable<Color>("md-sys-color-on-secondary")
        val mdSysColorOnSecondaryContainer = CSSStyleVariable<Color>("md-sys-color-on-secondary-container")
        val mdSysColorTertiary = CSSStyleVariable<Color>("md-sys-color-tertiary")
        val mdSysColorTertiaryContainer = CSSStyleVariable<Color>("md-sys-color-tertiary-container")
        val mdSysColorOnTertiary = CSSStyleVariable<Color>("md-sys-color-on-tertiary")
        val mdSysColorOnTertiaryContainer = CSSStyleVariable<Color>("md-sys-color-on-tertiary-container")
        val mdSysColorError = CSSStyleVariable<Color>("md-sys-color-error")
        val mdSysColorErrorContainer = CSSStyleVariable<Color>("md-sys-color-error-container")
        val mdSysColorOnError = CSSStyleVariable<Color>("md-sys-color-on-error")
        val mdSysColorOnErrorContainer = CSSStyleVariable<Color>("md-sys-color-on-error-container")
        val mdSysColorOutline = CSSStyleVariable<Color>("md-sys-color-outline")
        val mdSysColorBackground = CSSStyleVariable<Color>("md-sys-color-background")
        val mdSysColorOnBackground = CSSStyleVariable<Color>("md-sys-color-on-background")
        val mdSysColorSurface = CSSStyleVariable<Color>("md-sys-color-surface")
        val mdSysColorOnSurface = CSSStyleVariable<Color>("md-sys-color-on-surface")
        val mdSysColorSurfaceDim = CSSStyleVariable<Color>("md-sys-color-surface-dim")
        val mdSysColorSurfaceBright = CSSStyleVariable<Color>("md-sys-color-surface-bright")
        val mdSysColorSurfaceContainerLowest = CSSStyleVariable<Color>("md-sys-color-surface-container-lowest")
        val mdSysColorSurfaceContainerLow = CSSStyleVariable<Color>("md-sys-color-surface-container-low")
        val mdSysColorSurfaceContainer = CSSStyleVariable<Color>("md-sys-color-surface-container")
        val mdSysColorSurfaceContainerHigh = CSSStyleVariable<Color>("md-sys-color-surface-container-high")
        val mdSysColorSurfaceContainerHighest = CSSStyleVariable<Color>("md-sys-color-surface-container-highest")
        val mdSysColorSurfaceVariant = CSSStyleVariable<Color>("md-sys-color-surface-variant")
        val mdSysColorOnSurfaceVariant = CSSStyleVariable<Color>("md-sys-color-on-surface-variant")
        val mdSysColorInverseSurface = CSSStyleVariable<Color>("md-sys-color-inverse-surface")
        val mdSysColorInverseOnSurface = CSSStyleVariable<Color>("md-sys-color-inverse-on-surface")
        val mdSysColorInversePrimary = CSSStyleVariable<Color>("md-sys-color-inverse-primary")
        val mdSysColorShadow = CSSStyleVariable<Color>("md-sys-color-shadow")
        val mdSysColorSurfaceTint = CSSStyleVariable<Color>("md-sys-color-surface-tint")
        val mdSysColorOutlineVariant = CSSStyleVariable<Color>("md-sys-color-outline-variant")
        val mdSysColorScrim = CSSStyleVariable<Color>("md-sys-color-scrim")
    }

    @Suppress("ClassName")
    object typography : TypeScaleTokens("md-sys-typescale") {
        object displayLarge : Role("display-large")
        object displayMedium : Role("display-medium")
        object displaySmall : Role("display-small")
        object headlineLarge : Role("headline-large")
        object headlineMedium : Role("headline-medium")
        object headlineSmall : Role("headline-small")
        object titleLarge : Role("title-large")
        object titleMedium : Role("title-medium")
        object titleSmall : Role("title-small")
        object bodyLarge : Role("body-large")
        object bodyMedium : Role("body-medium")
        object bodySmall : Role("body-small")
        object labelLarge : Role("label-large")
        object labelMedium : Role("label-medium")
        object labelSmall : Role("label-small")

    }

}
