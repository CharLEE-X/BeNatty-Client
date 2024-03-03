package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import feature.product.catalogue.CatalogueContract
import feature.product.catalogue.CatalogueRoutes
import feature.product.catalogue.CatalogueViewModel
import feature.product.catalogue.Variant
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import web.HeadlineTextStyle
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.widgets.AppElevatedCard
import web.components.widgets.ImageOverlay
import web.pages.shop.home.gridModifier

@Composable
fun CataloguePage(
    mainRoutes: MainRoutes,
    variant: Variant,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        CatalogueViewModel(
            scope = scope,
            variant = variant,
            catalogueRoutes = CatalogueRoutes(
                onError = { message -> mainRoutes.onError(message) },
                goToProduct = { productId -> mainRoutes.goToProduct(productId) }
            ),
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.productPage,
        mainRoutes = mainRoutes,
        spacing = 1.em
    ) {
        Banner(
            state = state,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em)
        ) {
            CatalogueFilters(
                modifier = Modifier
                    .weight(1)
                    .backgroundColor(Colors.LightCoral)
            )
            CatalogueContent(
                vm = vm,
                state = state,
                modifier = Modifier.weight(4)
            )
        }
    }
}

@Composable
fun SearchBanner(
    modifier: Modifier = Modifier,
) {
    SpanText("Search Banner")
}

@Composable
private fun Banner(
    state: CatalogueContract.State,
    modifier: Modifier = Modifier,
) {
    if (state.showBanner) {
        CatalogueBanner(
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
private fun CatalogueContent(
    modifier: Modifier = Modifier,
    vm: CatalogueViewModel,
    state: CatalogueContract.State,
) {
    Row(
        modifier = gridModifier(3).then(modifier)
    ) {
        state.products.forEachIndexed { index, product ->
            CatalogItem(
                title = product.title,
                price = product.price,
                media = product.media,
                onClick = { vm.trySend(CatalogueContract.Inputs.OnGoToProductClicked(product.id)) },
                modifier = Modifier.thenIf(index > 2) { Modifier.padding(top = 1.em) }
            )
        }
    }
}

@Composable
private fun CatalogueFilters(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .gap(1.em)
    ) {
        SpanText("Catalogue Filters")
    }
}

@Composable
fun CatalogueBanner(
    modifier: Modifier = Modifier,
    title: String,
    height: CSSLengthOrPercentageNumericValue = 350.px,
    contentColor: Color = Colors.White,
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
            .overflow(Overflow.Hidden)
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
            ImageOverlay(
                shadowColor = shadowColor,
                overlayColor = overlayColor,
                hovered = false
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(50.px)
                    .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.gap(2.px)
                ) {
                    SpanText(
                        text = title.uppercase(),
                        modifier = HeadlineTextStyle.toModifier()
                            .color(contentColor)
                            .textShadow(
                                offsetX = 2.px,
                                offsetY = 2.px,
                                blurRadius = 8.px,
                                color = shadowColor
                            )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .height(2.px)
                            .fillMaxWidth(100.percent)
                            .backgroundColor(contentColor)
                            .borderRadius(2.px)
                            .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
                    )
                }
            }
        }
    }
}
