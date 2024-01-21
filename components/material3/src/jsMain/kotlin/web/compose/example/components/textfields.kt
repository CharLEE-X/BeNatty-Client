package web.compose.example.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import web.compose.extras.text.LargeTitle
import web.compose.material3.disabled
import web.compose.material3.textfield.FilledTextField
import web.compose.material3.textfield.OutlinedTextField
import web.compose.material3.textfield.TextFieldType
import web.compose.material3.textfield.error
import web.compose.material3.textfield.errorText
import web.compose.material3.textfield.label
import web.compose.material3.textfield.onInput
import web.compose.material3.textfield.required
import web.compose.material3.textfield.type
import web.compose.material3.textfield.value

@Composable
fun TextFieldShowcase() {
    var textFieldValue by remember { mutableStateOf("Initial text field value") }

    LargeTitle("Text Fields")

    FilledTextField({
        label = "Filled Text Field"
        value = textFieldValue
        onInput {
            textFieldValue = it.value ?: ""
        }
        style { padding(5.px) }
    })

    FilledTextField({
        label = "Filled Text Field"
        value = textFieldValue
        disabled()
        style { padding(5.px) }
    })

    FilledTextField({
        label = "Filled Text Field"
        value = "Some input"
        error = true
        errorText = "No valid input"
        style { padding(5.px) }
    })

    OutlinedTextField({
        label = "Outlined Text Field"
        value = textFieldValue
        onInput {
            textFieldValue = it.value ?: ""
        }
        style { padding(5.px) }
    })

    OutlinedTextField({
        label = "Input a number"
        value = "111"
        type = TextFieldType.NUMBER
        required = true
        style { padding(5.px) }
    })

    OutlinedTextField({
        label = "Input a password"
        value = "Welcome"
        type = TextFieldType.PASSWORD
        style { padding(5.px) }
    })

    OutlinedTextField({
        label = "Input an email"
        value = "john.doe@example.com"
        type = TextFieldType.EMAIL
        style { padding(5.px) }
    })

    OutlinedTextField({
        label = "Input an url"
        value = "https://www.example.com"
        type = TextFieldType.URL
        style { padding(5.px) }
    })

    OutlinedTextField({
        label = "Input an search"
        value = "Some search?"
        type = TextFieldType.SEARCH
        style { padding(5.px) }
    })
}
