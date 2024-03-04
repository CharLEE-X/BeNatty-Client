package web.pages.shop.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.silk.components.graphics.Image
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
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
