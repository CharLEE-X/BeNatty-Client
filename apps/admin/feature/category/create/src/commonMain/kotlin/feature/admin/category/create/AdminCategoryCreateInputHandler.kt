package feature.admin.category.create

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import data.service.CategoryService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<
    AdminCategoryCreateContract.Inputs,
    AdminCategoryCreateContract.Events,
    AdminCategoryCreateContract.State>

internal class AdminCategoryCreateInputHandler : KoinComponent, InputHandler<
    AdminCategoryCreateContract.Inputs,
    AdminCategoryCreateContract.Events,
    AdminCategoryCreateContract.State> {

    private val categoryService: CategoryService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputScope.handleInput(input: AdminCategoryCreateContract.Inputs) = when (input) {
        AdminCategoryCreateContract.Inputs.OnCreateClick -> handleOnCreateClick()

        is AdminCategoryCreateContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCategoryCreateContract.Inputs.SetName -> handleSetName(input.name)
        is AdminCategoryCreateContract.Inputs.SetNameShake -> updateState { it.copy(nameShake = input.shake) }

        AdminCategoryCreateContract.Inputs.CreateCategory -> handleCreateCategory()
        is AdminCategoryCreateContract.Inputs.ShakeErrors -> handleShakeErrors(name = input.name)
    }

    private suspend fun InputScope.handleOnCreateClick() {
        val state = getCurrentState()

        val isNameError = state.nameError != null

        if (isNameError) {
            postInput(AdminCategoryCreateContract.Inputs.ShakeErrors(name = isNameError))
            return
        } else {
            inputValidator.validateText(state.name, 1)?.let {
                postInput(AdminCategoryCreateContract.Inputs.SetName(state.name))
                return
            }
        }

        postInput(AdminCategoryCreateContract.Inputs.CreateCategory)
    }

    private suspend fun InputScope.handleSetName(name: String) {
        updateState {
            it.copy(
                name = name,
                nameError = inputValidator.validateText(name, 1),
            )
        }
    }

    private suspend fun InputScope.handleCreateCategory() {
        val state = getCurrentState()
        sideJob("CreateCategory") {
            postInput(AdminCategoryCreateContract.Inputs.SetLoading(true))
            categoryService.create(name = state.name).fold(
                { postEvent(AdminCategoryCreateContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminCategoryCreateContract.Events.GoToCategory(it.createCategory)) },
            )
            postInput(AdminCategoryCreateContract.Inputs.SetLoading(false))
        }
    }

    private suspend fun InputScope.handleShakeErrors(name: Boolean) {
        sideJob("ShakeErrors") {
            if (name) postInput(AdminCategoryCreateContract.Inputs.SetNameShake(shake = true))

            delay(Constants.shakeAnimantionDuration)

            if (name) postInput(AdminCategoryCreateContract.Inputs.SetNameShake(shake = false))
        }
    }
}
