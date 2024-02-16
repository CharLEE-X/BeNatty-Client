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
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scaleY
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiClose
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.dom.Text
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Dialog
import web.compose.material3.component.FilledButton
import web.compose.material3.component.FilledTonalIconButton
import web.compose.material3.component.TextButton
import web.compose.material3.component.labs.FilledCard

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
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(MaterialTheme.colors.mdSysColorOnBackground.value())
                .opacity(if (open) 0.7 else 0.0)
                .onClick { onClose() }
                .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease)),
        )
        FilledCard(
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
                            modifier = Modifier
                                .padding(1.em)
                                .roleStyle(MaterialTheme.typography.headlineLarge)
                        )
                    }
                    Spacer()
                    FilledTonalIconButton(
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
                        .borderRadius(20.px)
                        .objectFit(ObjectFit.Cover)
                        .onClick { it.preventDefault() }
                )
            }
        }
    }
}

@Composable
fun DeleteDialog(
    open: Boolean,
    title: String,
    actionYesText: String,
    actionNoText: String,
    contentText: String,
    closing: Boolean,
    onOpen: (Boolean) -> Unit,
    onClosing: (Boolean) -> Unit,
    onYes: () -> Unit,
    onNo: () -> Unit,
) {
    if (open || closing) {
        Dialog(
            open = open && !closing,
            onClosed = {
                onOpen(false)
                onClosing(false)
            },
            onClosing = { onClosing(true) },
            headline = {
                Text(title)
            },
            actions = {
                TextButton(
                    onClick = {
                        onYes()
                        onClosing(true)
                    }
                ) {
                    Text(actionYesText)
                }
                FilledButton(
                    onClick = {
                        onNo()
                        onClosing(true)
                    }
                ) {
                    Text(actionNoText)
                }
            }
        ) {
            SpanText(contentText)
        }
    }
}
