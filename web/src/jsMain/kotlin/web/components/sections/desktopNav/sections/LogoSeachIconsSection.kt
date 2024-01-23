package web.components.sections.desktopNav.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.MoonIcon
import com.varabyte.kobweb.silk.components.icons.SunIcon
import com.varabyte.kobweb.silk.components.icons.fa.FaMagnifyingGlass
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFavorite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Span
import web.components.sections.desktopNav.DesktopNavContract
import web.components.sections.desktopNav.DesktopNavViewModel
import web.components.sections.desktopNav.label
import web.compose.material3.component.IconButton
import web.compose.material3.component.Menu
import web.compose.material3.component.MenuItem
import web.compose.material3.component.OutlinedTextField
import web.compose.material3.component.TextButton
import web.compose.material3.component.TextFieldType

@Composable
fun LogoSearchButtonsSection(
    vm: DesktopNavViewModel,
    state: DesktopNavContract.State,
    isAuthenticated: Boolean,
    modifier: Modifier,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    onLogoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBasketClick: () -> Unit,
) {
    var colorMode by ColorMode.currentState
    var showAccountMenu by remember { mutableStateOf(false) }
    val accountMenuAnchor = "account-button"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.gap(1.em),
    ) {
        Image(
            src = "/logo.png",
            description = "",
            modifier = Modifier
                .height(4.cssRem)
                .display(DisplayStyle.Block)
                .onClick { onLogoClick() },
        )
        SearchBar(
            value = searchValue,
            onValueChange = onSearchValueChanged,
            placeholder = DesktopNavContract.Strings.search,
            onEnterClick = {},
            onSearchIconClick = { },
            containerShape = "30px",
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(.25.em)
        ) {
            Span(Modifier.position(Position.Relative).toAttrs()) {
                TextButton(
                    onClick = {
                        if (!isAuthenticated) onLoginClick() else {
                            showAccountMenu = true
                        }
                    },
                    leadingIcon = { MdiPerson() },
                    modifier = Modifier
                        .margin(right = 0.25.em)
                        .id(accountMenuAnchor)
                ) {
                    SpanText(state.accountLoginButtonText)
                }
                Menu(
                    anchor = accountMenuAnchor,
                    open = showAccountMenu,
                    onClosed = { showAccountMenu = false },
                    modifier = Modifier.zIndex(1)
                ) {
                    DesktopNavContract.AccountMenuItem.entries.forEach { item ->
                        MenuItem(
                            onCLick = { vm.trySend(DesktopNavContract.Inputs.OnAccountMenuItemClicked(item)) },
                            selected = false
                        ) {
                            SpanText(item.label())
                        }
                    }
                }
            }

            IconButton(
                onClick = { onFavoritesClick() },
            ) {
                MdiFavorite()
            }

            IconButton(
                onClick = { onBasketClick() },
            ) {
                MdiShoppingBasket()
            }

            IconButton(
                onClick = { colorMode = colorMode.opposite },
            ) {
                if (colorMode.isLight) MoonIcon() else SunIcon()
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onEnterClick: () -> Unit,
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
                    if (it.key == "Enter") onEnterClick()
                }
        )
    } else {
        IconButton(
            onClick = { onSearchIconClick(true) },
        ) {
            FaMagnifyingGlass(size = IconSize.SM)
        }
    }
}
