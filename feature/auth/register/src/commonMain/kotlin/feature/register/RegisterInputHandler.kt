package feature.register

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias RegisterInputScope = InputHandlerScope<RegisterContract.Inputs, RegisterContract.Events, RegisterContract.State>

internal class RegisterInputHandler :
    KoinComponent,
    InputHandler<RegisterContract.Inputs, RegisterContract.Events, RegisterContract.State> {

    private val authService: AuthService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputHandlerScope<RegisterContract.Inputs, RegisterContract.Events, RegisterContract.State>.handleInput(
        input: RegisterContract.Inputs,
    ) = when (input) {
        is RegisterContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is RegisterContract.Inputs.SetName -> handleSetName(input.name)
        is RegisterContract.Inputs.SetEmail -> handleSetEmail(input.email)
        RegisterContract.Inputs.ToggleNewsletterChecked -> updateState { it.copy(newsletterChecked = !it.newsletterChecked) }
        is RegisterContract.Inputs.SetPassword -> handleSetPassword(input.password)
        is RegisterContract.Inputs.SetRepeatPassword -> handleSetRepeatPassword(input.repeatPassword)

        RegisterContract.Inputs.TogglePasswordVisibility ->
            updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }

        RegisterContract.Inputs.ToggleRepeatPasswordVisibility ->
            updateState { it.copy(isRepeatPasswordVisible = !it.isRepeatPasswordVisible) }

        RegisterContract.Inputs.OnGoogleClick -> noOp()
        RegisterContract.Inputs.OnFacebookClick -> noOp()
        RegisterContract.Inputs.OnRegisterClick -> handleRegisterClick()
        RegisterContract.Inputs.OnAlreadyHaveAccountClick -> postEvent(RegisterContract.Events.GoToLogin)
        RegisterContract.Inputs.OnPrivacyPolicyClick -> postEvent(RegisterContract.Events.GoToPrivacyPolicy)
        RegisterContract.Inputs.OnTnCClick -> postEvent(RegisterContract.Events.GoToTnC)
        RegisterContract.Inputs.DisableButton -> updateState { it.copy(isButtonDisabled = true) }
    }

    private suspend fun RegisterInputScope.handleSetRepeatPassword(repeatPassword: String) {
        inputValidator.validateRepeatPassword(getCurrentState().password, repeatPassword)?.let { error ->
            updateState {
                it.copy(
                    repeatPassword = repeatPassword,
                    repeatPasswordError = error,
                    isButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                repeatPassword = repeatPassword,
                repeatPasswordError = null,
                isButtonDisabled = it.emailError != null || it.nameError != null || it.passwordError != null ||
                it.email.isEmpty() || it.name.isEmpty() || it.password.isEmpty() || it.repeatPassword.isEmpty(),
            )
        }
    }

    private suspend fun RegisterInputScope.handleSetPassword(password: String) {
        inputValidator.validatePassword(password)?.let { error ->
            updateState {
                it.copy(
                    password = password,
                    passwordError = error,
                    isButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                password = password,
                passwordError = null,
                isButtonDisabled = it.emailError != null || it.nameError != null || it.repeatPasswordError != null ||
                    it.email.isEmpty() || it.name.isEmpty() || it.password.isEmpty() || it.repeatPassword.isEmpty(),
            )
        }
    }

    private suspend fun RegisterInputScope.handleSetEmail(email: String) {
        inputValidator.validateEmail(email)?.let { error ->
            updateState {
                it.copy(
                    email = email,
                    emailError = error,
                    isButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                email = email,
                emailError = null,
                isButtonDisabled = it.nameError != null || it.passwordError != null || it.repeatPasswordError != null ||
                    it.email.isEmpty() || it.name.isEmpty() || it.password.isEmpty() || it.repeatPassword.isEmpty(),
            )
        }
    }

    private suspend fun RegisterInputScope.handleSetName(name: String) {
        inputValidator.validateName(name)?.let { error ->
            updateState {
                it.copy(
                    name = name,
                    nameError = error,
                    isButtonDisabled = true,
                )
            }
        } ?: updateState {
            it.copy(
                name = name,
                nameError = null,
                isButtonDisabled = it.emailError != null || it.passwordError != null || it.repeatPasswordError != null ||
                    it.email.isEmpty() || it.name.isEmpty() || it.password.isEmpty() || it.repeatPassword.isEmpty(),
            )
        }
    }

    private suspend fun RegisterInputScope.handleRegisterClick() {
        val state = getCurrentState()
        sideJob("handleRegistration") {
            postInput(RegisterContract.Inputs.SetIsLoading(true))
            authService.register(state.email, state.password).fold(
                onSuccess = { postEvent(RegisterContract.Events.OnAuthenticated) },
                onFailure = { postEvent(RegisterContract.Events.OnError(it.message ?: "Registration failed")) },
            )
            postInput(RegisterContract.Inputs.SetIsLoading(false))
        }
    }
}
