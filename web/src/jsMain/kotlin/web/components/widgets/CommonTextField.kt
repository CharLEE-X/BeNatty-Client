package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.components.icons.mdi.MdiError
import feature.account.profile.SHAKE_ANIM_DURATION
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextFieldType

@Composable
fun CommonTextfield(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMsg: String?,
    type: TextFieldType = TextFieldType.TEXT,
    autoComplete: AutoComplete = AutoComplete.off,
    required: Boolean = false,
    shake: Boolean = false,
    isEditing: Boolean = false,
    outlineColor: CSSColorValue? = if (isEditing) null else MaterialTheme.colors.mdSysColorSurface.value(),
    icon: @Composable () -> Unit,
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
        onInput = { onValueChange(it) },
        label = label,
        type = type,
        leadingIcon = { icon() },
        trailingIcon = { errorMsg?.let { MdiError() } },
        error = errorMsg != null,
        errorText = errorMsg,
        required = required,
        autoComplete = autoComplete,
        readOnly = !isEditing,
        outlineColor = outlineColor,
        modifier = modifier
            .translateX(translateX)
            .transition(CSSTransition("translate", SHAKE_ANIM_DURATION.inWholeSeconds.s))
    )
}
