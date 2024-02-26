package web.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import feature.shop.navbar.DesktopNavContract
import feature.shop.navbar.DesktopNavRoutes
import feature.shop.navbar.NavbarViewModel
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.widgets.TickerSection
import web.components.widgets.tickerHeight
import web.shadow

private enum class ScrollDirection { UP, DOWN }

@Composable
fun DesktopNav(
    onError: suspend (String) -> Unit,
    desktopNavRoutes: DesktopNavRoutes,
    onTopSpacingChanged: (CSSSizeValue<CSSUnit.px>) -> Unit,
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
    var showNavBarShadow by remember { mutableStateOf(false) }

    window.addEventListener("scroll", {
        val currentScroll = window.scrollY
        scrollDirection = if (lastScrollPosition < currentScroll) ScrollDirection.DOWN else ScrollDirection.UP
        lastScrollPosition = currentScroll

        showTicker = lastScrollPosition < 50.0
        showNavBar = scrollDirection == ScrollDirection.UP || lastScrollPosition < 60.0
        showNavBarShadow = scrollDirection == ScrollDirection.UP && !showTicker
    })

    val topSpacing: CSSSizeValue<CSSUnit.px> = when {
        showNavBar && showTicker -> 0.px
        showNavBar && !showTicker -> (-tickerHeight.value - 4).px
        else -> (-170).px
    }
    onTopSpacingChanged(topSpacing)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .position(Position.Fixed)
            .display(DisplayStyle.Block)
            .backgroundColor(MaterialTheme.colors.surface.value())
            .fillMaxWidth()
            .boxSizing(BoxSizing.BorderBox)
            .zIndex(10)
            .top(topSpacing)
            .boxShadow(
                offsetY = 0.px,
                blurRadius = if (showNavBarShadow) 20.px else 0.px,
                color = shadow(),
            )
            .transition(
                CSSTransition("top", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("box-shadow", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("position", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        TickerSection(
            tickerText = state.strings.ticker,
            onClick = { vm.trySend(DesktopNavContract.Inputs.OnTickerClick) },
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
        )
    }
}
