package web

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
import feature.router.RouterScreen
import feature.router.RouterViewModel
import web.components.layouts.PageLayout
import web.components.sections.desktopNav.DesktopNav
import web.components.sections.desktopNav.DesktopNavContract
import web.components.sections.footer.Footer
import web.pages.PageNotFoundPage
import web.pages.account.TrackOrderPage
import web.pages.account.favorites.FavoritesPage
import web.pages.account.order.OrderPage
import web.pages.account.profile.ProfilePage
import web.pages.account.returns.ReturnsPage
import web.pages.account.wishlist.WishlistPage
import web.pages.auth.ForgotPasswordPage
import web.pages.auth.LoginPage
import web.pages.auth.RegisterPage
import web.pages.auth.UpdatePasswordPage
import web.pages.blog.BlogPage
import web.pages.help.AccessibilityPage
import web.pages.help.CareerPage
import web.pages.help.CyberSecurityPage
import web.pages.help.HelpAndFAQPage
import web.pages.help.PressPage
import web.pages.help.PrivacyPolicyPage
import web.pages.help.ShippingPage
import web.pages.help.TermsOfServicePage
import web.pages.help.about.AboutPage
import web.pages.help.contact.ContactPage
import web.pages.home.HomeContent
import web.pages.payment.PaymentPage
import web.pages.payment.cart.CartPage
import web.pages.payment.checkout.CheckoutPage
import web.pages.product.ProductPage
import web.pages.product.catalogue.CataloguePage
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
            if (
                routerState.currentRouteOrNull?.matcher != RouterScreen.Login.matcher ||
                routerState.currentRouteOrNull?.matcher != RouterScreen.Register.matcher
            ) {
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
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.HelpAndFAQ.matcher.routeFormat))
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
            if (
                routerState.currentRouteOrNull?.matcher != RouterScreen.Login.matcher ||
                routerState.currentRouteOrNull?.matcher != RouterScreen.Register.matcher
            ) {
                Footer(
                    onError = onError,
                    goToAboutUs = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.About.matcher.routeFormat))
                    },
                    goToAccessibility = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Accessibility.matcher.routeFormat))
                    },
                    goToCareer = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Career.matcher.routeFormat))
                    },
                    goToContactUs = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Contact.matcher.routeFormat))
                    },
                    goToCyberSecurity = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.CyberSecurity.matcher.routeFormat))
                    },
                    goToFAQs = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.HelpAndFAQ.matcher.routeFormat))
                    },
                    goToPress = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Press.matcher.routeFormat))
                    },
                    goToPrivacyPolicy = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.PrivacyPolicy.matcher.routeFormat))
                    },
                    goToReturns = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Returns.matcher.routeFormat))
                    },
                    goToShipping = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Shipping.matcher.routeFormat))
                    },
                    goToTermsOfService = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.TC.matcher.routeFormat))
                    },
                    goToTrackOrder = {
                        router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.TrackOrder.matcher.routeFormat))
                    },
                )
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
                        onError = onError,
                        onAuthenticated = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Home.matcher.routeFormat))
                        },
                        goToRegister = {
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(RouterScreen.Register.matcher.routeFormat)
                            )
                        },
                        goToPrivacyPolicy = {
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(RouterScreen.PrivacyPolicy.matcher.routeFormat)
                            )
                        },
                        goToTnC = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.TC.matcher.routeFormat))
                        },
                        goToForgotPassword = {
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(RouterScreen.ForgotPassword.matcher.routeFormat)
                            )
                        },
                    )

                    RouterScreen.Register -> RegisterPage(
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
                        goToLogin = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Login.matcher.routeFormat))
                        },
                    )

                    RouterScreen.ForgotPassword -> ForgotPasswordPage(
                        goToLogin = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Login.matcher.routeFormat))
                        },
                    )

                    RouterScreen.UpdateEmail -> UpdatePasswordPage(
                        onError = onError,
                        goToLogin = {
                            router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Login.matcher.routeFormat))
                        },
                    )

                    RouterScreen.Catalogue -> {
                        CataloguePage(
                            onError = onError,
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
                            onMenuItemClicked = { router.route(it) },
                        )
                    }

                    RouterScreen.Profile -> {
                        ProfilePage(
                            onError = onError,
                            onMenuItemClicked = { router.route(it) },
                        )
                    }

                    RouterScreen.Wishlist -> {
                        WishlistPage(
                            onError = onError,
                            onMenuItemClicked = { router.route(it) },
                        )
                    }

                    RouterScreen.Returns -> {
                        ReturnsPage(
                            onError = onError,
                            onMenuItemClicked = { router.route(it) },
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

                    RouterScreen.HelpAndFAQ -> {
                        HelpAndFAQPage(
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

                    RouterScreen.PrivacyPolicy -> PrivacyPolicyPage()
                    RouterScreen.TC -> TermsOfServicePage()
                    RouterScreen.TrackOrder -> TrackOrderPage()
                    RouterScreen.Shipping -> ShippingPage()
                    RouterScreen.Career -> CareerPage()
                    RouterScreen.CyberSecurity -> CyberSecurityPage()
                    RouterScreen.Accessibility -> AccessibilityPage()
                    RouterScreen.Press -> PressPage()
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

private fun RouterViewModel.route(item: DesktopNavContract.AccountMenuItem) {
    when (item) {
        DesktopNavContract.AccountMenuItem.PROFILE -> {
            trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Profile.matcher.routeFormat))
        }

        DesktopNavContract.AccountMenuItem.ORDERS -> {
            trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Order.matcher.routeFormat))
        }

        DesktopNavContract.AccountMenuItem.RETURNS -> {
            trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Returns.matcher.routeFormat))
        }

        DesktopNavContract.AccountMenuItem.WISHLIST -> {
            trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Wishlist.matcher.routeFormat))
        }

        else -> {
            // no-op
        }
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
        RouterScreen.HelpAndFAQ -> "Help"
        RouterScreen.Favorites -> "Favorites"
        RouterScreen.Blog -> "Blog"
        RouterScreen.Register -> "Register"
        RouterScreen.ForgotPassword -> "Forgot Password"
        RouterScreen.UpdateEmail -> "Update Email"
        RouterScreen.PrivacyPolicy -> "Privacy Policy"
        RouterScreen.TC -> "Terms and Conditions"
        RouterScreen.Wishlist -> "Wishlist"
        RouterScreen.Returns -> "Returns"
        RouterScreen.TrackOrder -> "Track Order"
        RouterScreen.Shipping -> "Shipping"
        RouterScreen.Career -> "Career"
        RouterScreen.CyberSecurity -> "Cyber Security"
        RouterScreen.Accessibility -> "Accessibility"
        RouterScreen.Press -> "Press"
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
