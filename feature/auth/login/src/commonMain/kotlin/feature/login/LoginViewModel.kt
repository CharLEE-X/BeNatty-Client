package feature.login

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LoginViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    onAuthenticated: () -> Unit,
    goToRegister: () -> Unit,
    goToPrivacyPolicy: () -> Unit,
    goToTnC: () -> Unit,
    goToForgotPassword: () -> Unit,
) : BasicViewModel<
    LoginContract.Inputs,
    LoginContract.Events,
    LoginContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = LoginContract.State(),
            inputHandler = LoginInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = LoginEventHandler(
        onError = onError,
        onAuthenticated = onAuthenticated,
        goToSignUp = goToRegister,
        gotoPrivacyPolicy = goToPrivacyPolicy,
        gotoTnC = goToTnC,
        goToForgotPassword = goToForgotPassword,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(LoginContract.Inputs.DisableButton)
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
