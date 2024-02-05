package feature.admin.list

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

class AdminListViewModel(
    dataType: AdminListContract.DataType,
    isSlot1Sortable: Boolean,
    showSlot2: Boolean,
    isSlot2Sortable: Boolean,
    showSlot3: Boolean,
    isSlot3Sortable: Boolean,
    showSlot4: Boolean,
    isSlot4Sortable: Boolean,
    showSlot5: Boolean,
    isSlot5Sortable: Boolean,
    showSlot6: Boolean,
    isSlot6Sortable: Boolean,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToCreate: () -> Unit,
    goToDetail: (String) -> Unit,
) : BasicViewModel<
    AdminListContract.Inputs,
    AdminListContract.Events,
    AdminListContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminListContract.State(
                dataType = dataType,
                isSlot1Sortable = isSlot1Sortable,
                showSlot2 = showSlot2,
                isSlot2Sortable = isSlot2Sortable,
                showSlot3 = showSlot3,
                isSlot3Sortable = isSlot3Sortable,
                showSlot4 = showSlot4,
                isSlot4Sortable = isSlot4Sortable,
                showSlot5 = showSlot5,
                isSlot5Sortable = isSlot5Sortable,
                showSlot6 = showSlot6,
                isSlot6Sortable = isSlot6Sortable,
            ),
            inputHandler = AdminListInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminListEventHandler(
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
