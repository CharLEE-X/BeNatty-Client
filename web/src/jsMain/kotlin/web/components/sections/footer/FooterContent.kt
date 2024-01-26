package web.components.sections.footer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import feature.router.RouterScreen
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.sections.desktopNav.RoundedBgSeparator
import web.components.sections.footer.sections.FooterAboutUs
import web.components.sections.footer.sections.FooterBottomSection
import web.components.sections.footer.sections.FooterDeliverTo
import web.components.sections.footer.sections.FooterHelp
import web.components.sections.footer.sections.FooterSubscribe
import web.compose.material3.component.Divider

@Composable
fun Footer(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    onGoToAdminDashboard: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = onError,
            goToAboutUs = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.About.matcher.routeFormat))
            },
            goToAccessibility = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Accessibility.matcher.routeFormat))
            },
            goToCareer = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Career.matcher.routeFormat))
            },
            goToContactUs = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Contact.matcher.routeFormat))
            },
            goToCyberSecurity = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.CyberSecurity.matcher.routeFormat))
            },
            goToFAQs = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.HelpAndFAQ.matcher.routeFormat))
            },
            goToPress = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Press.matcher.routeFormat))
            },
            goToPrivacyPolicy = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.PrivacyPolicy.matcher.routeFormat))
            },
            goToReturns = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Returns.matcher.routeFormat))
            },
            goToShipping = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.Shipping.matcher.routeFormat))
            },
            goToTermsOfService = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.TC.matcher.routeFormat))
            },
            goToTrackOrder = {
                router.trySend(RouterContract.Inputs.GoToDestination(RouterScreen.TrackOrder.matcher.routeFormat))
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainer.value())
            .margin(top = 2.em)
    ) {
        RoundedBgSeparator(
            fromColor = MaterialTheme.colors.mdSysColorSurface.value(),
            toColor = MaterialTheme.colors.mdSysColorSurfaceContainer.value(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.5.cssRem, bottom = 2.5.em, leftRight = 3.cssRem)
        ) {
            FooterSubscribe(
                vm = vm,
                state = state,
                modifier = Modifier.weight(1f)
            )
            FooterHelp(
                vm = vm,
                state = state,
                modifier = Modifier.weight(1f)
            )
            FooterAboutUs(
                vm = vm,
                state = state,
                modifier = Modifier.weight(1f),
                onGoToAdminDashboard = onGoToAdminDashboard,
            )
            FooterDeliverTo(
                vm = vm,
                state = state,
                modifier = Modifier.weight(1f)
            )
        }
        Divider()
        FooterBottomSection(vm, state)
    }
}
