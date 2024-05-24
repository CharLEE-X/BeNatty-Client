package feature.admin.product.create

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

class AdminProductCreateViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    goToProduct: (String) -> Unit,
) : BasicViewModel<
    AdminProductCreateContract.Inputs,
    AdminProductCreateContract.Events,
    AdminProductCreateContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminProductCreateContract.State(),
            inputHandler = AdminProductCreateInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminProductCreateEventHandler(
        onError = onError,
        goBack = goBack,
        goToProduct = goToProduct,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = this::class.simpleName!!
    }
}
