package web.pages

import androidx.compose.runtime.*
import com.copperleaf.ballast.navigation.browser.BrowserHashNavigationInterceptor
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.*
import com.copperleaf.ballast.navigation.routing.currentDestinationOrNull
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.routing.stringPath
import feature.router.RouterViewModel
import feature.router.Screen
import feature.router.idPath
import web.components.layouts.AdminRoutes
import web.components.layouts.AdminSideNavRoutes
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
import web.pages.auth.LoginPage
import web.pages.auth.RegisterPage

@Composable
fun RouterContent(
    isAuthenticated: Boolean,
    onError: (String) -> Unit,
    homeScreen: Screen = Screen.AdminHome,
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

    val goBack: () -> Unit = { router.trySend(GoBack()) }
    val goToAdminHome: () -> Unit = { router.trySend(GoToDestination(Screen.AdminHome.route)) }
    val adminRoutes = AdminRoutes(
        goToAdminHome = goToAdminHome,
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
                Screen.Login -> LoginPage(
                    router = router,
                    onError = onError,
                )

                Screen.Register -> RegisterPage(
                    router = router,
                    onError = onError,
                )

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
