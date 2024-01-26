package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.style.common.SmoothColorTransitionDurationVar
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.sections.desktopNav.DesktopNavContract
import web.components.sections.desktopNav.label

@Composable
fun AccountLayout(
    item: DesktopNavContract.AccountMenuItem,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(70.percent)
                .gap(2.em)
        ) {
            MenuItems(
                initialItem = item,
                onMenuItemClicked = onMenuItemClicked,
                modifier = Modifier.width(15.em)
            )
            content()
        }
    }
}

@Composable
private fun MenuItems(
    modifier: Modifier,
    initialItem: DesktopNavContract.AccountMenuItem,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
) {
    val items = DesktopNavContract.AccountMenuItem.entries
        .filter { it != DesktopNavContract.AccountMenuItem.LOGOUT }
    var currentItem by remember { mutableStateOf(initialItem) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEach { item ->
            AccountItem(
                item = item,
                isCurrent = currentItem == item,
                onMenuItemClicked = {
                    currentItem = it
                    onMenuItemClicked(it)
                }
            )
        }
    }
}

@Composable
private fun AccountItem(
    item: DesktopNavContract.AccountMenuItem,
    isCurrent: Boolean,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    val bgColor = when {
        isCurrent -> MaterialTheme.colors.mdSysColorPrimary.value()
        !isCurrent && hovered -> MaterialTheme.colors.mdSysColorSurfaceContainerLow.value()
        else -> Colors.Transparent
    }
    val contentColor = when {
        isCurrent -> MaterialTheme.colors.mdSysColorOnPrimary.value()
        else -> MaterialTheme.colors.mdSysColorOnSurface.value()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(bgColor)
            .onClick {
                onMenuItemClicked(item)
            }
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .borderRadius(3.em)
            .transition(CSSTransition("background-color", SmoothColorTransitionDurationVar.value()))
            .cursor(Cursor.Pointer)
    ) {
        SpanText(
            text = item.label(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.5.em)
                .margin(left = 1.em)
                .color(contentColor)
        )
    }
}
