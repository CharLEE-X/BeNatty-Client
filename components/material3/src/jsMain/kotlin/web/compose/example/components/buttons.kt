package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.dom.Text
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.buttons.ElevatedButton
import web.compose.material3.buttons.FilledButton
import web.compose.material3.buttons.FilledTonalButton
import web.compose.material3.buttons.OutlinedButton
import web.compose.material3.buttons.TextButton
import web.compose.material3.fab.Fab
import web.compose.material3.fab.FabSize
import web.compose.material3.fab.label
import web.compose.material3.fab.size
import web.compose.material3.icon.Icon
import web.compose.material3.slot

@Composable
fun ButtonShowcase() {
    var clickedValue by remember { mutableStateOf("<<No button clicked>>") }

    LargeTitle("Buttons")

    LargeLabel("Button clicked: $clickedValue")

    FilledButton(
        onClick = { clickedValue = "filled button clicked" }
    ) {
        Text("Filled Button")
    }
    OutlinedButton(
        onClick = { clickedValue = "outlined button clicked" }
    ) {
        Text("Outlined Button")
    }
    ElevatedButton(
        onClick = { clickedValue = "elevated button clicked" }
    ) {
        Text("Elevated Button")
    }
    TextButton(
        onClick = { clickedValue = "text button clicked" }
    ) {
        Text("Text Button")
    }
    FilledTonalButton(
        onClick = { clickedValue = "tonal button clicked" }
    ) {
        Text("Tonal Button")
    }

    LargeTitle("FAB")
    Fab({ size = FabSize.SMALL; onClick { clickedValue = "Small FAB clicked" } }) {
        Icon({ slot = "icon" }, "edit")
    }
    Fab({ size = FabSize.MEDIUM; onClick { clickedValue = "Medium FAB clicked" } }) {
        Icon({ slot = "icon" }, "delete")
    }
    Fab({ size = FabSize.LARGE; onClick { clickedValue = "Large FAB clicked" } }) {
        Icon({ slot = "icon" }, "add")
    }
    Fab({ label = "FAB"; onClick { clickedValue = "FAB with label clicked" } })
}
