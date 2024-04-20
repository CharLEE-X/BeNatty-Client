package feature.product.page

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

class ProductPageViewModel(
    productId: String,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToProduct: suspend (String) -> Unit,
) : BasicViewModel<
    ProductPageContract.Inputs,
    ProductPageContract.Events,
    ProductPageContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = ProductPageContract.State(),
            inputHandler = ProductPageInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = ProductPageEventHandler(
        onError = onError,
        goToProduct = goToProduct,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(ProductPageContract.Inputs.Init(productId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
