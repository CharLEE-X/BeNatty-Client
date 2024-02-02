package feature.admin.category.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
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
        AdminCategoryPageContract.Inputs.CreateNew -> handleCreateNew()
        is AdminCategoryPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCategoryPageContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }
        is AdminCategoryPageContract.Inputs.SetId -> updateState { it.copy(id = input.id) }

        is AdminCategoryPageContract.Inputs.GetById -> handleGetById(input.id)
        is AdminCategoryPageContract.Inputs.SetCategoryProfile -> updateState { it.copy(original = input.category) }

        is AdminCategoryPageContract.Inputs.SetName -> handleSetName(input.fullName)
        is AdminCategoryPageContract.Inputs.SetNameShake -> updateState { it.copy(shakeName = input.shake) }

        AdminCategoryPageContract.Inputs.SaveDetails -> handleSaveDetails()
        is AdminCategoryPageContract.Inputs.SetSaveButtonDisabled ->
            updateState { it.copy(isSaveButtonDisabled = input.isDisabled) }

        is AdminCategoryPageContract.Inputs.SetDetailsEditable ->
            updateState { it.copy(isDetailsEditing = true) }

        is AdminCategoryPageContract.Inputs.Delete -> handleDelete()
        is AdminCategoryPageContract.Inputs.SetCreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminCategoryPageContract.Inputs.SetCreatedBy -> updateState { it.copy(createdBy = input.createdBy) }
        is AdminCategoryPageContract.Inputs.SetUpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
        is AdminCategoryPageContract.Inputs.SetDescription -> handleSetDescription(input.description)
        is AdminCategoryPageContract.Inputs.SetDisplay -> handleSetDisplay(input.display)
        is AdminCategoryPageContract.Inputs.SetParentId -> handleSetParentId(input.parentId)
    }

    private suspend fun InputScope.handleSetParentId(parentId: String) {
        updateState {
            val hasChanged = parentId != it.original.parentId ||
                it.display != it.original.display ||
                it.description != it.original.description ||
                it.name != it.original.name
            it.copy(
                parentId = parentId,
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
            if (id == null) {
                postInput(AdminCategoryPageContract.Inputs.SetScreenState(AdminCategoryPageContract.ScreenState.New.Create))
            } else {
                postInput(AdminCategoryPageContract.Inputs.SetLoading(isLoading = true))
                postInput(AdminCategoryPageContract.Inputs.SetId(id))
                postInput(AdminCategoryPageContract.Inputs.SetScreenState(AdminCategoryPageContract.ScreenState.Existing.Read))
                postInput(AdminCategoryPageContract.Inputs.GetById(id))
                postInput(AdminCategoryPageContract.Inputs.SetLoading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleCreateNew() {
        val state = getCurrentState()
        sideJob("handleCreateNewUser") {
            categoryService.create(name = state.name).fold(
                onSuccess = { data ->
                    postInput(
                        AdminCategoryPageContract.Inputs.SetCategoryProfile(
                            category = state.original.copy(
                                id = data.createCategory.id,
                                name = data.createCategory.name,
                                createdBy = data.createCategory.createdBy,
                                createdAt = data.createCategory.createdAt,
                            )
                        )
                    )
                    postInput(AdminCategoryPageContract.Inputs.SetScreenState(AdminCategoryPageContract.ScreenState.Existing.Read))
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
                    it.parentId != it.original.parentId
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
                    it.parentId != it.original.parentId
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
                it.parentId != it.original.parentId
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
                    if (isFullNameError) postInput(AdminCategoryPageContract.Inputs.SetNameShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(AdminCategoryPageContract.Inputs.SetNameShake(shake = false))
                }
            } else {
                id?.let { id ->
                    sideJob("handleSavePersonalDetails") {
                        categoryService.update(
                            id = id,
                            name = if (name != original.name) name else null,
                            description = if (description != original.description) description else null,
                            parentId = if (parentId != original.parentId) parentId else null,
                            display = if (display != original.display) display else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminCategoryPageContract.Inputs.SetCategoryProfile(
                                        category = this@with.original.copy(
                                            name = data.updateCategory.name,
                                            description = data.updateCategory.description,
                                            parentId = data.updateCategory.parentId,
                                            display = data.updateCategory.display,
                                            updatedAt = data.updateCategory.updatedAt,
                                        )
                                    )
                                )
                                postInput(AdminCategoryPageContract.Inputs.SetSaveButtonDisabled(isDisabled = true))
                                postInput(AdminCategoryPageContract.Inputs.SetDetailsEditable(isEditable = false))
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
                }
            }
        }
    }

    private suspend fun InputScope.handleGetById(id: String) {
        sideJob("handleGetUserProfile") {
            categoryService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        postInput(AdminCategoryPageContract.Inputs.SetCategoryProfile(data.getCategoryById))

                        postInput(AdminCategoryPageContract.Inputs.SetName(data.getCategoryById.name))
                        postInput(
                            AdminCategoryPageContract.Inputs.SetDescription(
                                data.getCategoryById.description ?: ""
                            )
                        )
                        postInput(AdminCategoryPageContract.Inputs.SetParentId(data.getCategoryById.parentId ?: ""))
                        postInput(AdminCategoryPageContract.Inputs.SetDisplay(data.getCategoryById.display))
                        postInput(AdminCategoryPageContract.Inputs.SetCreatedBy(data.getCategoryById.createdBy.toString()))
                        postInput(AdminCategoryPageContract.Inputs.SetCreatedAt(data.getCategoryById.createdAt.toLong()))
                        postInput(AdminCategoryPageContract.Inputs.SetUpdatedAt(data.getCategoryById.updatedAt.toLong()))
                        postInput(AdminCategoryPageContract.Inputs.SetSaveButtonDisabled(isDisabled = true))

                        try {
                            postInput(AdminCategoryPageContract.Inputs.SetCreatedAt(data.getCategoryById.createdAt.toLong()))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        data.getCategoryById.createdBy?.let {
                            postInput(AdminCategoryPageContract.Inputs.SetCreatedBy(it.toString()))
                        }
                        try {
                            postInput(AdminCategoryPageContract.Inputs.SetUpdatedAt(data.getCategoryById.updatedAt.toLong()))
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
