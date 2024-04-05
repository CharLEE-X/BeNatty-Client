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
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import feature.product.catalog.Variant
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.idPath
import feature.shop.navbar.DesktopNavContract
import kotlinx.serialization.json.Json
import web.components.layouts.AdminRoutes
import web.components.layouts.AdminSideNavRoutes
import web.components.layouts.MainRoutes
import web.pages.admin.category.AdminCategoryCreateContent
import web.pages.admin.category.AdminCategoryEditContent
import web.pages.admin.category.AdminCategoryListContent
import web.pages.admin.config.AdminConfigPage
import web.pages.admin.customer.AdminCustomerCreateContent
import web.pages.admin.customer.AdminCustomerEditContent
import web.pages.admin.customer.AdminCustomerListPage
import web.pages.admin.dashboard.AdminDashboardPage
import web.pages.admin.orders.AdminOrderListPage
import web.pages.admin.orders.AdminOrderPagePage
import web.pages.admin.product.AdminProductCreateContent
import web.pages.admin.product.AdminProductEditContent
import web.pages.admin.product.AdminProductListPage
import web.pages.admin.tag.AdminTagCreateContent
import web.pages.admin.tag.AdminTagEditContent
import web.pages.admin.tag.AdminTagListContent
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
import web.pages.shop.product.catalogue.CataloguePage
import web.pages.shop.product.page.ProductPage
import web.pages.shop.settings.SettingsPage

