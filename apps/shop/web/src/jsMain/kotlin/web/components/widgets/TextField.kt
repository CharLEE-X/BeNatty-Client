package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement
import web.AppColors

@Composable
fun AppOutlinedTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    password: Boolean = false,
    enabled: Boolean = true,
    valid: Boolean = true,
    required: Boolean = false,
    readOnly: Boolean = false,
    spellCheck: Boolean = false,
    autoComplete: AutoComplete? = null,
    onCommit: () -> Unit = {},
    bgColor: Color = ColorMode.current.toPalette().background,
    ref: ElementRefScope<HTMLInputElement>? = null,
) {
    TextInput(
        text = text,
        onTextChange = onTextChange,
        placeholder = placeholder,
        spellCheck = spellCheck,
        autoComplete = autoComplete,
        valid = valid,
        enabled = enabled,
        required = required,
        password = password,
        readOnly = readOnly,
        onCommit = onCommit,
        ref = ref,
        focusBorderColor = AppColors.brandColor,
        modifier = modifier
            .fillMaxWidth()
            .borderRadius(0.px)
            .height(48.px)
            .lineHeight(48.px)
            .backgroundColor(bgColor)
            .padding(leftRight = 1.em)
    )
}
