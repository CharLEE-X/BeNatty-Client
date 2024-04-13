package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle

@Composable
fun ExpandableSection(
    modifier: Modifier = Modifier,
    openInitially: Boolean = false,
    title: String,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    var headerHovered by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(openInitially) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .onClick { if (enabled) expanded = !expanded }
                .onMouseOver { headerHovered = true }
                .onMouseOut { headerHovered = false }
                .cursor(Cursor.Pointer)
        ) {
            SpanText(
                text = title.uppercase(),
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodyLarge)
                    .fontWeight(if (headerHovered && enabled) FontWeight.SemiBold else FontWeight.Normal)
            )
            RotatableChevron(
                hovered = headerHovered,
                open = expanded,
                modifier = Modifier
                    .fontSize(1.5.em)
            )
        }
        if (expanded) {
            Column(
                content = content,
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(0.5.em)
            )
        }
    }
}
