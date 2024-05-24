package feature.product.catalog

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class CatalogEventHandler(
    private val catalogueRoutes: CatalogueRoutes,
) : EventHandler<CatalogContract.Inputs, CatalogContract.Events, CatalogContract.State> {
    override suspend fun EventHandlerScope<
        CatalogContract.Inputs,
        CatalogContract.Events,
        CatalogContract.State
        >.handleEvent(event: CatalogContract.Events) = when (event) {
        is CatalogContract.Events.OnError -> catalogueRoutes.onError(event.message)
        is CatalogContract.Events.GoToProduct -> catalogueRoutes.goToProduct(event.productId)
    }
}
