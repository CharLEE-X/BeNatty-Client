package feature.admin.category.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AdminCategoryListViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToCreateCategory: () -> Unit,
    onCategoryClick: (String) -> Unit,
) : BasicViewModel<
    AdminCategoryListContract.Inputs,
    AdminCategoryListContract.Events,
    AdminCategoryListContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminCategoryListContract.State(),
            inputHandler = AdminCategoryListInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminCategoryListEventHandler(
        onError = onError,
        goToCreateCategory = goToCreateCategory,
        onCategoryClick = onCategoryClick,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminCategoryListContract.Inputs.GetPage(page = 1))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
