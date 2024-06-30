package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.icons.mdi.MdiKeyboardReturn
import com.varabyte.kobweb.silk.components.icons.mdi.MdiKeyboardTab
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s

@Composable
fun TrailingIconGoToNextOrSubmit(show: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(0.25.em)
            .opacity(if (show) 0.6 else 0)
            .transition(
                Transition.of("opacity", 0.3.s, TransitionTimingFunction.EaseInOut)
            )
    ) {
        MdiKeyboardTab()
        SpanText(
            text = "next or",
        )
        MdiKeyboardReturn()
        SpanText(
            text = "submit",
        )
    }
}

@Composable
fun TrailingIconGoToNext(show: Boolean) {
    MdiKeyboardTab(
        modifier = Modifier
            .opacity(if (show) 0.6 else 0)
            .transition(
                Transition.of("opacity", 0.3.s, TransitionTimingFunction.EaseInOut)
            )
    )
}

@Composable
fun TrailingIconSubmit(show: Boolean) {
    MdiKeyboardTab(
        modifier = Modifier
            .opacity(if (show) 0.6 else 0)
            .transition(
                Transition.of("opacity", 0.3.s, TransitionTimingFunction.EaseInOut)
            )
    )
}
