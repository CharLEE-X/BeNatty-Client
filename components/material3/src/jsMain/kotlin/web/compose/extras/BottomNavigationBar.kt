package web.compose.extras

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.bottom
import org.jetbrains.compose.web.css.left
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.ContentBuilder
import web.compose.material3.common.MdElement
import web.compose.material3.component.NavigationBar

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    content: ContentBuilder<MdElement>? = null
) {
    Style(StyleSheet().apply {
        root style {
            paddingBottom(80.px)
        }
    })
    NavigationBar(
        modifier = modifier.styleModifier {
            position(Position.Fixed)
            left(0.px)
            bottom(0.px)
            property("z-index", 99999)
            backgroundColor(Color.red)
        },
        content = content
    )
}
