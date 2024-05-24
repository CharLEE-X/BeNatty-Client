package feature.forgotpassword

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ForgotPasswordViewModel(
    scope: CoroutineScope,
    goToLogin: () -> Unit,
) : BasicViewModel<
    ForgotPasswordContract.Inputs,
    ForgotPasswordContract.Events,
    ForgotPasswordContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = ForgotPasswordContract.State(),
            inputHandler = ForgotPasswordInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = ForgotPasswordEventHandler(
        goToLogin = goToLogin,
    ),
    coroutineScope = scope,
) {
        init {
            trySend(ForgotPasswordContract.Inputs.DisableButton)
        }
    companion object {
        private val TAG = this::class.simpleName!!
    }
}
