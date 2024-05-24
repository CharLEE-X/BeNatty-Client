package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import web.util.onEnterKeyDown

@Composable
fun OutlinedMenu(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    selectedItem: String?,
    onItemSelected: (String) -> Unit,
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val anchor = "menu-${title.replace(" ", "-").lowercase()}"

    Span(Modifier.position(Position.Relative).toAttrs()) {
        AppOutlinedButton(
            onClick = { isMenuOpen = !isMenuOpen },
            modifier = modifier.id(anchor)
                .tabIndex(0)
                .onEnterKeyDown { isMenuOpen = !isMenuOpen },
        ) {
            Text(selectedItem ?: title)
        }
        SpanText("FIXME: Implement Menu component")
//        Menu(
//            anchor = anchor,
//            open = isMenuOpen,
//            onClosed = { isMenuOpen = false },
//        ) {
//            items.forEach { item ->
//                MenuItem(
//                    onCLick = { onItemSelected(item) },
//                    selected = selectedItem == item,
//                    modifier = Modifier
//                        .tabIndex(0)
//                        .onEnterKeyDown { onItemSelected(item) }
//                ) {
//                    Text(item)
//                }
//            }
//        }
    }
}
