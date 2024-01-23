package web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.currentRouteOrNull
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gridRow
import feature.router.RouterScreen
import feature.router.RouterViewModel
import web.components.layouts.PageLayout
import web.components.sections.Footer
import web.components.sections.desktopNav.DesktopNav
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

@Composable
fun RouterContent(
    isAuthenticated: Boolean,
    onLogOut: () -> Unit,
    onError: (String) -> Unit,
    homeScreen: RouterScreen = RouterScreen.Home,
    loginScreen: RouterScreen = RouterScreen.Login,
) {
    val scope = rememberCoroutineScope()
    val initialRoute = when (isAuthenticated) {
        true -> homeScreen
        false -> loginScreen
    }
    val router = remember(scope) {
        RouterViewModel(
            viewModelScope = scope,
            initialRoute = initialRoute,
            extraInterceptors = listOf(BrowserHashNavigationInterceptor(initialRoute)),
        )
    }
    val routerState by router.observeStates().collectAsState()
    val currentDestination = routerState.currentDestinationOrNull

    var searchValue by remember { mutableStateOf("") }

    val categories = Category.entries.toList()
    var currentCategory by remember { mutableStateOf<Category?>(null) }

    val categoryFilters = listOf(
        CategoryFilter("1", "All"),
        CategoryFilter("2", "Women"),
        CategoryFilter("3", "Men"),
        CategoryFilter("4", "Kids"),
    )

    var currentCategoryFilter by remember { mutableStateOf<CategoryFilter?>(null) }

    PageLayout(
        title = routerState.currentRouteOrNull?.pageTitle() ?: "",
        topBar = {
            if (routerState.currentRouteOrNull?.matcher != RouterScreen.Login.matcher) {
                DesktopNav(
                    isAuthenticated = isAuthenticated,
                    currentLanguageImageUrl = "https://m.media-amazon.com/images/I/61msrRHflnL._AC_SL1500_.jpg",
                    searchValue = searchValue,
                    onSearchValueChanged = { searchValue = it },
                    categories = categories,
                    categoryFilters = categoryFilters,
                    currentCategoryFilter = currentCategoryFilter,
                    onCategoryClick = {
                        currentCategory = it
                        currentCategoryFilter = categoryFilters.firstOrNull()
                    },
                    onCategoryFilterClick = { currentCategoryFilter = it },
                    onLogoClick = {
                        currentCategory = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat))
                    },
                    onLoginClick = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Login.matcher.routeFormat))
                    },
                    onFavoritesClick = {
//                    currentCategory = null
//                    currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Favorites.matcher.routeFormat))
                    },
                    onBasketClick = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Order.matcher.routeFormat))
                    },
                    onHelpAndFaqUrlClick = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Help.matcher.routeFormat))
                    },
                    onCurrencyAndLanguageClick = {
                        // TODO: Show lang and currency chooser
                    },
                    goToAccount = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Profile.matcher.routeFormat))
                    },
                    goToOrders = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Order.matcher.routeFormat))
                    },
                    goToReturns = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Order.matcher.routeFormat))
                    },
                    goToWishlist = {
                        currentCategory = null
                        currentCategoryFilter = null
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Favorites.matcher.routeFormat))
                    },
                    logOut = {
                        currentCategory = null
                        currentCategoryFilter = null
                        onLogOut()
                    },
                    onError = onError,
                )
            }
        },
        footer = {
            if (routerState.currentRouteOrNull?.matcher != RouterScreen.Login.matcher) {
                Footer(modifier = Modifier.fillMaxWidth().gridRow(2))
            }
        },
    ) {
        routerState.renderCurrentDestination(
            route = { routerScreen: RouterScreen ->
                when (routerScreen) {
                    RouterScreen.Home -> HomeContent(
                        onError = onError,
                        onProductClick = { id ->
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(
                                    feature.router.RouterScreen.Product
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
                        onError = onError,
                        onAuthenticated = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat))
                        },
                        gotoPrivacyPolicy = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.About.matcher.routeFormat))
                        },
                        gotoTnC = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.About.matcher.routeFormat))
                        },
                    )

                    RouterScreen.Catalogue -> {
                        CataloguePage(
                            onError = onError,
                            onProductClick = { id ->
                                router.trySend(
                                    RouterContract.Inputs.GoToDestination(
                                        feature.router.RouterScreen.Product
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
                            onError = onError,
                            onGoBackClick = { router.trySend(RouterContract.Inputs.GoBack()) }
                        )
                    }

                    RouterScreen.Cart -> {
                        CartPage(
                            onError = onError,
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
                            onError = onError,
                            goBack = { router.trySend(RouterContract.Inputs.GoBack()) },
                            goToPayment = {
                                router.trySend(
                                    RouterContract.Inputs.GoToDestination(RouterScreen.Payment.matcher.routeFormat)
                                )
                            },
                        )
                    }

                    RouterScreen.Payment -> {
                        PaymentPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Order -> {
                        OrderPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Profile -> {
                        ProfilePage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Settings -> {
                        SettingsPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.About -> {
                        AboutPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Contact -> {
                        ContactPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Help -> {
                        HelpPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Favorites -> {
                        FavoritesPage(
                            onError = onError,
                        )
                    }

                    RouterScreen.Blog -> {
                        BlogPage(
                            onError = onError,
                        )
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

enum class Category {
    Inspiration,
    Clothing,
    Shoes,
    Sports,
    Accessories,
    Jewellery,
    Bestsellers,
    Promos,
}

data class CategoryFilter(
    val id: String,
    val name: String,
)
