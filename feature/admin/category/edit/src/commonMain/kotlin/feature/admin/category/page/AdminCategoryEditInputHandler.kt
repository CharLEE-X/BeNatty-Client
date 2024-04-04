package feature.admin.category.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import core.util.millisToTime
import data.GetCategoryByIdQuery
import data.service.CategoryService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminCategoryEditContract.Inputs, AdminCategoryEditContract.Events, AdminCategoryEditContract.State>

internal class AdminUserPageInputHandler :
    KoinComponent,
    InputHandler<AdminCategoryEditContract.Inputs, AdminCategoryEditContract.Events, AdminCategoryEditContract.State> {

    private val categoryService: CategoryService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputScope.handleInput(input: AdminCategoryEditContract.Inputs) = when (input) {
        is AdminCategoryEditContract.Inputs.Init -> handleInit(input.id)

        is AdminCategoryEditContract.Inputs.GetCategoryById -> handleGetById(input.id)
        is AdminCategoryEditContract.Inputs.GetAllCategories -> handleGetAllCategories(input.currentCategoryId)
        AdminCategoryEditContract.Inputs.SaveEdit -> handleSaveEdit()
        is AdminCategoryEditContract.Inputs.ShakeErrors -> handleShakeErrors(
            name = input.name,
            display = input.display,
            weight = input.weight,
            length = input.length,
            width = input.width,
            height = input.height,
        )

        AdminCategoryEditContract.Inputs.OnSaveEditClick -> handleOnSaveEditClick()
        AdminCategoryEditContract.Inputs.OnDeleteClick -> handleDelete()
        AdminCategoryEditContract.Inputs.OnCancelEditClick -> handleCancel()
        AdminCategoryEditContract.Inputs.OnGoToParentClick -> handleGoToParent()
        AdminCategoryEditContract.Inputs.OnGoToUserCreatorClick -> handleGoToCreator()
        is AdminCategoryEditContract.Inputs.OnParentCategoryClick -> handleOnClickParentPicker(input.categoryName)
        AdminCategoryEditContract.Inputs.OnGoToCreateCategoryClick -> postEvent(AdminCategoryEditContract.Events.GoToCreateCategory)
        AdminCategoryEditContract.Inputs.OnImproveDescriptionClick -> handleImproveDescription()

        is AdminCategoryEditContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCategoryEditContract.Inputs.SetAllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminCategoryEditContract.Inputs.SetOriginalCategory ->
            updateState { it.copy(original = input.category).wasEdited() }

        is AdminCategoryEditContract.Inputs.SetCurrentCategory ->
            updateState { it.copy(current = input.category).wasEdited() }

        is AdminCategoryEditContract.Inputs.SetId -> updateState { it.copy(current = it.current.copy(id = input.id)) }
        is AdminCategoryEditContract.Inputs.SetName -> handleSetName(input.fullName)
        is AdminCategoryEditContract.Inputs.SetIsNameShake -> updateState { it.copy(shakeName = input.shake) }

        is AdminCategoryEditContract.Inputs.SetCreatedAt ->
            updateState { it.copy(current = it.current.copy(createdAt = input.createdAt)) }

        is AdminCategoryEditContract.Inputs.SetCreator ->
            updateState { it.copy(current = it.current.copy(creator = input.creator)) }

        is AdminCategoryEditContract.Inputs.SetUpdatedAt ->
            updateState { it.copy(current = it.current.copy(updatedAt = input.updatedAt)) }

        is AdminCategoryEditContract.Inputs.SetDescription -> handleSetDescription(input.description)
        is AdminCategoryEditContract.Inputs.SetDisplay -> handleSetDisplay(input.display)
        is AdminCategoryEditContract.Inputs.SetIsDisplayShake -> updateState { it.copy(shakeDisplay = input.shake) }
        is AdminCategoryEditContract.Inputs.SetParent -> handleSetParent(input.parent)
        is AdminCategoryEditContract.Inputs.SetHeight -> handleSetHeight(input.height)
        is AdminCategoryEditContract.Inputs.SetLength -> handleSetLength(input.length)
        is AdminCategoryEditContract.Inputs.SetRequiresShipping -> handleSetRequiresShipping(input.requiresShipping)
        is AdminCategoryEditContract.Inputs.SetWeight -> handleSetWeight(input.weight)
        is AdminCategoryEditContract.Inputs.SetWidth -> handleSetWidth(input.width)
        is AdminCategoryEditContract.Inputs.SetIsHeightShake -> updateState { it.copy(shakeHeight = input.shake) }
        is AdminCategoryEditContract.Inputs.SetIsLengthShake -> updateState { it.copy(shakeLength = input.shake) }
        is AdminCategoryEditContract.Inputs.SetIsWeightShake -> updateState { it.copy(shakeWeight = input.shake) }
        is AdminCategoryEditContract.Inputs.SetIsWidthShake -> updateState { it.copy(shakeWidth = input.shake) }
    }

    private suspend fun InputScope.handleShakeErrors(
        name: Boolean,
        display: Boolean,
        weight: Boolean,
        length: Boolean,
        width: Boolean,
        height: Boolean
    ) {
        sideJob("handleSavePersonalDetailsShakes") {
            if (name) postInput(AdminCategoryEditContract.Inputs.SetIsNameShake(shake = true))
            if (display) postInput(AdminCategoryEditContract.Inputs.SetIsDisplayShake(shake = true))
            if (weight) postInput(AdminCategoryEditContract.Inputs.SetIsWeightShake(shake = true))
            if (length) postInput(AdminCategoryEditContract.Inputs.SetIsLengthShake(shake = true))
            if (weight) postInput(AdminCategoryEditContract.Inputs.SetIsWidthShake(shake = true))
            if (height) postInput(AdminCategoryEditContract.Inputs.SetIsHeightShake(shake = true))

            delay(Constants.shakeAnimantionDuration)

            if (name) postInput(AdminCategoryEditContract.Inputs.SetIsNameShake(shake = false))
            if (display) postInput(AdminCategoryEditContract.Inputs.SetIsDisplayShake(shake = false))
            if (weight) postInput(AdminCategoryEditContract.Inputs.SetIsWeightShake(shake = false))
            if (length) postInput(AdminCategoryEditContract.Inputs.SetIsLengthShake(shake = false))
            if (width) postInput(AdminCategoryEditContract.Inputs.SetIsWidthShake(shake = false))
            if (height) postInput(AdminCategoryEditContract.Inputs.SetIsHeightShake(shake = false))
        }
    }

    private suspend fun InputScope.handleSaveEdit() {
        val current = getCurrentState().current
        val original = getCurrentState().original
        sideJob("handleSavePersonalDetails") {
            categoryService.update(
                id = current.id,
                name = if (current.name != original.name) current.name else null,
                description = if (current.description != original.description) current.description else null,
                parentId = current.parent?.id,
                display = if (current.display != original.display) current.display else null,
                weight = if (current.shippingPreset?.weight != original.shippingPreset?.weight) current.shippingPreset?.weight else null,
                length = if (current.shippingPreset?.length != original.shippingPreset?.length) current.shippingPreset?.length else null,
                width = if (current.shippingPreset?.width != original.shippingPreset?.width) current.shippingPreset?.width else null,
                height = if (current.shippingPreset?.height != original.shippingPreset?.height) current.shippingPreset?.height else null,
                requiresShipping = if (current.shippingPreset?.isPhysicalProduct != original.shippingPreset?.isPhysicalProduct) current.shippingPreset?.isPhysicalProduct else null,
            ).fold(
                { postEvent(AdminCategoryEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    postInput(
                        AdminCategoryEditContract.Inputs.SetOriginalCategory(
                            category = original.copy(
                                name = data.updateCategory.name,
                                description = data.updateCategory.description,
                                parent = GetCategoryByIdQuery.Parent(
                                    id = data.updateCategory.parent?.id ?: "",
                                    name = original.parent?.name ?: "",
                                    imageUrl = original.parent?.imageUrl ?: "",
                                ),
                                display = data.updateCategory.display,
                                updatedAt = data.updateCategory.updatedAt,
                                shippingPreset = GetCategoryByIdQuery.ShippingPreset(
                                    weight = data.updateCategory.shippingPreset?.weight ?: "",
                                    length = data.updateCategory.shippingPreset?.length ?: "",
                                    width = data.updateCategory.shippingPreset?.width ?: "",
                                    height = data.updateCategory.shippingPreset?.height ?: "",
                                    isPhysicalProduct = data.updateCategory.shippingPreset?.isPhysicalProduct
                                        ?: true,
                                ),
                            )
                        )
                    )
                    postInput(AdminCategoryEditContract.Inputs.OnCancelEditClick)
                },
            )
        }
    }

    private fun InputScope.handleImproveDescription() {
        noOp()
    }

    private suspend fun InputScope.handleSetDisplay(display: Boolean) {
        updateState { it.copy(current = it.current.copy(display = display)).wasEdited() }
    }

    private suspend fun InputScope.handleSetRequiresShipping(isPhysicalProduct: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shippingPreset = it.current.shippingPreset?.copy(isPhysicalProduct = isPhysicalProduct)
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
            it.copy(wasEdited = false, current = it.original).wasEdited()
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
                        imageUrl = chosenCategory.imageUrl ?: "",
                    )
                    postInput(AdminCategoryEditContract.Inputs.SetParent(newParent))
                } else {
                    postInput(AdminCategoryEditContract.Inputs.SetParent(null))
                }
            } ?: noOp()
    }

    private suspend fun InputScope.handleGoToCreator() {
        val state = getCurrentState()
        postEvent(AdminCategoryEditContract.Events.GoToUser(state.current.creator.id))
    }

    private suspend fun InputScope.handleGoToParent() {
        val state = getCurrentState()
        state.current.parent?.let { postEvent(AdminCategoryEditContract.Events.GoToUser(it.id)) }
    }

    private suspend fun InputScope.handleGetAllCategories(currentCategoryId: String?) {
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                {
                    postEvent(
                        AdminCategoryEditContract.Events.OnError(it.mapToUiMessage())
                    )
                },
                { data ->
                    val allCategories = data.getAllCategoriesAsMinimal
                        .filter { currentCategoryId?.let { categoryId -> it.id != categoryId } ?: true }
                    postInput(AdminCategoryEditContract.Inputs.SetAllCategories(allCategories))
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
            categoryService.deleteById(state.current.id).fold(
                { postEvent(AdminCategoryEditContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminCategoryEditContract.Events.GoToList) },
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String) {
        sideJob("handleInit") {
            postInput(AdminCategoryEditContract.Inputs.SetLoading(isLoading = true))
            postInput(AdminCategoryEditContract.Inputs.GetAllCategories(id))
            postInput(AdminCategoryEditContract.Inputs.SetId(id))
            postInput(AdminCategoryEditContract.Inputs.GetCategoryById(id))
            postInput(AdminCategoryEditContract.Inputs.SetLoading(isLoading = false))
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

    private suspend fun InputScope.handleOnSaveEditClick() {
        val state = getCurrentState()

        val isFullNameError = state.nameError != null
        val isDisplayError = state.displayError != null
        val isWeightError = state.weightError != null
        val isLengthError = state.lengthError != null
        val isWidthError = state.widthError != null
        val isHeightError = state.heightError != null
        val isNoError = !isFullNameError && !isDisplayError && !isWeightError && !isLengthError &&
            !isWidthError && !isHeightError

        if (!isNoError) {
            postInput(
                AdminCategoryEditContract.Inputs.ShakeErrors(
                    name = isFullNameError,
                    display = isDisplayError,
                    weight = isWeightError,
                    length = isLengthError,
                    width = isWidthError,
                    height = isHeightError,
                )
            )
        } else {
            val errors = mutableListOf<String>().apply {
                inputValidator.validateText(state.current.name, 1)?.let {
                    postInput(AdminCategoryEditContract.Inputs.SetName(state.current.name))
                    add(it)
                }
                state.current.description?.let { desc ->
                    inputValidator.validateText(desc, 1)?.let {
                        postInput(AdminCategoryEditContract.Inputs.SetDescription(it))
                        add(it)
                    }
                }
            }
            if (errors.isNotEmpty()) return
        }

        postInput(AdminCategoryEditContract.Inputs.SaveEdit)
    }

    private suspend fun InputScope.handleGetById(id: String) {
        sideJob("handleGetUserProfile") {
            categoryService.getById(id).fold(
                { postEvent(AdminCategoryEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val createdAt = millisToTime(data.getCategoryById.createdAt.toLong())
                    val updatedAt = millisToTime(data.getCategoryById.updatedAt.toLong())
                    val category = data.getCategoryById.copy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                    )
                    postInput(AdminCategoryEditContract.Inputs.SetDisplay(category.display))
                    postInput(AdminCategoryEditContract.Inputs.SetOriginalCategory(category))
                    postInput(AdminCategoryEditContract.Inputs.SetCurrentCategory(category))
                },
            )
        }
    }

    private fun AdminCategoryEditContract.State.wasEdited(): AdminCategoryEditContract.State = copy(
        wasEdited = current.name != original.name ||
            current.description != original.description ||
            current.display != original.display ||
            current.parent != original.parent ||
            current.shippingPreset?.weight != original.shippingPreset?.weight ||
            current.shippingPreset?.length != original.shippingPreset?.length ||
            current.shippingPreset?.width != original.shippingPreset?.width ||
            current.shippingPreset?.height != original.shippingPreset?.height ||
            current.shippingPreset?.isPhysicalProduct != original.shippingPreset?.isPhysicalProduct
    )
}
