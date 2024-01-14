package components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

@Composable
internal fun CounterAnimation(
    count: Int,
    content: @Composable (Int) -> Unit,
) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically { height -> height } + fadeIn(tween(durationMillis = 100))).togetherWith(
                    slideOutVertically { height -> -(height / 2) } + fadeOut(tween(durationMillis = 100)),
                )
            } else {
                (slideInVertically { height -> -(height / 2) } + fadeIn(tween(durationMillis = 100))).togetherWith(
                    slideOutVertically { height -> height } + fadeOut(tween(durationMillis = 100)),
                )
            }.using(SizeTransform(clip = false))
        },
    ) {
        content(it)
    }
}
