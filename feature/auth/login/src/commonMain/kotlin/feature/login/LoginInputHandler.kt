package feature.login

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias LoginInputScope = InputHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>

internal class LoginInputHandler :
    KoinComponent,
    InputHandler<LoginContract.Inputs, LoginContract.Events, LoginContract.State> {

    private val authService: AuthService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>.handleInput(
        input: LoginContract.Inputs,
    ) = when (input) {
        is LoginContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is LoginContract.Inputs.SetEmail -> handleSetEmail(input.email)
        is LoginContract.Inputs.SetPassword -> handleSetPassword(input.password)

        LoginContract.Inputs.TogglePasswordVisibility ->
            updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }

        LoginContract.Inputs.OnGoogleClick -> handleOnGoogleClick()
        LoginContract.Inputs.OnFacebookClick -> handleOnFacebookClick()
        LoginContract.Inputs.OnLoginClick -> handleOnLoginClick()
        LoginContract.Inputs.OnForgotPasswordClick -> postEvent(LoginContract.Events.GoToForgotPassword)
        LoginContract.Inputs.OnDontHaveAccountClick -> postEvent(LoginContract.Events.GoToSignUp)
    }

    private suspend fun LoginInputScope.handleSetPassword(password: String) {
        inputValidator.validatePassword(password)?.let { error ->
            updateState {
                it.copy(
                    password = password,
                    passwordError = error,
                    isButtonDisabled = true,
                )
            }
        } ?: run {
            updateState {
                it.copy(
                    password = password,
                    passwordError = null,
                    isButtonDisabled = it.emailError != null,
                )
            }
        }
    }

    private suspend fun LoginInputScope.handleSetEmail(email: String) {
        inputValidator.validateEmail(email)?.let { error ->
            updateState {
                it.copy(
                    email = email,
                    emailError = error,
                    isButtonDisabled = true,
                )
            }
        } ?: run {
            updateState {
                it.copy(
                    email = email,
                    emailError = null,
                    isButtonDisabled = it.passwordError != null,
                )
            }
        }
    }

    private suspend fun LoginInputScope.handleOnGoogleClick() {
        loginDefaultUser()
    }

    private suspend fun LoginInputScope.handleOnFacebookClick() {
        loginDefaultUser()
    }

    private suspend fun LoginInputScope.handleOnLoginClick() {
        val state = getCurrentState()
        sideJob("handleLogin") {
            authService.login(state.email, state.password).fold(
                onSuccess = { postEvent(LoginContract.Events.OnAuthenticated) },
                onFailure = { postEvent(LoginContract.Events.OnError(it.message ?: "Login failed")) },
            )
        }
    }

    private suspend fun LoginInputScope.loginDefaultUser() {
        sideJob("handleLogin") {
            authService.login("test@test.com", "P@ss1234").fold(
                onSuccess = { postEvent(LoginContract.Events.OnAuthenticated) },
                onFailure = { postEvent(LoginContract.Events.OnError(it.message ?: "Login failed")) },
            )
        }
    }
}
