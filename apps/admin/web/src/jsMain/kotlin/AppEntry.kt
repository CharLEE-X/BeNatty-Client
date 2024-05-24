package web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import feature.root.rootModule
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.vh
import org.koin.core.context.startKoin

private const val COLOR_MODE_KEY = "nataliashop:colorMode"

@Suppress("unused")
@InitSilk
fun initColorMode(ctx: InitSilkContext) {
    ctx.config.initialColorMode = localStorage.getItem(COLOR_MODE_KEY)?.let { ColorMode.valueOf(it) } ?: ColorMode.DARK
}

@Suppress("unused")
@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    SilkApp {
        val colorMode = ColorMode.current
        LaunchedEffect(colorMode) {
            localStorage.setItem(COLOR_MODE_KEY, colorMode.name)
        }

        try {
            startKoin {
                modules(rootModule)
            }
        } catch (e: Exception) {
            // Do nothing
        }

        Box(
            modifier = Modifier
                .minHeight(100.vh)
                .scrollBehavior(ScrollBehavior.Smooth)
                .backgroundColor(colorMode.toPalette().background)
                .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            content()
        }
    }
}

@JsModule("@js-joda/timezone")
@JsNonModule
external object JsJodaTimeZoneModule

private val jsJodaTz = JsJodaTimeZoneModule
