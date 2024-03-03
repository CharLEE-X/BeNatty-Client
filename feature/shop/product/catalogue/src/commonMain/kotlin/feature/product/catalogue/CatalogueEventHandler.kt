package feature.product.catalogue

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class CatalogueEventHandler(
    private val catalogueRoutes: CatalogueRoutes,
) : EventHandler<CatalogueContract.Inputs, CatalogueContract.Events, CatalogueContract.State> {
    override suspend fun EventHandlerScope<
        CatalogueContract.Inputs,
        CatalogueContract.Events,
        CatalogueContract.State
        >.handleEvent(event: CatalogueContract.Events) = when (event) {
        is CatalogueContract.Events.OnError -> catalogueRoutes.onError(event.message)
        is CatalogueContract.Events.GoToProduct -> catalogueRoutes.goToProduct(event.productId)
    }
}
