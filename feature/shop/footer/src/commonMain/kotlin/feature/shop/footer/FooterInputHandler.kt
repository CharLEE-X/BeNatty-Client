package feature.shop.footer

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.AuthService
import data.type.Role
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope =
    InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>

internal class FooterInputHandler :
    KoinComponent,
    InputHandler<FooterContract.Inputs, FooterContract.Events, FooterContract.State> {

    private val inputValidator by inject<InputValidator>()
    private val authService by inject<AuthService>()

    override suspend fun InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>.handleInput(
        input: FooterContract.Inputs,
    ) = when (input) {
        FooterContract.Inputs.Init -> handleInit()
        is FooterContract.Inputs.SetEmail -> handleSetEmail(input.email)
        FooterContract.Inputs.OnEmailSend -> handleOnEmailSend()
        FooterContract.Inputs.OnAccessibilityClicked -> postEvent(FooterContract.Events.GoToAccessibility)
        FooterContract.Inputs.OnPrivacyPolicyClicked -> postEvent(FooterContract.Events.GoToPrivacyPolicy)
        FooterContract.Inputs.OnTermsOfServiceClicked -> postEvent(FooterContract.Events.GoToTermsOfService)
        FooterContract.Inputs.OnAboutUsClick -> postEvent(FooterContract.Events.GoToAboutUs)
        FooterContract.Inputs.OnCareerClick -> postEvent(FooterContract.Events.GoToCareer)
        FooterContract.Inputs.OnContactUsClick -> postEvent(FooterContract.Events.GoToContactUs)
        FooterContract.Inputs.OnCyberSecurityClick -> postEvent(FooterContract.Events.GoToCyberSecurity)
        FooterContract.Inputs.OnFAQsClick -> postEvent(FooterContract.Events.GoToFAQs)
        FooterContract.Inputs.OnPressClick -> postEvent(FooterContract.Events.GoToPress)
        FooterContract.Inputs.OnReturnsClick -> postEvent(FooterContract.Events.GoToReturns)
        FooterContract.Inputs.OnShippingClick -> postEvent(FooterContract.Events.GoToShipping)
        FooterContract.Inputs.OnTrackOrderClick -> postEvent(FooterContract.Events.GoToTrackOrder)
        FooterContract.Inputs.OnGoToAdminHome -> postEvent(FooterContract.Events.GoToAdminHome)
        FooterContract.Inputs.OnCurrencyClick -> noOp()
        FooterContract.Inputs.OnLanguageClick -> noOp()
        is FooterContract.Inputs.OnEmailChange -> updateState { it.copy(email = input.email) }
    }

    private suspend fun InputScope.handleInit() {
        val userRole = authService.userRole
        updateState {
            it.copy(
                isAdmin = userRole == Role.Admin,
            )
        }
    }

    private fun InputScope.handleOnEmailSend() {
        noOp()
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        inputValidator.validateEmail(email)?.let { error ->
            updateState {
                it.copy(
                    email = email,
                    emailError = error,
                )
            }
        } ?: updateState {
            it.copy(
                email = email,
                emailError = null,
            )
        }
    }
}
