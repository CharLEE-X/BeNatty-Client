package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import feature.shop.footer.FooterRoutes
import feature.shop.navbar.DesktopNavRoutes
import kotlinx.browser.document
import org.jetbrains.compose.web.css.percent
import web.components.sections.desktopNav.DesktopNav
import web.components.sections.footer.Footer

data class MainParams(
    val goToHome: () -> Unit,
    val goToLogin: () -> Unit,
    val goToOrders: () -> Unit,
    val goToProfile: () -> Unit,
    val goToReturns: () -> Unit,
    val goToWishlist: () -> Unit,
    val goToHelpAndFaq: () -> Unit,
    val goToCatalogue: () -> Unit,
    val goToAboutUs: () -> Unit,
    val goToAccessibility: () -> Unit,
    val goToCareer: () -> Unit,
    val goToContactUs: () -> Unit,
    val goToCyberSecurity: () -> Unit,
    val goToFAQs: () -> Unit,
    val goToPress: () -> Unit,
    val goToPrivacyPolicy: () -> Unit,
    val goToShipping: () -> Unit,
    val goToTermsOfService: () -> Unit,
    val goToTrackOrder: () -> Unit,
    val goToAdminHome: () -> Unit,
    val onError: suspend (String) -> Unit,
)

@Composable
fun ShopMainLayout(
    title: String,
    mainParams: MainParams,
    content: @Composable ColumnScope.() -> Unit,
) {
    val desktopNavRoutes = DesktopNavRoutes(
        goToHome = mainParams.goToHome,
        goToLogin = mainParams.goToLogin,
        goToOrders = mainParams.goToOrders,
        goToProfile = mainParams.goToProfile,
        goToReturns = mainParams.goToReturns,
        goToWishlist = mainParams.goToWishlist,
        goToHelpAndFaq = mainParams.goToHelpAndFaq,
        goToCatalogue = mainParams.goToCatalogue,
        goToAbout = mainParams.goToAboutUs,
        goToShippingAndReturns = mainParams.goToShipping, // FIXME: Change to 'ShippingAndReturns'
    )
    val footerRoutes = FooterRoutes(
        goToAboutUs = mainParams.goToAboutUs,
        goToAccessibility = mainParams.goToAccessibility,
        goToCareer = mainParams.goToCareer,
        goToContactUs = mainParams.goToContactUs,
        goToCyberSecurity = mainParams.goToCyberSecurity,
        goToFAQs = mainParams.goToFAQs,
        goToPress = mainParams.goToPress,
        goToPrivacyPolicy = mainParams.goToPrivacyPolicy,
        goToShipping = mainParams.goToShipping,
        goToTermsOfService = mainParams.goToTermsOfService,
        goToTrackOrder = mainParams.goToTrackOrder,
        goToAdminHome = mainParams.goToAdminHome,
        goToReturns = mainParams.goToReturns,
    )

    PageLayout(
        title = title,
        topBar = {
            DesktopNav(
                desktopNavRoutes = desktopNavRoutes,
                onError = mainParams.onError,
            )
        },
        footer = {
            Footer(
                onError = mainParams.onError,
                footerRoutes = footerRoutes,
            )
        },
        content = content
    )
}

@Composable
private fun PageLayout(
    title: String,
    topBar: @Composable ColumnScope.() -> Unit,
    footer: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    LaunchedEffect(title) {
        document.title = "BeNatty - $title"
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(100.percent),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            topBar()
            content()
            footer()
        }
    }
}
