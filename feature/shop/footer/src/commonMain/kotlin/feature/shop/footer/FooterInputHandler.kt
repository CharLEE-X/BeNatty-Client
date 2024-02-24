package feature.shop.footer

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import data.type.Role
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope =
    InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>

internal class FooterInputHandler :
    KoinComponent,
    InputHandler<FooterContract.Inputs, FooterContract.Events, FooterContract.State> {

    private val authService by inject<AuthService>()

    override suspend fun InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>.handleInput(
        input: FooterContract.Inputs,
    ) = when (input) {
        FooterContract.Inputs.Init -> handleInit()
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
        FooterContract.Inputs.OnCompanyNameClick -> postEvent(FooterContract.Events.GoToCompanyWebsite)
        FooterContract.Inputs.OnCurrencyClick -> noOp()
        FooterContract.Inputs.OnLanguageClick -> noOp()
    }

    private suspend fun InputScope.handleInit() {
        val userRole = authService.userRole
        updateState {
            it.copy(
                isAdmin = userRole == Role.Admin,
            )
        }
    }
}
