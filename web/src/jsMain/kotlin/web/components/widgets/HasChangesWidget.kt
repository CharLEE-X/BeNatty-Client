package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCancel
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSave
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme

@Composable
fun HasChangesWidget(
    hasChanges: Boolean,
    messageText: String,
    saveText: String,
    resetText: String,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    val startOffset = 2.em
    val targetOffset = (-2).em

    var show by remember { mutableStateOf(hasChanges) }
    var translationY by remember { mutableStateOf(startOffset) }
    var opacity by remember { mutableStateOf(0) }

    LaunchedEffect(hasChanges) {
        if (hasChanges) {
            show = true
            translationY = targetOffset
            opacity = 1
        } else {
            translationY = startOffset
            opacity = 0
            delay(150)
            show = false
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .pointerEvents(PointerEvents.None)
    ) {
        if (show) {
            AppOutlinedCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .opacity(opacity)
                    .fillMaxWidth(80.percent)
                    .maxWidth(40.em)
                    .translateY(translationY)
                    .transition(
                        CSSTransition("translate", 0.15.s, TransitionTimingFunction.Ease),
                        CSSTransition("opacity", 0.15.s, TransitionTimingFunction.Ease),
                    )
                    .padding(1.em)
                    .pointerEvents(PointerEvents.Auto)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.gap(1.em)
                ) {
                    SpanText(
                        text = messageText,
                        modifier = Modifier
                            .fontWeight(FontWeight.SemiBold)
                            .userSelect(UserSelect.None)
                    )
                    Spacer()
                    AppFilledTonalButton(
                        onClick = { onCancel() },
                        leadingIcon = { MdiCancel() },
                    ) {
                        SpanText(resetText)
                    }
                    AppFilledButton(
                        onClick = { onSave() },
                        leadingIcon = { MdiSave() },
                        containerShape = 12.px,
                        containerColor = MaterialTheme.colors.tertiary.value(),
                    ) {
                        SpanText(saveText)
                    }
                }
            }
        }
    }
}
