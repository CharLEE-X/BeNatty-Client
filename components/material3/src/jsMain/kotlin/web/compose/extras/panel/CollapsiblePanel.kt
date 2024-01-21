package web.compose.extras.panel

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.CSSLengthValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnitTime
import org.jetbrains.compose.web.css.duration
import org.jetbrains.compose.web.css.overflow
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.transform
import org.jetbrains.compose.web.css.transitions
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
fun CollapsiblePanel(
    opened: Boolean,
    reverse: Boolean = false,
    animationDuration: CSSSizeValue<out CSSUnitTime> = 1.s,
    panelWidth: CSSLengthValue = 175.px,
    content: (@Composable () -> Unit)? = null
) {
    Div({
        @OptIn(ExperimentalComposeWebApi::class)
        style {
            overflow("hidden")
            if (!opened) {
                width(0.px)
            }
            if (opened) {
                width(panelWidth)
            }

            transitions {
                "width" { duration(animationDuration) }
            }
        }
    }) {
        Div({
            @OptIn(ExperimentalComposeWebApi::class)
            style {
                width(panelWidth)
                if (!opened) {
                    if (!reverse) {
                        transform { translateX(-panelWidth) }
                    }
                }

                transitions {
                    "transform" { duration(animationDuration) }
                }
            }
            addEventListener("transitionend") {
                console.log("Animation ended ")
            }
        }) {
            content?.invoke()
        }
    }
}
