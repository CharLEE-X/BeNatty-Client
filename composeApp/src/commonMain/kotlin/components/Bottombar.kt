package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.router.Screen

@Composable
internal fun BottomBar(
    modifier: Modifier = Modifier,
    routes: List<Screen>,
    currentRoute: Screen,
    onDestinationClick: (Screen) -> Unit,
    show: Boolean,
) {
    AnimatedVisibility(
        visible = show,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = modifier,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        bottom = 12.dp,
                    ),
            ) {
                routes.forEach { route ->
                    val hasNotification by remember { mutableStateOf(listOf(true, false).random()) }

                    BottomBarItem(
                        route = route,
                        isCurrentRoute = route == currentRoute,
                        onClick = { onDestinationClick(route) },
                        hasNotification = hasNotification,
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    route: Screen,
    isCurrentRoute: Boolean,
    onClick: () -> Unit,
    hasNotification: Boolean,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val tint by animateColorAsState(
        if (isCurrentRoute) {
            MaterialTheme.colors.onBackground
        } else {
            Color.Gray
        },
    )

    Box {
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "route",
            tint = tint,
            modifier = Modifier
                .size(32.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        )
        AnimatedVisibility(
            visible = hasNotification,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd),
        ) {
            NotificationDot()
        }
    }
}

@Composable
private fun NotificationDot() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colors.background,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colors.error,
            modifier = Modifier
                .size(12.dp)
                .padding(2.dp),
        ) {
            // No-op
        }
    }
}
