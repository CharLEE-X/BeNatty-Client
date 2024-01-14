package feature.login

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias LoginInputScope =
    InputHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>

internal class LoginInputHandler :
    KoinComponent,
    InputHandler<LoginContract.Inputs, LoginContract.Events, LoginContract.State> {

    private val authService: AuthService by inject()

    override suspend fun InputHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>.handleInput(
        input: LoginContract.Inputs,
    ) = when (input) {
        is LoginContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is LoginContract.Inputs.SetLogin -> updateState { it.copy(login = input.login) }
        is LoginContract.Inputs.SetPassword -> updateState { it.copy(password = input.password) }
        is LoginContract.Inputs.SetRepeatPassword -> updateState { it.copy(repeatPassword = input.repeatPassword) }
        is LoginContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }
        LoginContract.Inputs.TogglePasswordVisibility ->
            updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }

        LoginContract.Inputs.ToggleRepeatPasswordVisibility ->
            updateState { it.copy(isRepeatPasswordVisible = !it.isRepeatPasswordVisible) }

        LoginContract.Inputs.Login -> handleLogin()
        LoginContract.Inputs.Register -> handleRegistration()
        LoginContract.Inputs.ForgotPassword -> handleForgotPassword()
    }

    private suspend fun LoginInputScope.handleLogin() {
        if (!validateEmail()) {
            postEvent(LoginContract.Events.OnError("Invalid email"))
            return
        }
        val state = getCurrentState()
        sideJob("handleLogin") {
            authService.login(state.login, state.password)
                .onSuccess { postEvent(LoginContract.Events.OnAuthenticated) }
                .onFailure { postEvent(LoginContract.Events.OnError(it.message ?: "Login failed")) }
        }
    }

    private suspend fun LoginInputScope.handleRegistration() {
        if (!validateEmail()) {
            postEvent(LoginContract.Events.OnError("Invalid email"))
            return
        }
        if (!validatePasswords()) {
            postEvent(LoginContract.Events.OnError("Passwords don't match"))
            return
        }
        val state = getCurrentState()
        sideJob("handleRegistration") {
            authService.register(state.login, state.password)
                .onSuccess { postEvent(LoginContract.Events.OnAuthenticated) }
                .onFailure { postEvent(LoginContract.Events.OnError(it.message ?: "Registration failed")) }
        }
    }

    private suspend fun LoginInputScope.handleForgotPassword() {
        if (!validateEmail()) {
            postEvent(LoginContract.Events.OnError("Invalid email"))
            return
        }
        val state = getCurrentState()
        sideJob("handleForgotPassword") {
            authService.forgotPassword(state.login)
                .onSuccess { postEvent(LoginContract.Events.OnAuthenticated) }
                .onFailure { postEvent(LoginContract.Events.OnError(it.message ?: "Forgot password failed")) }
        }
    }

    private suspend fun LoginInputScope.validatePasswords(): Boolean {
        // todo validate strength of the password ?
        val state = getCurrentState()
        return if (state.password == state.repeatPassword) {
            true
        } else {
            postEvent(LoginContract.Events.OnError("Passwords don't match"))
            false
        }
    }

    private suspend fun LoginInputScope.validateEmail(): Boolean {
        // TODO("Not yet implemented")
        return true
    }
}
