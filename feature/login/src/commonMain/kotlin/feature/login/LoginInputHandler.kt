package feature.login

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias LoginInputScope = InputHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>

internal class LoginInputHandler :
    KoinComponent,
    InputHandler<LoginContract.Inputs, LoginContract.Events, LoginContract.State> {

    private val authService: AuthService by inject()

    override suspend fun InputHandlerScope<LoginContract.Inputs, LoginContract.Events, LoginContract.State>.handleInput(
        input: LoginContract.Inputs,
    ) = when (input) {
        is LoginContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is LoginContract.Inputs.SetName -> updateState { it.copy(name = input.name) }
        is LoginContract.Inputs.SetEmail -> updateState { it.copy(email = input.email) }
        LoginContract.Inputs.ToggleNewsletterChecked -> updateState { it.copy(newsletterChecked = !it.newsletterChecked) }
        is LoginContract.Inputs.SetPassword -> updateState { it.copy(password = input.password) }
        is LoginContract.Inputs.SetRepeatPassword -> updateState { it.copy(repeatPassword = input.repeatPassword) }
        is LoginContract.Inputs.ChangeScreenState -> handleChangeScreenState(input.screenState)
        is LoginContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }

        LoginContract.Inputs.TogglePasswordVisibility ->
            updateState { it.copy(isPasswordVisible = !it.isPasswordVisible) }

        LoginContract.Inputs.ToggleRepeatPasswordVisibility ->
            updateState { it.copy(isRepeatPasswordVisible = !it.isRepeatPasswordVisible) }

        LoginContract.Inputs.OnGoogleClick -> noOp()
        LoginContract.Inputs.OnFacebookClick -> noOp()
        LoginContract.Inputs.OnLoginRegisterActionButtonClick -> handleLoginRegisterActionButtonClick()
        LoginContract.Inputs.OnForgotPasswordClick -> handleOnForgotPasswordClick()

        LoginContract.Inputs.OnAlreadyHaveAccountClick -> handleAlreadyHaveAccountClick()

        LoginContract.Inputs.OnPrivacyPolicyClick -> handlePrivacyPolicyClick()
        LoginContract.Inputs.OnTnCClick -> handleTnCClick()
    }

    private suspend fun LoginInputScope.handleOnForgotPasswordClick() {
        postInput(LoginContract.Inputs.ChangeScreenState(LoginContract.ScreenState.FORGOT_PASSWORD))
    }

    private suspend fun LoginInputScope.handleLoginRegisterActionButtonClick() {
        when (getCurrentState().screenState) {
            LoginContract.ScreenState.FORGOT_PASSWORD -> handleForgotPassword()
            LoginContract.ScreenState.LOGIN -> handleLogin()
            LoginContract.ScreenState.REGISTER -> handleRegistration()
        }
    }

    private suspend fun LoginInputScope.handleChangeScreenState(screenState: LoginContract.ScreenState) {
        when (screenState) {
            LoginContract.ScreenState.REGISTER -> {
                updateState {
                    it.copy(
                        screenState = LoginContract.ScreenState.REGISTER,
                        showName = true,
                        name = "",
                        showPassword = true,
                        isPasswordVisible = true,
                        password = "",
                        showRepeatPassword = true,
                        repeatPassword = "",
                        isRepeatPasswordVisible = true,
                        showForgotPassword = false,
                        headerText = LoginContract.Strings.signUp,
                        showNewsletter = true,
                        newsletterChecked = false,
                        loginActionButtonText = LoginContract.Strings.signUp,
                        showAlreadyHaveAccount = true,
                        alreadyHaveAccountText = LoginContract.Strings.alreadyHaveAnAccount,
                        alreadyHaveAccountActionText = LoginContract.Strings.login,
                        showForgotPasswordDescription = false,
                        showPrivacyPolicy = true,
                    )
                }
            }

            LoginContract.ScreenState.LOGIN -> {
                updateState {
                    it.copy(
                        screenState = LoginContract.ScreenState.LOGIN,
                        showName = false,
                        name = "",
                        showPassword = true,
                        password = "",
                        isPasswordVisible = false,
                        showRepeatPassword = false,
                        repeatPassword = "",
                        isRepeatPasswordVisible = false,
                        showForgotPassword = true,
                        headerText = LoginContract.Strings.login,
                        showNewsletter = false,
                        newsletterChecked = false,
                        loginActionButtonText = LoginContract.Strings.login,
                        showAlreadyHaveAccount = true,
                        alreadyHaveAccountText = LoginContract.Strings.dontHaveAccount,
                        alreadyHaveAccountActionText = LoginContract.Strings.signUp,
                        showForgotPasswordDescription = false,
                        showPrivacyPolicy = false,
                    )
                }
            }

            LoginContract.ScreenState.FORGOT_PASSWORD -> {
                updateState {
                    it.copy(
                        screenState = LoginContract.ScreenState.FORGOT_PASSWORD,
                        showName = false,
                        name = "",
                        showPassword = false,
                        password = "",
                        isPasswordVisible = false,
                        showRepeatPassword = false,
                        repeatPassword = "",
                        isRepeatPasswordVisible = false,
                        showForgotPassword = false,
                        headerText = LoginContract.Strings.forgotPassword,
                        showForgotPasswordDescription = true,
                        showNewsletter = false,
                        newsletterChecked = false,
                        loginActionButtonText = LoginContract.Strings.getResetLink,
                        showAlreadyHaveAccount = false,
                        alreadyHaveAccountText = LoginContract.Strings.dontHaveAccount,
                        alreadyHaveAccountActionText = LoginContract.Strings.signUp,
                        showPrivacyPolicy = false,
                    )
                }
            }
        }
    }

    private suspend fun LoginInputScope.handleTnCClick() {
        postEvent(LoginContract.Events.GotoTnC)
    }

    private suspend fun LoginInputScope.handlePrivacyPolicyClick() {
        postEvent(LoginContract.Events.GotoPrivacyPolicy)
    }

    private suspend fun LoginInputScope.handleAlreadyHaveAccountClick() {
        when (getCurrentState().screenState) {
            LoginContract.ScreenState.FORGOT_PASSWORD, LoginContract.ScreenState.LOGIN -> {
                postInput(LoginContract.Inputs.ChangeScreenState(LoginContract.ScreenState.REGISTER))
            }

            LoginContract.ScreenState.REGISTER -> {
                postInput(LoginContract.Inputs.ChangeScreenState(LoginContract.ScreenState.LOGIN))
            }
        }
    }

    private suspend fun LoginInputScope.handleLogin() {
        if (!validateEmail()) {
            postEvent(LoginContract.Events.OnError("Invalid email"))
            return
        }
        val state = getCurrentState()
        sideJob("handleLogin") {
            authService.login(state.email, state.password)
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
            authService.register(state.email, state.password)
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
            authService.forgotPassword(state.email)
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
