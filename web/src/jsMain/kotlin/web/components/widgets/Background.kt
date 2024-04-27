package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.components.sections.ScrollDirection


@Composable
fun Background() {
    var lastScrollPosition by remember { mutableStateOf(0.0) }
    var scrollDirection: ScrollDirection by remember { mutableStateOf(ScrollDirection.DOWN) }

    var box1Offset by remember { mutableStateOf(0.px) }
    var box2Offset by remember { mutableStateOf(0.px) }

    window.addEventListener("scroll", {
        val currentScroll = window.scrollY
        scrollDirection = if (lastScrollPosition < currentScroll) ScrollDirection.DOWN else ScrollDirection.UP
        lastScrollPosition = currentScroll

        box1Offset = if (scrollDirection == ScrollDirection.DOWN) box1Offset - 2.px else box1Offset + 2.px
        box2Offset = if (scrollDirection == ScrollDirection.DOWN) box2Offset - 2.px else box2Offset + 2.px
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .translate(
                    tx = (-50).percent + box1Offset,
                    ty = (-50).percent + box1Offset,
                )
                .shadowModifier(MaterialTheme.colors.surfaceContainerHigh)
                .transition(CSSTransition("translate", 2.s, TransitionTimingFunction.EaseInOut))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .translate(
                    tx = (-50).percent + box2Offset,
                    ty = (50).percent + box2Offset,
                )
                .shadowModifier(
                    MaterialTheme.colors.tertiaryContainer.toRgb().copy(
                        alpha = if (ColorMode.current.isLight) 200 else 50
                    )
                )
                .transition(CSSTransition("translate", 3.s, TransitionTimingFunction.EaseInOut))
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .translate(
                    tx = (50).percent + box2Offset,
                    ty = (50).percent + box2Offset,
                )
                .shadowModifier(
                    MaterialTheme.colors.surfaceContainerHighest.toRgb()
                        .copy(alpha = if (ColorMode.current.isLight) 200 else 100)
                )
                .transition(CSSTransition("translate", 2.s, TransitionTimingFunction.EaseInOut))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .translate(
                    tx = (50).percent + box2Offset,
                    ty = (-50).percent + box2Offset,
                )
                .shadowModifier(
                    MaterialTheme.colors.primaryContainer.toRgb().copy(
                        alpha = if (ColorMode.current.isLight) 200 else 50
                    )
                )
                .transition(CSSTransition("translate", 3.5.s, TransitionTimingFunction.EaseInOut))
        )
    }
}

private fun Modifier.shadowModifier(
    color: CSSColorValue,
    offsetX: CSSLengthNumericValue = 0.px,
    offsetY: CSSLengthNumericValue = 0.px,
) = this
    .size(800.px)
    .backgroundColor(color)
    .borderRadius(50.percent)
    .boxShadow(
        offsetX = offsetX,
        offsetY = offsetY,
        color = color,
        blurRadius = 80.px,
        spreadRadius = 100.px,
    )
    .transition(CSSTransition("box-shadow", 0.7.s, TransitionTimingFunction.EaseInOut))
