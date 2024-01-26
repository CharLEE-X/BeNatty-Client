package web.components.sections.footer

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.PrintlnLogger
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope

class FooterViewModel(
    scope: CoroutineScope,
    onError: suspend (String) -> Unit,
    goToAboutUs: () -> Unit,
    goToAccessibility: () -> Unit,
    goToCareer: () -> Unit,
    goToContactUs: () -> Unit,
    goToCyberSecurity: () -> Unit,
    goToFAQs: () -> Unit,
    goToPress: () -> Unit,
    goToPrivacyPolicy: () -> Unit,
    goToReturns: () -> Unit,
    goToShipping: () -> Unit,
    goToTermsOfService: () -> Unit,
    goToTrackOrder: () -> Unit,
) : BasicViewModel<
    FooterContract.Inputs,
    FooterContract.Events,
    FooterContract.State,
    >(
    config = BallastViewModelConfiguration.Builder()
        .apply {
//            this += LoggingInterceptor()
            logger = { PrintlnLogger() }
        }
        .withViewModel(
            initialState = FooterContract.State(),
            inputHandler = FooterInputHandler(),
            name = TAG,
        )
        .build(),
    eventHandler = FooterEventHandler(
        onError = onError,
        goToAboutUs = goToAboutUs,
        goToAccessibility = goToAccessibility,
        goToCareer = goToCareer,
        goToContactUs = goToContactUs,
        goToCyberSecurity = goToCyberSecurity,
        goToFAQs = goToFAQs,
        goToPress = goToPress,
        goToPrivacyPolicy = goToPrivacyPolicy,
        goToReturns = goToReturns,
        goToShipping = goToShipping,
        goToTermsOfService = goToTermsOfService,
        goToTrackOrder = goToTrackOrder,
    ),
    coroutineScope = scope,
) {
    companion object {
        private val TAG = this::class.simpleName!!
    }
}
