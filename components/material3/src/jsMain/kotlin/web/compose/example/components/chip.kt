package web.compose.example.components

import androidx.compose.runtime.Composable
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.AssistChip
import web.compose.material3.component.ChipSet
import web.compose.material3.component.FilterChip
import web.compose.material3.component.InputChip
import web.compose.material3.component.SuggestionChip

@Composable
fun ChipShowcase() {
    LargeTitle("Chip")
    ChipSet {
        SuggestionChip("Suggestion chip")
        InputChip("Input chip")
        FilterChip("Filter chip")
        AssistChip("Assist chip")
    }
}
