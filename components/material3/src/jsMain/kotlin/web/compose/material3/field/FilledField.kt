package web.compose.material3.field

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.MdElement

@Composable
fun FilledField(
    label: String? = null,
    value: String? = null,
    errorText: String? = null,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    MdFieldTagElement(
        name = "filled",
        label = label,
        value = value,
        errorText = errorText,
        isError = isError,
        modifier = modifier,
        content = content
    )
}
