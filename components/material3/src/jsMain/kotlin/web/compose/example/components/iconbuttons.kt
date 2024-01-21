package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.icon.Icon
import web.compose.material3.iconbutton.FilledIconButton
import web.compose.material3.iconbutton.IconButton
import web.compose.material3.iconbutton.OutlinedIconButton
import web.compose.material3.iconbutton.TonalIconButton
import web.compose.material3.iconbutton.disabled
import web.compose.material3.iconbutton.selected
import web.compose.material3.iconbutton.toggle
import web.compose.material3.slot

@Composable
fun IconButtonsShowcase() {
    var clickedValue by remember { mutableStateOf("<<No button clicked>>") }
    var toggleButtonSelected by remember { mutableStateOf(false) }

    LargeTitle("Icon Buttons")

    LargeLabel("Button clicked: $clickedValue")

    FilledIconButton({
        onClick { clickedValue = "filled icon button clicked" }
        style { padding(5.px) }
    }) { Icon("star") }
    OutlinedIconButton({
        onClick { clickedValue = "outlined icon button clicked" }
        style { padding(5.px) }
    }) { Icon("login") }
    TonalIconButton({
        onClick { clickedValue = "tonal icon button clicked" }
        style { padding(5.px) }
    }) { Icon("public") }
    IconButton({
        onClick { toggleButtonSelected = !toggleButtonSelected }
        style { padding(5.px) }
    }) { Icon("menu") }
    FilledIconButton({
        toggle()
        selected(toggleButtonSelected)
        style { padding(5.px) }
    }) {
        Icon("lock")
        Icon({ slot = "selectedIcon" }, "lock_open")
    }
    FilledIconButton({
        disabled()
        style { padding(5.px) }
    }) {
        Icon("star")
    }
}
