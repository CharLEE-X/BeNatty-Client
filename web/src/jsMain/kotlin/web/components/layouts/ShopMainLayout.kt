package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import feature.shop.footer.FooterRoutes
import feature.shop.navbar.DesktopNavRoutes
import kotlinx.browser.document
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
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
    var showCartSidebar by remember { mutableStateOf(false) }

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
        showCartSidebar = { showCartSidebar = true },
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

    var topSpacing: CSSSizeValue<CSSUnit.px> by remember { mutableStateOf(0.px) }

    PageLayout(
        title = title,
        topSpacing = topSpacing + 60.px,
        topBar = {
            DesktopNav(
                desktopNavRoutes = desktopNavRoutes,
                onError = mainParams.onError,
                onTopSpacingChanged = { topSpacing = it }
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
    topSpacing: CSSSizeValue<CSSUnit.px>,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topSpacing)
                .transition(CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            topBar()
            content()
            footer()
        }
    }
}
