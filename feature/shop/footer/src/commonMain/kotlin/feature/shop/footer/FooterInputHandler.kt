package feature.shop.footer

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import data.service.AuthService
import data.service.ConfigService
import data.type.Role
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope =
    InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>

internal class FooterInputHandler :
    KoinComponent,
    InputHandler<FooterContract.Inputs, FooterContract.Events, FooterContract.State> {

    private val authService: AuthService by inject()
    private val configService: ConfigService by inject()

    override suspend fun InputHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>.handleInput(
        input: FooterContract.Inputs,
    ) = when (input) {
        FooterContract.Inputs.Init -> handleInit()
        FooterContract.Inputs.GetConfig -> handleFetchConfig()
        FooterContract.Inputs.CheckUserRole -> handleCheckUserRole()

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
        FooterContract.Inputs.OnCompanyNameClick -> handleCompanyNameClick()
        FooterContract.Inputs.OnCurrencyClick -> noOp()
        FooterContract.Inputs.OnLanguageClick -> noOp()
        FooterContract.Inputs.OnTickerClick -> postEvent(FooterContract.Events.GoToCatalogue)

        is FooterContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is FooterContract.Inputs.SetCompanyInfo -> updateState { it.copy(companyInfo = input.companyInfo) }
        is FooterContract.Inputs.SetFooterConfig -> updateState { it.copy(footerConfig = input.footerConfig) }
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
                { postEvent(FooterContract.Events.OnError(it.mapToUiMessage())) },
                {
                    postInput(FooterContract.Inputs.SetCompanyInfo(companyInfo = it.getConfig.companyInfo))
                    postInput(FooterContract.Inputs.SetFooterConfig(footerConfig = it.getConfig.footerConfig))
                },
            )
        }
    }

    private suspend fun InputScope.handleCompanyNameClick() {
        getCurrentState().companyInfo?.contactInfo?.companyWebsite?.let { url ->
            postEvent(FooterContract.Events.GoToCompanyWebsite(url))
        } ?: noOp()
    }

    private suspend fun InputScope.handleInit() {
        sideJob("InitFooter") {
            postInput(FooterContract.Inputs.SetIsLoading(isLoading = true))
            postInput(FooterContract.Inputs.CheckUserRole)
            postInput(FooterContract.Inputs.GetConfig)
            postInput(FooterContract.Inputs.SetIsLoading(isLoading = false))
        }
    }
}
