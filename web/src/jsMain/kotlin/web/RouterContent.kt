package web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.GoToDestination
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import feature.router.RouterViewModel
import feature.router.Screen
import web.components.layouts.AccountLayout
import web.components.layouts.AppLayout
import web.components.sections.desktopNav.DesktopNavContract
import web.pages.PageNotFoundPage
import web.pages.account.TrackOrderPage
import web.pages.account.order.OrderPage
import web.pages.account.profile.ProfilePage
import web.pages.account.returns.ReturnsPage
import web.pages.account.wishlist.WishlistPage
import web.pages.admin.category.AdminCategoryListPage
import web.pages.admin.category.AdminCategoryPage
import web.pages.admin.dashboard.AdminDashboardPage
import web.pages.admin.orders.AdminOrderListPage
import web.pages.admin.orders.AdminOrderPagePage
import web.pages.admin.products.AdminProductListPage
import web.pages.admin.products.AdminProductPagePage
import web.pages.admin.tag.AdminTagListPage
import web.pages.admin.tag.AdminTagPage
import web.pages.admin.users.AdminCustomerCreatePage
import web.pages.admin.users.AdminCustomersPage
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
    homeScreen: Screen = Screen.Home,
    loginScreen: Screen = Screen.Login,
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

    LaunchedEffect(routerState.backstack) {
        println("DEBUG backstack: ${routerState.backstack}")
    }

    @Composable
    fun appRoute(content: @Composable ColumnScope.() -> Unit) = AppLayout(
        router = router,
        state = routerState,
        isAuthenticated = isAuthenticated,
        onLogOut = onLogOut,
        onError = onError,
        content = content,
    )

    val goBack: () -> Unit = { router.trySend(RouterContract.Inputs.GoBack()) }
    val goToAdminHome: () -> Unit = { router.trySend(GoToDestination(Screen.AdminHome.route)) }

    routerState.renderCurrentDestination(
        route = { screen: Screen ->
            when (screen) {
                Screen.Home -> appRoute {
                    HomeContent(
                        onError = onError,
                        router = router,
                    )
                }

                Screen.Login -> LoginPage(
                    router = router,
                    onError = onError,
                )

                Screen.Register -> RegisterPage(
                    router = router,
                    onError = onError,
                )

                Screen.ForgotPassword -> appRoute {
                    ForgotPasswordPage(
                        router = router,
                    )
                }

                Screen.UpdateEmail -> appRoute {
                    UpdatePasswordPage(
                        router = router,
                        onError = onError,
                    )
                }

                Screen.Catalogue -> appRoute {
                    CataloguePage(
                        router = router,
                        onError = onError,
                    )
                }

                Screen.Product -> appRoute {
                    val id: String by currentDestination!!.stringPath("id")
                    ProductPage(
                        router = router,
                        id = id,
                        onError = onError,
                    )
                }

                Screen.Cart -> appRoute {
                    CartPage(
                        router = router,
                        onError = onError,
                    )
                }

                Screen.Checkout -> appRoute {
                    CheckoutPage(
                        router = router,
                        onError = onError,
                    )
                }

                Screen.Payment -> appRoute {
                    PaymentPage(
                        onError = onError,
                    )
                }

                Screen.Order -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.ORDERS,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        OrderPage(onError = onError)
                    }
                }

                Screen.Profile -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.PROFILE,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        ProfilePage(onError = onError)
                    }
                }

                Screen.Wishlist -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.WISHLIST,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        WishlistPage(onError = onError)
                    }
                }

                Screen.Returns -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.RETURNS,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        ReturnsPage(onError = onError)
                    }
                }

                Screen.Settings -> appRoute {
                    SettingsPage(
                        onError = onError,
                    )
                }

                Screen.About -> appRoute {
                    AboutPage(
                        onError = onError,
                    )
                }

                Screen.Contact -> appRoute {
                    ContactPage(
                        onError = onError,
                    )
                }

                Screen.HelpAndFAQ -> appRoute {
                    HelpAndFAQPage(
                        onError = onError,
                    )
                }

                Screen.Blog -> appRoute {
                    BlogPage(
                        onError = onError,
                    )
                }

                Screen.PrivacyPolicy -> appRoute {
                    PrivacyPolicyPage()
                }

                Screen.TC -> appRoute {
                    TermsOfServicePage()
                }

                Screen.TrackOrder -> appRoute {
                    TrackOrderPage()
                }

                Screen.Shipping -> appRoute {
                    ShippingPage()
                }

                Screen.Career -> appRoute {
                    CareerPage()
                }

                Screen.CyberSecurity -> appRoute {
                    CyberSecurityPage()
                }

                Screen.Accessibility -> appRoute {
                    AccessibilityPage()
                }

                Screen.Press -> appRoute {
                    PressPage()
                }

                Screen.AdminHome -> AdminDashboardPage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminCustomers -> AdminCustomersPage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminCustomerCreate -> AdminCustomerCreatePage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminCustomerProfile -> {
                    val id by stringPath()
                    AdminCustomerCreatePage(
                        router = router,
//                        userId = id,
                        onError = onError,
                        goToAdminHome = goToAdminHome,
                    )
                }

                Screen.AdminProducts -> AdminProductListPage(
                    router = router,
                    onError = onError,
                    goBack = goBack,
                    goToAdminHome = goToAdminHome,
                    goToProductCreate = { router.trySend(GoToDestination(Screen.AdminProductCreate.route)) },
                    goToProductDetail = { id ->
                        router.trySend(
                            GoToDestination(Screen.AdminProductPage.directions().pathParameter("id", id).build())
                        )
                    }
                )

                Screen.AdminProductCreate -> AdminProductPagePage(
                    productId = null,
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminProductPage -> {
                    val id: String by stringPath()
                    AdminProductPagePage(
                        productId = id,
                        router = router,
                        onError = onError,
                        goToAdminHome = goToAdminHome,
                    )
                }

                Screen.AdminOrderList -> AdminOrderListPage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminOrderPageNew -> AdminOrderPagePage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminOrderPageExisting -> {
                    val id: String by stringPath()
                    AdminOrderPagePage(
                        router = router,
                        onError = onError,
                        goToAdminHome = goToAdminHome,
                    )
                }

                Screen.AdminCategoryList -> AdminCategoryListPage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminCategoryPageNew -> AdminCategoryPage(
                    router = router,
                    id = null,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminCategoryPageExisting -> {
                    val id: String by stringPath()
                    AdminCategoryPage(
                        router = router,
                        id = id,
                        onError = onError,
                        goToAdminHome = goToAdminHome,
                    )
                }

                Screen.AdminTagList -> AdminTagListPage(
                    router = router,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminTagCreate -> AdminTagPage(
                    router = router,
                    id = null,
                    onError = onError,
                    goToAdminHome = goToAdminHome,
                )

                Screen.AdminTagPageExisting -> {
                    val id: String by currentDestination!!.stringPath("id")
                    AdminTagPage(
                        router = router,
                        id = id,
                        onError = onError,
                        goToAdminHome = goToAdminHome,
                    )
                }
            }
        },
        notFound = { url ->
            PageNotFoundPage(
                url = url,
                goBack = goBack,
            )
        }
    )
}

private fun RouterViewModel.route(item: DesktopNavContract.AccountMenuItem) {
    when (item) {
        DesktopNavContract.AccountMenuItem.PROFILE -> {
            trySend(GoToDestination(Screen.Profile.matcher.routeFormat))
        }

        DesktopNavContract.AccountMenuItem.ORDERS -> {
            trySend(GoToDestination(Screen.Order.matcher.routeFormat))
        }

        DesktopNavContract.AccountMenuItem.RETURNS -> {
            trySend(GoToDestination(Screen.Returns.matcher.routeFormat))
        }

        DesktopNavContract.AccountMenuItem.WISHLIST -> {
            trySend(GoToDestination(Screen.Wishlist.matcher.routeFormat))
        }

        else -> {
            // no-op
        }
    }
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
