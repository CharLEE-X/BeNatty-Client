package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.silk.components.graphics.Image
import feature.shop.home.HomeContract
import feature.shop.home.HomeRoutes
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.px
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

@Composable
fun BannerSection(vm: HomeViewModel, state: HomeContract.State) {
    Row(
        modifier = gridModifier(columns = 2)
            .maxHeight(400.px)
    ) {
        CollageItem(
            title = state.landingConfig?.bannerSection?.left?.title ?: state.strings.trendingNow,
            description = state.landingConfig?.bannerSection?.left?.description
                ?: state.strings.exploreLatestFashionTrendsHere,
            onClick = { vm.trySend(HomeContract.Inputs.OnBannerLeftClick) },
            textPosition = TextPosition.LeftBottom,
        ) { imageModifier ->
            Image(
                src = state.landingConfig?.bannerSection?.left?.imageUrl ?: "",
                alt = state.landingConfig?.bannerSection?.left?.title ?: "",
                modifier = imageModifier
            )
        }
        CollageItem(
            title = state.landingConfig?.bannerSection?.right?.title ?: state.strings.ecoFriendlyClothing,
            description = state.landingConfig?.bannerSection?.right?.description
                ?: state.strings.cottonNoArtificialIngredients,
            onClick = { vm.trySend(HomeContract.Inputs.OnBannerRightClick) },
            textPosition = TextPosition.RightTop,
        ) { imageModifier ->
            Image(
                src = state.landingConfig?.bannerSection?.right?.imageUrl ?: "",
                alt = state.landingConfig?.bannerSection?.right?.title ?: "",
                modifier = imageModifier
            )
        }
    }
}
