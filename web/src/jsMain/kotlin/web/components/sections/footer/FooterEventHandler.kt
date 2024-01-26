package web.components.sections.footer

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias FooterEventHandlerScope =
    EventHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>

internal class FooterEventHandler(
    private val onError: suspend (String) -> Unit,
    private val goToAboutUs: () -> Unit,
    private val goToAccessibility: () -> Unit,
    private val goToCareer: () -> Unit,
    private val goToContactUs: () -> Unit,
    private val goToCyberSecurity: () -> Unit,
    private val goToFAQs: () -> Unit,
    private val goToPress: () -> Unit,
    private val goToPrivacyPolicy: () -> Unit,
    private val goToReturns: () -> Unit,
    private val goToShipping: () -> Unit,
    private val goToTermsOfService: () -> Unit,
    private val goToTrackOrder: () -> Unit,
) : KoinComponent, EventHandler<FooterContract.Inputs, FooterContract.Events, FooterContract.State> {
    override suspend fun FooterEventHandlerScope.handleEvent(event: FooterContract.Events, ) = when (event) {
        is FooterContract.Events.OnError -> onError(event.message)
        FooterContract.Events.GoToAboutUs -> goToAboutUs()
        FooterContract.Events.GoToAccessibility -> goToAccessibility()
        FooterContract.Events.GoToCareer -> goToCareer()
        FooterContract.Events.GoToContactUs -> goToContactUs()
        FooterContract.Events.GoToCyberSecurity -> goToCyberSecurity()
        FooterContract.Events.GoToFAQs -> goToFAQs()
        FooterContract.Events.GoToPress -> goToPress()
        FooterContract.Events.GoToPrivacyPolicy -> goToPrivacyPolicy()
        FooterContract.Events.GoToReturns -> goToReturns()
        FooterContract.Events.GoToShipping -> goToShipping()
        FooterContract.Events.GoToTermsOfService -> goToTermsOfService()
        FooterContract.Events.GoToTrackOrder -> goToTrackOrder()
    }
}
