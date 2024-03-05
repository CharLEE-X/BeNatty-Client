package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import feature.product.catalog.CatalogContract
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.HeadlineTextStyle
import web.components.widgets.AppElevatedCard

@Composable
fun CatalogBanner(
    state: CatalogContract.State,
    modifier: Modifier = Modifier,
) {
    if (state.showBanner) {
        Banner(
            title = state.bannerTitle ?: "",
            modifier = modifier
        ) { imageModifier ->
            Image(
                src = state.bannerImageUrl ?: "",
                alt = state.bannerTitle ?: "",
                modifier = imageModifier
            )
        }
    }
    if (state.showSearch) {
        SearchBanner(
            modifier = modifier
        )
    }
}

@Composable
private fun Banner(
    modifier: Modifier = Modifier,
    title: String,
    height: CSSLengthOrPercentageNumericValue = 350.px,
    contentColor: Color = MaterialTheme.colors.background,
    shadowColor: Color = Color.rgb(30, 30, 59),
    borderRadius: CSSLengthOrPercentageNumericValue = 12.px,
    image: @Composable (imageModifier: Modifier) -> Unit,
) {
    val overlayColor = if (ColorMode.current.isLight) shadowColor else Color.rgb(179, 176, 248)

    AppElevatedCard(
        elevation = 0,
        modifier = modifier
            .fillMaxWidth()
            .maxHeight(height)
            .position(Position.Relative)
            .aspectRatio(1.0)
            .borderRadius(borderRadius)
            .overflow(Overflow.Clip)
            .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .backgroundColor(MaterialTheme.colors.surfaceContainerHighest)
        ) {
            val imageModifier = Modifier
                .fillMaxSize()
                .borderRadius(borderRadius)
                .objectFit(ObjectFit.Cover)
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            image(imageModifier)
//            ImageOverlay(
//                shadowColor = shadowColor,
//                overlayColor = overlayColor,
//                hovered = false
//            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(50.px)
                    .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            ) {
                SpanText(
                    text = title.uppercase(),
                    modifier = HeadlineTextStyle.toModifier()
                        .color(contentColor)
                )
            }
        }
    }
}

@Composable
private fun SearchBanner(
    modifier: Modifier = Modifier,
) {
    SpanText("Search Banner")
}
