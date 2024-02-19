package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import feature.shop.account.profile.SHAKE_ANIM_DURATION
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextFieldType

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
    type: TextFieldType = TextFieldType.TEXT,
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

    OutlinedTextField(
        value = value,
        onInput = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Row(
                modifier = Modifier.translateX((-.5).em)
            ) {
                errorText?.let { MdiError() }
                trailingIcon?.invoke(this)
            }
        },
        hasLeadingIcon = hasLeadingIcon,
        hasTrailingIcon = hasTrailingIcon,
        containerShape = containerShape,
        outlineColor = unFocusedOutlineColor,
        focusOutlineColor = focusedOutlineColor,
        hoverOutlineColor = hoverOutlineColor,
        inputTextColor = inputTextColor,
        type = type,
        autoComplete = autoComplete,
        required = required,
        disabled = disabled,
        error = error,
        errorText = errorText,
        prefixText = prefixText,
        suffixText = suffixText,
        supportingText = supportingText,
        textDirection = textDirection,
        rows = rows,
        cols = cols,
        max = max,
        maxLength = maxLength,
        min = min,
        minLength = minLength,
        pattern = pattern,
        readOnly = readOnly,
        multiple = multiple,
        step = step,
        modifier = modifier
            .translateX(translateX)
            .transition(
                CSSTransition("translate", SHAKE_ANIM_DURATION.inWholeSeconds.s),
                CSSTransition("border-color", 0.3.s, TransitionTimingFunction.Ease),
            )
    )
}
