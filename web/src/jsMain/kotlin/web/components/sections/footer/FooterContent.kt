package web.components.sections.footer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.GoToDestination
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.ReplaceTopDestination
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import feature.router.RouterViewModel
import feature.router.Screen
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
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = onError,
            goToAboutUs = { router.trySend(GoToDestination(Screen.About.route)) },
            goToAccessibility = { router.trySend(GoToDestination(Screen.Accessibility.route)) },
            goToCareer = { router.trySend(GoToDestination(Screen.Career.route)) },
            goToContactUs = { router.trySend(GoToDestination(Screen.Contact.route)) },
            goToCyberSecurity = { router.trySend(GoToDestination(Screen.CyberSecurity.route)) },
            goToFAQs = { router.trySend(GoToDestination(Screen.HelpAndFAQ.route)) },
            goToPress = { router.trySend(GoToDestination(Screen.Press.route)) },
            goToPrivacyPolicy = { router.trySend(GoToDestination(Screen.PrivacyPolicy.route)) },
            goToReturns = { router.trySend(GoToDestination(Screen.Returns.route)) },
            goToShipping = { router.trySend(GoToDestination(Screen.Shipping.route)) },
            goToTermsOfService = { router.trySend(GoToDestination(Screen.TC.route)) },
            goToTrackOrder = { router.trySend(GoToDestination(Screen.TrackOrder.route)) },
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
                onGoToAdminDashboard = { router.trySend(ReplaceTopDestination(Screen.AdminHome.route)) }
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
