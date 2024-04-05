package feature.admin.product.edit

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

class AdminProductEditViewModel(
    productId: String,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    goToCreateCategory: () -> Unit,
    goToCreateTag: () -> Unit,
    goToCustomer: (String) -> Unit,
    goToProduct: (String) -> Unit,
) : BasicViewModel<
    AdminProductEditContract.Inputs,
    AdminProductEditContract.Events,
    AdminProductEditContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminProductEditContract.State(),
            inputHandler = AdminProductEditInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminProductEditEventHandler(
        onError = onError,
        goBack = goBack,
        goToCategoryCreate = goToCreateCategory,
        goToTagCreate = goToCreateTag,
        goToCustomer = goToCustomer,
        goToProduct = goToProduct,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminProductEditContract.Inputs.Init(productId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
