package com.charleex.nataliashop

import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color

/**
 * @property nearBackground A useful color to apply to a container that should differentiate itself from the background
 *   but just a little.
 */
class SitePalette(
    val background: Color,
    val onBackground: Color,
    val nearBackground: Color,
    val border: Color,
    val surface: Color,
    val onSurface: Color,

    val primary: Color,
    val primaryLighter: Color,
    val primaryDarker: Color,

    val secondary: Color,
    val secondaryDarker: Color,
    val secondaryLighter: Color,

    val error: Color,

    val neutral50: Color,
    val neutral100: Color,
    val neutral200: Color,
    val neutral300: Color,
    val neutral400: Color,
    val neutral500: Color,
    val neutral600: Color,
    val neutral700: Color,
    val neutral800: Color,
    val neutral900: Color
)

object SitePalettes {
    val light = SitePalette(
        background = Colors.White,
        onBackground = Colors.Black,
        nearBackground = theme.Colors.neutral100.rgb(),
        border = theme.Colors.neutral300.rgb(),
        surface = Colors.Gray,
        onSurface = Colors.Black,
        primary = theme.Colors.primary.rgb(),
        primaryLighter = theme.Colors.primary.rgb(),
        primaryDarker = theme.Colors.primary.rgb(),
        secondary = theme.Colors.secondary.rgb(),
        secondaryDarker = theme.Colors.secondary.rgb(),
        secondaryLighter = theme.Colors.secondary.rgb(),
        error = theme.Colors.red.rgb(),
        neutral50 = theme.Colors.neutral50.rgb(),
        neutral100 = theme.Colors.neutral100.rgb(),
        neutral200 = theme.Colors.neutral200.rgb(),
        neutral300 = theme.Colors.neutral300.rgb(),
        neutral400 = theme.Colors.neutral400.rgb(),
        neutral500 = theme.Colors.neutral500.rgb(),
        neutral600 = theme.Colors.neutral600.rgb(),
        neutral700 = theme.Colors.neutral700.rgb(),
        neutral800 = theme.Colors.neutral800.rgb(),
        neutral900 = theme.Colors.neutral900.rgb(),
    )
    val dark = SitePalette(
        background = Colors.Black,
        onBackground = Colors.White,
        nearBackground = theme.Colors.neutral900.rgb(),
        border = theme.Colors.neutral700.rgb(),
        surface = Colors.DarkGray,
        onSurface = Colors.White,
        primaryLighter = theme.Colors.primaryLighter.rgb(),
        primary = theme.Colors.primary.rgb(),
        primaryDarker = theme.Colors.primaryDarker.rgb(),
        secondaryLighter = theme.Colors.secondaryLighter.rgb(),
        secondary = theme.Colors.secondary.rgb(),
        secondaryDarker = theme.Colors.secondaryDarker.rgb(),
        error = theme.Colors.red.rgb(),
        neutral50 = theme.Colors.neutral900.rgb(),
        neutral100 = theme.Colors.neutral800.rgb(),
        neutral200 = theme.Colors.neutral700.rgb(),
        neutral300 = theme.Colors.neutral600.rgb(),
        neutral400 = theme.Colors.neutral500.rgb(),
        neutral500 = theme.Colors.neutral400.rgb(),
        neutral600 = theme.Colors.neutral300.rgb(),
        neutral700 = theme.Colors.neutral200.rgb(),
        neutral800 = theme.Colors.neutral100.rgb(),
        neutral900 = theme.Colors.neutral50.rgb(),
    )
}

fun ColorMode.toSitePalette(): SitePalette {
    return when (this) {
        ColorMode.LIGHT -> SitePalettes.light
        ColorMode.DARK -> SitePalettes.dark
    }
}

@InitSilk
fun initTheme(ctx: InitSilkContext) {
    ctx.theme.palettes.light.background = SitePalettes.light.background
    ctx.theme.palettes.light.color = SitePalettes.light.onBackground
    ctx.theme.palettes.dark.background = SitePalettes.dark.background
    ctx.theme.palettes.dark.color = SitePalettes.dark.onBackground
}

private fun androidx.compose.ui.graphics.Color.rgb(): Color = Color.rgb(red, green, blue)
