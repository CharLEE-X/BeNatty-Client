package feature.shop.footer

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import org.koin.core.component.KoinComponent

private typealias FooterEventHandlerScope =
    EventHandlerScope<FooterContract.Inputs, FooterContract.Events, FooterContract.State>

internal class FooterEventHandler(
    private val footerRoutes: FooterRoutes,
    private val onError: suspend (String) -> Unit,
    private val goToCompanyWebsite: (String) -> Unit,
) : KoinComponent, EventHandler<FooterContract.Inputs, FooterContract.Events, FooterContract.State> {
    override suspend fun FooterEventHandlerScope.handleEvent(event: FooterContract.Events) = when (event) {
        is FooterContract.Events.OnError -> onError(event.message)
        FooterContract.Events.GoToAboutUs -> footerRoutes.goToAboutUs()
        FooterContract.Events.GoToAccessibility -> footerRoutes.goToAccessibility()
        FooterContract.Events.GoToCareer -> footerRoutes.goToCareer()
        FooterContract.Events.GoToContactUs -> footerRoutes.goToContactUs()
        FooterContract.Events.GoToCyberSecurity -> footerRoutes.goToCyberSecurity()
        FooterContract.Events.GoToFAQs -> footerRoutes.goToFAQs()
        FooterContract.Events.GoToPress -> footerRoutes.goToPress()
        FooterContract.Events.GoToPrivacyPolicy -> footerRoutes.goToPrivacyPolicy()
        FooterContract.Events.GoToReturns -> footerRoutes.goToReturns()
        FooterContract.Events.GoToShipping -> footerRoutes.goToShipping()
        FooterContract.Events.GoToTermsOfService -> footerRoutes.goToTermsOfService()
        FooterContract.Events.GoToTrackOrder -> footerRoutes.goToTrackOrder()
        FooterContract.Events.GoToAdminHome -> footerRoutes.goToAdminHome()
        is FooterContract.Events.GoToCompanyWebsite -> goToCompanyWebsite(event.url)
        FooterContract.Events.GoToCatalogue -> footerRoutes.goToCatalogue()
    }
}
