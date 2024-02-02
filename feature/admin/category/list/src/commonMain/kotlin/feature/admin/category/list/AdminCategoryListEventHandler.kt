package feature.admin.category.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCategoryListEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToCreateCategory: () -> Unit,
    private val onCategoryClick: (String) -> Unit,
) : EventHandler<AdminCategoryListContract.Inputs, AdminCategoryListContract.Events, AdminCategoryListContract.State> {
    override suspend fun EventHandlerScope<AdminCategoryListContract.Inputs, AdminCategoryListContract.Events, AdminCategoryListContract.State>.handleEvent(
        event: AdminCategoryListContract.Events,
    ) = when (event) {
        is AdminCategoryListContract.Events.OnError -> onError(event.message)
        is AdminCategoryListContract.Events.GoToCreateCategory -> goToCreateCategory()
    }
}
