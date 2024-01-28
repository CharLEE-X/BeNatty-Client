package feature.router

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.copperleaf.ballast.navigation.routing.Backstack
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.GoBack
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.GoToDestination
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.ReplaceTopDestination
import com.copperleaf.ballast.navigation.routing.currentRouteOrNull
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination
import com.copperleaf.ballast.navigation.vm.Router
import components.BottomBar
import components.BottomSheet
import components.ModalSheet
import feature.login.LoginContent
import feature.profile.ProfileContent
import feature.router.RouterScreen.Home
import feature.router.RouterScreen.Login
import feature.router.RouterScreen.Product
import feature.router.sheets.MenuDrawer
import feature.updateProfile.UpdateProfileContent
import theme.safePaddingValues

@Composable
internal fun RouterContent(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    isAuthenticated: Boolean,
    onLogOut: () -> Unit,
    homeScreen: RouterScreen = Home,
    loginScreen: RouterScreen = Login,
) {
    val scope = rememberCoroutineScope()
    val initialRoute = when (isAuthenticated) {
        true -> homeScreen
        false -> loginScreen
    }
    val router: Router<RouterScreen> = remember(scope) {
        RouterViewModel(
            viewModelScope = scope,
            initialRoute = initialRoute,
        )
    }
    val routerState: Backstack<RouterScreen> by router.observeStates().collectAsState()
    val onError: suspend (String) -> Unit = { snackbarHostState.showSnackbar(it) }

    LaunchedEffect(isAuthenticated) {
        if (!isAuthenticated) {
            if (routerState.currentRouteOrNull != loginScreen) {
                println("User is not authenticated, redirecting to login screen")
                router.trySend(ReplaceTopDestination(Login.matcher.routeFormat))
            }
        } else {
            if (routerState.currentRouteOrNull == loginScreen) {
                println("User is authenticated, redirecting to matcher screen")
                router.trySend(ReplaceTopDestination(Home.matcher.routeFormat))
            }
        }
    }

    var isModalShowing by remember { mutableStateOf(false) }

    var menuDrawer by remember {
        mutableStateOf(
            ModalShow(
                id = 0,
                modalSheet = ModalSheet.MenuDrawer,
                show = false,
            ),
        )
    }
    val onMenuClick: () -> Unit = {
        menuDrawer = menuDrawer.copy(show = true)
        isModalShowing = true
    }

    Scaffold(
        modifier = modifier,
        topBar = {},
        bottomBar = {
            BottomBar(
                routes = bottomBarRoutes,
                currentRoute = routerState.currentRouteOrNull ?: loginScreen,
                onDestinationClick = { route ->
                    router.trySend(ReplaceTopDestination(route.matcher.routeFormat))
                },
                show = !isModalShowing && (routerState.currentRouteOrNull ?: loginScreen) in bottomBarRoutes,
                modifier = Modifier
                    .padding(bottom = safePaddingValues.calculateBottomPadding()),
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            routerState.renderCurrentDestination(
                route = { routerScreen: RouterScreen ->
                    when (routerScreen) {
                        Login -> LoginContent(
                            onError = onError,
                            onAuthenticated = { router.trySend(ReplaceTopDestination(homeScreen.matcher.routeFormat)) },
                        )

                        Home -> ProfileContent(
                            goBack = { router.trySend(GoBack()) },
                        )

                        Product -> UpdateProfileContent(
                            onError = onError,
                            onBackClicked = { router.trySend(GoBack()) },
                            isModalShowing = { isModalShowing = it },
                        )

                        RouterScreen.Catalogue -> TODO()
                        RouterScreen.Cart -> TODO()
                        RouterScreen.Checkout -> TODO()
                        RouterScreen.Order -> TODO()
                        RouterScreen.Payment -> TODO()
                        RouterScreen.Profile -> TODO()
                        RouterScreen.Settings -> TODO()
                        RouterScreen.About -> TODO()
                        RouterScreen.Contact -> TODO()
                        RouterScreen.HelpAndFAQ -> TODO()
                        RouterScreen.Blog -> TODO()
                    }
                },
                notFound = { router.trySend(GoToDestination(Login.matcher.routeFormat)) },
            )
            BottomSheet(
                showSheet = menuDrawer.show,
                modalSheet = menuDrawer.modalSheet,
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars),
            ) {
                MenuDrawer(
                    snackbarHostState = snackbarHostState,
                    onCloseClicked = {
                        menuDrawer = menuDrawer.copy(show = false)
                        isModalShowing = false
                    },
                    onLogOut = {
                        onLogOut()
                        menuDrawer = menuDrawer.copy(show = false)
                    },
                )
            }
        }
    }
}

data class ModalShow(
    val id: Int,
    val modalSheet: ModalSheet,
    val show: Boolean,
)
