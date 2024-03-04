package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import feature.product.catalogue.CatalogueContract
import feature.product.catalogue.CatalogueRoutes
import feature.product.catalogue.CatalogueViewModel
import feature.product.catalogue.Variant
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.roleStyle
import web.HeadlineTextStyle
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.widgets.AppElevatedCard
import web.pages.shop.home.gridModifier
import web.util.glossy

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(left = 24.px, right = 24.px, top = 24.px, bottom = 48.px)
                .glossy()
                .gap(2.em)
        ) {
            Banner(state = state)
            CatalogueHeader(vm, state)
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
}

@Composable
fun CatalogueHeader(vm: CatalogueViewModel, state: CatalogueContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SpanText(
            text = "${state.pageInfo.count} products",
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyLarge)
                .color(MaterialTheme.colors.onSurface)
        )
        Spacer()
        FiltersButton(
            sortByText = "Sort by",
            currentFilter = "Best selling",
            menuOpened = false,
            onFilterClicked = { }
        )
    }
}

@Composable
private fun FiltersButton(
    sortByText: String,
    currentFilter: String,
    menuOpened: Boolean,
    onFilterClicked: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
    val borderColor = when {
        hovered -> MaterialTheme.colors.onSurface
        else -> MaterialTheme.colors.surfaceContainerLowest
    }
    val backgroundColor = when {
        hovered -> MaterialTheme.colors.surfaceContainerLow
        else -> MaterialTheme.colors.surfaceContainerLowest
    }

    Box(
        modifier = Modifier
            .padding(left = 1.em, right = 0.5.em, top = 0.5.em, bottom = 0.5.em)
            .backgroundColor(backgroundColor)
            .borderRadius(36.px)
            .border(
                width = 1.px,
                color = borderColor,
                style = LineStyle.Solid
            )
            .cursor(Cursor.Pointer)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .transition(CSSTransition.group(listOf("border", "background-color"), 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(0.25.em)
                .color(MaterialTheme.colors.onSurface)
        ) {
            Column(
                modifier = Modifier
                    .gap(0.25.em)
            ) {
                SpanText(
                    text = "$sortByText:",
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.bodySmall)
                )
                SpanText(
                    text = currentFilter,
                    modifier = Modifier.fontWeight(FontWeight.Bold)
                        .roleStyle(MaterialTheme.typography.bodyLarge)
                )
            }
            MdiChevronLeft(
                modifier = Modifier
                    .rotate(if (menuOpened) 90.deg else 270.deg)
                    .transition(CSSTransition("rotate", 0.3.s, TransitionTimingFunction.Ease))
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
