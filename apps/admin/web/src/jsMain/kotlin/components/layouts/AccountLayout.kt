package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEast
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import web.util.onEnterKeyDown

@Composable
fun SideNavMainItem(
    label: String,
    isLogout: Boolean = false,
    isCurrent: Boolean,
    icon: @Composable (() -> Unit)? = null,
    onMenuItemClicked: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
//    val bgColor = if (isLogout) {
//        when {
//            hovered -> Colors.RedContainer
//            else -> Colors.Transparent
//        }
//    } else {
//        when {
//            isCurrent && hovered -> MaterialTheme.colors.primaryContainer
//            isCurrent && !hovered -> MaterialTheme.colors.primary
//            !isCurrent && hovered -> MaterialTheme.colors.surfaceContainerLow
//            else -> Colors.Transparent
//        }
//    }
//
//    val contentColor = if (isLogout) {
//        when {
//            hovered -> MaterialTheme.colors.onErrorContainer
//            else -> Colors.Red
//        }
//    } else {
//        when {
//            isCurrent && hovered -> MaterialTheme.colors.onPrimaryContainer
//            isCurrent && !hovered -> MaterialTheme.colors.onPrimary
//            !isCurrent && hovered -> ColorMode.current.toPalette().color
//            else -> ColorMode.current.toPalette().color
//        }
//    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .onClick { onMenuItemClicked() }
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .cursor(Cursor.Pointer)
            .fillMaxWidth()
            .padding(
                leftRight = 1.em,
                topBottom = 0.5.em,
            )
            .gap(0.5.em)
//            .backgroundColor(bgColor)
//            .color(contentColor)
            .transition(
                Transition.of("background-color", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("color", 0.3.s, TransitionTimingFunction.Ease)
            )
            .tabIndex(0)
            .onEnterKeyDown(onMenuItemClicked)
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
//    val bgColor = when {
//        isSubCurrent && hovered -> MaterialTheme.colors.primaryContainer
//        isSubCurrent && !hovered -> MaterialTheme.colors.primary
//        !isSubCurrent && hovered -> MaterialTheme.colors.surfaceContainerLow
//        else -> Colors.Transparent
//    }
//    val contentColor = when {
//        isSubCurrent && hovered -> MaterialTheme.colors.onPrimaryContainer
//        isSubCurrent && !hovered -> MaterialTheme.colors.onPrimary
//        !isSubCurrent && hovered -> ColorMode.current.toPalette().color
//        else -> ColorMode.current.toPalette().color
//    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
//            .backgroundColor(bgColor)
            .onClick { onMenuItemClicked() }
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .transition(Transition.of("background-color", 0.3.s, TransitionTimingFunction.Ease))
            .cursor(Cursor.Pointer)
            .tabIndex(0)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
//                .color(contentColor)
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
                        Transition.of("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        Transition.of("translate", 0.3.s, TransitionTimingFunction.Ease),
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

