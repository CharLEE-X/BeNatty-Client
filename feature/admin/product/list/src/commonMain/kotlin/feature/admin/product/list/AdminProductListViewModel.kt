package feature.admin.product.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AdminProductListViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    AdminProductListContract.Inputs,
    AdminProductListContract.Events,
    AdminProductListContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminProductListContract.State(),
            inputHandler = AdminProductListInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminProductListEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminProductListContract.Inputs.GetProductsPage(1))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
