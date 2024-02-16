package web.components.sections.desktopNav.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.MoonIcon
import com.varabyte.kobweb.silk.components.icons.SunIcon
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFavorite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span
import web.components.sections.desktopNav.DesktopNavContract
import web.components.sections.desktopNav.DesktopNavViewModel
import web.components.sections.desktopNav.label
import web.components.widgets.AppIconButton
import web.components.widgets.AppTextButton
import web.components.widgets.Logo
import web.components.widgets.SearchBar
import web.compose.material3.component.labs.Menu
import web.compose.material3.component.labs.MenuItem

@Composable
fun LogoSearchButtonsSection(
    vm: DesktopNavViewModel,
    isAuthenticated: Boolean,
    modifier: Modifier,
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    onLogoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onCartClick: () -> Unit,
) {
    var colorMode by ColorMode.currentState
    var showAccountMenu by remember { mutableStateOf(false) }
    val accountMenuAnchor = "account-button"

    val size = 3.em

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.gap(1.em),
    ) {
        Logo(
            size = size,
            onClick = { onLogoClick() },
        )
        SearchBar(
            value = searchValue,
            onValueChange = onSearchValueChanged,
            placeholder = DesktopNavContract.Strings.search,
            height = size,
            onEnterPress = {},
            onSearchIconClick = { },
            containerShape = 30.px,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(.25.em)
        ) {
            Span(Modifier.position(Position.Relative).toAttrs()) {
                AppTextButton(
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
                    SpanText(if (!isAuthenticated) DesktopNavContract.Strings.login else "Account")
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

            AppIconButton(
                onClick = { onWishlistClick() },
            ) {
                MdiFavorite()
            }

            AppIconButton(
                onClick = { onCartClick() },
            ) {
                MdiShoppingBasket()
            }

            AppIconButton(
                onClick = { colorMode = colorMode.opposite },
            ) {
                if (colorMode.isLight) MoonIcon() else SunIcon()
            }
        }
    }
}
