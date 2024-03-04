package feature.product.catalog

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class CatalogViewModel(
    scope: CoroutineScope,
    catalogueRoutes: CatalogueRoutes,
    variant: Variant,
) : BasicViewModel<
    CatalogContract.Inputs,
    CatalogContract.Events,
    CatalogContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = CatalogContract.State(),
            inputHandler = CatalogInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = CatalogEventHandler(
        catalogueRoutes = catalogueRoutes,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(CatalogContract.Inputs.Init(variant))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
