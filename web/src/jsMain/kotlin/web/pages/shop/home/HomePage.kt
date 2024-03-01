package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import feature.shop.home.HomeContract
import feature.shop.home.HomeRoutes
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth

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
            ),
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.home,
        mainRoutes = mainRoutes,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .margin(0.px)
                .padding(leftRight = 20.px, top = 100.px)
        ) {
            Collage(
                modifier = Modifier.fillMaxWidth(),
                items = state.collageItems,
                shopNowText = state.strings.shopNow,
                onCollageItemClick = { vm.trySend(HomeContract.Inputs.OnCollageItemClick(it)) },
            )

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
}
