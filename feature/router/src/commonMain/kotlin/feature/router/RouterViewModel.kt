package feature.router

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope

typealias RouterInterceptor = BallastInterceptor<RouterContract.Inputs<RouterScreen>, RouterContract.Events<RouterScreen>, RouterContract.State<RouterScreen>>

class RouterViewModel(
    viewModelScope: CoroutineScope,
    initialRoute: RouterScreen,
    extraInterceptors: List<RouterInterceptor> = emptyList(),
) : BasicRouter<RouterScreen>(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = ::PrintlnLogger
        }
        .withRouter(
            routingTable = RoutingTable.fromEnum(RouterScreen.entries.toTypedArray()),
            initialRoute = initialRoute,
        )
        .apply { interceptors += extraInterceptors }
        .build(),
    eventHandler = RouterEventHandler(),
    coroutineScope = viewModelScope,
)
