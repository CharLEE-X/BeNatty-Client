package web.components.sections.footer

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias FooterInput =
    InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>

internal class FooterInputHandler :
    KoinComponent,
    InputHandler<FooterContract.Inputs, FooterContract.Events, FooterContract.State> {

    private val inputValidator by inject<InputValidator>()

    override suspend fun InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>.handleInput(
        input: FooterContract.Inputs,
    ) = when (input) {
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
        FooterContract.Inputs.OnCurrencyClick -> noOp()
        FooterContract.Inputs.OnLanguageClick -> noOp()
    }

    private fun FooterInput.handleOnEmailSend() {
        // TODO: implement
        noOp()
    }

    private suspend fun FooterInput.handleSetEmail(email: String) {
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
