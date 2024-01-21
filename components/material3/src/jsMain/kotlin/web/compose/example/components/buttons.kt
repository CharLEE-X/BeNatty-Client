package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.silk.components.icons.mdi.MdiHome
import org.jetbrains.compose.web.dom.Text
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.common.slot
import web.compose.material3.component.ElevatedButton
import web.compose.material3.component.Fab
import web.compose.material3.component.FabSize
import web.compose.material3.component.FilledButton
import web.compose.material3.component.FilledTonalButton
import web.compose.material3.component.Icon
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.TextButton

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
        trailingIcon = {
            MdiHome()
        },
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
    Fab(
        fabSize = FabSize.SMALL,
        onClick = { clickedValue = "Small FAB clicked" }
    ) {
        Icon(
            modifier = Modifier.attrsModifier {
                slot = "icon"
            },
            iconIdentifier = "edit"
        )
    }
    Fab(
        fabSize = FabSize.MEDIUM,
        onClick = { clickedValue = "Medium FAB clicked" }
    ) {
        Icon(
            modifier = Modifier.attrsModifier { slot = "icon" },
            iconIdentifier = "delete"
        )
    }
    Fab(
        fabSize = FabSize.LARGE,
        onClick = { clickedValue = "Large FAB clicked" }
    ) {
        Icon(
            modifier = Modifier.attrsModifier { slot = "icon" },
            iconIdentifier = "add"
        )
    }
    Fab(
        label = "FAB",
        onClick = { clickedValue = "FAB with label clicked" }
    )
}
