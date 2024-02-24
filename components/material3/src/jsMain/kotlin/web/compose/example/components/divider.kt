package web.compose.example.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.listStyle
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul
import theme.MaterialTheme
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.Divider

object DividerStyleSheet : StyleSheet() {
    val list by style {
        border(1.px, LineStyle.Solid, MaterialTheme.colors.outline.value())
        margin(0.px)
        padding(0.px)
        width(200.px)
    }
    val listItem by style {
        color(MaterialTheme.colors.onBackground.value())
        fontFamily("system-ui")
        listStyle("none")
        margin(16.px)
    }
}

@Composable
fun DividerShowcase() {
    LargeTitle("Divider")

    Style(DividerStyleSheet)

    Ul({ classes(DividerStyleSheet.list) }) {
        Li({ classes(DividerStyleSheet.listItem) }) { LargeLabel("Default divider") }
        Divider()
        Li({ classes(DividerStyleSheet.listItem) }) { LargeLabel("Divider (inset = true)") }
        Divider(inset = true)
        Li({ classes(DividerStyleSheet.listItem) }) { LargeLabel("Divider (insetStart = true)") }
        Divider(insetStart = true)
        Li({ classes(DividerStyleSheet.listItem) }) { LargeLabel("Divider (insetEnd = true)") }
        Divider(insetEnd = true)
        Li({ classes(DividerStyleSheet.listItem) }) { LargeLabel("Final item") }
    }
}
