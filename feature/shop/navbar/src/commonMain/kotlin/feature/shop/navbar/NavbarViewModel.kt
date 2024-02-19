package feature.shop.navbar

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class NavbarViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    desktopNavRoutes: DesktopNavRoutes,
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
            inputHandler = NavbarInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler = NavbarEventHandler(
        onError = onError,
        desktopNavRoutes = desktopNavRoutes,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = NavbarViewModel::class.simpleName!!
    }
}
