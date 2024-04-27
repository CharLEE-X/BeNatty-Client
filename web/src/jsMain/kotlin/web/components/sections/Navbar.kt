package web.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignContent
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLightMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiMenu
import com.varabyte.kobweb.silk.components.icons.mdi.MdiModeNight
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson2
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import theme.MaterialTheme
import theme.sp
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.Logo
import web.components.widgets.RotatableChevron
import web.components.widgets.SearchBar
import web.components.widgets.ShimmerHeader
import web.compose.material3.component.IconButton
import web.util.glossy
import web.util.onEnterKeyDown

@Composable
fun NavBar(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    storeText: String,
    aboutText: String,
    searchPlaceholder: String,
    shippingReturnsText: String,
    basketCount: Int,
    storeMenuItems: List<String>,
    onStoreMenuItemSelected: (String) -> Unit,
    onStoreClick: () -> Unit,
    onAboutClick: () -> Unit,
    onShippingReturnsClick: () -> Unit,
    onLogoClick: () -> Unit,
    onSearchValueChanged: (String) -> Unit,
    onEnterPress: () -> Unit,
    onProfileClick: () -> Unit,
    onBasketClick: () -> Unit,
) {
    var searchValue by remember { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .padding(leftRight = 24.px, topBottom = 20.px)
                .gap(20.px)
                .display(DisplayStyle.Grid)
                .styleModifier { property("grid-template-columns", "1fr auto 1fr") }
        ) {
            ListMenu(
                storeText = storeText,
                aboutText = aboutText,
                shippingReturnsText = shippingReturnsText,
                storeMenuItems = storeMenuItems,
                onStoreClick = onStoreClick,
                onAboutClick = onAboutClick,
                onShippingReturnsClick = onShippingReturnsClick,
                onStoreMenuItemSelected = onStoreMenuItemSelected,
            )
            Logo(
                onClick = onLogoClick,
                modifier = Modifier.margin(leftRight = 1.em)
            )
            RightSection(
                isLoading = isLoading,
                searchPlaceholder = searchPlaceholder,
                searchValue = searchValue,
                basketCount = basketCount,
                onSearchValueChanged = {
                    searchValue = it
                    onSearchValueChanged(it)
                },
                onEnterPress = onEnterPress,
                onProfileClick = onProfileClick,
                onBasketClick = onBasketClick,
            )
        }
    }
}

