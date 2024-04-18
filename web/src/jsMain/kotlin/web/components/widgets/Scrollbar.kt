package web.components.widgets

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.jetbrains.compose.web.css.DisplayStyle
import theme.toColors

val themeScrollbarStyle by ComponentStyle {
    base {
        Modifier.styleModifier {
            property("-ms-overflow-style", "none")
            property("scrollbar-width", "thin")
            val trackColor = if (colorMode.isLight) {
                colorMode.toColors().sysColorTertiaryContainer
            } else {
                colorMode.toColors().sysColorSecondaryContainer
            }
            property(
                "scrollbar-color",
                "${trackColor.toRgb()} transparent"
            )
        }
    }
    cssRule("::-webkit-scrollbar") {
        Modifier.display(DisplayStyle.None)
    }
}
