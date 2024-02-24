package web.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.GoToDestination
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.PopUntilRoute
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.ReplaceTopDestination
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.idPath
import feature.shop.navbar.DesktopNavContract
import web.components.layouts.AccountLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.MainParams
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
import web.pages.shop.account.TrackOrderPage
import web.pages.shop.account.order.OrderPage
import web.pages.shop.account.profile.ProfilePage
import web.pages.shop.account.returns.ReturnsPage
import web.pages.shop.account.wishlist.WishlistPage
import web.pages.shop.auth.ForgotPasswordPage
import web.pages.shop.auth.LoginPage
import web.pages.shop.auth.RegisterPage
import web.pages.shop.auth.UpdatePasswordPage
import web.pages.shop.blog.BlogPage
import web.pages.shop.help.AccessibilityPage
import web.pages.shop.help.CareerPage
import web.pages.shop.help.CyberSecurityPage
import web.pages.shop.help.HelpAndFAQPage
import web.pages.shop.help.PressPage
import web.pages.shop.help.PrivacyPolicyPage
import web.pages.shop.help.ShippingPage
import web.pages.shop.help.TermsOfServicePage
import web.pages.shop.help.about.AboutPage
import web.pages.shop.help.contact.ContactPage
import web.pages.shop.home.HomeContent
import web.pages.shop.payment.PaymentPage
import web.pages.shop.payment.cart.CartPage
import web.pages.shop.payment.checkout.CheckoutPage
import web.pages.shop.product.ProductPage
import web.pages.shop.product.catalogue.CataloguePage
import web.pages.shop.settings.SettingsPage

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
//            extraInterceptors = listOf(BrowserHistoryNavigationInterceptor("/", initialRoute)),
        )
    }
    val routerState by router.observeStates().collectAsState()
    val currentDestination = routerState.currentDestinationOrNull

    LaunchedEffect(routerState.backstack) {
        println("DEBUG backstack: ${routerState.backstack}")
    }

    val goBack: () -> Unit = { router.trySend(RouterContract.Inputs.GoBack()) }
    val goToShopHome: () -> Unit = { router.trySend(GoToDestination(Screen.Home.route)) }
    val goToAdminHome: () -> Unit = { router.trySend(GoToDestination(Screen.AdminHome.route)) }
    val goToLogin: () -> Unit = { router.trySend(GoToDestination(Screen.Login.route)) }
    val mainParams = MainParams(
        goToHome = goToShopHome,
        goToLogin = { router.trySend(GoToDestination(Screen.Login.route)) },
        goToOrders = { router.trySend(GoToDestination(Screen.Order.route)) },
        goToProfile = { router.trySend(GoToDestination(Screen.Profile.route)) },
        goToReturns = { router.trySend(GoToDestination(Screen.Returns.route)) },
        goToWishlist = { router.trySend(GoToDestination(Screen.Wishlist.route)) },
        goToHelpAndFaq = { router.trySend(GoToDestination(Screen.HelpAndFAQ.route)) },
        goToCatalogue = { router.trySend(GoToDestination(Screen.Catalogue.route)) },
        goToAboutUs = { router.trySend(GoToDestination(Screen.About.route)) },
        goToAccessibility = { router.trySend(GoToDestination(Screen.Accessibility.route)) },
        goToCareer = { router.trySend(GoToDestination(Screen.Career.route)) },
        goToContactUs = { router.trySend(GoToDestination(Screen.Contact.route)) },
        goToCyberSecurity = { router.trySend(GoToDestination(Screen.CyberSecurity.route)) },
        goToFAQs = { router.trySend(GoToDestination(Screen.HelpAndFAQ.route)) },
        goToPress = { router.trySend(GoToDestination(Screen.Press.route)) },
        goToPrivacyPolicy = { router.trySend(GoToDestination(Screen.PrivacyPolicy.route)) },
        goToShipping = { router.trySend(GoToDestination(Screen.Shipping.route)) },
        goToTermsOfService = { router.trySend(GoToDestination(Screen.TC.route)) },
        goToTrackOrder = { router.trySend(GoToDestination(Screen.TrackOrder.route)) },
        goToAdminHome = { router.trySend(ReplaceTopDestination(Screen.AdminHome.route)) },
        onError = onError,
    )
    val adminRoutes = AdminRoutes(
        goToAdminHome = goToAdminHome,
        goToShopHome = goToShopHome,
    )

    @Composable
    fun authenticatedRoute(block: @Composable () -> Unit) {
        if (isAuthenticated) block() else router.trySend(PopUntilRoute(inclusive = false, route = Screen.Login))
    }

    routerState.renderCurrentDestination(
        route = { screen: Screen ->
            when (screen) {
                Screen.Home -> HomeContent(
                    mainParams = mainParams,
                    goToCatalogue = { router.trySend(GoToDestination(Screen.Catalogue.route)) },
                )

                Screen.Login -> LoginPage(
                    router = router,
                    onError = onError,
                )

                Screen.Register -> RegisterPage(
                    router = router,
                    onError = onError,
                )

                Screen.ForgotPassword -> ForgotPasswordPage(
                    mainParams = mainParams,
                    goToLogin = goToLogin,
                )

                Screen.UpdatePassword -> authenticatedRoute {
                    UpdatePasswordPage(
                        mainParams = mainParams,
                        goToLogin = goToLogin,
                    )
                }

                Screen.Catalogue ->
                    CataloguePage(
                        mainParams = mainParams,
                        goToProduct = { router.trySend(GoToDestination(Screen.Product.idPath(it))) },
                    )

                Screen.Product -> {
                    val id: String by currentDestination!!.stringPath("id")
                    ProductPage(
                        router = router,
                        id = id,
                        onError = onError,
                    )
                }

                Screen.Cart -> CartPage(
                    router = router,
                    onError = onError,
                )

                Screen.Checkout -> CheckoutPage(
                    router = router,
                    onError = onError,
                )

                Screen.Payment -> PaymentPage(
                    onError = onError,
                )

                Screen.Order -> authenticatedRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.ORDERS,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        OrderPage(onError = onError)
                    }
                }

                Screen.Profile -> authenticatedRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.PROFILE,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        ProfilePage(onError = onError)
                    }
                }

                Screen.Wishlist -> authenticatedRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.WISHLIST,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        WishlistPage(onError = onError)
                    }
                }

                Screen.Returns -> authenticatedRoute {
                    AccountLayout(
                        item = DesktopNavContract.AccountMenuItem.RETURNS,
                        onMenuItemClicked = { router.route(it) },
                    ) {
                        ReturnsPage(onError = onError)
                    }
                }

                Screen.Settings -> authenticatedRoute {
                    SettingsPage(
                        onError = onError,
                    )
                }

                Screen.About -> AboutPage(
                    onError = onError,
                )

                Screen.Contact -> ContactPage(
                    onError = onError,
                )

                Screen.HelpAndFAQ -> HelpAndFAQPage(
                    onError = onError,
                )

                Screen.Blog -> BlogPage(
                    onError = onError,
                )

                Screen.PrivacyPolicy -> PrivacyPolicyPage()

                Screen.TC -> TermsOfServicePage()

                Screen.TrackOrder -> TrackOrderPage()

                Screen.Shipping -> ShippingPage()

                Screen.Career -> CareerPage()

                Screen.CyberSecurity -> CyberSecurityPage()

                Screen.Accessibility -> AccessibilityPage()

                Screen.Press -> PressPage()

                Screen.AdminHome -> authenticatedRoute {
                    AdminDashboardPage(
                        router = router,
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminCustomers -> authenticatedRoute {
                    AdminCustomersPage(
                        router = router,
                        onError = onError,
                        goBack = goBack,
                        adminRoutes = adminRoutes,
                        goToCreateCustomer = { router.trySend(GoToDestination(Screen.AdminCustomerCreate.route)) },
                        goToCustomer = { router.trySend(GoToDestination(Screen.AdminCustomerProfile.idPath(it))) },
                    )
                }

                Screen.AdminCustomerCreate -> authenticatedRoute {
                    AdminCustomerCreatePage(
                        router = router,
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminCustomerProfile -> authenticatedRoute {
                    val id by stringPath()
                    AdminCustomerCreatePage(
                        router = router,
//                        userId = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminProducts -> authenticatedRoute {
                    AdminProductListPage(
                        router = router,
                        onError = onError,
                        goBack = goBack,
                        adminRoutes = adminRoutes,
                        goToCreateProduct = { router.trySend(GoToDestination(Screen.AdminProductCreate.route)) },
                        goToProduct = { router.trySend(GoToDestination(Screen.AdminProductPage.idPath(it))) }
                    )
                }

                Screen.AdminProductCreate -> authenticatedRoute {
                    AdminProductPagePage(
                        productId = null,
                        router = router,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goBackToProducts = goBack,
                        goToCreateCategory = { router.trySend(ReplaceTopDestination(Screen.AdminCategoryPageNew.route)) },
                        goToCreateTag = { router.trySend(ReplaceTopDestination(Screen.AdminTagCreate.route)) },
                        goToCustomer = { router.trySend(ReplaceTopDestination(Screen.AdminCustomerProfile.idPath(it))) },
                        goToProduct = { router.trySend(ReplaceTopDestination(Screen.AdminProductPage.idPath(it))) },
                    )
                }

                Screen.AdminProductPage -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminProductPagePage(
                        productId = id,
                        router = router,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goBackToProducts = goBack,
                        goToCreateCategory = { router.trySend(ReplaceTopDestination(Screen.AdminCategoryPageNew.route)) },
                        goToCreateTag = { router.trySend(ReplaceTopDestination(Screen.AdminTagCreate.route)) },
                        goToCustomer = { router.trySend(ReplaceTopDestination(Screen.AdminCustomerProfile.idPath(it))) },
                        goToProduct = { router.trySend(ReplaceTopDestination(Screen.AdminProductPage.idPath(it))) },
                    )
                }

                Screen.AdminOrderList -> authenticatedRoute {
                    AdminOrderListPage(
                        router = router,
                        onError = onError,
                        goBack = goBack,
                        adminRoutes = adminRoutes,
                        goToCreateOrder = { router.trySend(GoToDestination(Screen.AdminOrderPageNew.route)) },
                        goToOrder = { router.trySend(GoToDestination(Screen.AdminOrderPageExisting.idPath(it))) },
                    )
                }

                Screen.AdminOrderPageNew -> authenticatedRoute {
                    AdminOrderPagePage(
                        router = router,
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminOrderPageExisting -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminOrderPagePage(
                        router = router,
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminCategoryList -> authenticatedRoute {
                    AdminCategoryListPage(
                        router = router,
                        onError = onError,
                        goBack = goBack,
                        adminRoutes = adminRoutes,
                        goToCreateCategory = { router.trySend(GoToDestination(Screen.AdminCategoryPageNew.route)) },
                        goToCategory = { router.trySend(GoToDestination(Screen.AdminCategoryProfile.idPath(it))) },
                    )
                }

                Screen.AdminCategoryPageNew -> authenticatedRoute {
                    AdminCategoryPage(
                        router = router,
                        id = null,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCustomers = { router.trySend(GoToDestination(Screen.AdminCategoryList.route)) },
                        goToCustomer = { router.trySend(GoToDestination(Screen.AdminCustomerProfile.idPath(it))) },
                        goToCreateCategory = { router.trySend(GoToDestination(Screen.AdminCategoryPageNew.route)) },
                        goToCategory = { router.trySend(GoToDestination(Screen.AdminCategoryProfile.idPath(it))) },
                    )
                }

                Screen.AdminCategoryProfile -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminCategoryPage(
                        router = router,
                        id = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCustomers = { router.trySend(GoToDestination(Screen.AdminCategoryList.route)) },
                        goToCustomer = { router.trySend(GoToDestination(Screen.AdminCustomerProfile.idPath(it))) },
                        goToCreateCategory = { router.trySend(GoToDestination(Screen.AdminCategoryPageNew.route)) },
                        goToCategory = { router.trySend(GoToDestination(Screen.AdminCategoryProfile.idPath(it))) },
                    )
                }

                Screen.AdminTagList -> authenticatedRoute {
                    AdminTagListPage(
                        router = router,
                        onError = onError,
                        goBack = goBack,
                        adminRoutes = adminRoutes,
                        goToTagCreate = { router.trySend(GoToDestination(Screen.AdminTagCreate.route)) },
                        goToTag = { router.trySend(GoToDestination(Screen.AdminTagPageExisting.idPath(it))) },
                    )
                }

                Screen.AdminTagCreate -> authenticatedRoute {
                    AdminTagPage(
                        router = router,
                        id = null,
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminTagPageExisting -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminTagPage(
                        router = router,
                        id = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
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
