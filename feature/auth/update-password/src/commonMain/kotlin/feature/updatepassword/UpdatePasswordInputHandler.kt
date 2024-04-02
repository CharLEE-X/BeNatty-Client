package feature.updatepassword

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AuthService
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias UpdatePasswordInputScope = InputHandlerScope<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State>

internal class UpdatePasswordInputHandler :
    KoinComponent,
    InputHandler<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State> {

    private val authService by inject<AuthService>()
    private val userService by inject<UserService>()

    override suspend fun InputHandlerScope<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State>.handleInput(
        input: UpdatePasswordContract.Inputs,
    ) = when (input) {
        is UpdatePasswordContract.Inputs.SetPassword -> updateState { it.copy(password = input.password) }
        UpdatePasswordContract.Inputs.ToggleShowPassword -> updateState { it.copy(showPassword = !it.showPassword) }
        is UpdatePasswordContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }
        UpdatePasswordContract.Inputs.OnChangePasswordClick -> handleOnChangePasswordClick()
        is UpdatePasswordContract.Inputs.ShowError -> updateState {
            it.copy(showError = true, errorMessage = input.message)
        }

        UpdatePasswordContract.Inputs.HideError -> updateState { it.copy(showError = false, errorMessage = "") }
        UpdatePasswordContract.Inputs.OnLoginClick -> postEvent(UpdatePasswordContract.Events.GoToLogin)
    }

    private suspend fun UpdatePasswordInputScope.handleOnChangePasswordClick() {
        val state = getCurrentState()
        sideJob("handleOnChangePasswordClick") {
            if (validatePassword(state.password)) {
                authService.userId?.let { userId ->
                    userService.update(
                        id = userId,
                        password = state.password,
                        email = null,
                        detailsFirstName = null,
                        detailsLastName = null,
                        language = null,
                        detailPhone = null,
                        country = null,
                        addressFirstName = null,
                        addressLastName = null,
                        addressPhone = null,
                        company = null,
                        address = null,
                        apartment = null,
                        city = null,
                        postcode = null,
                        collectTax = null,
                        marketingEmails = null,
                        marketingSms = null,
                    ).fold(
                        { postInput(UpdatePasswordContract.Inputs.ShowError(it.mapToUiMessage())) },
                        {
                            postInput(
                                UpdatePasswordContract.Inputs.SetScreenState(UpdatePasswordContract.ScreenState.Success)
                            )
                        },
                    )
                } ?: postEvent(UpdatePasswordContract.Events.GoToLogin)
            } else {
                postInput(UpdatePasswordContract.Inputs.ShowError("Password must be at least 8 characters long"))
            }
        }
    }
}

private fun validatePassword(password: String): Boolean {
    return password.length >= 8
}
