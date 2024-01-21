package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.dom.Label
import web.compose.extras.text.LargeLabel
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.Checkbox

@Composable
fun CheckboxShowcase() {
    var checkboxStatus by remember { mutableStateOf(false) }

    LargeTitle("Checkbox")
    Label(null, { style { display(DisplayStyle.Flex); alignItems(AlignItems.Center) } }) {
        Checkbox(
            value = checkboxStatus,
            onClick = { checkboxStatus = !checkboxStatus }
        )
        LargeLabel("Enabled checkbox", true)
    }
    Label(null, { style { display(DisplayStyle.Flex); alignItems(AlignItems.Center) } }) {
        Checkbox(
            value = checkboxStatus,
            onClick = { checkboxStatus = !checkboxStatus },
            indeterminate = true
        )
        LargeLabel("Indeterminate checkbox", true)
    }
    Label(null, { style { display(DisplayStyle.Flex); alignItems(AlignItems.Center) } }) {
        Checkbox(
            value = checkboxStatus,
            onClick = { checkboxStatus = !checkboxStatus },
            modifier = Modifier.disabled()
        )
        LargeLabel("Disabled checkbox", true)
    }
}
