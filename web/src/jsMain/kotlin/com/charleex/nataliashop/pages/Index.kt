package com.charleex.nataliashop.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.charleex.nataliashop.components.layouts.PageLayout
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.currentRouteOrNull
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridItem
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import feature.router.RouterScreen
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div

// Container that has a tagline and grid on desktop, and just the tagline on mobile
val HeroContainerStyle by ComponentStyle {
    base { Modifier.fillMaxWidth().gap(2.cssRem) }
    Breakpoint.MD { Modifier.margin { top(20.vh) } }
}

// A demo grid that appears on the homepage because it looks good
val HomeGridStyle by ComponentStyle.base {
    Modifier
        .gap(0.5.cssRem)
        .width(70.cssRem)
        .height(18.cssRem)
}

private val GridCellColorVar by StyleVariable<Color>()
val HomeGridCellStyle by ComponentStyle.base {
    Modifier
        .backgroundColor(GridCellColorVar.value())
        .boxShadow(blurRadius = 0.6.cssRem, color = GridCellColorVar.value())
        .borderRadius(1.cssRem)
}

@Composable
private fun GridCell(color: Color, row: Int, column: Int, width: Int? = null, height: Int? = null) {
    Div(
        HomeGridCellStyle.toModifier()
            .setVariable(GridCellColorVar, color)
            .gridItem(row, column, width, height)
            .toAttrs(),
    )
}

@Page
@Composable
fun HomePage() {
    val initialRoute = RouterScreen.Home
    val scope = rememberCoroutineScope()
    val router = remember(scope) {
        RouterViewModel(
            viewModelScope = scope,
            initialRoute = initialRoute,
            extraInterceptors = listOf(BrowserHashNavigationInterceptor(initialRoute)),
        )
    }
    val routerState by router.observeStates().collectAsState()
    val currentDestination = routerState.currentDestinationOrNull

    PageLayout(routerState.currentRouteOrNull?.pageTitle() ?: "") {
        routerState.renderCurrentDestination(
            route = { routerScreen: RouterScreen ->
                when (routerScreen) {
                    RouterScreen.Home -> Home(
                        onProductClick = { id ->
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(
                                    RouterScreen.ProductDetails
                                        .directions()
                                        .pathParameter("id", id)
                                        .build()
                                )
                            )
                        },
                        onSignOutClick = {
                            router.trySend(
                                RouterContract.Inputs.ReplaceTopDestination(RouterScreen.Login.matcher.routeFormat)
                            )
                        }
                    )

                    RouterScreen.Login -> Login(
                        onLogin = {
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat)
                            )
                        }
                    )

                    RouterScreen.ProductDetails -> {
                        val id: String by currentDestination!!.stringPath("id")
                        ProductDetails(
                            id = id,
                            onGoBackClick = { router.trySend(RouterContract.Inputs.GoBack()) }
                        )
                    }
                }
            },
            notFound = { url ->
                PageNotFound(
                    url = url,
                    onGoBackClick = { router.trySend(RouterContract.Inputs.GoBack()) }
                )
            }
        )
    }
}

private fun RouterScreen.pageTitle(shopName: String = "Natalia's Shop"): String {
    val title = when (this) {
        RouterScreen.Home -> "NatShop"
        RouterScreen.Login -> "Login"
        RouterScreen.ProductDetails -> "Product Details"
    }
    return "$shopName - $title"
}
