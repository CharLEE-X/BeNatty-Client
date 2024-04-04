package feature.admin.product.page

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.withViewModel
import core.models.PageScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AdminProductPageViewModel(
    productId: String?,
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goBackToProducts: () -> Unit,
    goToCreateCategory: () -> Unit,
    goToCreateTag: () -> Unit,
    goToCustomerDetails: (String) -> Unit,
    goToProductDetail: (String) -> Unit,
) : BasicViewModel<
    AdminProductPageContract.Inputs,
    AdminProductPageContract.Events,
    AdminProductPageContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = AdminProductPageContract.State(
                pageScreenState = if (productId == null) PageScreenState.New else PageScreenState.Existing,
            ),
            inputHandler = AdminProductPageInputHandler(),
            name = TAG,
        )
        .dispatchers(
            inputsDispatcher = Dispatchers.Main.immediate,
            eventsDispatcher = Dispatchers.Main.immediate,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .build(),
    eventHandler = AdminProductPageEventHandler(
        onError = onError,
        goBackToProducts = goBackToProducts,
        goToCreateCategory = goToCreateCategory,
        goToCreateTag = goToCreateTag,
        goToCustomerDetails = goToCustomerDetails,
        goToProduct = goToProductDetail,
    ),
    coroutineScope = scope,
) {
    init {
        trySend(AdminProductPageContract.Inputs.Init(productId))
    }

    companion object {
        private val TAG = this::class.simpleName!!
    }
}
