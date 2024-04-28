package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
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
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import feature.shop.cart.CartViewModel
import feature.shop.footer.FooterRoutes
import feature.shop.footer.FooterViewModel
import feature.shop.navbar.DesktopNavRoutes
import feature.shop.navbar.NavbarViewModel
import kotlinx.browser.document
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import web.components.sections.CartPanel
import web.components.sections.DesktopNavContent
import web.components.sections.Footer
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
    val goToCheckout: () -> Unit,
)

data class GlobalVMs(
    val cartVm: CartViewModel,
    val navbarVm: NavbarViewModel,
    val footerVM: FooterViewModel,
)

@Composable
fun ShopMainLayout(
    title: String,
    globalVMs: GlobalVMs,
    mainRoutes: MainRoutes,
    isFullLayout: Boolean = true,
    desktopNavRoutes: DesktopNavRoutes,
    footerRoutes: FooterRoutes,
    spacing: CSSLengthOrPercentageNumericValue = sectionsGap,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val cartState by globalVMs.cartVm.observeStates().collectAsState()

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topSpacing + 60.px)
                .transition(CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            DesktopNavContent(
                desktopNavRoutes = desktopNavRoutes,
                globalVMs = globalVMs,
                onError = mainRoutes.onError,
                onTopSpacingChanged = { topSpacing = it },
                isFullLayout = isFullLayout,
                modifier = Modifier.zIndex(10),
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
                isFullLayout = isFullLayout
            )
        }
        CartPanel(
            vm = globalVMs.cartVm,
            state = cartState,
            zIndex = 100,
        )
        Span(Modifier.zIndex(2000).toAttrs()) {
            overlay()
        }
    }
}
