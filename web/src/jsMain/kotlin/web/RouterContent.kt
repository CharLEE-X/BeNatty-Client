package web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import feature.router.RouterScreen
import feature.router.RouterViewModel
import feature.router.goBack
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
import web.pages.admin.users.AdminUserListPage
import web.pages.admin.users.AdminUserPagePage
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

    @Composable
    fun appRoute(content: @Composable ColumnScope.() -> Unit) = AppLayout(
        router = router,
        state = routerState,
        isAuthenticated = isAuthenticated,
        onLogOut = onLogOut,
        onError = onError,
        content = content,
    )

    routerState.renderCurrentDestination(
        route = { routerScreen: RouterScreen ->
            when (routerScreen) {
                RouterScreen.Home -> appRoute {
                    HomeContent(
                        onError = onError,
                        router = router,
                    )
                }

                RouterScreen.Login -> LoginPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.Register -> RegisterPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.ForgotPassword -> appRoute {
                    ForgotPasswordPage(
                        router = router,
                    )
                }

                RouterScreen.UpdateEmail -> appRoute {
                    UpdatePasswordPage(
                        router = router,
                        onError = onError,
                    )
                }

                RouterScreen.Catalogue -> appRoute {
                    CataloguePage(
                        router = router,
                        onError = onError,
                    )
                }

                RouterScreen.Product -> appRoute {
                    val id: String by currentDestination!!.stringPath("id")
                    ProductPage(
                        router = router,
                        id = id,
                        onError = onError,
                    )
                }

                RouterScreen.Cart -> appRoute {
                    CartPage(
                        router = router,
                        onError = onError,
                    )
                }

                RouterScreen.Checkout -> appRoute {
                    CheckoutPage(
                        router = router,
                        onError = onError,
                    )
                }

                RouterScreen.Payment -> appRoute {
                    PaymentPage(
                        onError = onError,
                    )
                }

                RouterScreen.Order -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.ORDERS,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        OrderPage(onError = onError)
                    }
                }

                RouterScreen.Profile -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.PROFILE,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        ProfilePage(onError = onError)
                    }
                }

                RouterScreen.Wishlist -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.WISHLIST,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        WishlistPage(onError = onError)
                    }
                }

                RouterScreen.Returns -> appRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.RETURNS,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        ReturnsPage(onError = onError)
                    }
                }

                RouterScreen.Settings -> appRoute {
                    SettingsPage(
                        onError = onError,
                    )
                }

                RouterScreen.About -> appRoute {
                    AboutPage(
                        onError = onError,
                    )
                }

                RouterScreen.Contact -> appRoute {
                    ContactPage(
                        onError = onError,
                    )
                }

                RouterScreen.HelpAndFAQ -> appRoute {
                    HelpAndFAQPage(
                        onError = onError,
                    )
                }

                RouterScreen.Blog -> appRoute {
                    BlogPage(
                        onError = onError,
                    )
                }

                RouterScreen.PrivacyPolicy -> appRoute {
                    PrivacyPolicyPage()
                }

                RouterScreen.TC -> appRoute {
                    TermsOfServicePage()
                }

                RouterScreen.TrackOrder -> appRoute {
                    TrackOrderPage()
                }

                RouterScreen.Shipping -> appRoute {
                    ShippingPage()
                }

                RouterScreen.Career -> appRoute {
                    CareerPage()
                }

                RouterScreen.CyberSecurity -> appRoute {
                    CyberSecurityPage()
                }

                RouterScreen.Accessibility -> appRoute {
                    AccessibilityPage()
                }

                RouterScreen.Press -> appRoute {
                    PressPage()
                }

                RouterScreen.AdminDashboard -> AdminDashboardPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminUserList -> AdminUserListPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminUserPageNew -> AdminUserPagePage(
                    router = router,
                    userId = null,
                    onError = onError,
                )

                RouterScreen.AdminUserPageExisting -> {
                    val id by stringPath()
                    AdminUserPagePage(
                        router = router,
                        userId = id,
                        onError = onError,
                    )
                }

                RouterScreen.AdminProductList -> AdminProductListPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminProductPageNew -> AdminProductPagePage(
                    productId = null,
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminProductPageExisting -> {
                    val id: String by stringPath()
                    AdminProductPagePage(
                        productId = id,
                        router = router,
                        onError = onError,
                    )
                }

                RouterScreen.AdminOrderList -> AdminOrderListPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminOrderPageNew -> AdminOrderPagePage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminOrderPageExisting -> {
                    val id: String by stringPath()
                    AdminOrderPagePage(
                        router = router,
                        onError = onError,
                    )
                }

                RouterScreen.AdminCategoryList -> AdminCategoryListPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminCategoryPageNew -> AdminCategoryPage(
                    router = router,
                    id = null,
                    onError = onError,
                )

                RouterScreen.AdminCategoryPageExisting -> {
                    val id: String by stringPath()
                    AdminCategoryPage(
                        router = router,
                        id = id,
                        onError = onError,
                    )
                }

                RouterScreen.AdminTagList -> AdminTagListPage(
                    router = router,
                    onError = onError,
                )

                RouterScreen.AdminTagPageNew -> AdminTagPage(
                    router = router,
                    id = null,
                    onError = onError,
                )

                RouterScreen.AdminTagPageExisting -> {
                    val id: String by currentDestination!!.stringPath("id")
                    AdminTagPage(
                        router = router,
                        id = id,
                        onError = onError,
                    )
                }
            }
        },
        notFound = { url ->
            PageNotFoundPage(
                url = url,
                onGoBackClick = { router.goBack() }
            )
        }
    )
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
