package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scaleY
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiClose
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.s
import web.H1Variant
import web.HeadlineStyle

@Composable
fun ImagePreviewDialog(
    open: Boolean,
    imageUrl: String,
    alt: String?,
    onClose: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .zIndex(200)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(ColorMode.current.toPalette().color)
                .opacity(if (open) 0.7 else 0.0)
                .onClick { onClose() }
                .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease)),
        )
        AppFilledCard(
            modifier = Modifier
                .fillMaxSize(70.percent)
                .opacity(if (open) 1.0 else 0.0)
                .scaleY(if (open) 1.0 else 0.5)
                .transition(
                    CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.em)
                    .gap(1.em)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    alt?.let {
                        SpanText(
                            text = it,
                            modifier = HeadlineStyle.toModifier(H1Variant)
                                .padding(1.em)
                        )
                    }
                    Spacer()
                    AppIconButton(
                        onClick = { onClose() },
                    ) {
                        MdiClose()
                    }
                }
                Image(
                    src = imageUrl,
                    alt = alt ?: "",
                    modifier = Modifier
                        .fillMaxSize()
                        .objectFit(ObjectFit.Cover)
                        .onClick { it.preventDefault() }
                )
            }
        }
    }
}

@Composable
fun TakeActionDialog(
    open: Boolean,
    title: String,
    actionYesText: String = getString(Strings.Delete),
    actionNoText: String = getString(Strings.Discard),
    contentText: String,
    closing: Boolean,
    onOpen: (Boolean) -> Unit,
    onClosing: (Boolean) -> Unit,
    onYes: () -> Unit,
    onNo: () -> Unit,
) {
    if (open || closing) {
        SpanText("FIXME: Implement TakeActionDialog")
//        Dialog(
//            open = open && !closing,
//            onClosed = {
//                onOpen(false)
//                onClosing(false)
//            },
//            onClosing = { onClosing(true) },
//            headline = {
//                Text(title)
//            },
//            actions = {
//                AppFilledButton(
//                    onClick = {
//                        onNo()
//                        onClosing(true)
//                    },
//                    leadingIcon = { MdiCancel() }
//                ) {
//                    Text(actionNoText)
//                }
//                AppFilledButton(
//                    onClick = {
//                        onYes()
//                        onClosing(true)
//                    },
//                    leadingIcon = { MdiDelete() },
//                    containerColor = Colors.Red
//                ) {
//                    Text(actionYesText)
//                }
//            },
//            modifier = Modifier
//                .zIndex(1000)
//        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .margin(
                    topBottom = 0.5.em,
                    leftRight = 2.em
                )
        ) {
            SpanText(contentText)
        }
    }
}
