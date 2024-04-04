package feature.admin.customer.create

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import data.service.UserService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State>

internal class AdminCustomerCreateInputHandler :
    KoinComponent,
    InputHandler<AdminCustomerCreateContract.Inputs, AdminCustomerCreateContract.Events, AdminCustomerCreateContract.State> {

    private val userService: UserService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<
        AdminCustomerCreateContract.Inputs,
        AdminCustomerCreateContract.Events,
        AdminCustomerCreateContract.State>.handleInput(
        input: AdminCustomerCreateContract.Inputs,
    ) = when (input) {
        AdminCustomerCreateContract.Inputs.CreateCustomer -> handleCreateCustomer()
        AdminCustomerCreateContract.Inputs.OnCreateClick -> handleOnCreateClick()
        is AdminCustomerCreateContract.Inputs.ShakeErrors -> handleShakeErrors(input.email)
        AdminCustomerCreateContract.Inputs.OnGoBackClick -> handleGoBack()

        is AdminCustomerCreateContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminCustomerCreateContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is AdminCustomerCreateContract.Inputs.SetEmailError -> updateState { it.copy(emailError = input.error) }
        is AdminCustomerCreateContract.Inputs.SetDetailFirstName -> updateState { it.copy(firstName = input.firstName) }
        is AdminCustomerCreateContract.Inputs.SetDetailLastName -> updateState { it.copy(lastName = input.lastName) }
        is AdminCustomerCreateContract.Inputs.SetEmailShake -> updateState { it.copy(emailShake = input.shake) }
    }

    private suspend fun InputScope.handleOnCreateClick() {
        val state = getCurrentState()

        if (state.emailError == null) {
            inputValidator.validateText(state.email, 1)?.let {
                postInput(AdminCustomerCreateContract.Inputs.SetEmail(state.email))
                return
            }
        }

        val isEmailError = state.emailError != null
        if (isEmailError) {
            postInput(AdminCustomerCreateContract.Inputs.ShakeErrors(email = isEmailError))
            return
        }

        postInput(AdminCustomerCreateContract.Inputs.CreateCustomer)
    }

    private suspend fun InputScope.handleCreateCustomer() {
        val state = getCurrentState()
        sideJob("CreateCustomer") {
            userService.create(
                email = state.email,
                firstName = state.firstName,
                lastName = state.lastName,
            ).fold(
                { postEvent(AdminCustomerCreateContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminCustomerCreateContract.Events.GoToCustomer(it.createUser.id)) },
            )
        }
    }

    private suspend fun InputScope.handleShakeErrors(email: Boolean) {
        sideJob("ShakeErrors") {
            if (email) postInput(AdminCustomerCreateContract.Inputs.SetEmailShake(shake = true))

            delay(Constants.shakeAnimantionDuration)

            if (email) postInput(AdminCustomerCreateContract.Inputs.SetEmailShake(shake = false))
        }
    }

    private suspend fun InputScope.handleGoBack() {
        postEvent(AdminCustomerCreateContract.Events.GoBack)
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        updateState { it.copy(email = email, emailError = inputValidator.validateEmail(email)) }
    }
}
