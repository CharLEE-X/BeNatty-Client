package feature.shop.footer

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class FooterViewModel(
    scope: CoroutineScope,
    footerRoutes: FooterRoutes,
    onError: suspend (String) -> Unit,
    goToCompanyWebsite: (String) -> Unit,
) : BasicViewModel<
    FooterContract.Inputs,
    FooterContract.Events,
    FooterContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = FooterContract.State(),
            inputHandler = FooterInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler = FooterEventHandler(
        footerRoutes = footerRoutes,
        onError = onError,
        goToCompanyWebsite = goToCompanyWebsite,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(FooterContract.Inputs.Init)
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
