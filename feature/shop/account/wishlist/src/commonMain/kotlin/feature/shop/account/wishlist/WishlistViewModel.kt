package feature.shop.account.wishlist

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class WishlistViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
) : BasicViewModel<
    WishlistContract.Inputs,
    WishlistContract.Events,
    WishlistContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = WishlistContract.State(),
            inputHandler = WishlistInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = WishlistEventHandler(
        onError = onError,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = this::class.simpleName!!
    }
}
