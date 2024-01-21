package web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import web.components.layouts.PageLayout
import web.pages.about.AboutPage
import web.pages.blog.BlogPage
import web.pages.cart.CartPage
import web.pages.catalogue.CataloguePage
import web.pages.checkout.CheckoutPage
import web.pages.contact.ContactPage
import web.pages.favorites.FavoritesPage
import web.pages.help.HelpPage
import web.pages.home.HomeContent
import web.pages.login.LoginPage
import web.pages.order.OrderPage
import web.pages.payment.PaymentPage
import web.pages.product.ProductPage
import web.pages.profile.ProfilePage
import web.pages.settings.SettingsPage

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

@Suppress("unused")
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
                    RouterScreen.Home -> HomeContent(
                        onProductClick = { id ->
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(
                                    RouterScreen.Product
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

                    RouterScreen.Login -> LoginPage(
                        onLogin = {
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat)
                            )
                        }
                    )

                    RouterScreen.Catalogue -> {
                        CataloguePage(
                            onProductClick = { id ->
                                router.trySend(
                                    RouterContract.Inputs.GoToDestination(
                                        RouterScreen.Product
                                            .directions()
                                            .pathParameter("id", id)
                                            .build()
                                    )
                                )
                            },
                        )
                    }

                    RouterScreen.Product -> {
                        val id: String by currentDestination!!.stringPath("id")
                        ProductPage(
                            id = id,
                            onGoBackClick = { router.trySend(RouterContract.Inputs.GoBack()) }
                        )
                    }

                    RouterScreen.Cart -> {
                        CartPage(
                            goBack = { router.trySend(RouterContract.Inputs.GoBack()) },
                            goToCheckout = {
                                router.trySend(
                                    RouterContract.Inputs.GoToDestination(RouterScreen.Checkout.matcher.routeFormat)
                                )
                            },
                        )
                    }

                    RouterScreen.Checkout -> {
                        CheckoutPage(
                            goBack = { router.trySend(RouterContract.Inputs.GoBack()) },
                            goToPayment = {
                                router.trySend(
                                    RouterContract.Inputs.GoToDestination(RouterScreen.Payment.matcher.routeFormat)
                                )
                            },
                        )
                    }

                    RouterScreen.Payment -> {
                        PaymentPage()
                    }

                    RouterScreen.Order -> {
                        OrderPage()
                    }

                    RouterScreen.Profile -> {
                        ProfilePage()
                    }

                    RouterScreen.Settings -> {
                        SettingsPage()
                    }

                    RouterScreen.About -> {
                        AboutPage()
                    }

                    RouterScreen.Contact -> {
                        ContactPage()
                    }

                    RouterScreen.Help -> {
                        HelpPage()
                    }

                    RouterScreen.Favorites -> {
                        FavoritesPage()
                    }

                    RouterScreen.Blog -> {
                        BlogPage()
                    }
                }
            },
            notFound = { url ->
                PageNotFoundPage(
                    url = url,
                    onGoBackClick = { router.trySend(RouterContract.Inputs.GoBack()) }
                )
            }
        )
    }
}

private fun RouterScreen.pageTitle(shopName: String = "Natalia's Shop"): String {
    // TODO: Localize
    val title = when (this) {
        RouterScreen.Home -> "NatShop"
        RouterScreen.Login -> "Login"
        RouterScreen.Product -> "Product"
        RouterScreen.Catalogue -> "Catalogue"
        RouterScreen.Cart -> "Cart"
        RouterScreen.Checkout -> "Checkout"
        RouterScreen.Payment -> "Payment"
        RouterScreen.Order -> "Order"
        RouterScreen.Profile -> "Profile"
        RouterScreen.Settings -> "Settings"
        RouterScreen.About -> "About"
        RouterScreen.Contact -> "Contact"
        RouterScreen.Help -> "Help"
        RouterScreen.Favorites -> "Favorites"
        RouterScreen.Blog -> "Blog"
    }
    return "$shopName - $title"
}
