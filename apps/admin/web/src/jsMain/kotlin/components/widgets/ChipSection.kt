package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.silk.components.text.SpanText

@Composable
fun FilterChipSection(
    modifier: Modifier = Modifier,
    chips: List<String>,
    selectedChips: List<String>,
    onChipClick: (String) -> Unit,
    canBeEmpty: Boolean = true,
    noChipsText: String,
    onCreateClick: () -> Unit,
    afterTitle: @Composable (() -> Unit) = {},
) {
    afterTitle()
    if (chips.isNotEmpty()) {
        SpanText("FIXME")
//        ChipSet(modifier) {
//            chips.forEach { chip ->
//                FilterChip(
//                    label = chip.enumCapitalized(),
//                    selected = chip in selectedChips,
//                    onClick = { onChipClick(chip) },
//                )
//            }
//        }
    } else {
        if (canBeEmpty) {
            SpanText(
                text = noChipsText,
                modifier = Modifier.fontStyle(FontStyle.Italic)
            )
            CreateButton(onClick = { onCreateClick() })
        }
    }
}
