package feature.admin.category.page

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class AdminCategoryEditEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToUserList: () -> Unit,
    private val goToUser: (String) -> Unit,
    private val goToCreateCategory: () -> Unit,
    private val goToCategory: (String) -> Unit,
) : EventHandler<AdminCategoryEditContract.Inputs, AdminCategoryEditContract.Events, AdminCategoryEditContract.State> {
    override suspend fun EventHandlerScope<AdminCategoryEditContract.Inputs, AdminCategoryEditContract.Events, AdminCategoryEditContract.State>.handleEvent(
        event: AdminCategoryEditContract.Events,
    ) = when (event) {
        is AdminCategoryEditContract.Events.OnError -> onError(event.message)
        AdminCategoryEditContract.Events.GoToList -> goToUserList()
        is AdminCategoryEditContract.Events.GoToUser -> goToUser(event.id)
        AdminCategoryEditContract.Events.GoToCreateCategory -> goToCreateCategory()
        is AdminCategoryEditContract.Events.GoToCategory -> goToCategory(event.id)
    }
}
