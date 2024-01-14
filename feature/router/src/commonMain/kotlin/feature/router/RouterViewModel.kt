package feature.router

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.fromEnum
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

class RouterViewModel(
    viewModelScope: CoroutineScope,
    initialRoute: RouterScreen,
) : KoinComponent, BasicRouter<RouterScreen>(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = ::PrintlnLogger
        }
        .withRouter(
            routingTable = RoutingTable.fromEnum(RouterScreen.entries.toTypedArray()),
            initialRoute = initialRoute,
        )
        .build(),
    eventHandler = RouterEventHandler(),
    coroutineScope = viewModelScope,
)
