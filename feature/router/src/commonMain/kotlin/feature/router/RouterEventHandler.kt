package feature.router

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract

internal class RouterEventHandler :
    EventHandler<
        RouterContract.Inputs<Screen>,
        RouterContract.Events<Screen>,
        RouterContract.State<Screen>,
        > {
    override suspend fun EventHandlerScope<
        RouterContract.Inputs<Screen>,
        RouterContract.Events<Screen>,
        RouterContract.State<Screen>,
        >.handleEvent(
        event: RouterContract.Events<Screen>,
    ) {
        when {
            event is RouterContract.Events.BackstackEmptied ->
                postInput(RouterContract.Inputs.GoToDestination(Screen.Login.matcher.routeFormat))
        }
    }
}
