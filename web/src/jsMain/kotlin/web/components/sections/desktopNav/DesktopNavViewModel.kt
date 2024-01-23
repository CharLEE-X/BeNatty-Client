package web.components.sections.desktopNav

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class DesktopNavViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToOrders: () -> Unit,
    goToAccount: () -> Unit,
    goToReturns: () -> Unit,
    goToWishlist: () -> Unit,
    logOut: () -> Unit,
) : BasicViewModel<
    DesktopNavContract.Inputs,
    DesktopNavContract.Events,
    DesktopNavContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = DesktopNavContract.State(),
            inputHandler = DesktopNavInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler = DesktopNavEventHandler(
        onError = onError,
        goToOrders = goToOrders,
        goToAccount = goToAccount,
        goToReturns = goToReturns,
        goToWishlist = goToWishlist,
        logOut = logOut,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = DesktopNavViewModel::class.simpleName!!
    }
}
