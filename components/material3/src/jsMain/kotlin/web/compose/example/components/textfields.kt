package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px
import web.compose.extras.text.LargeTitle
import web.compose.material3.component.FilledTextField
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextFieldType

@Composable
fun TextFieldShowcase() {
    var textFieldValue by remember { mutableStateOf("Initial text field value") }

    LargeTitle("Text Fields")

    FilledTextField(
        label = "Filled Text Field",
        value = textFieldValue,
        onInput = { textFieldValue = it },
        modifier = Modifier.padding(5.px),
    )

    FilledTextField(
        label = "Filled Text Field",
        value = textFieldValue,
        disabled = true,
        modifier = Modifier.padding(5.px)
    )

    FilledTextField(
        label = "Filled Text Field",
        value = "Some input",
        error = true,
        errorText = "No valid input",
        modifier = Modifier.padding(5.px),
    )

    OutlinedTextField(
        label = "Outlined Text Field",
        value = textFieldValue,
        onInput = { textFieldValue = it },
        modifier = Modifier.padding(5.px)
    )

    OutlinedTextField(
        label = "Input a number",
        value = "111",
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.padding(5.px),
    )

    OutlinedTextField(
        label = "Input a password",
        value = "Welcome",
        type = TextFieldType.PASSWORD,
        modifier = Modifier.padding(5.px),
    )

    OutlinedTextField(
        label = "Input an email",
        value = "john.doe@example.com",
        type = TextFieldType.EMAIL,
        modifier = Modifier.padding(5.px),
    )

    OutlinedTextField(
        label = "Input an url",
        value = "https://www.example.com",
        type = TextFieldType.URL,
        modifier = Modifier.padding(5.px),
    )

    OutlinedTextField(
        label = "Input an search",
        value = "Some search?",
        type = TextFieldType.SEARCH,
        modifier = Modifier.padding(5.px),
    )
}
