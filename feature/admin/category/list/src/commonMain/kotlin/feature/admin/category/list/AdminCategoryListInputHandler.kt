package feature.admin.category.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.CategoryService
import data.type.PageInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminCategoryListContract.Inputs, AdminCategoryListContract.Events, AdminCategoryListContract.State>

internal class AdminCategoryListInputHandler :
    KoinComponent,
    InputHandler<AdminCategoryListContract.Inputs, AdminCategoryListContract.Events, AdminCategoryListContract.State> {

    private val categoryService: CategoryService by inject()

    override suspend fun InputScope.handleInput(input: AdminCategoryListContract.Inputs) = when (input) {
        is AdminCategoryListContract.Inputs.GetPage -> handleGetPage(input.page)
        is AdminCategoryListContract.Inputs.SetCategories -> updateState { it.copy(categories = input.categories) }
        is AdminCategoryListContract.Inputs.SetPerPage -> updateState { it.copy(perPage = input.perPage) }
        is AdminCategoryListContract.Inputs.SetInfo -> updateState { it.copy(info = input.pageInfo) }
        AdminCategoryListContract.Inputs.OnCreateCategoryClicked ->
            postEvent(AdminCategoryListContract.Events.GoToCreateCategory)
    }

    private suspend fun InputScope.handleGetPage(page: Int) {
        val state = getCurrentState()
        sideJob("handleGetUsersPage") {
            val pageInput = PageInput(page = page, size = state.perPage)
            categoryService.getAsPage(pageInput).fold(
                onSuccess = {
                    postInput(AdminCategoryListContract.Inputs.SetCategories(it.getCategoriesAsPage.categories))
                    postInput(AdminCategoryListContract.Inputs.SetInfo(it.getCategoriesAsPage.info))
                },
                onFailure = {
                    postEvent(AdminCategoryListContract.Events.OnError(it.message ?: "Error fetching categories"))
                }
            )
        }
    }
}
