package feature.admin.category.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.util.millisToTime
import data.GetCategoriesAllMinimalQuery
import data.GetCategoryByIdQuery
import data.service.CategoryService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminCategoryPageContract.Inputs, AdminCategoryPageContract.Events, AdminCategoryPageContract.State>

internal class AdminUserPageInputHandler :
    KoinComponent,
    InputHandler<AdminCategoryPageContract.Inputs, AdminCategoryPageContract.Events, AdminCategoryPageContract.State> {

    private val categoryService: CategoryService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<AdminCategoryPageContract.Inputs, AdminCategoryPageContract.Events, AdminCategoryPageContract.State>.handleInput(
        input: AdminCategoryPageContract.Inputs,
    ) = when (input) {
        is AdminCategoryPageContract.Inputs.Init -> handleInit(input.id)

        is AdminCategoryPageContract.Inputs.Get.CategoryById -> handleGetById(input.id)
        is AdminCategoryPageContract.Inputs.Get.AllCategories -> handleGetAllCategories()

        is AdminCategoryPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCategoryPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }
        is AdminCategoryPageContract.Inputs.Set.AllCategories -> updateState { it.copy(categories = input.categories) }
        is AdminCategoryPageContract.Inputs.Set.OriginalCategory -> updateState { it.copy(original = input.category) }
        is AdminCategoryPageContract.Inputs.Set.Id -> updateState { it.copy(id = input.id) }
        is AdminCategoryPageContract.Inputs.Set.Name -> handleSetName(input.fullName)
        is AdminCategoryPageContract.Inputs.Set.IsNameShake -> updateState { it.copy(shakeName = input.shake) }

        is AdminCategoryPageContract.Inputs.Set.IsSaveButtonDisabled ->
            updateState { it.copy(isSaveButtonDisabled = input.isDisabled) }

        is AdminCategoryPageContract.Inputs.Set.IsDetailsEditable -> updateState { it.copy(isDetailsEditing = true) }
        is AdminCategoryPageContract.Inputs.Set.CreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminCategoryPageContract.Inputs.Set.Creator -> updateState { it.copy(creator = input.creator) }
        is AdminCategoryPageContract.Inputs.Set.UpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
        is AdminCategoryPageContract.Inputs.Set.Description -> handleSetDescription(input.description)
        is AdminCategoryPageContract.Inputs.Set.Display -> handleSetDisplay(input.display)
        is AdminCategoryPageContract.Inputs.Set.Parent -> handleSetParent(input.parent)

        AdminCategoryPageContract.Inputs.OnClick.CreateNew -> handleCreateNew()
        AdminCategoryPageContract.Inputs.OnClick.SaveDetails -> handleSaveDetails()
        AdminCategoryPageContract.Inputs.OnClick.Delete -> handleDelete()
        AdminCategoryPageContract.Inputs.OnClick.Edit -> handleEdit()
        AdminCategoryPageContract.Inputs.OnClick.Cancel -> handleCancel()
        AdminCategoryPageContract.Inputs.OnClick.Parent -> handleGoToParent()
        AdminCategoryPageContract.Inputs.OnClick.Creator -> handleGoToCreator()
        is AdminCategoryPageContract.Inputs.OnClick.ParentPicker -> handleOnClickParentPicker(input.category)
    }

    private suspend fun InputScope.handleCancel() {
        updateState {
            it.copy(
                name = it.original.name,
                description = it.original.description ?: "",
                parent = it.original.parent,
                display = it.original.display,
                isSaveButtonDisabled = true,
                isDetailsEditing = false,
            )
        }
    }

    private suspend fun InputScope.handleEdit() {
        updateState { it.copy(isDetailsEditing = true) }
    }

    private suspend fun InputScope.handleOnClickParentPicker(category: GetCategoriesAllMinimalQuery.GetCategoriesAllMinimal) {
        val newParent = GetCategoryByIdQuery.Parent(
            id = category.id,
            name = category.name,
        )
        postInput(AdminCategoryPageContract.Inputs.Set.Parent(newParent))
    }

    private suspend fun InputScope.handleGoToCreator() {
        val state = getCurrentState()
        postEvent(AdminCategoryPageContract.Events.GoToUser(state.creator.id.toString()))
    }

    private suspend fun InputScope.handleGoToParent() {
        val state = getCurrentState()
        state.parent?.let { postEvent(AdminCategoryPageContract.Events.GoToUser(it.id.toString())) }
    }

    private suspend fun InputScope.handleGetAllCategories() {
        val state = getCurrentState()
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                onSuccess = { data ->
                    val categories = data.getCategoriesAllMinimal
                        .filter { it.id != state.id }
                    postInput(AdminCategoryPageContract.Inputs.Set.AllCategories(categories))
                },
                onFailure = {
                    postEvent(
                        AdminCategoryPageContract.Events.OnError(it.message ?: "Error while getting all categories")
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleSetParent(parent: GetCategoryByIdQuery.Parent?) {
        updateState {
            val hasChanged = parent != it.original.parent?.id ||
                it.display != it.original.display ||
                it.description != it.original.description ||
                it.name != it.original.name
            it.copy(
                parent = parent,
                isSaveButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleDelete() {
        getCurrentState().id?.let { id ->
            sideJob("handleDeleteCategory") {
                categoryService.deleteById(id).fold(
                    onSuccess = {
                        postEvent(AdminCategoryPageContract.Events.GoToList)
                    },
                    onFailure = {
                        postEvent(
                            AdminCategoryPageContract.Events.OnError(
                                it.message ?: "Error while deleting category"
                            )
                        )
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            postInput(AdminCategoryPageContract.Inputs.Get.AllCategories)
            if (id == null) {
                postInput(AdminCategoryPageContract.Inputs.Set.StateOfScreen(AdminCategoryPageContract.ScreenState.Create))
            } else {
                postInput(AdminCategoryPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminCategoryPageContract.Inputs.Set.Id(id))
                postInput(AdminCategoryPageContract.Inputs.Get.CategoryById(id))
                postInput(AdminCategoryPageContract.Inputs.Set.StateOfScreen(AdminCategoryPageContract.ScreenState.Existing.Read))
                postInput(AdminCategoryPageContract.Inputs.Set.Loading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleCreateNew() {
        val state = getCurrentState()
        sideJob("handleCreateNewUser") {
            categoryService.create(name = state.name).fold(
                onSuccess = { data ->
                    val creator = GetCategoryByIdQuery.Creator(
                        id = data.createCategory.creator.id,
                        name = data.createCategory.creator.name,
                    )
                    val createdAt = millisToTime(data.createCategory.createdAt.toLong())

                    println("Category created at: $createdAt ${data.createCategory.createdAt}")

                    postInput(
                        AdminCategoryPageContract.Inputs.Set.OriginalCategory(
                            category = state.original.copy(
                                id = data.createCategory.id,
                                name = data.createCategory.name,
                                creator = creator,
                                createdAt = createdAt,
                                updatedAt = createdAt
                            )
                        )
                    )

                    postInput(AdminCategoryPageContract.Inputs.Set.Creator(creator))
                    postInput(AdminCategoryPageContract.Inputs.Set.CreatedAt(createdAt))
                    postInput(AdminCategoryPageContract.Inputs.Set.UpdatedAt(createdAt))

                    postInput(AdminCategoryPageContract.Inputs.Set.StateOfScreen(AdminCategoryPageContract.ScreenState.Existing.Read))
                },
                onFailure = {
                    postEvent(
                        AdminCategoryPageContract.Events.OnError(
                            it.message ?: "Error while creating new user"
                        )
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        with(name) {
            updateState {
                val hasChanged = name != it.original.name ||
                    it.display != it.original.display ||
                    it.description != it.original.description ||
                    it.parent != it.original.parent
                it.copy(
                    name = this,
                    nameError = if (it.isSaveButtonDisabled && it.isCreateButtonDisabled) null else {
                        inputValidator.validateText(this)
                    },
                    isSaveButtonDisabled = !hasChanged,
                    isCreateButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun InputScope.handleSetDescription(description: String) {
        with(description) {
            updateState {
                val hasChanged = description != it.original.description ||
                    it.display != it.original.display ||
                    it.name != it.original.name ||
                    it.parent != it.original.parent
                it.copy(
                    description = this,
                    isSaveButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun InputScope.handleSetDisplay(display: Boolean) {
        updateState {
            val hasChanged = display != it.original.display ||
                it.description != it.original.description ||
                it.name != it.original.name ||
                it.parent != it.original.parent
            it.copy(
                display = display,
                isSaveButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleSaveDetails() {
        with(getCurrentState()) {
            val isFullNameError = nameError != null
            val isNoError = !isFullNameError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isFullNameError) postInput(AdminCategoryPageContract.Inputs.Set.IsNameShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(AdminCategoryPageContract.Inputs.Set.IsNameShake(shake = false))
                }
            } else {
                id?.let { id ->
                    sideJob("handleSavePersonalDetails") {
                        categoryService.update(
                            id = id,
                            name = if (name != original.name) name else null,
                            description = if (description != original.description) description else null,
                            parentId = parent?.id?.toString(),
                            display = if (display != original.display) display else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminCategoryPageContract.Inputs.Set.OriginalCategory(
                                        category = this@with.original.copy(
                                            name = data.updateCategory.name,
                                            description = data.updateCategory.description,
                                            parent = data.updateCategory.parentId?.toString()?.let { id ->
                                                GetCategoryByIdQuery.Parent(
                                                    id = id,
                                                    name = data.updateCategory.name
                                                )
                                            },
                                            display = data.updateCategory.display,
                                            updatedAt = data.updateCategory.updatedAt,
                                        )
                                    )
                                )
                                postInput(AdminCategoryPageContract.Inputs.OnClick.Cancel)
                            },
                            onFailure = {
                                postEvent(
                                    AdminCategoryPageContract.Events.OnError(
                                        it.message ?: "Error while updating details"
                                    )
                                )
                            },
                        )
                    }
                } ?: postEvent(AdminCategoryPageContract.Events.OnError("Error while updating details"))
            }
        }
    }

    private suspend fun InputScope.handleGetById(id: String) {
        sideJob("handleGetUserProfile") {
            categoryService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        postInput(AdminCategoryPageContract.Inputs.Set.OriginalCategory(data.getCategoryById))

                        postInput(AdminCategoryPageContract.Inputs.Set.Name(data.getCategoryById.name))
                        postInput(
                            AdminCategoryPageContract.Inputs.Set.Description(
                                data.getCategoryById.description ?: ""
                            )
                        )
                        postInput(AdminCategoryPageContract.Inputs.Set.Parent(data.getCategoryById.parent))
                        postInput(AdminCategoryPageContract.Inputs.Set.Display(data.getCategoryById.display))
                        postInput(AdminCategoryPageContract.Inputs.Set.Creator(data.getCategoryById.creator))
                        postInput(AdminCategoryPageContract.Inputs.Set.IsSaveButtonDisabled(isDisabled = true))

                        try {
                            val createdAt = millisToTime(data.getCategoryById.createdAt.toLong())
                            postInput(AdminCategoryPageContract.Inputs.Set.CreatedAt(createdAt))

                            val updatedAt = millisToTime(data.getCategoryById.updatedAt.toLong())
                            postInput(AdminCategoryPageContract.Inputs.Set.UpdatedAt(updatedAt))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    onFailure = {
                        postEvent(
                            AdminCategoryPageContract.Events.OnError(it.message ?: "Error while getting user profile")
                        )
                    },
                )
            }
        }
    }
}
