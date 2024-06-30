package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.product.catalog.CatalogContract
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.HeadlineStyle
import web.components.widgets.Shimmer
import web.components.widgets.ShimmerButton
import web.pages.home.TextPosition

@Composable
fun CatalogBanner(
    state: CatalogContract.State,
    modifier: Modifier = Modifier,
) {
    if (state.showBanner) {
        val bannerHeight: CSSLengthOrPercentageNumericValue = 400.px

        if (!state.isCatalogConfigLoading) {
            Banner(
                title = state.bannerTitle ?: "",
                height = bannerHeight,
            ) { imageModifier ->
                Image(
//                    src = state.bannerImageUrl ?: "", // FIXME
                    src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/icon__hero--image-1.jpg?v=1635967751&width=2000",
                    alt = state.bannerTitle ?: "",
                    modifier = imageModifier
                )
            }
        } else {
            BannerShimmer(Modifier.height(bannerHeight))
        }
    }
    if (state.showSearch) {
        SearchBanner(
            modifier = modifier
                .tabIndex(0)
        )
    }
}

@Composable
private fun BannerShimmer(modifier: Modifier) {
    Shimmer(
        textPosition = TextPosition.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        ShimmerButton(Modifier.width(300.px))
    }
}

@Composable
private fun Banner(
    title: String,
    height: CSSLengthOrPercentageNumericValue,
    image: @Composable (imageModifier: Modifier) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .maxHeight(height)
            .aspectRatio(1.0)
            .overflow(Overflow.Clip)
            .transition(Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        val imageModifier = Modifier
            .fillMaxSize()
            .objectFit(ObjectFit.Cover)
            .transition(Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease))
        image(imageModifier)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(50.px)
                .transition(Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            SpanText(
                text = title.uppercase(),
                modifier = HeadlineStyle.toModifier()
                    .color(Colors.White)
            )
        }
    }
}

@Composable
private fun SearchBanner(
    modifier: Modifier = Modifier,
) {
    SpanText("Search Banner")
}
