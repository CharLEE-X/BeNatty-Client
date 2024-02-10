package feature.admin.category.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.util.millisToTime
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

    override suspend fun InputScope.handleInput(input: AdminCategoryPageContract.Inputs) = when (input) {
        is AdminCategoryPageContract.Inputs.Init -> handleInit(input.id)

        is AdminCategoryPageContract.Inputs.Get.CategoryById -> handleGetById(input.id)
        is AdminCategoryPageContract.Inputs.Get.AllCategories -> handleGetAllCategories(input.currentCategoryId)

        AdminCategoryPageContract.Inputs.OnClick.Create -> handleCreateNew()
        AdminCategoryPageContract.Inputs.OnClick.SaveEdit -> handleSaveDetails()
        AdminCategoryPageContract.Inputs.OnClick.Delete -> handleDelete()
        AdminCategoryPageContract.Inputs.OnClick.CancelEdit -> handleCancel()
        AdminCategoryPageContract.Inputs.OnClick.GoToParent -> handleGoToParent()
        AdminCategoryPageContract.Inputs.OnClick.GotToUserCreator -> handleGoToCreator()
        is AdminCategoryPageContract.Inputs.OnClick.ParentCategorySelected -> handleOnClickParentPicker(input.categoryName)
        AdminCategoryPageContract.Inputs.OnClick.GoToCreateCategory -> postEvent(AdminCategoryPageContract.Events.GoToCreateCategory)

        is AdminCategoryPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCategoryPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }
        is AdminCategoryPageContract.Inputs.Set.AllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminCategoryPageContract.Inputs.Set.OriginalCategory ->
            updateState { it.copy(original = input.category).wasEdited() }

        is AdminCategoryPageContract.Inputs.Set.CurrentCategory ->
            updateState { it.copy(current = input.category).wasEdited() }

        is AdminCategoryPageContract.Inputs.Set.Id -> updateState { it.copy(current = it.current.copy(id = input.id)) }
        is AdminCategoryPageContract.Inputs.Set.Name -> handleSetName(input.fullName)
        is AdminCategoryPageContract.Inputs.Set.IsNameShake -> updateState { it.copy(shakeName = input.shake) }

        is AdminCategoryPageContract.Inputs.Set.CreatedAt ->
            updateState { it.copy(current = it.current.copy(createdAt = input.createdAt)) }

        is AdminCategoryPageContract.Inputs.Set.Creator ->
            updateState { it.copy(current = it.current.copy(creator = input.creator)) }

        is AdminCategoryPageContract.Inputs.Set.UpdatedAt ->
            updateState { it.copy(current = it.current.copy(updatedAt = input.updatedAt)) }

        is AdminCategoryPageContract.Inputs.Set.Description -> handleSetDescription(input.description)
        is AdminCategoryPageContract.Inputs.Set.Display -> handleSetDisplay(input.display)
        is AdminCategoryPageContract.Inputs.Set.IsDisplayShake -> updateState { it.copy(shakeDisplay = input.shake) }
        is AdminCategoryPageContract.Inputs.Set.Parent -> handleSetParent(input.parent)
        is AdminCategoryPageContract.Inputs.Set.Height -> handleSetHeight(input.height)
        is AdminCategoryPageContract.Inputs.Set.Length -> handleSetLength(input.length)
        is AdminCategoryPageContract.Inputs.Set.RequiresShipping -> handleSetRequiresShipping(input.requiresShipping)
        is AdminCategoryPageContract.Inputs.Set.Weight -> handleSetWeight(input.weight)
        is AdminCategoryPageContract.Inputs.Set.Width -> handleSetWidth(input.width)
        is AdminCategoryPageContract.Inputs.Set.IsHeightShake -> updateState { it.copy(shakeHeight = input.shake) }
        is AdminCategoryPageContract.Inputs.Set.IsLengthShake -> updateState { it.copy(shakeLength = input.shake) }
        is AdminCategoryPageContract.Inputs.Set.IsWeightShake -> updateState { it.copy(shakeWeight = input.shake) }
        is AdminCategoryPageContract.Inputs.Set.IsWidthShake -> updateState { it.copy(shakeWidth = input.shake) }
    }

    private suspend fun InputScope.handleSetDisplay(display: Boolean) {
        updateState { it.copy(current = it.current.copy(display = display)).wasEdited() }
    }

    private suspend fun InputScope.handleSetRequiresShipping(requiresShipping: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shippingPreset = it.current.shippingPreset?.copy(requiresShipping = requiresShipping)
                ),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetWidth(width: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shippingPreset = it.current.shippingPreset?.copy(
                        width = width
                    )
                ),
                widthError = if (it.wasEdited) inputValidator.validateNumberPositive(width.toInt()) else null,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetWeight(weight: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shippingPreset = it.current.shippingPreset?.copy(
                        weight = weight
                    )
                ),
                weightError = if (it.wasEdited) inputValidator.validateNumberPositive(weight.toInt()) else null,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetLength(length: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shippingPreset = it.current.shippingPreset?.copy(
                        length = length
                    )
                ),
                lengthError = if (it.wasEdited) inputValidator.validateNumberPositive(length.toInt()) else null,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetHeight(height: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shippingPreset = it.current.shippingPreset?.copy(
                        height = height
                    )
                ),
                heightError = if (it.wasEdited) inputValidator.validateNumberPositive(height.toInt()) else null,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleCancel() {
        updateState {
            it.copy(
                wasEdited = false,
                current = it.original,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnClickParentPicker(categoryName: String) {
        getCurrentState().allCategories
            .firstOrNull { it.name == categoryName }
            ?.let { chosenCategory ->
                val currentParent = getCurrentState().current.parent
                if (currentParent == null || currentParent.id != chosenCategory.id) {
                    val newParent = GetCategoryByIdQuery.Parent(
                        id = chosenCategory.id,
                        name = chosenCategory.name,
                    )
                    postInput(AdminCategoryPageContract.Inputs.Set.Parent(newParent))
                } else {
                    postInput(AdminCategoryPageContract.Inputs.Set.Parent(null))
                }
            } ?: noOp()
    }

    private suspend fun InputScope.handleGoToCreator() {
        val state = getCurrentState()
        postEvent(AdminCategoryPageContract.Events.GoToUser(state.current.creator.id.toString()))
    }

    private suspend fun InputScope.handleGoToParent() {
        val state = getCurrentState()
        state.current.parent?.let { postEvent(AdminCategoryPageContract.Events.GoToUser(it.id.toString())) }
    }

    private suspend fun InputScope.handleGetAllCategories(currentCategoryId: String?) {
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                onSuccess = { data ->
                    val allCategories = data.getCategoriesAllMinimal
                        .filter { currentCategoryId?.let { categoryId -> it.id != categoryId } ?: true }
                    postInput(AdminCategoryPageContract.Inputs.Set.AllCategories(allCategories))
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
        updateState { it.copy(current = it.current.copy(parent = parent)).wasEdited() }
    }

    private suspend fun InputScope.handleDelete() {
        val state = getCurrentState()
        sideJob("handleDeleteCategory") {
            categoryService.deleteById(state.current.id.toString()).fold(
                onSuccess = {
                    postEvent(AdminCategoryPageContract.Events.GoToList)
                },
                onFailure = {
                    postEvent(AdminCategoryPageContract.Events.OnError(it.message ?: "Error while deleting category"))
                },
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            postInput(AdminCategoryPageContract.Inputs.Get.AllCategories(id))
            if (id == null) {
                postInput(AdminCategoryPageContract.Inputs.Set.StateOfScreen(AdminCategoryPageContract.ScreenState.New))
            } else {
                postInput(AdminCategoryPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminCategoryPageContract.Inputs.Set.Id(id))
                postInput(AdminCategoryPageContract.Inputs.Get.CategoryById(id))
                postInput(AdminCategoryPageContract.Inputs.Set.StateOfScreen(AdminCategoryPageContract.ScreenState.Existing))
                postInput(AdminCategoryPageContract.Inputs.Set.Loading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleCreateNew() {
        val state = getCurrentState()
        sideJob("handleCreateNewUser") {
            categoryService.create(name = state.current.name).fold(
                onSuccess = { data ->
                    postEvent(AdminCategoryPageContract.Events.GoToCategory(data.createCategory.id.toString()))
                },
                onFailure = {
                    postEvent(AdminCategoryPageContract.Events.OnError(it.message ?: "Error while creating new user"))
                },
            )
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        with(name) {
            updateState {
                it.copy(
                    current = it.current.copy(name = this),
                    nameError = if (it.wasEdited) inputValidator.validateText(this) else null,
                ).wasEdited()
            }
        }
    }

    private suspend fun InputScope.handleSetDescription(description: String) {
        updateState { it.copy(current = it.current.copy(description = description)).wasEdited() }
    }

    private suspend fun InputScope.handleSaveDetails() {
        with(getCurrentState()) {
            val isFullNameError = nameError != null
            val isDisplayError = displayError != null
            val isWeightError = weightError != null
            val isLengthError = lengthError != null
            val isWidthError = widthError != null
            val isHeightError = heightError != null
            val isNoError = !isFullNameError && !isDisplayError && !isWeightError && !isLengthError &&
                !isWidthError && !isHeightError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isFullNameError) postInput(AdminCategoryPageContract.Inputs.Set.IsNameShake(shake = true))
                    if (isDisplayError) postInput(AdminCategoryPageContract.Inputs.Set.IsDisplayShake(shake = true))
                    if (isWeightError) postInput(AdminCategoryPageContract.Inputs.Set.IsWeightShake(shake = true))
                    if (isLengthError) postInput(AdminCategoryPageContract.Inputs.Set.IsLengthShake(shake = true))
                    if (isWidthError) postInput(AdminCategoryPageContract.Inputs.Set.IsWidthShake(shake = true))
                    if (isHeightError) postInput(AdminCategoryPageContract.Inputs.Set.IsHeightShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(AdminCategoryPageContract.Inputs.Set.IsNameShake(shake = false))
                    if (isDisplayError) postInput(AdminCategoryPageContract.Inputs.Set.IsDisplayShake(shake = false))
                    if (isWeightError) postInput(AdminCategoryPageContract.Inputs.Set.IsWeightShake(shake = false))
                    if (isLengthError) postInput(AdminCategoryPageContract.Inputs.Set.IsLengthShake(shake = false))
                    if (isWidthError) postInput(AdminCategoryPageContract.Inputs.Set.IsWidthShake(shake = false))
                    if (isHeightError) postInput(AdminCategoryPageContract.Inputs.Set.IsHeightShake(shake = false))
                }
            } else {
                sideJob("handleSavePersonalDetails") {
                    categoryService.update(
                        id = current.id.toString(),
                        name = if (current.name != original.name) current.name else null,
                        description = if (current.description != original.description) current.description else null,
                        parentId = current.parent?.id?.toString(),
                        display = if (current.display != original.display) current.display else null,
                        weight = if (current.shippingPreset?.weight != original.shippingPreset?.weight) current.shippingPreset?.weight else null,
                        length = if (current.shippingPreset?.length != original.shippingPreset?.length) current.shippingPreset?.length else null,
                        width = if (current.shippingPreset?.width != original.shippingPreset?.width) current.shippingPreset?.width else null,
                        height = if (current.shippingPreset?.height != original.shippingPreset?.height) current.shippingPreset?.height else null,
                        requiresShipping = if (current.shippingPreset?.requiresShipping != original.shippingPreset?.requiresShipping) current.shippingPreset?.requiresShipping else null,
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
                                        shippingPreset = GetCategoryByIdQuery.ShippingPreset(
                                            weight = data.updateCategory.shippingPreset.weight ?: "",
                                            length = data.updateCategory.shippingPreset.length ?: "",
                                            width = data.updateCategory.shippingPreset.width ?: "",
                                            height = data.updateCategory.shippingPreset.height ?: "",
                                            requiresShipping = data.updateCategory.shippingPreset.requiresShipping,
                                        ),
                                    )
                                )
                            )
                            postInput(AdminCategoryPageContract.Inputs.OnClick.CancelEdit)
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

    private suspend fun InputScope.handleGetById(id: String) {
        sideJob("handleGetUserProfile") {
            categoryService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        val createdAt = millisToTime(data.getCategoryById.createdAt.toLong())
                        val updatedAt = millisToTime(data.getCategoryById.updatedAt.toLong())
                        val category = data.getCategoryById.copy(
                            createdAt = createdAt,
                            updatedAt = updatedAt,
                        )
                        postInput(AdminCategoryPageContract.Inputs.Set.Display(category.display))
                        postInput(AdminCategoryPageContract.Inputs.Set.OriginalCategory(category))
                        postInput(AdminCategoryPageContract.Inputs.Set.CurrentCategory(category))
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

    private fun AdminCategoryPageContract.State.wasEdited(): AdminCategoryPageContract.State = copy(
        wasEdited = current.name != original.name ||
            current.description != original.description ||
            current.display != original.display ||
            current.parent != original.parent ||
            current.shippingPreset?.weight != original.shippingPreset?.weight ||
            current.shippingPreset?.length != original.shippingPreset?.length ||
            current.shippingPreset?.width != original.shippingPreset?.width ||
            current.shippingPreset?.height != original.shippingPreset?.height ||
            current.shippingPreset?.requiresShipping != original.shippingPreset?.requiresShipping
    )
}
