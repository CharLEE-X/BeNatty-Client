package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
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
import web.components.widgets.Background
import web.util.sectionsGap

data class MainRoutes(
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
    val goToProduct: (String) -> Unit,
)

@Composable
fun ShopMainLayout(
    title: String,
    mainRoutes: MainRoutes,
    spacing: CSSLengthOrPercentageNumericValue = sectionsGap,
    content: @Composable ColumnScope.() -> Unit,
) {
    var showCartSidebar by remember { mutableStateOf(false) }

    val desktopNavRoutes = DesktopNavRoutes(
        goToHome = mainRoutes.goToHome,
        goToLogin = mainRoutes.goToLogin,
        goToOrders = mainRoutes.goToOrders,
        goToProfile = mainRoutes.goToProfile,
        goToReturns = mainRoutes.goToReturns,
        goToWishlist = mainRoutes.goToWishlist,
        goToHelpAndFaq = mainRoutes.goToHelpAndFaq,
        goToCatalogue = mainRoutes.goToCatalogue,
        goToAbout = mainRoutes.goToAboutUs,
        goToShippingAndReturns = mainRoutes.goToShipping, // FIXME: Change to 'ShippingAndReturns'
        showCartSidebar = { showCartSidebar = true },
    )
    val footerRoutes = FooterRoutes(
        goToAboutUs = mainRoutes.goToAboutUs,
        goToAccessibility = mainRoutes.goToAccessibility,
        goToCareer = mainRoutes.goToCareer,
        goToContactUs = mainRoutes.goToContactUs,
        goToCyberSecurity = mainRoutes.goToCyberSecurity,
        goToFAQs = mainRoutes.goToFAQs,
        goToPress = mainRoutes.goToPress,
        goToPrivacyPolicy = mainRoutes.goToPrivacyPolicy,
        goToShipping = mainRoutes.goToShipping,
        goToTermsOfService = mainRoutes.goToTermsOfService,
        goToTrackOrder = mainRoutes.goToTrackOrder,
        goToAdminHome = mainRoutes.goToAdminHome,
        goToReturns = mainRoutes.goToReturns,
        goToCatalogue = mainRoutes.goToCatalogue,
    )

    var topSpacing: CSSSizeValue<CSSUnit.px> by remember { mutableStateOf(0.px) }

    LaunchedEffect(title) {
        document.title = "BeNatty - $title"
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(100.percent),
    ) {
        Background()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topSpacing + 60.px)
                .transition(CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            DesktopNav(
                desktopNavRoutes = desktopNavRoutes,
                onError = mainRoutes.onError,
                onTopSpacingChanged = { topSpacing = it }
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .maxWidth(oneLayoutMaxWidth)
                    .padding(
                        top = 100.px,
                        bottom = spacing
                    )
                    .gap(spacing)
            ) {
                content()
            }
            Footer(
                onError = mainRoutes.onError,
                footerRoutes = footerRoutes,
            )
        }
    }
}
