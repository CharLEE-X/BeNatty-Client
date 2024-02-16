package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em

@Composable
fun Logo(
    size: CSSLengthOrPercentageNumericValue = 4.em,
    onClick: () -> Unit,
) {
    Image(
        src = "/logo.png",
        description = "",
        modifier = Modifier
            .size(size)
            .display(DisplayStyle.Block)
            .onClick { onClick() },
    )
}
