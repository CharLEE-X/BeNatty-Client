package web.components.sections.footer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import feature.shop.footer.FooterContract
import feature.shop.footer.FooterRoutes
import feature.shop.footer.FooterViewModel
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.sections.footer.sections.FooterAboutUs
import web.components.sections.footer.sections.FooterBottomSection
import web.components.sections.footer.sections.FooterDeliverTo
import web.components.sections.footer.sections.FooterHelp
import web.components.sections.footer.sections.FooterSubscribe
import web.compose.material3.component.Divider

@Composable
fun Footer(
    footerRoutes: FooterRoutes,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        FooterViewModel(
            scope = scope,
            onError = onError,
            footerRoutes = footerRoutes,
        )
    }
    val state by vm.observeStates().collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainer.value())
            .margin(top = 2.em)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.5.cssRem, bottom = 2.5.em, leftRight = 3.cssRem)
        ) {
            FooterSubscribe(
                modifier = Modifier.weight(1f),
                subscribeText = state.strings.subscribe,
                emailPlaceholder = state.strings.email,
                emailText = state.email,
                emailError = state.emailError,
                onEmailSend = { vm.trySend(FooterContract.Inputs.OnEmailSend) },
                onEmailChange = { vm.trySend(FooterContract.Inputs.OnEmailChange(it)) },

                )
            FooterHelp(
                helpText = state.strings.help,
                trackOrderText = state.strings.trackOrder,
                shippingText = state.strings.shipping,
                returnsText = state.strings.returns,
                faQsText = state.strings.faQs,
                contactUsText = state.strings.contactUs,
                onTrackOrderClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
                onShippingClick = { vm.trySend(FooterContract.Inputs.OnShippingClick) },
                onReturnsClick = { vm.trySend(FooterContract.Inputs.OnReturnsClick) },
                onFAQsClick = { vm.trySend(FooterContract.Inputs.OnFAQsClick) },
                onContactUsClick = { vm.trySend(FooterContract.Inputs.OnContactUsClick) },
                modifier = Modifier.weight(1f),
            )
            FooterAboutUs(
                showAdminButton = state.isAdmin,
                aboutUsSmallText = state.strings.aboutUsSmall,
                aboutUsText = state.strings.aboutUs,
                careerText = state.strings.career,
                cyberSecurityText = state.strings.cyberSecurity,
                pressText = state.strings.press,
                adminTextText = state.strings.admin,
                onAboutUsClick = { vm.trySend(FooterContract.Inputs.OnAboutUsClick) },
                onCareerClick = { vm.trySend(FooterContract.Inputs.OnCareerClick) },
                onCyberSecurityClick = { vm.trySend(FooterContract.Inputs.OnCyberSecurityClick) },
                onPressClick = { vm.trySend(FooterContract.Inputs.OnPressClick) },
                onGoToAdminHome = { vm.trySend(FooterContract.Inputs.OnGoToAdminHome) },
                modifier = Modifier.weight(1f),
            )
            FooterDeliverTo(
                currentCountryText = state.currentCountryText,
                countryImageUrl = state.countryImageUrl,
                languageText = state.strings.language,
                currentLanguageText = state.currentLanguageText,
                deliverToText = state.strings.deliverTo,
                onCurrencyClick = { vm.trySend(FooterContract.Inputs.OnCurrencyClick) },
                onLanguageClick = { vm.trySend(FooterContract.Inputs.OnLanguageClick) },
                modifier = Modifier.weight(1f),
            )
        }
        Divider()
        FooterBottomSection(vm, state)
    }
}
