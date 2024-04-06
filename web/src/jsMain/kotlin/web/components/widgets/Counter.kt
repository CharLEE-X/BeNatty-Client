package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.AnimationFillMode
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Li

@Composable
fun CounterAnimation(counter: Int) {
    Box(modifier = WrapStyle.toModifier()) {
        counter.toString().forEachIndexed { index, digit ->
            Li(UnitsStyle.toAttrs()) {
                SpanText(digit.toString())
            }
        }
    }
}

val WrapStyle by ComponentStyle {
    base {
        Modifier
            .textAlign(TextAlign.Center)
            .overflow(Overflow.Auto)
            .height(35.px)
            .lineHeight(35.px)
            .padding(0.px, 10.px)
            .position(Position.Relative)
            .display(DisplayStyle.Flex)
    }
}
val UnitsStyle by ComponentStyle.base {
    Modifier
        .animation(
            Units.toAnimation(
                colorMode,
                duration = 1.s,
                timingFunction = AnimationTimingFunction.steps(100),
                iterationCount = AnimationIterationCount.of(10),
                fillMode = AnimationFillMode.Forwards
            )
        )
}
val Units by Keyframes {
    0.percent {
        Modifier
            .transform { translateY((-1000).percent) }
    }
    100.percent {
        Modifier
            .transform { translateY(0.percent) }
    }
}
