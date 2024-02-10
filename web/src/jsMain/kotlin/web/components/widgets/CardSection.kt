package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.compose.material3.component.labs.ElevatedCard


@Composable
fun CardSection(
    title: String,
    elevation: Int? = 0,
    content: @Composable () -> Unit,
) {
    ElevatedCard(
        elevation = elevation,
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.em)
                .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainerHigh.value())
                .borderRadius(topLeft = 1.em, topRight = 1.em)
        ) {
            SectionHeader(text = title)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.em)
                .gap(1.em)
        ) {
            content()
        }
    }
}
