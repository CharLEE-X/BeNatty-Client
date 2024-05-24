package feature.admin.tag.edit

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

class AdminTagEditViewModel(
    id: String,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    goToUser: (String) -> Unit,
    goToTag: (String) -> Unit,
) : BasicViewModel<
    AdminTagEditContract.Inputs,
    AdminTagEditContract.Events,
    AdminTagEditContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminTagEditContract.State(),
            inputHandler = AdminTagEditInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminTagEditEventHandler(
        onError = onError,
        goBack = goBack,
        goToTag = goToTag,
        goToUser = goToUser,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminTagEditContract.Inputs.Init(id = id))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}

