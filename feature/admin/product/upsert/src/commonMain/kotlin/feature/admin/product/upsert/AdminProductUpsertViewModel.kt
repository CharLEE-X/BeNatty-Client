package feature.admin.product.upsert

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AdminProductUpsertViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    AdminProductUpsertContract.Inputs,
    AdminProductUpsertContract.Events,
    AdminProductUpsertContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminProductUpsertContract.State(),
            inputHandler = AdminProductUpsertInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminProductUpsertEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminProductUpsertContract.Inputs.GetProductsPage(1))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