@Composable
private fun RightSection(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    searchValue: String,
    basketCount: Int,
    searchPlaceholder: String,
    onSearchValueChanged: (String) -> Unit,
    onEnterPress: () -> Unit,
    onProfileClick: () -> Unit,
    onBasketClick: () -> Unit,
    backgroundColor: CSSColorValue = MaterialTheme.colors.surfaceContainerLow,
    contentColor: CSSColorValue = MaterialTheme.colors.onSurface,
) {
    val breakpoint = rememberBreakpoint()

    when (breakpoint) {
        Breakpoint.ZERO,
        Breakpoint.SM -> {
            Box(
                contentAlignment = Alignment.CenterEnd,
            ) {
                SpanText(
                    text = basketCount.toString(),
                    modifier = Modifier
                        .justifyContent(JustifyContent.Center)
                        .display(DisplayStyle.Flex)
                        .alignItems(AlignItems.Center)
                        .color(contentColor)
                        .backgroundColor(backgroundColor)
                        .borderRadius(50.percent)
                        .margin(leftRight = 30.px)
                        .fontWeight(FontWeight.Bold)
                        .margin(1.em)
                        .fontSize(16.px)
                        .lineHeight(130.percent)
                        .size(30.px)
                        .tabIndex(0)
                        .onEnterKeyDown(onBasketClick)
                )
            }
        }

        else -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = modifier.gap(1.em)
            ) {
                SearchBar(
                    value = searchValue,
                    onValueChange = onSearchValueChanged,
                    placeholder = searchPlaceholder,
                    onEnterPress = onEnterPress,
                    modifier = Modifier
                        .height(50.px)
                        .width(224.px)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxHeight()
                        .gap(0.5.em)
                ) {
                    val iconStyle = IconStyle.OUTLINED
                    val iconModifier = Modifier.color(contentColor)

                    if (!isLoading) {
                        IconButton(
                            onClick = { onProfileClick() },
                            modifier = Modifier.onEnterKeyDown(onProfileClick)
                        ) {
                            MdiPerson2(
                                style = IconStyle.OUTLINED,
                                modifier = iconModifier
                            )
                        }
                    } else {
                        ShimmerHeader(Modifier.aspectRatio(1))
                    }

                    var colorMode by ColorMode.currentState
                    IconButton(
                        onClick = { colorMode = colorMode.opposite },
                        modifier = Modifier
                            .onEnterKeyDown { colorMode = colorMode.opposite }
                    ) {
                        if (colorMode.isLight) MdiLightMode(iconModifier, iconStyle)
                        else MdiModeNight(iconModifier, iconStyle)
                    }
                    Box(
                        modifier = Modifier
                            .backgroundColor(contentColor)
                            .width(0.5.px)
                            .minHeight(30.px)
                            .fillMaxHeight()
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(24.px)
                                .backgroundColor(MaterialTheme.colors.onSurface)
                                .borderRadius(50.percent)
                                .translateY(if (basketCount > 0) (-28).px else 0.px)
                                .opacity(if (basketCount > 0) 1.0 else 0.0)
                                .transition(
                                    CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                                    CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                                )
                        ) {
                            SpanText(
                                text = basketCount.toString(),
                                modifier = Modifier
                                    .fontSize(12.sp)
                                    .fontWeight(FontWeight.SemiBold)
                                    .color(MaterialTheme.colors.surface)
                            )
                        }
                        IconButton(
                            onClick = { onBasketClick() },
                            modifier = Modifier.onEnterKeyDown(onBasketClick)
                        ) {
                            MdiShoppingBasket(iconModifier, iconStyle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListMenu(
    modifier: Modifier = Modifier,
    storeText: String,
    aboutText: String,
    shippingReturnsText: String,
    storeMenuItems: List<String>,
    onStoreMenuItemSelected: (String) -> Unit,
    onStoreClick: () -> Unit,
    onAboutClick: () -> Unit,
    onShippingReturnsClick: () -> Unit,
) {
    val breakpoint = rememberBreakpoint()

    val scope = rememberCoroutineScope()
    var scheduled: Job? = null

    var isStoreButtonHovered by remember { mutableStateOf(false) }
    var isAboutButtonHovered by remember { mutableStateOf(false) }
    var isShippingButtonHovered by remember { mutableStateOf(false) }
    var isMenuHovered by remember { mutableStateOf(isStoreButtonHovered) }
    var open by remember { mutableStateOf(isStoreButtonHovered) }

    fun scheduleCloseMenu() {
        scheduled = scope.launch {
            delay(1000)
            if (!(isStoreButtonHovered || isMenuHovered)) {
                open = false
            }
        }
    }

    LaunchedEffect(isStoreButtonHovered, isMenuHovered) {
        if (isStoreButtonHovered || isMenuHovered) {
            scheduled?.cancel()
            open = true
        } else {
            scheduleCloseMenu()
        }
    }

    when (breakpoint) {
        Breakpoint.ZERO,
        Breakpoint.SM -> {
            MdiMenu(
                modifier = modifier.onClick { isStoreButtonHovered = true }
            )
        }

        else -> {
            var isListMenuItemFocused by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .display(DisplayStyle.Flex)
                    .flexWrap(FlexWrap.Wrap)
                    .alignItems(AlignItems.Center)
                    .alignContent(AlignContent.Center)
                    .gap(16.px)
                    .onFocusIn { isListMenuItemFocused = true }
                    .onFocusOut { isListMenuItemFocused = false }
            ) {
                Span(
                    Modifier
                        .position(Position.Relative)
                        .toAttrs()
                ) {
                    ListMenuItem(
                        text = storeText,
                        hasDropdown = true,
                        onClick = onStoreClick,
                        hovered = isStoreButtonHovered || isMenuHovered,
                        modifier = Modifier
                            .onMouseEnter { isStoreButtonHovered = true }
                            .onMouseLeave { isStoreButtonHovered = false }
                    )
                    AppMenu(
                        open = open || isListMenuItemFocused,
                        items = storeMenuItems,
                        onItemSelected = onStoreMenuItemSelected,
                        modifier = Modifier
                            .translateX((-16).px)
                            .margin(top = 10.px)
                            .onMouseOver { isMenuHovered = true }
                            .onMouseOut {
                                isMenuHovered = false
                                scheduleCloseMenu()
                            }
                    )
                }
                ListMenuItem(
                    text = aboutText,
                    hovered = isAboutButtonHovered,
                    onClick = onAboutClick,
                    modifier = Modifier
                        .onMouseEnter { isAboutButtonHovered = true }
                        .onMouseLeave { isAboutButtonHovered = false }
                )
                ListMenuItem(
                    text = shippingReturnsText,
                    hovered = isShippingButtonHovered,
                    onClick = onShippingReturnsClick,
                    modifier = Modifier
                        .onMouseEnter { isShippingButtonHovered = true }
                        .onMouseLeave { isShippingButtonHovered = false }
                )
            }
        }
    }
}

@Composable
fun AppMenu(
    modifier: Modifier = Modifier,
    open: Boolean,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    underlineColor: CSSColorValue = MaterialTheme.colors.onSurface,
    fontSize: CSSLengthOrPercentageNumericValue = 14.px,
    itemsGap: CSSLengthOrPercentageNumericValue = 0.px,
    bgAlpha: Int = 225,
) {
    Box(
        modifier = modifier
            .position(Position.Absolute)
            .zIndex(5)
            .width(200.px)
            .opacity(if (open) 1.0 else 0.0)
            .visibility(if (open) Visibility.Visible else Visibility.Hidden)
            .userSelect(UserSelect.None)
            .glossy(
                color = MaterialTheme.colors.background,
                alpha = bgAlpha,
            )
            .border(
                width = 1.px,
                color = MaterialTheme.colors.surfaceContainerHighest,
                style = LineStyle.Solid
            )
            .translate(
                tx = (-12).px,
                ty = if (open) (-5).px else 20.px,
            )
            .transition(
                CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("top", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("visibility", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
            )
            .tabIndex(0)
    ) {
        Column(
            modifier = Modifier
                .gap(itemsGap)
                .fontWeight(400)
                .fontSize(14.px)
                .lineHeight(18.px)
                .padding(leftRight = 20.px, topBottom = 16.px)
        ) {
            items.forEach { item ->
                MenuItem(
                    onStoreMenuItemSelected = onItemSelected,
                    item = item,
                    underlineColor = underlineColor,
                    fontSize = fontSize,
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    onStoreMenuItemSelected: (String) -> Unit,
    item: String,
    modifier: Modifier = Modifier,
    underlineColor: CSSColorValue,
    fontSize: CSSLengthOrPercentageNumericValue
) {
    var itemHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onClick { onStoreMenuItemSelected(item) }
            .cursor(Cursor.Pointer)
            .padding(leftRight = 12.px)
            .onMouseEnter {
                itemHovered = true
            }
            .onMouseLeave { itemHovered = false }
            .tabIndex(0)
            .onEnterKeyDown { onStoreMenuItemSelected(item) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .gap(1.px)
        ) {
            SpanText(
                text = item,
                modifier = Modifier
                    .fontSize(fontSize)
                    .whiteSpace(WhiteSpace.NoWrap)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .onClick { onStoreMenuItemSelected(item) }
                    .height(2.px)
                    .fillMaxWidth(if (itemHovered) 100.percent else 0.percent)
                    .backgroundColor(underlineColor)
                    .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
            )
        }
    }
}

@Composable
private fun ListMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    hovered: Boolean,
    hasDropdown: Boolean = false,
    contentColor: CSSColorValue = MaterialTheme.colors.onSurface,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { onClick() }
            .gap(2.px)
            .cursor(Cursor.Pointer)
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(12.px)
        ) {
            SpanText(
                text = text.uppercase(),
                modifier = Modifier
                    .color(contentColor)
                    .whiteSpace(WhiteSpace.NoWrap)
            )
            if (hasDropdown) {
                RotatableChevron(
                    hovered = hovered,
                    open = hovered,
                    color = contentColor,
                    modifier = Modifier
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                .backgroundColor(contentColor)
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}
