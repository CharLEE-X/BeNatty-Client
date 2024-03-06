package feature.shop.home

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.GetLandingConfigQuery
import data.service.AuthService
import data.service.ConfigService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>

internal class HomeInputHandler :
    KoinComponent,
    InputHandler<HomeContract.Inputs, HomeContract.Events, HomeContract.State> {

    private val inputValidator by inject<InputValidator>()
    private val authService: AuthService by inject()
    private val configService: ConfigService by inject()

    override suspend fun InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleInput(
        input: HomeContract.Inputs,
    ) = when (input) {
        HomeContract.Inputs.Init -> handleInit()
        is HomeContract.Inputs.FetchLandingConfig -> handleFetchLandingConfig()
        is HomeContract.Inputs.FetchProducts -> handleFetchProducts()

        is HomeContract.Inputs.OnCollageItemClick -> handleCollageItemClick(input.item)
        HomeContract.Inputs.OnEmailSend -> handleOnEmailSend()
        is HomeContract.Inputs.OnEmailChange -> updateState { it.copy(email = input.email) }
        HomeContract.Inputs.OnPrivacyPolicyClick -> postEvent(HomeContract.Events.GoToPrivacyPolicy)
        HomeContract.Inputs.OnTermsOfServiceClick -> postEvent(HomeContract.Events.GoToTermsOfService)

        is HomeContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is HomeContract.Inputs.SetLandingConfig -> updateState { it.copy(landingConfig = input.landingConfig) }
        is HomeContract.Inputs.SetProducts -> updateState { it.copy(products = input.products) }
        is HomeContract.Inputs.SetEmail -> handleSetEmail(input.email)
        HomeContract.Inputs.OnBannerLeftClick -> postEvent(HomeContract.Events.GoToCatalogue)
        HomeContract.Inputs.OnBannerRightClick -> postEvent(HomeContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleCollageItemClick(item: GetLandingConfigQuery.CollageItem) {
        postEvent(HomeContract.Events.GoToCatalogue)
    }

    private suspend fun InputScope.handleFetchLandingConfig() {
        sideJob("handleFetchHomeConfig") {
            configService.getLandingConfig().fold(
                onSuccess = { config ->
                    postInput(HomeContract.Inputs.SetLandingConfig(config.getLandingConfig))
                },
                onFailure = { error ->
                    postEvent(HomeContract.Events.OnError(error.message ?: "Unknown error"))
                }
            )
        }
    }

    private suspend fun InputScope.handleFetchProducts() {
        noOp()
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(HomeContract.Inputs.SetIsLoading(isLoading = true))
            postInput(HomeContract.Inputs.FetchLandingConfig)
            postInput(HomeContract.Inputs.FetchProducts)
            delay(5000)
            postInput(HomeContract.Inputs.SetIsLoading(isLoading = false))
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
