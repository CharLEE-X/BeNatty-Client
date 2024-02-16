package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.silk.components.icons.fa.FaMagnifyingGlass
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import web.compose.material3.component.TextFieldType

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    height: CSSLengthOrPercentageNumericValue,
    onEnterPress: () -> Unit,
    onSearchIconClick: (Boolean) -> Unit,
    containerShape: CSSLengthOrPercentageNumericValue? = null,
) {
    val breakpoint = rememberBreakpoint()

    LaunchedEffect(breakpoint) {
        if (breakpoint >= Breakpoint.SM) onSearchIconClick(false)
    }

    if (breakpoint >= Breakpoint.SM) {
        AppOutlinedTextField(
            placeholder = placeholder,
            value = value,
            onValueChange = { onValueChange(it) },
            type = TextFieldType.SEARCH,
            leadingIcon = { FaMagnifyingGlass() },
            containerShape = containerShape,
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .onKeyDown {
                    if (it.key == "Enter") onEnterPress()
                }
        )
    } else {
        AppIconButton(
            onClick = { onSearchIconClick(true) },
        ) {
            FaMagnifyingGlass(size = IconSize.SM)
        }
    }
}
