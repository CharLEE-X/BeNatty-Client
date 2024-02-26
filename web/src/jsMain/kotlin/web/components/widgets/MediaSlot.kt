package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAddAPhoto
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCloudUpload
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import org.w3c.files.File
import theme.MaterialTheme
import theme.roleStyle


@Composable
fun MediaSlot(
    url: String?,
    alt: String?,
    errorText: String?,
    cornerRadius: CSSLengthOrPercentageNumericValue = 14.px,
    isImagesLoading: Boolean = false,
    onFileDropped: (File) -> Unit,
    onImageClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    var imageHovered by remember { mutableStateOf(false) }
    var addIconHovered by remember { mutableStateOf(false) }
    var deleteIconHovered by remember { mutableStateOf(false) }

    val size = 200.px

    AppFilledCard(
        modifier = Modifier
            .size(size)
            .borderRadius(cornerRadius)
            .overflow(Overflow.Hidden)
            .onMouseOver { imageHovered = true }
            .onMouseOut { imageHovered = false }
    ) {
        url?.let {
            Box {
                Image(
                    src = url,
                    alt = alt ?: "",
                    modifier = Modifier
                        .size(size)
                        .borderRadius(cornerRadius)
                        .onClick { onImageClick() }
                        .scale(if (imageHovered) 1.05 else 1.0)
                        .onMouseOver { imageHovered = true }
                        .onMouseOut { imageHovered = false }
                        .objectFit(ObjectFit.Cover)
                        .transition(CSSTransition("scale", 0.5.s, TransitionTimingFunction.Ease))
                )
                Box(
                    modifier = Modifier
                        .size(size)
                        .borderRadius(cornerRadius)
                        .objectFit(ObjectFit.Cover)
                        .backgroundColor(
//                            if (imageHovered || deleteIconHovered) {
                            MaterialTheme.colors.onSurface.value()
//                            } else Colors.Transparent
                        )
                        .transition(CSSTransition("backgroundColor", 0.3.s, TransitionTimingFunction.Ease))
                ) {}
                AppFilledTonalIconButton(
                    onClick = { onDeleteClick() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .margin(0.5.em)
                        .onMouseOver { deleteIconHovered = true }
                        .onMouseOut { deleteIconHovered = false }
                        .opacity(if (imageHovered || deleteIconHovered) 1.0 else 0.0)
                        .scale(if (deleteIconHovered) 1.05 else 1.0)
                        .transition(
                            CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                        )
                ) {
                    MdiDelete()
                }
            }
        } ?: Box(
            contentAlignment = Alignment.Center,
            modifier = com.varabyte.kobweb.compose.ui.Modifier
                .borderRadius(cornerRadius)
                .border(
                    width = 2.px,
                    color = MaterialTheme.colors.inverseSurface.value(),
                    style = LineStyle.Dashed,
                )
                .size(size)
                .backgroundColor(
                    if (imageHovered || addIconHovered) {
                        MaterialTheme.colors.surfaceContainer.value()
                    } else MaterialTheme.colors.surface.value()
                )
                .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            if (isImagesLoading) {
                var opacity by remember { mutableStateOf(1.0) }
                LaunchedEffect(isImagesLoading) {
                    while (isImagesLoading) {
                        opacity = 0.5
                        delay(600)
                        opacity = 1.0
                        delay(600)
                    }
                }
                MdiCloudUpload(
                    modifier = Modifier
                        .opacity(opacity)
                        .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
                )
            } else {
                ImagePicker(
                    onFileSelected = { onFileDropped(it) },
                    modifier = Modifier
                        .size(size)
                        .onMouseOver { imageHovered = true }
                        .onMouseOut { imageHovered = false }
                )
                MdiAddAPhoto(
                    modifier = Modifier
                        .onMouseEnter { addIconHovered = true }
                        .onMouseLeave { addIconHovered = false }
                        .opacity(if (imageHovered || addIconHovered) 1.0 else 0.5)
                        .scale(if (imageHovered || addIconHovered) 1.05 else 1.0)
                        .transition(
                            CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                        )
                )
                errorText?.let { errorText ->
                    SpanText(
                        text = errorText,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .roleStyle(MaterialTheme.typography.labelSmall)
                            .color(MaterialTheme.colors.error.value())
                            .margin(0.5.em)
                    )
                }
            }
        }
    }
}
