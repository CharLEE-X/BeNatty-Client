package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import feature.shop.home.HomeContract
import feature.shop.home.HomeRoutes
import feature.shop.home.HomeViewModel
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout

@Composable
fun HomeContent(
    mainRoutes: MainRoutes,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        HomeViewModel(
            scope = scope,
            onError = mainRoutes.onError,
            homeRoutes = HomeRoutes(
                home = mainRoutes.goToHome,
                privacyPolicy = mainRoutes.goToPrivacyPolicy,
                termsOfService = mainRoutes.goToTermsOfService,
                catalogue = mainRoutes.goToCatalogue,
            ),
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.home,
        mainRoutes = mainRoutes,
    ) {
        state.landingConfig?.collageItems?.let {
            Collage(
                modifier = Modifier.fillMaxWidth(),
                items = it,
                shopNowText = state.strings.shopNow,
                onCollageItemClick = { vm.trySend(HomeContract.Inputs.OnCollageItemClick(it)) },
            )
        }
        BannerSection(vm, state)
        HomeSubscribe(
            subscribeText = state.strings.subscribeToOurNewsletter,
            subscribeDescText = state.strings.beFirstToGetLatestOffers,
            emailPlaceholder = state.strings.email,
            emailText = state.email,
            privacyPolicyText = state.strings.privacyPolicy,
            andText = state.strings.and,
            termsOfServiceText = state.strings.termsOfService,
            byAgreeingText = state.strings.byAgreeing,
            onPrivacyPolicyClick = { vm.trySend(HomeContract.Inputs.OnPrivacyPolicyClick) },
            onTermsOfServiceClick = { vm.trySend(HomeContract.Inputs.OnTermsOfServiceClick) },
            onEmailSend = { vm.trySend(HomeContract.Inputs.OnEmailSend) },
            onEmailChange = { vm.trySend(HomeContract.Inputs.OnEmailChange(it)) },
        )
    }
}
