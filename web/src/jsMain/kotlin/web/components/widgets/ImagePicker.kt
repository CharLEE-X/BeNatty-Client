package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onDragOver
import com.varabyte.kobweb.compose.ui.modifiers.onDrop
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get
import theme.MaterialTheme

@Composable
fun ImagePicker(
    modifier: Modifier,
    onFileSelected: (File) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(
                width = 1.px,
                color = MaterialTheme.colors.mdSysColorOnSurface.value(),
                style = LineStyle.Dashed
            )
            .borderRadius(8.px)
            .onDragOver { it.preventDefault() }
            .onDrop { event ->
                event.preventDefault()
                val files = event.dataTransfer?.files
                if (files != null && files.length > 0) {
                    val file = files[0]!!
                    onFileSelected(file)
                }
            }
            .onClick {
                // Trigger file input click on area click (optional)
                // You might need to handle this with JavaScript interop or ensure the input is accessible for clicking
            }
    ) {
        Input(
            type = InputType.File,
            attrs = Modifier
                .fillMaxSize()
                .opacity(0.0)
                .toAttrs {
                    onChange { event ->
                        val input = event.nativeEvent.target as HTMLInputElement
                        val file = input.files?.item(0)
                        if (file != null) {
                            onFileSelected(file)
                        }
                    }
                }
        )
        SpanText("Drag and drop your file here or click to select")
    }
}
