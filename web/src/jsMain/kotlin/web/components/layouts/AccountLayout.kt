package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEast
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.shop.navbar.DesktopNavContract
import feature.shop.navbar.label
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme

@Composable
fun AccountLayout(
    item: DesktopNavContract.AccountMenuItem,
    logoutText: String,
    onLogoutClicked: () -> Unit,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(70.percent)
                .gap(2.em)
        ) {
            MenuItems(
                initialItem = item,
                logoutText = logoutText,
                onLogoutClicked = onLogoutClicked,
                onMenuItemClicked = onMenuItemClicked,
                modifier = Modifier.width(15.em)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
                    .padding(top = 0.5.em)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun MenuItems(
    modifier: Modifier,
    initialItem: DesktopNavContract.AccountMenuItem,
    logoutText: String,
    onMenuItemClicked: (DesktopNavContract.AccountMenuItem) -> Unit,
    onLogoutClicked: () -> Unit,
) {
    val items = DesktopNavContract.AccountMenuItem.entries
        .filter { it != DesktopNavContract.AccountMenuItem.LOGOUT }
    var currentItem by remember { mutableStateOf(initialItem) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEach { item ->
            SideNavMainItem(
                label = item.label(),
                isCurrent = currentItem == item,
                onMenuItemClicked = {
                    currentItem = item
                    onMenuItemClicked(item)
                }
            )
        }
        SideNavMainItem(
            label = logoutText,
            isCurrent = false,
            isLogout = true,
            onMenuItemClicked = { onLogoutClicked() }
        )
    }
}

@Composable
fun SideNavMainItem(
    label: String,
    isLogout: Boolean = false,
    isCurrent: Boolean,
    icon: @Composable (() -> Unit)? = null,
    onMenuItemClicked: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
    val bgColor = if (isLogout) {
        when {
            hovered -> MaterialTheme.colors.errorContainer.value()
            else -> Colors.Transparent
        }
    } else {
        when {
            isCurrent && hovered -> MaterialTheme.colors.primaryContainer.value()
            isCurrent && !hovered -> MaterialTheme.colors.primary.value()
            !isCurrent && hovered -> MaterialTheme.colors.surfaceContainerLow.value()
            else -> Colors.Transparent
        }
    }

    val contentColor = if (isLogout) {
        when {
            hovered -> MaterialTheme.colors.onErrorContainer.value()
            else -> MaterialTheme.colors.error.value()
        }
    } else {
        when {
            isCurrent && hovered -> MaterialTheme.colors.onPrimaryContainer.value()
            isCurrent && !hovered -> MaterialTheme.colors.onPrimary.value()
            !isCurrent && hovered -> MaterialTheme.colors.onSurface.value()
            else -> MaterialTheme.colors.onSurface.value()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .onClick { onMenuItemClicked() }
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .borderRadius(0.5.em)
            .cursor(Cursor.Pointer)
            .fillMaxWidth()
            .padding(
                leftRight = 1.em,
                topBottom = 0.5.em,
            )
            .gap(0.5.em)
            .backgroundColor(bgColor)
            .color(contentColor)
            .transition(
                CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease)
            )
    ) {
        icon?.invoke()
        SpanText(
            text = label,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer()
    }
}

@Composable
fun SideNavSubItem(
    label: String,
    isSubCurrent: Boolean,
    onMenuItemClicked: () -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    val bgColor = when {
        isSubCurrent && hovered -> MaterialTheme.colors.primaryContainer.value()
        isSubCurrent && !hovered -> MaterialTheme.colors.primary.value()
        !isSubCurrent && hovered -> MaterialTheme.colors.surfaceContainerLow.value()
        else -> Colors.Transparent
    }
    val contentColor = when {
        isSubCurrent && hovered -> MaterialTheme.colors.onPrimaryContainer.value()
        isSubCurrent && !hovered -> MaterialTheme.colors.onPrimary.value()
        !isSubCurrent && hovered -> MaterialTheme.colors.onSurface.value()
        else -> MaterialTheme.colors.onSurface.value()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(bgColor)
            .onClick { onMenuItemClicked() }
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .borderRadius(0.5.em)
            .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease))
            .cursor(Cursor.Pointer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .color(contentColor)
                .padding(
                    leftRight = 1.em,
                    topBottom = 0.5.em,
                )
                .gap(0.5.em)
        ) {
            MdiEast(
                modifier = Modifier
                    .opacity(if (isSubCurrent) 1f else 0f)
                    .translateX(if (isSubCurrent) 0.em else (-0.5).em)
                    .transition(
                        CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                    )
            )
            SpanText(
                text = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .textOverflow(TextOverflow.Ellipsis)
            )
            Spacer()
        }
    }
}

