package feature.admin.category.create

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

class AdminCategoryCreateViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToCategory: (String) -> Unit,
) : BasicViewModel<
    AdminCategoryCreateContract.Inputs,
    AdminCategoryCreateContract.Events,
    AdminCategoryCreateContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminCategoryCreateContract.State(),
            inputHandler = AdminCategoryCreateInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminCategoryCreateEventHandler(
        onError = onError,
        goToCategory = goToCategory,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = this::class.simpleName!!
    }
}
