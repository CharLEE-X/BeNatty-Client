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
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.open
import feature.product.catalog.CatalogVariant
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.idPath
import feature.shop.cart.CartContract
import feature.shop.cart.CartViewModel
import feature.shop.footer.FooterRoutes
import feature.shop.footer.FooterViewModel
import feature.shop.navbar.DesktopNavRoutes
import feature.shop.navbar.NavbarContract
import feature.shop.navbar.NavbarViewModel
import kotlinx.browser.window
import kotlinx.serialization.json.Json
import web.components.layouts.GlobalVMs
import web.components.layouts.MainRoutes
import web.pages.account.TrackOrderPage
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
import web.pages.payment.checkout.CheckoutPage
import web.pages.product.catalogue.CatalogContent
import web.pages.product.page.ProductPage
import web.pages.settings.SettingsPage

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
                        .pathParameter(
                            "variant",
                            Json.encodeToString(CatalogVariant.serializer(), CatalogVariant.Catalog)
                        )
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
        goToProduct = { productId -> router.trySend(GoToDestination(Screen.Product.idPath(productId))) },
        onError = onError,
        goToCheckout = { router.trySend(GoToDestination(Screen.Checkout.route)) },
    )
    val desktopNavRoutes = DesktopNavRoutes(
        goToHome = mainRoutes.goToHome,
        goToLogin = mainRoutes.goToLogin,
        goToOrders = mainRoutes.goToOrders,
        goToProfile = mainRoutes.goToProfile,
        goToReturns = mainRoutes.goToReturns,
        goToWishlist = mainRoutes.goToWishlist,
        goToHelpAndFaq = mainRoutes.goToHelpAndFaq,
        goToCatalogue = mainRoutes.goToCatalogue,
        goToAbout = mainRoutes.goToAboutUs,
        goToShippingAndReturns = mainRoutes.goToShipping, // FIXME: Change to 'ShippingAndReturns'
        goToProductDetail = mainRoutes.goToProduct,
    )
    val footerRoutes = FooterRoutes(
        goToAboutUs = mainRoutes.goToAboutUs,
        goToAccessibility = mainRoutes.goToAccessibility,
        goToCareer = mainRoutes.goToCareer,
        goToContactUs = mainRoutes.goToContactUs,
        goToCyberSecurity = mainRoutes.goToCyberSecurity,
        goToFAQs = mainRoutes.goToFAQs,
        goToPress = mainRoutes.goToPress,
        goToPrivacyPolicy = mainRoutes.goToPrivacyPolicy,
        goToShipping = mainRoutes.goToShipping,
        goToTermsOfService = mainRoutes.goToTermsOfService,
        goToTrackOrder = mainRoutes.goToTrackOrder,
        goToReturns = mainRoutes.goToReturns,
        goToCatalogue = mainRoutes.goToCatalogue,
    )

    val cartVm = remember(scope) {
        CartViewModel(
            scope = scope,
            onError = mainRoutes.onError,
            goToLogin = mainRoutes.goToLogin,
            goToProduct = mainRoutes.goToProduct,
            goToCheckout = mainRoutes.goToCheckout,
        )
    }
    val navbarVm = remember(scope) {
        NavbarViewModel(
            scope = scope,
            onError = mainRoutes.onError,
            desktopNavRoutes = desktopNavRoutes,
            showCartSidebar = { cartVm.trySend(CartContract.Inputs.ShowCart) },
            goToProductDetail = mainRoutes.goToProduct,
        )
    }
    val footerVM = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = mainRoutes.onError,
            footerRoutes = footerRoutes,
            goToCompanyWebsite = { window.open(it, OpenLinkStrategy.IN_NEW_TAB) }
        )
    }
    val globalVMs = GlobalVMs(cartVm, navbarVm, footerVM)

    @Composable
    fun authenticatedRoute(block: @Composable () -> Unit) {
        if (isAuthenticated) block() else router.trySend(PopUntilRoute(inclusive = false, route = Screen.Login))
    }

    routerState.renderCurrentDestination(
        route = { screen: Screen ->
            when (screen) {
                Screen.Home -> HomeContent(
                    mainRoutes = mainRoutes,
                    globalVMs = globalVMs,
                    desktopNavRoutes = desktopNavRoutes,
                    footerRoutes = footerRoutes,
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
                    globalVMs = globalVMs,
                    desktopNavRoutes = desktopNavRoutes,
                    footerRoutes = footerRoutes,

                    )

                Screen.UpdatePassword -> authenticatedRoute {
                    UpdatePasswordPage(
                        mainRoutes = mainRoutes,
                        goToLogin = goToLogin,
                        globalVMs = globalVMs,
                        desktopNavRoutes = desktopNavRoutes,
                        footerRoutes = footerRoutes,

                        )
                }

                Screen.Catalogue -> {
                    val variant: String by stringPath()
                    val catalogVariantClass = Json.decodeFromString<CatalogVariant>(variant)
                    CatalogContent(
                        mainRoutes = mainRoutes,
                        catalogVariant = catalogVariantClass,
                        globalVMs = globalVMs,
                        desktopNavRoutes = desktopNavRoutes,
                        footerRoutes = footerRoutes,
                    )
                }

                Screen.Product -> {
                    val id: String by stringPath()
                    ProductPage(
                        productId = id,
                        mainRoutes = mainRoutes,
                        desktopNavRoutes = desktopNavRoutes,
                        footerRoutes = footerRoutes,
                        globalVMs = globalVMs,
                    )
                }

                Screen.Checkout -> CheckoutPage(
                    globalVMs = globalVMs,
                    mainRoutes = mainRoutes,
                    desktopNavRoutes = desktopNavRoutes,
                    footerRoutes = footerRoutes,
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

private fun RouterViewModel.route(item: NavbarContract.AccountMenuItem) {
    when (item) {
        NavbarContract.AccountMenuItem.PROFILE -> {
            trySend(GoToDestination(Screen.Profile.matcher.routeFormat))
        }

        NavbarContract.AccountMenuItem.ORDERS -> {
            trySend(GoToDestination(Screen.Order.matcher.routeFormat))
        }

        NavbarContract.AccountMenuItem.RETURNS -> {
            trySend(GoToDestination(Screen.Returns.matcher.routeFormat))
        }

        NavbarContract.AccountMenuItem.WISHLIST -> {
            trySend(GoToDestination(Screen.Wishlist.matcher.routeFormat))
        }

        else -> {
            // no-op
        }
    }
}
