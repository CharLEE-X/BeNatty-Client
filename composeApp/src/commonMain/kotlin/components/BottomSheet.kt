package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val animationDuration = 300

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    modalSheet: ModalSheet,
    showSheet: Boolean,
    containerColor: Color = MaterialTheme.colors.primary,
    content: @Composable ColumnScope.() -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = if (showSheet) Color.Black.copy(alpha = .5f) else Color.Transparent,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing,
        ),
    )

    fun <T> animationSpec() = tween<T>(
        durationMillis = animationDuration,
        easing = FastOutSlowInEasing,
    )

    val enter = when (modalSheet) {
        ModalSheet.MenuDrawer -> slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = animationSpec(),
        )

        ModalSheet.None -> slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = animationSpec(),
        )
    }

    val exit = when (modalSheet) {
        ModalSheet.MenuDrawer -> slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = animationSpec(),
        )

        ModalSheet.None -> slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = animationSpec(),
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor),
    ) {
        AnimatedVisibility(
            visible = showSheet,
            enter = enter,
            exit = exit,
        ) {
            Surface(
                color = containerColor,
            ) {
                Column {
                    content()
                }
            }
        }
    }
}

sealed interface ModalSheet {
    data object None : ModalSheet
    data object MenuDrawer : ModalSheet
}
