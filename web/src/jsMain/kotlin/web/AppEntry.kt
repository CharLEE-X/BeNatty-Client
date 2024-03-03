package web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import feature.root.rootModule
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.vh
import org.koin.core.context.startKoin
import theme.MaterialTheme
import theme.OldColorsJs
import web.compose.material3.theming.MaterialTheme

private const val COLOR_MODE_KEY = "nataliashop:colorMode"

@Suppress("unused")
@InitSilk
fun initColorMode(ctx: InitSilkContext) {
    ctx.config.initialColorMode = localStorage.getItem(COLOR_MODE_KEY)?.let { ColorMode.valueOf(it) } ?: ColorMode.DARK
}

@InitSilk
fun registerKeyframes(ctx: InitSilkContext) = ctx.config.apply {
    val offset = ((-3..3).random() * 100).px
    val size = ((1..6).random() * 100).px
    val color = listOf(
        OldColorsJs.primary, OldColorsJs.secondary, OldColorsJs.green, OldColorsJs.purple, OldColorsJs.red
    ).random()

    (1..9).forEach {
        ctx.stylesheet.registerKeyframes("bg-keyframe-$it") {
            val keyframes = Modifier
                .boxShadow(
                    offsetX = offset,
                    offsetY = offset,
                    blurRadius = size,
                    spreadRadius = size,
                    color = color,
                )

            from { keyframes }
            to { keyframes }
        }
    }
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
            console.error("Failed to start Koin", e)
        }

        MaterialTheme(colorScheme(colorMode), appFontScheme) {
            Box(
                modifier = Modifier
                    .minHeight(100.vh)
                    .scrollBehavior(ScrollBehavior.Smooth)
                    .backgroundColor(MaterialTheme.colors.background)
                    .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease))
            ) {
                content()
            }
        }
    }
}

@JsModule("@js-joda/timezone")
@JsNonModule
external object JsJodaTimeZoneModule

private val jsJodaTz = JsJodaTimeZoneModule
