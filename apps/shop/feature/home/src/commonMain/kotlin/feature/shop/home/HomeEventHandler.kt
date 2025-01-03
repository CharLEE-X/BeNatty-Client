package feature.shop.home

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class HomeEventHandler(
    private val onError: suspend (String) -> Unit,
    private val homeRoutes: HomeRoutes,
) : EventHandler<HomeContract.Inputs, HomeContract.Events, HomeContract.State> {
    override suspend fun EventHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleEvent(
        event: HomeContract.Events,
    ): Unit = when (event) {
        is HomeContract.Events.OnError -> onError(event.message)
        HomeContract.Events.GoToPrivacyPolicy -> homeRoutes.privacyPolicy()
        HomeContract.Events.GoToTermsOfService -> homeRoutes.termsOfService()
        HomeContract.Events.GoToCatalogue -> homeRoutes.catalogue()
        is HomeContract.Events.GoToProduct -> homeRoutes.goToProduct(event.id)
    }
}
