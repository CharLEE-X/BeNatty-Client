package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
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
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
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
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronRight
import com.varabyte.kobweb.silk.components.icons.mdi.MdiMenu
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson2
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingBasket
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import web.NavIconStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppElevatedCard
import web.components.widgets.Logo
import web.components.widgets.SearchBar


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    storeText: String,
    aboutText: String,
    searchPlaceholder: String,
    shippingReturnsText: String,
    basketCount: Int,
    storeMenuItems: List<String>,
    backgroundColor: CSSColorValue,
    contentColor: CSSColorValue,
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
                .padding(leftRight = 20.px, topBottom = 20.px)
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
                contentColor = contentColor,
            )
            Logo(
                onClick = onLogoClick,
                modifier = Modifier.margin(leftRight = 1.em)
            )
            RightSection(
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
                backgroundColor = backgroundColor,
                contentColor = contentColor,
            )
        }
    }
}


@Composable
private fun RightSection(
    modifier: Modifier = Modifier,
    searchValue: String,
    basketCount: Int,
    searchPlaceholder: String,
    onSearchValueChanged: (String) -> Unit,
    onEnterPress: () -> Unit,
    onProfileClick: () -> Unit,
    onBasketClick: () -> Unit,
    backgroundColor: CSSColorValue,
    contentColor: CSSColorValue,
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
                    MdiPerson2(
                        style = IconStyle.OUTLINED,
                        modifier = NavIconStyle.toModifier().onClick { onProfileClick() }
                    )
                    Box(
                        modifier = Modifier
                            .backgroundColor(contentColor)
                            .width(0.5.px)
                            .minHeight(30.px)
                            .fillMaxHeight()
                    )
                    MdiShoppingBasket(
                        style = IconStyle.OUTLINED,
                        modifier = NavIconStyle.toModifier().onClick { onBasketClick() }
                    )
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
    contentColor: CSSColorValue,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .display(DisplayStyle.Flex)
                    .flexWrap(FlexWrap.Wrap)
                    .alignItems(AlignItems.Center)
                    .alignContent(AlignContent.Center)
                    .gap(16.px)
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
                        contentColor = contentColor,
                        modifier = Modifier
                            .onMouseEnter { isStoreButtonHovered = true }
                            .onMouseLeave { isStoreButtonHovered = false }
                    )
                    StoreSubMenu(
                        open = open,
                        items = storeMenuItems,
                        onStoreMenuItemSelected = onStoreMenuItemSelected,
                        onMenuItemHovered = { isMenuHovered = it },
                        contentColor = contentColor,
                        modifier = Modifier
                            .translateX((-16).px)
                            .padding(top = 10.px)
                            .onMouseEnter { isMenuHovered = true }
                            .onMouseLeave {
                                isMenuHovered = false
                                scheduleCloseMenu()
                            }
                    )
                }
                ListMenuItem(
                    text = aboutText,
                    hovered = isAboutButtonHovered,
                    onClick = onAboutClick,
                    contentColor = contentColor,
                    modifier = Modifier
                        .onMouseEnter { isAboutButtonHovered = true }
                        .onMouseLeave { isAboutButtonHovered = false }
                )
                ListMenuItem(
                    text = shippingReturnsText,
                    hovered = isShippingButtonHovered,
                    onClick = onShippingReturnsClick,
                    contentColor = contentColor,
                    modifier = Modifier
                        .onMouseEnter { isShippingButtonHovered = true }
                        .onMouseLeave { isShippingButtonHovered = false }
                )
            }
        }
    }
}

@Composable
private fun StoreSubMenu(
    modifier: Modifier = Modifier,
    open: Boolean,
    items: List<String>,
    onStoreMenuItemSelected: (String) -> Unit,
    onMenuItemHovered: (Boolean) -> Unit,
    contentColor: CSSColorValue,
) {
    AppElevatedCard(
        modifier = modifier
            .position(Position.Absolute)
            .zIndex(9)
            .opacity(if (open) 1.0 else 0.0)
            .visibility(if (open) Visibility.Visible else Visibility.Hidden)
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
    ) {
        Column(
            modifier = Modifier
                .width(200.px)
                .fontWeight(300)
                .fontSize(14.px)
                .lineHeight(18.px)
                .padding(leftRight = 20.px, topBottom = 16.px)
        ) {
            items.forEach { item ->
                var itemHovered by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onClick { onStoreMenuItemSelected(item) }
                        .cursor(Cursor.Pointer)
                        .padding(leftRight = 12.px)
                        .onMouseEnter {
                            onMenuItemHovered(true)
                            itemHovered = true
                        }
                        .onMouseLeave { itemHovered = false }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .gap(1.px)
                    ) {
                        SpanText(
                            text = item,
                            modifier = Modifier
                                .fontSize(14.px)
                                .whiteSpace(WhiteSpace.NoWrap)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .onClick { onStoreMenuItemSelected(item) }
                                .height(1.px)
                                .fillMaxWidth(if (itemHovered) 100.percent else 0.percent)
                                .backgroundColor(contentColor)
                                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    hovered: Boolean,
    hasDropdown: Boolean = false,
    contentColor: CSSColorValue,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { onClick() }
            .gap(2.px)
            .cursor(Cursor.Pointer)
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
                MdiChevronRight(
                    modifier = Modifier
                        .size(12.px)
                        .rotate(90.deg)
                        .translateY((-6).px)
                        .color(contentColor)
                        .fontWeight(if (hovered) FontWeight.Bold else FontWeight.Normal)
                        .transition(
                            CSSTransition("font-weight", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("rotate", 0.3.s, TransitionTimingFunction.Ease),
                        )
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