package feature.register

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

class RegisterViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    onAuthenticated: () -> Unit,
    goToLogin: () -> Unit,
    goToPrivacyPolicy: () -> Unit,
    goToTnC: () -> Unit,
) : BasicViewModel<
    RegisterContract.Inputs,
    RegisterContract.Events,
    RegisterContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = RegisterContract.State(),
            inputHandler = RegisterInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = RegisterEventHandler(
        onError = onError,
        onAuthenticated = onAuthenticated,
        goToLogin = goToLogin,
        gotoPrivacyPolicy = goToPrivacyPolicy,
        gotoTnC = goToTnC,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = this::class.simpleName!!
    }
}
