package web.compose.extras.fileupload

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import kotlinx.browser.document
import org.jetbrains.compose.web.attributes.multiple
import org.jetbrains.compose.web.css.DisplayStyle.Companion.None
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.FileInput
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.files.File
import web.compose.material3.component.FilledButton

@Composable
fun FilledFileInput(
    onFilesSelected: ((List<File>?) -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    FileInput {
        id("fileInputElement")
        multiple()
        style { display(None) }
        onChange {
            val files = it.target.files?.asList()

            onFilesSelected?.invoke(files)
        }
    }

    FilledButton(
        onClick = {
            val fileInputElement = document.getElementById("fileInputElement") as HTMLInputElement
            fileInputElement.click()
        },
        content = content
    )
}
