package feature.admin.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class AdminListViewModel(
    dataType: AdminListContract.DataType,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToCreate: () -> Unit,
    goToDetail: (String) -> Unit,
) : BasicViewModel<
    AdminListContract.Inputs,
    AdminListContract.Events,
    AdminListContract.State,
    >(
    config =
    BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminListContract.State(dataType = dataType),
            inputHandler = AdminListInputHandler(),
            name = TAG,
        )
//        .dispatchers(
//            inputsDispatcher = Dispatchers.Main.immediate,
//            eventsDispatcher = Dispatchers.Main.immediate,
//            sideJobsDispatcher = Dispatchers.Default,
//            interceptorDispatcher = Dispatchers.Default,
//        )
        .build(),
    eventHandler =
    AdminListEventHandler(
        onError = onError,
        goToCreate = goToCreate,
        goToDetail = goToDetail,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminListContract.Inputs.Get.Page(page = 0))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
