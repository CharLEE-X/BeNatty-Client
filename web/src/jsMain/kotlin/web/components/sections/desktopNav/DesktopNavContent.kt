package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.BoxSizing
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
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignContent
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
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
import com.varabyte.kobweb.silk.components.style.common.SmoothColorTransitionDurationVar
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import feature.shop.navbar.DesktopNavContract
import feature.shop.navbar.DesktopNavRoutes
import feature.shop.navbar.NavbarViewModel
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.dom.Span
import theme.MaterialTheme
import theme.OldColorsJs
import web.components.layouts.oneLayoutMaxWidth
import web.components.sections.desktopNav.sections.TickerSection
import web.components.widgets.Logo
import web.components.widgets.SearchBar

private enum class ScrollDirection { UP, DOWN }

@Composable
fun DesktopNav(
    onError: suspend (String) -> Unit,
    desktopNavRoutes: DesktopNavRoutes,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        NavbarViewModel(
            scope = scope,
            onError = onError,
            desktopNavRoutes = desktopNavRoutes,
        )
    }
    val state by vm.observeStates().collectAsState()

    var lastScrollPosition by remember { mutableStateOf(0.0) }
    var scrollDirection: ScrollDirection by remember { mutableStateOf(ScrollDirection.DOWN) }

    var showTicker by remember { mutableStateOf(true) }
    var showNavBar by remember { mutableStateOf(true) }
    var showNavBarShadow by remember { mutableStateOf(true) }

    window.addEventListener("scroll", {
        val currentScroll = window.scrollY
        scrollDirection = if (lastScrollPosition < currentScroll) ScrollDirection.DOWN else ScrollDirection.UP
        lastScrollPosition = currentScroll

        showTicker = lastScrollPosition < 50.0
        showNavBar = scrollDirection == ScrollDirection.UP || lastScrollPosition < 60.0
        showNavBarShadow = scrollDirection == ScrollDirection.UP && !showTicker

        println("scrollPosition: $lastScrollPosition, scrollDirection: $scrollDirection, showNavbar: $showNavBar, showTicker: $showTicker, showNavBarShadow: $showNavBarShadow")
    })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .position(Position.Fixed)
            .display(DisplayStyle.Block)
            .fillMaxWidth()
            .boxSizing(BoxSizing.BorderBox)
            .zIndex(10)
    ) {
        TickerSection(
            tickerText = state.strings.ticker,
            onClick = { vm.trySend(DesktopNavContract.Inputs.OnTickerClick) },
            modifier = Modifier
                .zIndex(20)
//                .position(if (showTicker) Position.Fixed else Position.Absolute)
//                .display(DisplayStyle.Block)
//                .top(0.px)
//                .translateY(if (showTicker) 0.px else (-40).px)
                .transition(
                    CSSTransition("translate", SmoothColorTransitionDurationVar.value()),
                    CSSTransition("position", SmoothColorTransitionDurationVar.value()),
                )
        )
        NavBar(
            storeText = state.strings.store,
            aboutText = state.strings.about,
            shippingReturnsText = state.strings.shippingReturns,
            searchPlaceholder = state.strings.search,
            basketCount = state.basketCount,
            storeMenuItems = state.storeMenuItems,
            onStoreClick = { vm.trySend(DesktopNavContract.Inputs.OnStoreClick) },
            onAboutClick = { vm.trySend(DesktopNavContract.Inputs.OnAboutClick) },
            onShippingReturnsClick = { vm.trySend(DesktopNavContract.Inputs.OnShippingAndReturnsClick) },
            onStoreMenuItemSelected = { vm.trySend(DesktopNavContract.Inputs.OnStoreMenuItemSelected(it)) },
            onLogoClick = { vm.trySend(DesktopNavContract.Inputs.OnLogoClick) },
            onSearchValueChanged = { vm.trySend(DesktopNavContract.Inputs.OnSearchValueChanged(it)) },
            onEnterPress = { vm.trySend(DesktopNavContract.Inputs.OnSearchEnterPress) },
            onProfileClick = { vm.trySend(DesktopNavContract.Inputs.OnProfileClick) },
            onBasketClick = { vm.trySend(DesktopNavContract.Inputs.OnBasketClick) },
            modifier = Modifier
                .zIndex(5)
//                .position(if (showNavBar) Position.Fixed else Position.Relative)
//                .display(DisplayStyle.Block)
//                .top(if (showTicker) tickerHeight else 0.px)
//                .translateY(if (showNavBar) 0.px else (-100).px)
                .boxShadow(
                    offsetY = 0.px,
                    blurRadius = if (showNavBarShadow) 20.px else 0.px,
                    color = OldColorsJs.neutral200,
                )
                .transition(
                    CSSTransition("translate", SmoothColorTransitionDurationVar.value()),
                    CSSTransition("box-shadow", SmoothColorTransitionDurationVar.value()),
                    CSSTransition("position", SmoothColorTransitionDurationVar.value()),
                )
        )
    }
}

