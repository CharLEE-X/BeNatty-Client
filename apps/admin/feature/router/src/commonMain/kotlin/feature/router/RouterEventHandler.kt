package feature.router

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class RouterEventHandler : EventHandler<
    RouterContract.Inputs<Screen>,
    RouterContract.Events<Screen>,
    RouterContract.State<Screen>,
    >, KoinComponent {

    private val authService by inject<AuthService>()

    override suspend fun EventHandlerScope<
        RouterContract.Inputs<Screen>,
        RouterContract.Events<Screen>,
        RouterContract.State<Screen>,
        >.handleEvent(event: RouterContract.Events<Screen>) {
        when {
            event is RouterContract.Events.BackstackEmptied -> {
                if (authService.isAuth()) {
                    postInput(RouterContract.Inputs.GoToDestination(Screen.AdminHome.matcher.routeFormat))
                } else {
                    postInput(RouterContract.Inputs.GoToDestination(Screen.Login.matcher.routeFormat))
                }
            }
        }
    }
}
