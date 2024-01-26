package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem

@Composable
fun Logo(
    onClick: () -> Unit,
) {
    Image(
        src = "/logo.png",
        description = "",
        modifier = Modifier
            .height(4.cssRem)
            .display(DisplayStyle.Block)
            .onClick { onClick() },
    )
}
