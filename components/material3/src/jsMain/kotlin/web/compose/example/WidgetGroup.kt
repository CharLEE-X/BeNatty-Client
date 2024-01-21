package web.compose.example

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.style
import org.jetbrains.compose.web.css.width
import web.compose.extras.Column
import web.compose.extras.Row
import web.compose.extras.text.LargeHeadline

@Composable
fun WidgetGroup(title: String, content: @Composable () -> Unit) {
    Column {
        LargeHeadline(title)

        Row({ style { border { style(LineStyle.Dotted); width(2.px); color(Color.lightgray) } } }) {
            content()
        }
    }
}
