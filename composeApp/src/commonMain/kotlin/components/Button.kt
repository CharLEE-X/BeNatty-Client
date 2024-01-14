package components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun IconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    size: Dp = 32.dp,
    colorActive: Color = MaterialTheme.colors.primary,
    colorInActive: Color = MaterialTheme.colors.onBackground,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val transition = updateTransition(isPressed, label = "transition")
    val scaleState by transition.animateFloat { if (it) .9f else 1f }

    val colorState by animateColorAsState(if (isPressed) colorActive else colorInActive)

    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = colorState,
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                scaleX = scaleState
                scaleY = scaleState
            }
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick,
            )
    )
}
