package feature.router

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope

typealias RouterInterceptor = BallastInterceptor<RouterContract.Inputs<Screen>, RouterContract.Events<Screen>, RouterContract.State<Screen>>

class RouterViewModel(
    viewModelScope: CoroutineScope,
    initialRoute: Screen,
    extraInterceptors: List<RouterInterceptor> = emptyList(),
) : BasicRouter<Screen>(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = ::PrintlnLogger
        }
        .withRouter(
            routingTable = RoutingTable.fromEnum(Screen.entries.toTypedArray()),
            initialRoute = initialRoute,
        )
        .apply { interceptors += extraInterceptors }
        .build(),
    eventHandler = RouterEventHandler(),
    coroutineScope = viewModelScope,
)

fun RouterViewModel.goHome() =
    trySend(RouterContract.Inputs.GoToDestination(Screen.Home.matcher.routeFormat))

fun <T : Route> T.idPath(id: String): String = this.directions().pathParameter("id", id).build()
