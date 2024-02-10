package feature.admin.category.page

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

class AdminCategoryPageViewModel(
    id: String?,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToUserList: () -> Unit,
    goToUser: (String) -> Unit,
    goToCreateCategory: () -> Unit,
    goToCategory: (String) -> Unit,
) : BasicViewModel<
    AdminCategoryPageContract.Inputs,
    AdminCategoryPageContract.Events,
    AdminCategoryPageContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminCategoryPageContract.State(
                screenState = if (id == null) {
                    AdminCategoryPageContract.ScreenState.New
                } else {
                    AdminCategoryPageContract.ScreenState.Existing
                }
            ),
            inputHandler = AdminUserPageInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminCategoryPageEventHandler(
        onError = onError,
        goToUserList = goToUserList,
        goToUser = goToUser,
        goToCreateCategory = goToCreateCategory,
        goToCategory = goToCategory,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminCategoryPageContract.Inputs.Init(id = id))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