@Composable
fun NavBar(
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .position(Position.Fixed)
            .fillMaxWidth()
            .maxWidth(oneLayoutMaxWidth)
            .backgroundColor(Colors.White)
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
        )
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
                        .color(MaterialTheme.colors.mdSysColorBackground.value())
                        .backgroundColor(MaterialTheme.colors.mdSysColorOnBackground.value())
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
                    onSearchIconClick = { },
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
                    NavIcon(
                        icon = { MdiPerson2(style = IconStyle.OUTLINED) },
                        onClick = onProfileClick
                    )
                    Box(
                        modifier = Modifier
                            .backgroundColor(MaterialTheme.colors.mdSysColorOnBackground.value())
                            .width(0.5.px)
                            .minHeight(30.px)
                            .fillMaxHeight()
                    )
                    NavIcon(
                        icon = { MdiShoppingBasket(style = IconStyle.OUTLINED) },
                        onClick = onBasketClick
                    )
                }
            }
        }
    }
}

@Composable
private fun NavIcon(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Span(
        Modifier
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
            .onClick { onClick() }
            .opacity(if (hovered) 0.6 else 1.0)
            .fontSize(24.px)
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
            .cursor(Cursor.Pointer)
            .toAttrs()
    ) {
        icon()
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
                        modifier = Modifier
                            .onMouseEnter { isStoreButtonHovered = true }
                            .onMouseLeave { isStoreButtonHovered = false }
                    )
                    StoreSubMenu(
                        open = open,
                        items = storeMenuItems,
                        onStoreMenuItemSelected = onStoreMenuItemSelected,
                        onMenuItemHovered = { isMenuHovered = it },
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
private fun StoreSubMenu(
    modifier: Modifier = Modifier,
    open: Boolean,
    items: List<String>,
    onStoreMenuItemSelected: (String) -> Unit,
    onMenuItemHovered: (Boolean) -> Unit,
) {
    Box(
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
                .background(Colors.White)
                .boxShadow(
                    offsetX = 0.px,
                    offsetY = 4.px,
                    blurRadius = 20.px,
                    color = OldColorsJs.neutral200,
                )
                .borderRadius(12.px)
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
                                .backgroundColor(MaterialTheme.colors.mdSysColorOnBackground.value())
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
                    .color(MaterialTheme.colors.mdSysColorOnBackground.value())
                    .whiteSpace(WhiteSpace.NoWrap)
            )
            if (hasDropdown) {
                MdiChevronRight(
                    modifier = Modifier
                        .size(12.px)
                        .rotate(90.deg)
                        .translateY((-6).px)
                        .color(MaterialTheme.colors.mdSysColorOnBackground.value())
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
                .backgroundColor(MaterialTheme.colors.mdSysColorOnBackground.value())
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}
