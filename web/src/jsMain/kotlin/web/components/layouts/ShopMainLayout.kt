package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import feature.shop.footer.FooterRoutes
import feature.shop.navbar.DesktopNavRoutes
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.components.sections.desktopNav.DesktopNav
import web.components.sections.desktopNav.ScrollDirection
import web.components.sections.footer.Footer
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
                        leftRight = 20.px,
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

@Composable
fun Background() {
    var lastScrollPosition by remember { mutableStateOf(0.0) }
    var scrollDirection: ScrollDirection by remember { mutableStateOf(ScrollDirection.DOWN) }

    var box1Offset by remember { mutableStateOf(0.px) }
    var box2Offset by remember { mutableStateOf(0.px) }

    window.addEventListener("scroll", {
        val currentScroll = window.scrollY
        scrollDirection = if (lastScrollPosition < currentScroll) ScrollDirection.DOWN else ScrollDirection.UP
        lastScrollPosition = currentScroll

        box1Offset = if (scrollDirection == ScrollDirection.DOWN) box1Offset - 2.px else box1Offset + 2.px
        box2Offset = if (scrollDirection == ScrollDirection.DOWN) box2Offset - 2.px else box2Offset + 2.px
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .translate(
                    tx = (-50).percent + box1Offset,
                    ty = (-50).percent + box1Offset,
                )
                .shadowModifier(MaterialTheme.colors.surfaceContainerHigh)
                .transition(CSSTransition("translate", 2.s, TransitionTimingFunction.EaseInOut))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .translate(
                    tx = (-50).percent + box2Offset,
                    ty = (50).percent + box2Offset,
                )
                .shadowModifier(MaterialTheme.colors.tertiaryContainer.toRgb().copy(alpha = 50))
                .transition(CSSTransition("translate", 3.s, TransitionTimingFunction.EaseInOut))
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .translate(
                    tx = (50).percent + box2Offset,
                    ty = (50).percent + box2Offset,
                )
                .shadowModifier(MaterialTheme.colors.surfaceContainerHighest.toRgb().copy(alpha = 100))
                .transition(CSSTransition("translate", 2.s, TransitionTimingFunction.EaseInOut))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .translate(
                    tx = (50).percent + box2Offset,
                    ty = (-50).percent + box2Offset,
                )
                .shadowModifier(MaterialTheme.colors.primaryContainer.toRgb().copy(alpha = 50))
                .transition(CSSTransition("translate", 3.5.s, TransitionTimingFunction.EaseInOut))
        )
    }
}

private fun Modifier.shadowModifier(
    color: CSSColorValue,
    offsetX: CSSLengthNumericValue = 0.px,
    offsetY: CSSLengthNumericValue = 0.px,
) = this
    .size(800.px)
    .backgroundColor(color)
    .borderRadius(50.percent)
    .boxShadow(
        offsetX = offsetX,
        offsetY = offsetY,
        color = color,
        blurRadius = 80.px,
        spreadRadius = 100.px,
    )
    .transition(CSSTransition("box-shadow", 0.7.s, TransitionTimingFunction.EaseInOut))
