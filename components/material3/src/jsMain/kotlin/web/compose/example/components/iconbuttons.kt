package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.common.slot
import web.compose.material3.component.FilledIconButton
import web.compose.material3.component.Icon
import web.compose.material3.component.IconButton
import web.compose.material3.component.OutlinedIconButton
import web.compose.material3.component.TonalIconButton

@Composable
fun IconButtonsShowcase() {
    var clickedValue by remember { mutableStateOf("<<No button clicked>>") }
    var toggleButtonSelected by remember { mutableStateOf(false) }

    LargeTitle("Icon Buttons")

    LargeLabel("Button clicked: $clickedValue")

    FilledIconButton(
        onClick = { clickedValue = "filled icon button clicked" },
        modifier = Modifier.padding(5.px)
    ) { Icon(iconIdentifier = "star") }
    OutlinedIconButton(
        onClick = { clickedValue = "outlined icon button clicked" },
        modifier = Modifier.padding(5.px)
    ) { Icon(iconIdentifier = "login") }
    TonalIconButton(
        onClick = { clickedValue = "tonal icon button clicked" },
        modifier = Modifier.padding(5.px)
    ) { Icon(iconIdentifier = "public") }
    IconButton(
        onClick = { toggleButtonSelected = !toggleButtonSelected },
        modifier = Modifier.padding(5.px)
    ) { Icon("menu") }
    FilledIconButton(
        toggle = true,
        selected = toggleButtonSelected,
        modifier = Modifier.padding(5.px)
    ) {
        Icon(iconIdentifier = "lock")
        Icon(
            modifier = Modifier.attrsModifier {
                slot = "selectedIcon"
            },
            iconIdentifier = "lock_open"
        )
    }
    FilledIconButton(
        disabled = true,
        modifier = Modifier.padding(5.px)
    ) {
        Icon("star")
    }
}
