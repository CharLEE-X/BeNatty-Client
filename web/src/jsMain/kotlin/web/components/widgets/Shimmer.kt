package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.BackgroundImage
import com.varabyte.kobweb.compose.css.BackgroundPosition
import com.varabyte.kobweb.compose.css.BackgroundSize
import com.varabyte.kobweb.compose.css.CSSBackground
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSPosition
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundPosition
import com.varabyte.kobweb.compose.ui.modifiers.backgroundSize
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.sp
import web.pages.shop.home.TextPosition

val ShimmerAnimation by Keyframes {
    from {
        Modifier.backgroundPosition(BackgroundPosition.of(CSSPosition(110.percent)))
    }
    to {
        Modifier.backgroundPosition(BackgroundPosition.of(CSSPosition((-20).percent)))
    }
}

@Composable
fun Shimmer(
    modifier: Modifier = Modifier,
    textPosition: TextPosition = TextPosition.Center,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
    lightColor: Color = MaterialTheme.colors.surfaceContainerLowest,
    darkColor: Color = MaterialTheme.colors.surfaceContainerHighest,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .borderRadius(borderRadius)
            .background(
                CSSBackground(
                    image = BackgroundImage.of(
                        linearGradient(angle = (-45).deg) {
                            add(darkColor)
                            add(darkColor)
                            add(lightColor)
                            add(darkColor)
                            add(darkColor)
                        }
                    )
                )
            )
            .backgroundSize(BackgroundSize.of(400.percent))
            .animation(
                ShimmerAnimation.toAnimation(
                    duration = 1.5.s,
                    timingFunction = AnimationTimingFunction.Linear,
                    iterationCount = AnimationIterationCount.Infinite,
                )
            )
    ) {
        Box(
            contentAlignment = when (textPosition) {
                TextPosition.Center -> Alignment.Center
                TextPosition.LeftBottom -> Alignment.BottomStart
                TextPosition.RightTop -> Alignment.TopEnd
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = when (textPosition) {
                    TextPosition.Center -> Alignment.CenterHorizontally
                    TextPosition.LeftBottom -> Alignment.Start
                    TextPosition.RightTop -> Alignment.End
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(0.5.em)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ShimmerHeader(
    modifier: Modifier = Modifier,
    borderRadius: CSSLengthOrPercentageNumericValue = 8.px
) {
    Shimmer(
        borderRadius = borderRadius,
        modifier = modifier.height(40.sp)
    )
}

@Composable
fun ShimmerButton(
    modifier: Modifier = Modifier,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px
) {
    Shimmer(
        borderRadius = borderRadius,
        modifier = modifier.height(72.px)
    )
}

@Composable
fun ShimmerText(
    modifier: Modifier = Modifier,
    borderRadius: CSSLengthOrPercentageNumericValue = 6.px
) {
    Shimmer(
        borderRadius = borderRadius,
        modifier = modifier.height(20.sp)
    )
}