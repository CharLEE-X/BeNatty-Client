import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.navigation.browser.withBrowserHistoryRouter
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.plusAssign
import feature.router.RouterEventHandler
import feature.router.Screen
import kotlinx.coroutines.CoroutineScope

class JsRouterViewModel(
    viewModelScope: CoroutineScope,
    initialRoute: Screen,
    extras: BallastViewModelConfiguration.Builder.() -> BallastViewModelConfiguration.Builder = { this },
) : BasicRouter<Screen>(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = ::PrintlnLogger
        }
        .extras()
        .withBrowserHistoryRouter(
            RoutingTable.fromEnum(Screen.entries.toTypedArray()),
            basePath = "/",
            initialRoute = initialRoute
        )
        .build(),
    eventHandler = RouterEventHandler(),
    coroutineScope = viewModelScope,
)
