package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun TopBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit,
    extraActionsStart: @Composable () -> Unit,
    actionCenter: @Composable (Modifier) -> Unit,
    extraActionsEnd: @Composable () -> Unit,
    actionEnd: @Composable () -> Unit,
    isMenuButtonVisible: Boolean,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            AnimatedVisibility(
                visible = isMenuButtonVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                HamburgerMenu(onClick = onMenuClicked)
            }
            extraActionsStart()
            Spacer(modifier = Modifier.weight(1f))
            extraActionsEnd()
            actionEnd()
        }
        actionCenter(Modifier.align(Alignment.Center))
    }
}

@Composable
private fun HamburgerMenu(
    modifier: Modifier = Modifier,
    contentDescription: String = "Menu",
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = contentDescription,
        tint = ColorMode.current.toPalette().color,
        modifier = modifier
            .size(32.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    )
}

@Composable
internal fun BarTitleStyledHeader(headerText: String) {
    Text(
        text = headerText.lowercase(),
        fontSize = MaterialTheme.typography.h5.fontSize,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.primary,
    )
}
