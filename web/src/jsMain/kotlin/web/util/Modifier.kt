package web.util

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier

fun Modifier.onEnterKeyDown(block: () -> Unit) = attrsModifier {
    onKeyDown { if (it.key == "Enter") block() }
}
