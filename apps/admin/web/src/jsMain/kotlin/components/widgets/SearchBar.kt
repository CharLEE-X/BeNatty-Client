package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.components.forms.TextInput
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.px

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onEnterPress: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = { MdiSearch() },
    trailingIcon: @Composable (() -> Unit)? = null,
    containerShape: CSSLengthOrPercentageNumericValue = 30.px,
) {
    TextInput(
        text = value,
        onTextChanged = onValueChange,
        placeholder = placeholder,
        spellCheck = false,
        autoComplete = AutoComplete.off,
        onCommit = onEnterPress,
    )
}
