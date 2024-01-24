package feature.updatepassword

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias UpdatePasswordInputScope = InputHandlerScope<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State>

internal class UpdatePasswordInputHandler :
    KoinComponent,
    InputHandler<UpdatePasswordContract.Inputs, UpdatePasswordContract.Events, UpdatePasswordContract.State> {

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
                userService.updateUser(password = state.password).fold(
                    onSuccess = {
                        postInput(
                            UpdatePasswordContract.Inputs.SetScreenState(UpdatePasswordContract.ScreenState.Success)
                        )
                    },
                    onFailure = {
                        postInput(
                            UpdatePasswordContract.Inputs.ShowError(it.message ?: "Error while updating password")
                        )
                    },
                )
            } else {
                postInput(UpdatePasswordContract.Inputs.ShowError("Password must be at least 8 characters long"))
            }
        }
    }
}

private fun validatePassword(password: String): Boolean {
    return password.length >= 8
}
