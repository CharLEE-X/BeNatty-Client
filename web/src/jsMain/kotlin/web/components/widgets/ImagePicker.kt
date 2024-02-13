package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onDragOver
import com.varabyte.kobweb.compose.ui.modifiers.onDrop
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Input
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get

@Composable
fun ImagePicker(
    modifier: Modifier,
    onFileSelected: (File) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .opacity(0)
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
    }
}
