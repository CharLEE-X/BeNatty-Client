package feature.forgotpassword

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias ForgotPasswordInput = InputHandlerScope<ForgotPasswordContract.Inputs, ForgotPasswordContract.Events, ForgotPasswordContract.State>

internal class ForgotPasswordInputHandler :
    KoinComponent,
    InputHandler<ForgotPasswordContract.Inputs, ForgotPasswordContract.Events, ForgotPasswordContract.State> {

    private val authService: AuthService by inject()

    override suspend fun InputHandlerScope<ForgotPasswordContract.Inputs, ForgotPasswordContract.Events, ForgotPasswordContract.State>.handleInput(
        input: ForgotPasswordContract.Inputs,
    ) = when (input) {
        is ForgotPasswordContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is ForgotPasswordContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is ForgotPasswordContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }
        ForgotPasswordContract.Inputs.OnGetLinkClick -> handleOnForgotPasswordClick()
        ForgotPasswordContract.Inputs.OnGoToLoginClick -> postEvent(ForgotPasswordContract.Events.GoToLogin)
        is ForgotPasswordContract.Inputs.ShowError ->
            updateState { it.copy(showError = true, errorMessage = input.message) }

        ForgotPasswordContract.Inputs.HideError -> updateState { it.copy(showError = false, errorMessage = "") }
        ForgotPasswordContract.Inputs.OpenGmail -> handleOpenGmail()
        ForgotPasswordContract.Inputs.OpenOutlook -> handleOpenOutlook()
        ForgotPasswordContract.Inputs.DisableButton -> updateState { it.copy(buttonDisabled = true) }
    }

    private suspend fun ForgotPasswordInput.handleSetEmail(email: String) {
        if (validateEmail(email)) {
            updateState {
                it.copy(
                    email = email,
                    showError = false,
                    errorMessage = null,
                    buttonDisabled = false,
                )
            }
        } else {
            updateState {
                it.copy(
                    email = email,
                    showError = true,
                    errorMessage = "Invalid email",
                    buttonDisabled = true,
                )
            }
        }
    }

    private fun handleOpenOutlook() {
        TODO("Not yet implemented")
    }

    private fun handleOpenGmail() {
        TODO("Not yet implemented")
    }

    private suspend fun ForgotPasswordInput.handleOnForgotPasswordClick() {
        val state = getCurrentState()
        sideJob("handleForgotPassword") {
            postInput(ForgotPasswordContract.Inputs.SetIsLoading(true))
            authService.forgotPassword(state.email).fold(
                { postInput(ForgotPasswordContract.Inputs.ShowError(it.mapToUiMessage())) },
                { result ->
                    if (result.forgotPassword.isForgotPasswordEmailSent) {
                        postInput(
                            ForgotPasswordContract.Inputs.SetScreenState(ForgotPasswordContract.ScreenState.CheckEmail)
                        )
                    } else {
                        postInput(ForgotPasswordContract.Inputs.ShowError("Forgot password failed"))
                    }
                },
            )
            postInput(ForgotPasswordContract.Inputs.SetIsLoading(false))
        }
    }
}

private fun validateEmail(email: String): Boolean {
    return email.contains("@") && email.length > 6
}
