package feature.admin.category.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCategoryPageEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToUserList: () -> Unit,
    private val goToUser: (String) -> Unit,
    private val goToCreateCategory: () -> Unit,
    private val goToCategory: (String) -> Unit,
) : EventHandler<AdminCategoryPageContract.Inputs, AdminCategoryPageContract.Events, AdminCategoryPageContract.State> {
    override suspend fun EventHandlerScope<AdminCategoryPageContract.Inputs, AdminCategoryPageContract.Events, AdminCategoryPageContract.State>.handleEvent(
        event: AdminCategoryPageContract.Events,
    ) = when (event) {
        is AdminCategoryPageContract.Events.OnError -> onError(event.message)
        AdminCategoryPageContract.Events.GoToList -> goToUserList()
        is AdminCategoryPageContract.Events.GoToUser -> goToUser(event.id)
        AdminCategoryPageContract.Events.GoToCreateCategory -> goToCreateCategory()
        is AdminCategoryPageContract.Events.GoToCategory -> goToCategory(event.id)
    }
}
