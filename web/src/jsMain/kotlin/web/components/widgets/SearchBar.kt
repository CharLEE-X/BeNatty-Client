package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.silk.components.icons.fa.FaMagnifyingGlass
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextFieldType

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onEnterPress: () -> Unit,
    onSearchIconClick: (Boolean) -> Unit,
    containerShape: String? = null,
) {
    val breakpoint = rememberBreakpoint()

    LaunchedEffect(breakpoint) {
        if (breakpoint >= Breakpoint.SM) onSearchIconClick(false)
    }

    if (breakpoint >= Breakpoint.SM) {
        OutlinedTextField(
            placeholder = placeholder,
            value = value,
            onInput = { onValueChange(it) },
            type = TextFieldType.SEARCH,
            leadingIcon = { FaMagnifyingGlass() },
            containerShape = containerShape,
            modifier = modifier
                .fillMaxWidth()
                .onKeyDown {
                    if (it.key == "Enter") onEnterPress()
                }
        )
    } else {
        web.compose.material3.component.IconButton(
            onClick = { onSearchIconClick(true) },
        ) {
            FaMagnifyingGlass(size = IconSize.SM)
        }
    }
}
