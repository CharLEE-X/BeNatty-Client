package feature.admin.product.page

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AdminProductPageViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    AdminProductPageContract.Inputs,
    AdminProductPageContract.Events,
    AdminProductPageContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminProductPageContract.State(),
            inputHandler = AdminProductPageInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminProductPageEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminProductPageContract.Inputs.GetProductsPage(1))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
