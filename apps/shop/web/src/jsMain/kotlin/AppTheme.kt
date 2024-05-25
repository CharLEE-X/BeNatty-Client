package web

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.graphics.lightened
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.button
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.link
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette

const val HEADLINE_FONT = "Montserrat"

@Composable
fun shadow() = if (ColorMode.current.isLight) Colors.Black.copy(alpha = 25).toRgb()
else Colors.White.copy(alpha = 25).toRgb()

object AppColors {
    val brandColor = Color.rgb(0xc0a888)
    val brandLightened = brandColor.lightened(0.1f)
    val lightBg = Color.rgb(0xf5f5f5)

    @Composable
    fun divider() = ColorMode.current.toPalette().color.toRgb().copy(alpha = 50)
}

@Suppress("unused")
@InitSilk
fun overrideSilkTheme(ctx: InitSilkContext) {
    with(AppColors) {
        ctx.theme.palettes.apply {
            light.apply {
                color = Colors.Black.lightened(0.1f)
                background = Colors.White
                border = Colors.DarkSlateGray
                link.visited = ctx.theme.palettes.light.link.default
                button.apply {
                    default = brandColor
                    hover = brandLightened
                    focus = brandLightened
                    pressed = brandLightened
                }
//            focusOutline
//            overlay
//            placeholder
//            checkbox
//            input
//            switch
//            tab
//            tooltip
            }

            dark.apply {
                color = Colors.White.darkened(0.1f)
                background = Colors.Black
                border = Colors.LightSlateGray
                link.apply {
                    val linkDark = Color.rgb(0x1a85ff)
                    default = linkDark
                    visited = linkDark
                }
                button.apply {
                    default = brandColor
                    hover = brandLightened
                    focus = brandLightened
                    pressed = brandLightened
                }
            }
        }
    }
}
