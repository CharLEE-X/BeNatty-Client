package feature.admin.product.create

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import data.service.ProductService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<
    AdminProductCreateContract.Inputs,
    AdminProductCreateContract.Events,
    AdminProductCreateContract.State>

internal class AdminProductCreateInputHandler : KoinComponent, InputHandler<
    AdminProductCreateContract.Inputs,
    AdminProductCreateContract.Events,
    AdminProductCreateContract.State> {

    private val productService by inject<ProductService>()
    private val inputValidator by inject<InputValidator>()

    override suspend fun InputScope.handleInput(input: AdminProductCreateContract.Inputs) = when (input) {
        AdminProductCreateContract.Inputs.CreateProduct -> handleCreateProduct()
        AdminProductCreateContract.Inputs.OnCreateClick -> handleOnCreateClick()
        is AdminProductCreateContract.Inputs.ShakeErrors -> handleShakeErrors(name = input.name)

        is AdminProductCreateContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductCreateContract.Inputs.SetName -> handleSetName(input.name)
        is AdminProductCreateContract.Inputs.SetNameShake -> updateState { it.copy(nameShake = input.shake) }
    }

    private suspend fun InputScope.handleOnCreateClick() {
        val state = getCurrentState()

        val isNameError = state.nameError != null

        if (isNameError) {
            postInput(AdminProductCreateContract.Inputs.ShakeErrors(name = isNameError))
            return
        } else {
            inputValidator.validateText(state.name, 1)?.let {
                postInput(AdminProductCreateContract.Inputs.SetName(state.name))
                return
            }
        }

        postInput(AdminProductCreateContract.Inputs.CreateProduct)
    }

    private suspend fun InputScope.handleSetName(name: String) {
        updateState {
            it.copy(
                name = name,
                nameError = inputValidator.validateText(name, 1),
            )
        }
    }

    private suspend fun InputScope.handleCreateProduct() {
        val state = getCurrentState()
        sideJob("CreateCategory") {
            postInput(AdminProductCreateContract.Inputs.SetLoading(true))
            productService.create(name = state.name).fold(
                { postEvent(AdminProductCreateContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminProductCreateContract.Events.GoToProduct(it.createProduct)) },
            )
            postInput(AdminProductCreateContract.Inputs.SetLoading(false))
        }
    }

    private suspend fun InputScope.handleShakeErrors(name: Boolean) {
        sideJob("ShakeErrors") {
            if (name) postInput(AdminProductCreateContract.Inputs.SetNameShake(shake = true))

            delay(Constants.shakeAnimantionDuration)

            if (name) postInput(AdminProductCreateContract.Inputs.SetNameShake(shake = false))
        }
    }
}
