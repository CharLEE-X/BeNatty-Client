package web.compose.example.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.borderWidth
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import theme.MaterialTheme
import web.compose.extras.text.LargeBody
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.Elevation

private object ElevationStyleSheet : StyleSheet() {
    val box by style {
        margin(8.px)
        backgroundColor(MaterialTheme.colors.mdSysColorPrimaryContainer.value())
        borderWidth(0.px);
        borderRadius(16.px);
        color(MaterialTheme.colors.mdSysColorOnPrimaryContainer.value())
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        position(Position.Relative) /* This is needed for Elevation to work correctly */
        width(80.px)
    }
}

@Composable
fun ElevationShowcase() {

    LargeTitle("Elevation")

    Style(ElevationStyleSheet)

    Div({ classes(ElevationStyleSheet.box) }) {
        LargeBody("Level 0")
        Elevation(0)
    }
    Div({ classes(ElevationStyleSheet.box) }) {
        LargeBody("Level 1")
        Elevation(1)
    }
    Div({ classes(ElevationStyleSheet.box) }) {
        LargeBody("Level 2")
        Elevation(2)
    }
    Div({ classes(ElevationStyleSheet.box) }) {
        LargeBody("Level 3")
        Elevation(3)
    }
    Div({ classes(ElevationStyleSheet.box) }) {
        LargeBody("Level 4")
        Elevation(4)
    }
}
