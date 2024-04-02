package web.pages.shop.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.silk.components.graphics.Image
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.components.widgets.ShimmerHeader
import web.components.widgets.ShimmerText
import web.util.glossy

@Composable
fun BannerSection(vm: HomeViewModel, state: HomeContract.State) {
    Row(
        modifier = gridModifier(columns = 2)
            .position(Position.Relative)
            .maxHeight(400.px)
            .padding(leftRight = 24.px, topBottom = 48.px)
            .glossy()
    ) {
        if (!state.isLoading) {
            CollageItem(
                title = state.landingConfig?.bannerSection?.left?.title ?: state.strings.trendingNow,
                description = state.landingConfig?.bannerSection?.left?.description
                    ?: state.strings.exploreLatestFashionTrendsHere,
                onClick = { vm.trySend(HomeContract.Inputs.OnBannerLeftClick) },
                textPosition = TextPosition.LeftBottom,
            ) { imageModifier ->
                Image(
                    src = state.landingConfig?.bannerSection?.left?.mediaUrl ?: "",
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
                    src = state.landingConfig?.bannerSection?.right?.mediaUrl ?: "",
                    alt = state.landingConfig?.bannerSection?.right?.title ?: "",
                    modifier = imageModifier
                )
            }
        } else {
            ShimmerCollageItem(Modifier.fillMaxWidth().height(350.px)) {
                ShimmerHeader(Modifier.fillMaxWidth(60.percent))
                ShimmerText(Modifier.fillMaxWidth(50.percent))
            }
            ShimmerCollageItem(
                textPosition = TextPosition.RightTop,
                modifier = Modifier.fillMaxWidth().height(350.px)
            ) {
                ShimmerHeader(Modifier.fillMaxWidth(80.percent))
                ShimmerText(Modifier.fillMaxWidth(60.percent))
            }
        }
    }
}
