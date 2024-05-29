package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.px
import web.AppColors

@Composable
fun AppDividerHorizontal(color: Color = AppColors.divider(), modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.px)
            .backgroundColor(color)
    )
}

@Composable
fun AppDividerVertical(color: Color = AppColors.divider()) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.px)
            .backgroundColor(color)
    )
}
