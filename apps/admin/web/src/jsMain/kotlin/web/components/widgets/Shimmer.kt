package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.BackgroundImage
import com.varabyte.kobweb.compose.css.BackgroundPosition
import com.varabyte.kobweb.compose.css.BackgroundSize
import com.varabyte.kobweb.compose.css.CSSBackground
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSPosition
import com.varabyte.kobweb.compose.css.CSSTimeNumericValue
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundPosition
import com.varabyte.kobweb.compose.ui.modifiers.backgroundSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.animation.Keyframes
import com.varabyte.kobweb.silk.components.animation.toAnimation
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.AppColors
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
//    lightColor: Color = MaterialTheme.colors.surfaceContainerLowest,
//    darkColor: Color = MaterialTheme.colors.surfaceContainerHighest,
    delay: CSSTimeNumericValue = 0.s,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                CSSBackground(
                    image = BackgroundImage.of(
                        linearGradient(angle = (-45).deg) {
                            add(AppColors.brandColor)
                            add(AppColors.brandColor)
                            add(AppColors.brandLightened)
                            add(AppColors.brandColor)
                            add(AppColors.brandColor)
                        }
                    )
                )
            )
            .backgroundSize(BackgroundSize.of(400.percent))
            .animation(
                ShimmerAnimation.toAnimation(
                    duration = 1.5.s,
                    delay = delay,
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
) {
    Shimmer(
        modifier = modifier.height(40.px)
    )
}

@Composable
fun ShimmerButton(
    modifier: Modifier = Modifier,
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px
) {
    Shimmer(
        modifier = modifier.height(72.px)
    )
}

@Composable
fun ShimmerText(
    modifier: Modifier = Modifier,
) {
    Shimmer(
        modifier = modifier.height(20.px)
    )
}

@Composable
fun ShimmerLoader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.gap(0.5.em)
    ) {
        Shimmer(Modifier.size(20.px))
        Shimmer(delay = 100.ms, modifier = Modifier.size(20.px))
        Shimmer(delay = 200.ms, modifier = Modifier.size(20.px))
    }
}
