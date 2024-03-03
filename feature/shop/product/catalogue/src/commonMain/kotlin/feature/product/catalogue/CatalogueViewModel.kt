package feature.product.catalogue

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

class CatalogueViewModel(
    scope: CoroutineScope,
    catalogueRoutes: CatalogueRoutes,
    variant: Variant,
) : BasicViewModel<
    CatalogueContract.Inputs,
    CatalogueContract.Events,
    CatalogueContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = CatalogueContract.State(),
            inputHandler = CatalogueInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = CatalogueEventHandler(
        catalogueRoutes = catalogueRoutes,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(CatalogueContract.Inputs.Init(variant))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
