package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import component.localization.Strings
import component.localization.getString
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
        title = getString(Strings.Home),
        mainRoutes = mainRoutes,
    ) {
        state.landingConfig?.collageItems?.let {
            Collage(
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isLoading,
                items = it,
                shopNowText = getString(Strings.ShopNow),
                onCollageItemClick = { vm.trySend(HomeContract.Inputs.OnCollageItemClick(it)) },
            )
        }
        BannerSection(vm, state)
        HomeSubscribe(
            isLoading = state.isLoading,
            subscribeText = getString(Strings.SubscribeToOurNewsletter),
            subscribeDescText = getString(Strings.BeFirstToGetLatestOffers),
            emailPlaceholder = getString(Strings.Email),
            emailText = state.email,
            privacyPolicyText = getString(Strings.PrivacyPolicy),
            andText = getString(Strings.And),
            termsOfServiceText = getString(Strings.TermsOfService),
            byAgreeingText = getString(Strings.ByAgreeing),
            onPrivacyPolicyClick = { vm.trySend(HomeContract.Inputs.OnPrivacyPolicyClick) },
            onTermsOfServiceClick = { vm.trySend(HomeContract.Inputs.OnTermsOfServiceClick) },
            onEmailSend = { vm.trySend(HomeContract.Inputs.OnEmailSend) },
            onEmailChange = { vm.trySend(HomeContract.Inputs.OnEmailChange(it)) },
        )
    }
}
