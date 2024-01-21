package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.MoonIcon
import com.varabyte.kobweb.silk.components.icons.SunIcon
import com.varabyte.kobweb.silk.components.icons.fa.FaMagnifyingGlass
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFavorite
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Input
import theme.toSitePalette
import web.components.widgets.IconButton
import web.components.widgets.noBorder

@Composable
fun LogoSearchButtonsSection(
    searchValue: String,
    onSearchValueChanged: (String) -> Unit,
    logoIconUrl: String,
    horizontalMargin: CSSSizeValue<CSSUnit.em>,
    onLogoClick: () -> Unit,
    onLoginClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBasketClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(left = horizontalMargin, right = (horizontalMargin.value / 2).em)
            .gap(1.em),
    ) {
        Logo(
            description = "NataliaShop", // TODO: Localize
            iconUrl = logoIconUrl,
            onLogoClick = onLogoClick,
        )
        SearchBar(
            value = searchValue,
            onValueChange = onSearchValueChanged,
            placeholder = "Search", // TODO: Localize
            onEnterClick = {},
            onSearchIconClick = { },
        )
        Buttons(
            loginProfileText = "Log in", // TODO: Localize
            onLoginProfileClick = onLoginClick,
            onFavoritesClick = onFavoritesClick,
            onBasketClick = onBasketClick,
        )
    }
}

@Composable
private fun Logo(
    iconUrl: String,
    description: String,
    onLogoClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(0.5.em)
            .onClick { onLogoClick() },
    ) {
        Link("/") {
            Image(
                src = "/kobweb-logo.png",
                description = description,
                modifier = Modifier
                    .height(2.cssRem)
                    .display(DisplayStyle.Block),
            )
        }
//        Image(
//            src = iconUrl,
//            alt = "",
//            width = 50,
//            height = 50,
//            modifier = Modifier
//                .objectFit(ObjectFit.Cover),
//        )
//        Column(
//
//        ) {
//            SpanText(
//                text = textTop,
//                modifier = Modifier
//                    .fontSize(.8.em),
//            )
//            SpanText(
//                text = textBottom,
//                modifier = Modifier
//                    .fontSize(.8.em),
//            )
//        }
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
) {
    val breakpoint = rememberBreakpoint()
    val colorMode by ColorMode.currentState
    val darkTheme = colorMode.isDark

    var focused by remember { mutableStateOf(false) }

    val borderColor = colorMode.toSitePalette().border
    val inputTextColor = if (darkTheme) Colors.White else Colors.Black

    val searchIconColor = if (focused) {
        colorMode.toSitePalette().primary
    } else borderColor

    LaunchedEffect(breakpoint) {
        if (breakpoint >= Breakpoint.SM) onSearchIconClick(false)
    }

    if (breakpoint >= Breakpoint.SM) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(left = 20.px)
                .height(54.px)
//                .backgroundColor(bgColor)
                .borderRadius(r = 100.px)
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = borderColor,
                )
                .transition(CSSTransition("border", 200.ms)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FaMagnifyingGlass(
                modifier = Modifier
                    .margin(right = 14.px)
                    .color(searchIconColor)
                    .transition(CSSTransition("color", 200.ms)),
                size = IconSize.SM,
            )
            Input(
                type = InputType.Text,
                attrs = Modifier
                    .fillMaxSize()
                    .color(inputTextColor)
                    .backgroundColor(Colors.Transparent)
                    .noBorder()
                    .onFocusIn { focused = true }
                    .onFocusOut { focused = false }
                    .onKeyDown {
                        if (it.key == "Enter") {
                            onEnterClick()
                        }
                    }
                    .toAttrs {
                        value(value)
                        onInput { onValueChange(it.value) }
                        placeholder(placeholder)
                    },
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FaMagnifyingGlass(
                modifier = Modifier
                    .margin(right = 14.px)
                    .color(colorMode.toSitePalette().primary)
                    .cursor(Cursor.Pointer)
                    .onClick { onSearchIconClick(true) },
                size = IconSize.SM,
            )
            Spacer()
        }
    }
}

@Composable
private fun Buttons(
    loginProfileText: String,
    onLoginProfileClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onBasketClick: () -> Unit,
) {
    val colorMode by ColorMode.currentState

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(.25.em)
            .color(colorMode.toSitePalette().onSurface),
    ) {
        LoginProfileButton(
            text = loginProfileText,
            tooltip = "Log in", // TODO: Localize
            onClick = onLoginProfileClick,
        )
        FavoriteButton(
            tooltip = "Favorites", // TODO: Localize
            onClick = onFavoritesClick,
        )
        BasketButton(
            tooltip = "Basket", // TODO: Localize
            onClick = onBasketClick,
        )
        ColorModeButton(
            tooltip = "Toggle color mode", // TODO: Localize
        )
    }
}

val ClickableStyle by ComponentStyle {
    hover {
        Modifier
            .cursor(Cursor.Pointer)
            .color(colorMode.toSitePalette().primary)
    }
}

@Composable
private fun LoginProfileButton(
    text: String,
    tooltip: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = ClickableStyle.toModifier()
            .gap(0.25.em)
            .margin(right = 0.25.em)
            .onClick { onClick() },
    ) {
        MdiPerson(
            modifier = Modifier.fontSize(20.px),
        )
        SpanText(
            text = text,
            modifier = Modifier
                .whiteSpace(WhiteSpace.NoWrap)
                .fontWeight(FontWeight.SemiBold)
                .fontSize(0.8.em),
        )
    }
    Tooltip(ElementTarget.PreviousSibling, tooltip, placement = PopupPlacement.BottomRight)
}

@Composable
private fun FavoriteButton(
    tooltip: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = { onClick() },
    ) {
        MdiFavorite(
            modifier = ClickableStyle.toModifier(),
        )
    }
    Tooltip(ElementTarget.PreviousSibling, tooltip, placement = PopupPlacement.BottomRight)
}


@Composable
private fun BasketButton(
    tooltip: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = { onClick() },
    ) {
        MdiShoppingBasket(
            modifier = ClickableStyle.toModifier(),
        )
    }
    Tooltip(ElementTarget.PreviousSibling, tooltip, placement = PopupPlacement.BottomRight)
}

@Composable
private fun ColorModeButton(
    tooltip: String,
) {
    var colorMode by ColorMode.currentState

    IconButton(
        onClick = { colorMode = colorMode.opposite },
    ) {
        val modifier = ClickableStyle.toModifier()
        if (colorMode.isLight) MoonIcon(modifier) else SunIcon(modifier)
    }

    Tooltip(ElementTarget.PreviousSibling, tooltip, placement = PopupPlacement.BottomRight)
}
