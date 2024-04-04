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
import com.varabyte.kobweb.compose.ui.modifiers.color
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
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCancel
import com.varabyte.kobweb.silk.components.icons.mdi.MdiClose
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Text
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Dialog

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
                .backgroundColor(MaterialTheme.colors.onBackground)
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
                            modifier = Modifier
                                .padding(1.em)
                                .roleStyle(MaterialTheme.typography.headlineLarge)
                        )
                    }
                    Spacer()
                    AppFilledTonalIconButton(
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
fun TakeActionDialog(
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
                AppFilledTonalButton(
                    onClick = {
                        onNo()
                        onClosing(true)
                    },
                    leadingIcon = { MdiCancel() }
                ) {
                    Text(actionNoText)
                }
                AppFilledButton(
                    onClick = {
                        onYes()
                        onClosing(true)
                    },
                    leadingIcon = { MdiDelete() },
                    containerColor = MaterialTheme.colors.error
                ) {
                    Text(actionYesText)
                }
            }
        ) {
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
}

@Composable
fun AddContentDialog(
    open: Boolean,
    title: String,
    actionAddText: String,
    actionDismissText: String,
    closing: Boolean,
    isAddButtonEnabled: Boolean,
    addIcon: @Composable (Modifier) -> Unit = { MdiAdd(it) },
    onOpen: (Boolean) -> Unit,
    onClosing: (Boolean) -> Unit,
    onAdd: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
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
                AppFilledButton(
                    onClick = {
                        onDismiss()
                        onClosing(true)
                    },
                    leadingIcon = { MdiCancel(Modifier.color(MaterialTheme.colors.onTertiary)) },
                    containerColor = MaterialTheme.colors.tertiary,
                    modifier = Modifier.width(150.px)
                ) {
                    SpanText(
                        text = actionDismissText,
                        modifier = Modifier.color(MaterialTheme.colors.onTertiary)
                    )
                }
                AppFilledButton(
                    onClick = { onAdd() },
                    leadingIcon = { addIcon(Modifier.color(MaterialTheme.colors.onSecondary)) },
                    containerColor = MaterialTheme.colors.secondary,
                    disabled = !isAddButtonEnabled,
                    modifier = Modifier.width(150.px)
                ) {
                    SpanText(
                        text = actionAddText,
                        modifier = Modifier.color(MaterialTheme.colors.onSecondary)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.em)
                    .gap(1.em)
            ) {
                content()
            }
        }
    }
}
