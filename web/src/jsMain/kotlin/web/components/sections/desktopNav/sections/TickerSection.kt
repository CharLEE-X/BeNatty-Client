package web.components.sections.desktopNav.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEco
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme

val TickerAnimation by Keyframes {
    from { Modifier.translateX(0.percent) }
    to { Modifier.translateX((-100).percent) }
}

private const val CHAR_WIDTH = 10

@Composable
fun TickerSection(
    tickerText: String,
    onClick: () -> Unit
) {
    var repeatCount by remember { mutableStateOf(5) }

    LaunchedEffect(window.innerWidth) {
        val textWidthEstimate = tickerText.length * CHAR_WIDTH
        val viewportWidth = window.innerWidth
        repeatCount = (viewportWidth / textWidthEstimate) + 1
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .boxSizing(BoxSizing.BorderBox)
            .display(DisplayStyle.Block)
            .minHeight(40.px)
            .padding(topBottom = 0.5.em)
            .backgroundColor(MaterialTheme.colors.mdSysColorOnBackground.value())
            .color(MaterialTheme.colors.mdSysColorBackground.value())
            .onClick { onClick() }
            .cursor(Cursor.Pointer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .gap(5.em)
                .animation(
                    TickerAnimation.toAnimation(
                        duration = 40.s,
                        timingFunction = AnimationTimingFunction.Linear,
                        iterationCount = AnimationIterationCount.Infinite,
                    )
                )
        ) {
            repeat(repeatCount) {
                MdiEco()
                SpanText(
                    text = tickerText,
                    modifier = Modifier
                        .fontSize(16.px)
                        .whiteSpace(WhiteSpace.NoWrap)
                )
            }
        }
    }
}
