package feature.router

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouterContract.Events
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs
import com.copperleaf.ballast.navigation.routing.RouterContract.State
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.navigation.vm.withRouter
import com.copperleaf.ballast.plusAssign
import kotlinx.coroutines.CoroutineScope

typealias RouterInterceptor = BallastInterceptor<Inputs<Screen>, Events<Screen>, State<Screen>>

class RouterViewModel(
    viewModelScope: CoroutineScope,
    initialRoute: Screen,
    extraInterceptors: List<RouterInterceptor> = emptyList(),
) : BasicRouter<Screen>(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
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

fun RouterViewModel.goHome() = trySend(Inputs.GoToDestination(Screen.AdminHome.matcher.routeFormat))

fun <T : Route> T.idPath(id: String): String = this.directions().pathParameter("id", id).build()
