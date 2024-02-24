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

fun Modifier.roleStyle(role: TypeScaleTokens.Role): Modifier = styleModifier { applyStyle(role) }

object MaterialTheme {
    @Suppress("ClassName")
    object colors {
        val primary = CSSStyleVariable<Color>("md-sys-color-primary")
        val primaryContainer = CSSStyleVariable<Color>("md-sys-color-primary-container")
        val onPrimary = CSSStyleVariable<Color>("md-sys-color-on-primary")
        val onPrimaryContainer = CSSStyleVariable<Color>("md-sys-color-on-primary-container")
        val secondary = CSSStyleVariable<Color>("md-sys-color-secondary")
        val secondaryContainer = CSSStyleVariable<Color>("md-sys-color-secondary-container")
        val onSecondary = CSSStyleVariable<Color>("md-sys-color-on-secondary")
        val onSecondaryContainer = CSSStyleVariable<Color>("md-sys-color-on-secondary-container")
        val tertiary = CSSStyleVariable<Color>("md-sys-color-tertiary")
        val tertiaryContainer = CSSStyleVariable<Color>("md-sys-color-tertiary-container")
        val onTertiary = CSSStyleVariable<Color>("md-sys-color-on-tertiary")
        val onTertiaryContainer = CSSStyleVariable<Color>("md-sys-color-on-tertiary-container")
        val error = CSSStyleVariable<Color>("md-sys-color-error")
        val errorContainer = CSSStyleVariable<Color>("md-sys-color-error-container")
        val onError = CSSStyleVariable<Color>("md-sys-color-on-error")
        val onErrorContainer = CSSStyleVariable<Color>("md-sys-color-on-error-container")
        val outline = CSSStyleVariable<Color>("md-sys-color-outline")
        val background = CSSStyleVariable<Color>("md-sys-color-background")
        val onBackground = CSSStyleVariable<Color>("md-sys-color-on-background")
        val surface = CSSStyleVariable<Color>("md-sys-color-surface")
        val onSurface = CSSStyleVariable<Color>("md-sys-color-on-surface")
        val surfaceDim = CSSStyleVariable<Color>("md-sys-color-surface-dim")
        val surfaceBright = CSSStyleVariable<Color>("md-sys-color-surface-bright")
        val surfaceContainerLowest = CSSStyleVariable<Color>("md-sys-color-surface-container-lowest")
        val surfaceContainerLow = CSSStyleVariable<Color>("md-sys-color-surface-container-low")
        val surfaceContainer = CSSStyleVariable<Color>("md-sys-color-surface-container")
        val surfaceContainerHigh = CSSStyleVariable<Color>("md-sys-color-surface-container-high")
        val surfaceContainerHighest = CSSStyleVariable<Color>("md-sys-color-surface-container-highest")
        val surfaceVariant = CSSStyleVariable<Color>("md-sys-color-surface-variant")
        val onSurfaceVariant = CSSStyleVariable<Color>("md-sys-color-on-surface-variant")
        val inverseSurface = CSSStyleVariable<Color>("md-sys-color-inverse-surface")
        val inverseOnSurface = CSSStyleVariable<Color>("md-sys-color-inverse-on-surface")
        val inversePrimary = CSSStyleVariable<Color>("md-sys-color-inverse-primary")
        val shadow = CSSStyleVariable<Color>("md-sys-color-shadow")
        val surfaceTint = CSSStyleVariable<Color>("md-sys-color-surface-tint")
        val outlineVariant = CSSStyleVariable<Color>("md-sys-color-outline-variant")
        val scrim = CSSStyleVariable<Color>("md-sys-color-scrim")
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
