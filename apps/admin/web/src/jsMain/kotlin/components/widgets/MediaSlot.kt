package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAddAPhoto
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCloudUpload
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEdit
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.w3c.files.File
import web.H3Variant
import web.HeadlineStyle
import web.util.onEnterKeyDown

@Composable
fun MediaSlot(
    modifier: Modifier = Modifier,
    url: String?,
    alt: String?,
    errorText: String?,
    isImagesLoading: Boolean = false,
    isImageClickable: Boolean = true,
    hasDeleteButton: Boolean = true,
    hasEditButton: Boolean = false,
    onFileDropped: (File) -> Unit,
    onImageClick: (url: String?) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    var imageHovered by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .overflow(Overflow.Hidden)
            .onClick { if (isImageClickable) onImageClick(url) }
            .onMouseOver { if (isImageClickable) imageHovered = true }
            .onMouseOut { if (isImageClickable) imageHovered = false }
            .thenIf(url == null) {
                Modifier.border(
                    width = 2.px,
                    color = ColorMode.current.toPalette().border,
                    style = LineStyle.Dashed,
                )
            }
            .thenIf(
                isImageClickable, Modifier
                    .tabIndex(0)
                    .onEnterKeyDown { onImageClick(url) }
            )
    ) {
        url?.let {
            Image(
                src = url,
                alt = alt ?: "",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(if (imageHovered && isImageClickable) 1.05 else 1.0)
                    .objectFit(ObjectFit.Cover)
                    .transition(Transition.of("scale", 0.5.s, TransitionTimingFunction.Ease))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .objectFit(ObjectFit.Cover)
                    .backgroundColor(ColorMode.current.toPalette().color)
                    .transition(Transition.of("backgroundColor", 0.3.s, TransitionTimingFunction.Ease))
            ) {}
            if (hasDeleteButton) {
                ActionIcon(
                    onClick = onDeleteClick,
                    icon = { MdiDelete() },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            if (hasEditButton) {
                ActionIcon(
                    onClick = onEditClick,
                    icon = { MdiEdit() },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        } ?: if (isImagesLoading) {
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
                    .transition(Transition.of("opacity", 0.3.s, TransitionTimingFunction.Ease))
            )
        } else {
            ImagePicker(
                onFileSelected = { onFileDropped(it) },
                modifier = modifier
                    .onMouseOver { imageHovered = true }
                    .onMouseOut { imageHovered = false }
            )
            ActionIcon(
                onClick = { onImageClick(url) },
                icon = { MdiAddAPhoto() },
            )
            errorText?.let { errorText ->
                SpanText(
                    text = errorText,
                    modifier = HeadlineStyle.toModifier(H3Variant)
                        .align(Alignment.BottomCenter)
                        .color(Colors.Red)
                        .margin(0.5.em)
                )
            }
        }
    }
}

@Composable
private fun ActionIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable (IconStyle) -> Unit,
) {
    AppIconButton(
        onClick = onClick,
        icon = { icon(it) },
        modifier = modifier
            .margin(1.em)
    )
}
