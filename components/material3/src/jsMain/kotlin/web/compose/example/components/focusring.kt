package web.compose.example.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.borderWidth
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import web.compose.extras.text.LargeBody
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.FocusRing
import web.compose.material3.theming.MdSysColorVariables

private object FocusRingStyleSheet : StyleSheet() {
    val box by style {
        margin(8.px)
        backgroundColor(MdSysColorVariables.mdSysColorPrimaryContainer.value())
        borderWidth(0.px);
        borderRadius(50.percent);
        color(MdSysColorVariables.mdSysColorOnPrimaryContainer.value())
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)
        position(Position.Relative) /* This is needed for Focus Ring to work correctly */
        width(80.px)
        height(80.px)
    }
}

@Composable
fun FocusRingShowcase() {
    Style(FocusRingStyleSheet)

    LargeTitle("Focus Ring")

    LargeBody(
        """
        Use the keyboard <Tab> key to change focus of elements. This should trigger the focus ring.
    """.trimIndent()
    )

    Div({ tabIndex(1); classes(FocusRingStyleSheet.box) }) {
        LargeLabel("default")
        FocusRing()
    }

    Div({ tabIndex(2); classes(FocusRingStyleSheet.box) }) {
        LargeLabel("inward")
        FocusRing(inward = true)
    }

    Div({ tabIndex(3); classes(FocusRingStyleSheet.box) }) {
        LargeLabel("visible")
        FocusRing(visible = true)
    }
}
