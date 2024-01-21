package web.compose.material3.buttons

import androidx.compose.runtime.Composable
import androidx.compose.web.events.SyntheticMouseEvent
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.dom.ContentBuilder

@Composable
fun FilledButton(
    onClick: (SyntheticMouseEvent) -> Unit,
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdButtonElement>? = null
) {
    MdButtonTagElement(
        name = "filled",
        onClick = onClick,
        modifier = modifier,
        content = content
    )
}
