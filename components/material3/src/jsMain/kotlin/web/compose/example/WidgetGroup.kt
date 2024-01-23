package web.compose.example

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import web.compose.extras.text.LargeHeadline

@Composable
fun WidgetGroup(title: String, content: @Composable () -> Unit) {
    Column {
        LargeHeadline(title)

        Row(
            modifier = Modifier
                .padding(1.em)
                .gap(1.em)
                .border(width = 2.px, color = Color.lightgray, style = LineStyle.Dotted)
        ) {
            content()
        }
    }
}
