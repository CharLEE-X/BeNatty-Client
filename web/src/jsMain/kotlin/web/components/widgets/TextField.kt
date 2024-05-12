package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.components.forms.TextInput
import feature.shop.account.profile.SHAKE_ANIM_DURATION
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable RowScope.() -> Unit)? = null,
    hasLeadingIcon: Boolean = false,
    hasTrailingIcon: Boolean = false,
    containerShape: CSSLengthOrPercentageNumericValue = 12.px,
    inputTextColor: CSSColorValue? = null,
    type: InputType.InputTypeWithStringValue = InputType.Text,
    autoComplete: AutoComplete = AutoComplete.off,
    required: Boolean = false,
    disabled: Boolean = false,
    error: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    suffixText: String? = null,
    supportingText: String? = null,
    textDirection: String? = null,
    rows: Int? = null,
    cols: Int? = null,
    max: String? = null,
    maxLength: Int? = null,
    min: String? = null,
    minLength: Int? = null,
    pattern: String? = null,
    readOnly: Boolean = false,
    multiple: Boolean = false,
    step: String? = null,
    shake: Boolean = false,
    modifier: Modifier = Modifier,
    unFocusedOutlineColor: CSSColorValue? = null,
    focusedOutlineColor: CSSColorValue? = null,
    hoverOutlineColor: CSSColorValue? = null,
) {
    var translateX by remember { mutableStateOf(0.em) }

    LaunchedEffect(shake) {
        if (shake) {
            translateX = 0.5.em
            delay(SHAKE_ANIM_DURATION / 4)
            translateX = (-0.5).em
            delay(SHAKE_ANIM_DURATION / 4)
            translateX = 0.5.em
            delay(SHAKE_ANIM_DURATION / 4)
            translateX = 0.em
        }
    }

    TextInput(
        text = value,
        onTextChanged = onValueChange,
        placeholder = placeholder,
        spellCheck = false,
        autoComplete = AutoComplete.off,
    )
}
