package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.H1Variant
import web.HeadlineStyle

@Composable
fun PageHeader(
    title: String,
    content: @Composable RowScope.() -> Unit = {}
) {
    SectionHeader(
        text = title,
        content = content
    )
    HorizontalDivider(modifier = Modifier.margin(top = 1.em, bottom = 1.em))
}

@Composable
fun SectionHeader(
    text: String,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        SpanText(
            text = text,
            modifier = HeadlineStyle.toModifier(H1Variant)
        )
        Spacer()
        content()
    }
}
