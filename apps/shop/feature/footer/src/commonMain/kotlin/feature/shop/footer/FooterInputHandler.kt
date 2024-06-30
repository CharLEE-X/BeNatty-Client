package feature.shop.footer

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AuthService
import data.service.ConfigService
import data.type.Role
import feature.shop.footer.FooterContract.Events
import feature.shop.footer.FooterContract.Inputs
import feature.shop.footer.FooterContract.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<Inputs, Events, State>

internal class FooterInputHandler : KoinComponent, InputHandler<Inputs, Events, State> {
    private val authService: AuthService by inject()
    private val configService: ConfigService by inject()

    override suspend fun InputHandlerScope<Inputs, Events, State>.handleInput(input: Inputs) = when (input) {
        Inputs.Init -> handleInit()
        Inputs.GetConfig -> handleFetchConfig()
        Inputs.CheckUserRole -> handleCheckUserRole()

        Inputs.OnAccessibilityClicked -> postEvent(Events.GoToAccessibility)
        Inputs.OnPrivacyPolicyClicked -> postEvent(Events.GoToPrivacyPolicy)
        Inputs.OnTermsOfServiceClicked -> postEvent(Events.GoToTermsOfService)
        Inputs.OnAboutUsClick -> postEvent(Events.GoToAboutUs)
        Inputs.OnCareerClick -> postEvent(Events.GoToCareer)
        Inputs.OnContactUsClick -> postEvent(Events.GoToContactUs)
        Inputs.OnCyberSecurityClick -> postEvent(Events.GoToCyberSecurity)
        Inputs.OnFAQsClick -> postEvent(Events.GoToFAQs)
        Inputs.OnPressClick -> postEvent(Events.GoToPress)
        Inputs.OnReturnsClick -> postEvent(Events.GoToReturns)
        Inputs.OnShippingClick -> postEvent(Events.GoToShipping)
        Inputs.OnTrackOrderClick -> postEvent(Events.GoToTrackOrder)
        Inputs.OnGoToAdminHome -> noOp()
        Inputs.OnCompanyNameClick -> handleCompanyNameClick()
        Inputs.OnCurrencyClick -> noOp()
        Inputs.OnLanguageClick -> noOp()
        Inputs.OnTickerClick -> postEvent(Events.GoToCatalogue)
        Inputs.OnConnectEmailSend -> noOp()

        is Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is Inputs.SetCompanyInfo -> updateState { it.copy(companyInfo = input.companyInfo) }
        is Inputs.SetFooterConfig -> updateState { it.copy(footerConfig = input.footerConfig) }
        is Inputs.SetConnectEmail -> updateState { it.copy(connectEmail = input.email) }
    }

    private suspend fun InputScope.handleCheckUserRole() {
        val userRole = authService.userRole
        updateState {
            it.copy(
                isAdmin = userRole == Role.Admin,
            )
        }
    }

    private suspend fun InputScope.handleFetchConfig() {
        sideJob("handleFetchConfig") {
            configService.fetchConfig().fold(
                { postEvent(Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(Inputs.SetCompanyInfo(companyInfo = it.getConfig.companyInfo))
                    postInput(Inputs.SetFooterConfig(footerConfig = it.getConfig.footerConfig))
                },
            )
        }
    }

    private suspend fun InputScope.handleCompanyNameClick() {
        getCurrentState().companyInfo?.contactInfo?.companyWebsite?.let { url ->
            postEvent(Events.GoToCompanyWebsite(url))
        } ?: noOp()
    }

    private suspend fun InputScope.handleInit() {
        sideJob("InitFooter") {
            postInput(Inputs.SetIsLoading(isLoading = true))
            postInput(Inputs.CheckUserRole)
            postInput(Inputs.GetConfig)
            postInput(Inputs.SetIsLoading(isLoading = false))
        }
    }
}
