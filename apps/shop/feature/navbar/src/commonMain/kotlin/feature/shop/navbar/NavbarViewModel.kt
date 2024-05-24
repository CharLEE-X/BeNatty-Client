package feature.shop.navbar

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class NavbarViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    desktopNavRoutes: DesktopNavRoutes,
    showCartSidebar: (Boolean) -> Unit,
    goToProductDetail: suspend (String) -> Unit,
) : BasicViewModel<
    NavbarContract.Inputs,
    NavbarContract.Events,
    NavbarContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = NavbarContract.State(),
            inputHandler = NavbarInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler = NavbarEventHandler(
        onError = onError,
        desktopNavRoutes = desktopNavRoutes,
        showCartSidebar = showCartSidebar,
        goToProductDetail = goToProductDetail,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(NavbarContract.Inputs.Init)
    }

    companion object {
        private val TAG = NavbarViewModel::class.simpleName!!
    }
}