@Composable
fun RouterContent(
    isAuthenticated: Boolean,
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
    val routerState: RouterContract.State<Screen> by router.observeStates().collectAsState()
    val currentDestination = routerState.currentDestinationOrNull

    LaunchedEffect(routerState.backstack) {
        println("DEBUG backstack: ${routerState.backstack}")
    }

    val goBack: () -> Unit = { router.trySend(RouterContract.Inputs.GoBack()) }
    val goToShopHome: () -> Unit = { router.trySend(GoToDestination(Screen.Home.route)) }
    val goToAdminHome: () -> Unit = { router.trySend(GoToDestination(Screen.AdminHome.route)) }
    val goToLogin: () -> Unit = { router.trySend(GoToDestination(Screen.Login.route)) }
    val mainRoutes = MainRoutes(
        goToHome = goToShopHome,
        goToLogin = { router.trySend(GoToDestination(Screen.Login.route)) },
        goToOrders = { router.trySend(GoToDestination(Screen.Order.route)) },
        goToProfile = { router.trySend(GoToDestination(Screen.Profile.route)) },
        goToReturns = { router.trySend(GoToDestination(Screen.Returns.route)) },
        goToWishlist = { router.trySend(GoToDestination(Screen.Wishlist.route)) },
        goToHelpAndFaq = { router.trySend(GoToDestination(Screen.HelpAndFAQ.route)) },
        goToCatalogue = {
            router.trySend(
                GoToDestination(
                    Screen.Catalogue.directions()
                        .pathParameter("variant", Json.encodeToString(Variant.serializer(), Variant.Catalog))
                        .build()
                )
            )
        },
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
        goToProduct = { productId -> router.trySend(GoToDestination(Screen.Product.idPath(productId))) },
        onError = onError,
    )
    val adminRoutes = AdminRoutes(
        goToAdminHome = goToAdminHome,
        goToShopHome = goToShopHome,
        adminSideNavRoutes = AdminSideNavRoutes(
            routerState = routerState,
            currentDestination = currentDestination?.originalRoute,
            goToAdminHome = goToAdminHome,
            goToAdminConfig = { router.trySend(GoToDestination(Screen.AdminConfig.route)) },
            goToAdminUsers = { router.trySend(GoToDestination(Screen.AdminUsers.route)) },
            goToAdminProducts = { router.trySend(GoToDestination(Screen.AdminProducts.route)) },
            goToAdminOrderList = { router.trySend(GoToDestination(Screen.AdminOrderList.route)) },
            goToAdminCategoryList = { router.trySend(GoToDestination(Screen.AdminCategoryList.route)) },
            goToAdminTagList = { router.trySend(GoToDestination(Screen.AdminTagList.route)) },
        ),
        goBack = goBack,
    )

    @Composable
    fun authenticatedRoute(block: @Composable () -> Unit) {
        if (isAuthenticated) block() else router.trySend(PopUntilRoute(inclusive = false, route = Screen.Login))
    }

    routerState.renderCurrentDestination(
        route = { screen: Screen ->
            when (screen) {
                Screen.Home -> HomeContent(
                    mainRoutes = mainRoutes,
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
                    mainRoutes = mainRoutes,
                    goToLogin = goToLogin,
                )

                Screen.UpdatePassword -> authenticatedRoute {
                    UpdatePasswordPage(
                        mainRoutes = mainRoutes,
                        goToLogin = goToLogin,
                    )
                }

                Screen.Catalogue -> {
                    val variant: String by stringPath()
                    val variantClass = Json.decodeFromString<Variant>(variant)
                    CataloguePage(
                        mainRoutes = mainRoutes,
                        variant = variantClass
                    )
                }

                Screen.Product -> {
                    val id: String by stringPath()
                    ProductPage(
                        productId = id,
                        mainRoutes = mainRoutes,
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
                    OrderPage(
                        onError = onError,
                        onMenuItemClicked = { router.route(it) },
                    )
                }

                Screen.Profile -> authenticatedRoute {
                    ProfilePage(
                        onError = onError,
                        onMenuItemClicked = { router.route(it) },
                    )
                }

                Screen.Wishlist -> authenticatedRoute {
                    WishlistPage(
                        onError = onError,
                        onMenuItemClicked = { router.route(it) },
                    )
                }

                Screen.Returns -> authenticatedRoute {
                    ReturnsPage(
                        onError = onError,
                        onMenuItemClicked = { router.route(it) },
                    )
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
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminConfig -> authenticatedRoute {
                    AdminConfigPage(
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminUsers -> authenticatedRoute {
                    AdminCustomerListPage(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCreateCustomer = { router.trySend(GoToDestination(Screen.AdminUserCreate.route)) },
                        goToCustomer = { router.trySend(GoToDestination(Screen.AdminUserProfile.idPath(it))) },
                    )
                }

                Screen.AdminUserCreate -> authenticatedRoute {
                    AdminCustomerCreateContent(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCustomerPage = { router.trySend(ReplaceTopDestination(Screen.AdminUserProfile.idPath(it))) },
                    )
                }

                Screen.AdminUserProfile -> authenticatedRoute {
                    val id by stringPath()
                    AdminCustomerEditContent(
                        userId = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCustomerPage = {
                            router.trySend(ReplaceTopDestination(Screen.AdminUserProfile.idPath(it)))
                        },
                    )
                }

                Screen.AdminProducts -> authenticatedRoute {
                    AdminProductListPage(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCreateProduct = { router.trySend(GoToDestination(Screen.AdminProductCreate.route)) },
                        goToProduct = { router.trySend(ReplaceTopDestination(Screen.AdminProductProfile.idPath(it))) }
                    )
                }

                Screen.AdminProductCreate -> authenticatedRoute {
                    AdminProductCreateContent(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToProduct = { router.trySend(ReplaceTopDestination(Screen.AdminProductProfile.idPath(it))) },
                    )
                }

                Screen.AdminProductProfile -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminProductEditContent(
                        productId = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCreateCategory = { router.trySend(ReplaceTopDestination(Screen.AdminCategoryCreate.route)) },
                        goToCreateTag = { router.trySend(ReplaceTopDestination(Screen.AdminTagCreate.route)) },
                        goToCustomer = { router.trySend(ReplaceTopDestination(Screen.AdminUserProfile.idPath(it))) },
                        goToProduct = { router.trySend(ReplaceTopDestination(Screen.AdminProductProfile.idPath(it))) },
                    )
                }

                Screen.AdminOrderList -> authenticatedRoute {
                    AdminOrderListPage(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCreateOrder = { router.trySend(GoToDestination(Screen.AdminOrderCreate.route)) },
                        goToOrder = { router.trySend(GoToDestination(Screen.AdminOrderProfile.idPath(it))) },
                    )
                }

                Screen.AdminOrderCreate -> authenticatedRoute {
                    AdminOrderPagePage(
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminOrderProfile -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminOrderPagePage(
                        onError = onError,
                        adminRoutes = adminRoutes,
                    )
                }

                Screen.AdminCategoryList -> authenticatedRoute {
                    AdminCategoryListContent(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCreateCategory = { router.trySend(GoToDestination(Screen.AdminCategoryCreate.route)) },
                        goToCategory = { router.trySend(GoToDestination(Screen.AdminCategoryProfile.idPath(it))) },
                    )
                }

                Screen.AdminCategoryCreate -> authenticatedRoute {
                    AdminCategoryCreateContent(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCategory = { router.trySend(ReplaceTopDestination(Screen.AdminCategoryProfile.idPath(it))) },
                    )
                }

                Screen.AdminCategoryProfile -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminCategoryEditContent(
                        id = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToCustomers = { router.trySend(GoToDestination(Screen.AdminCategoryList.route)) },
                        goToCustomer = { router.trySend(GoToDestination(Screen.AdminUserProfile.idPath(it))) },
                        goToCreateCategory = { router.trySend(GoToDestination(Screen.AdminCategoryCreate.route)) },
                        goToCategory = { router.trySend(ReplaceTopDestination(Screen.AdminCategoryProfile.idPath(it))) },
                    )
                }

                Screen.AdminTagList -> authenticatedRoute {
                    AdminTagListContent(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToTagCreate = { router.trySend(GoToDestination(Screen.AdminTagCreate.route)) },
                        goToTag = { router.trySend(GoToDestination(Screen.AdminTagProfile.idPath(it))) },
                    )
                }

                Screen.AdminTagCreate -> authenticatedRoute {
                    AdminTagCreateContent(
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToTag = { router.trySend(ReplaceTopDestination(Screen.AdminTagProfile.idPath(it))) },
                    )
                }

                Screen.AdminTagProfile -> authenticatedRoute {
                    val id: String by stringPath()
                    AdminTagEditContent(
                        id = id,
                        onError = onError,
                        adminRoutes = adminRoutes,
                        goToTag = { router.trySend(ReplaceTopDestination(Screen.AdminTagProfile.idPath(it))) },
                        goToUser = { router.trySend(GoToDestination(Screen.AdminUserProfile.idPath(it))) },
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
