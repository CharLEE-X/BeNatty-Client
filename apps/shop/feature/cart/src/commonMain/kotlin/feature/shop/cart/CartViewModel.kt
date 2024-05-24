package feature.shop.cart

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class CartViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToLogin: () -> Unit,
    goToProduct: (String) -> Unit,
    goToCheckout: () -> Unit,
) : BasicViewModel<
    CartContract.Inputs,
    CartContract.Events,
    CartContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = CartContract.State(),
            inputHandler = CartInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler = CartEventHandler(
        onError = onError,
        goToLogin = goToLogin,
        goToProduct = goToProduct,
        goToCheckout = goToCheckout,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(CartContract.Inputs.Init)
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
