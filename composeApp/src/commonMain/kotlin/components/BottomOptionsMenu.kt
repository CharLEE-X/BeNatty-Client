package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import theme.Dimensions

private const val ANIM_DURATION = 800

data class BottomOption(val title: String, val textColor: Color = Color.Black, val onClick: () -> Unit)

@Composable
fun BottomOptionsMenu(
    modifier: Modifier = Modifier,
    options: List<BottomOption>,
    containerColor: Color = Color.Transparent,
) {
    val bgColor by animateColorAsState(
        targetValue = Color.Black.copy(alpha = .5f),
        animationSpec = tween(
            durationMillis = ANIM_DURATION,
            easing = FastOutSlowInEasing,
        ),
    )

    fun <T> animationSpec() = tween<T>(
        durationMillis = ANIM_DURATION,
        easing = FastOutSlowInEasing,
    )

    val enter = slideInVertically(
        initialOffsetY = { it },
        animationSpec = animationSpec(),
    )

    val exit = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = animationSpec(),
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(bottom = Dimensions.BOTTOM_BAR_HEIGHT.dp),
    ) {
        AnimatedVisibility(
            visible = true,
            enter = enter,
            exit = exit,
        ) {
            Surface(
                color = containerColor,
                shape = RoundedCornerShape(48.dp),
                modifier = Modifier.fillMaxWidth(0.75f),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    options.forEach { option ->
                        Button(
                            onClick = option.onClick,
                            shape = RoundedCornerShape(0.dp),
                            elevation = null,
                            modifier = Modifier.fillMaxWidth()
                                .padding(0.dp),
                        ) {
                            Text(text = option.title)
                        }
                    }
                }
            }
        }
    }
}
