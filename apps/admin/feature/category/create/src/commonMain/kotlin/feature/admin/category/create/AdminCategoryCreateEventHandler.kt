package feature.admin.category.create

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCategoryCreateEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToCategory: (String) -> Unit,
) : EventHandler<
    AdminCategoryCreateContract.Inputs,
    AdminCategoryCreateContract.Events,
    AdminCategoryCreateContract.State,
    > {
    override suspend fun EventHandlerScope<
        AdminCategoryCreateContract.Inputs,
        AdminCategoryCreateContract.Events,
        AdminCategoryCreateContract.State,
        >.handleEvent(
        event: AdminCategoryCreateContract.Events,
    ) = when (event) {
        is AdminCategoryCreateContract.Events.OnError -> onError(event.message)
        is AdminCategoryCreateContract.Events.GoToCategory -> goToCategory(event.id)
    }
}
