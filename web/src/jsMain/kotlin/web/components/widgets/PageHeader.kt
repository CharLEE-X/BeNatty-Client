package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.TypeScaleTokens
import theme.roleStyle
import web.compose.material3.component.Divider

@Composable
fun PageHeader(
    title: String
) {
    SectionHeader(
        text = title,
        style = MaterialTheme.typography.displayMedium,
    )
    Divider(modifier = Modifier.margin(top = 1.em, bottom = 1.em))
}

@Composable
fun SectionHeader(text: String, style: TypeScaleTokens.Role = MaterialTheme.typography.headlineLarge) {
    SpanText(
        text = text,
        modifier = Modifier
            .roleStyle(style)
    )
}
