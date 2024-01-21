package web.compose.example.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import web.compose.extras.text.LargeBody
import web.compose.extras.text.LargeTitle
import web.compose.material3.common.slot
import web.compose.material3.component.FilledField
import web.compose.material3.component.Icon
import web.compose.material3.component.OutlinedField

@Composable
fun FieldsShowcase() {
    LargeTitle("Fields")

    FilledField(
        label = "A label"
    ) {
        LargeBody("Some text")

        Icon(
            modifier = Modifier.attrsModifier {
                slot = "start"
            },
            iconIdentifier = "search"
        )
        Icon(
            modifier = Modifier.attrsModifier {
                slot = " end "
            },
            iconIdentifier = " event "
        )
        Span({ slot = "supporting-text" }) { Text("Supporting text") }
        Span({ slot = "supporting-text-end" }) { Text("Supporting text end") }
    }

    OutlinedField(
        label = "An outlined field label",
        value = "Field value",
    ) {
        LargeBody("Some text")
    }
    OutlinedField(
        value = "Field value",
        label = "An outlined field label",
        isError = true,
        errorText = "An error occurred",
    ) {
        LargeBody("Some text")
    }
}
