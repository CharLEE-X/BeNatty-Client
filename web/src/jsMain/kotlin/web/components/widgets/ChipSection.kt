package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import org.jetbrains.compose.web.css.em
import web.compose.material3.component.ChipSet
import web.compose.material3.component.FilledButton
import web.compose.material3.component.FilterChip


@Composable
fun FilterChipSection(
    title: String,
    chips: List<String>,
    selectedChips: List<String>,
    onChipClick: (String) -> Unit,
    canBeEmpty: Boolean = true,
    noChipsText: String,
    createText: String,
    onCreateClick: () -> Unit,
    afterTitle: @Composable (() -> Unit) = {},
) {
    SpanText(text = title)
    afterTitle()
    if (chips.isNotEmpty()) {
        ChipSet {
            chips.forEach { chip ->
                FilterChip(
                    label = chip.enumCapitalized(),
                    selected = chip in selectedChips,
                    onClick = { onChipClick(chip) },
                )
            }
        }
    } else {
        if (canBeEmpty) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.gap(1.em),
            ) {
                SpanText(
                    text = noChipsText,
                    modifier = Modifier.fontStyle(FontStyle.Italic)
                )
                FilledButton(
                    onClick = { onCreateClick() },
                ) {
                    SpanText(text = createText)
                }
            }
        }
    }
}
