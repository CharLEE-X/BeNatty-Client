package web.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignContent
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiFacebook
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLightMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiModeNight
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson2
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSearch
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingCart
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.GetRecommendedProductsQuery
import feature.shop.navbar.NavbarContract
import feature.shop.navbar.NavbarViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignContent
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.H3Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppDividerHorizontal
import web.components.widgets.AppDividerVertical
import web.components.widgets.AppIconButton
import web.components.widgets.Logo
import web.components.widgets.RotatableChevron
import web.components.widgets.ShimmerHeader
import web.pages.shop.home.gridModifier
import web.pages.shop.product.page.ProductPrice
import web.util.onEnterKeyDown

@Composable
fun NavBar(
    vm: NavbarViewModel, state: NavbarContract.State,
    isFullLayout: Boolean,
    basketCount: Int,
) {
    val scope = rememberCoroutineScope()

    var isShopHovered by remember { mutableStateOf(false) }
    var isShopBigMenuHovered by remember { mutableStateOf(false) }
    var showShopBigMenu by remember { mutableStateOf(false) }

    var isExploreBigMenuHovered by remember { mutableStateOf(false) }
    var showExploreMenu by remember { mutableStateOf(false) }
    var isExploreHovered by remember { mutableStateOf(false) }

    var shopCloseScheduled: Job? = null
    var exploreCloseScheduled: Job? = null

    fun scheduleCloseMenu() {
        shopCloseScheduled = scope.launch {
            delay(500)
            if (!(isShopHovered || isShopBigMenuHovered)) {
                showShopBigMenu = false
            }
        }
    }

    LaunchedEffect(isShopHovered, isShopBigMenuHovered, isExploreHovered, isExploreBigMenuHovered) {
        if (isShopHovered || isShopBigMenuHovered) {
            shopCloseScheduled?.cancel()
            showShopBigMenu = true
        } else {
            shopCloseScheduled = scope.launch {
                delay(500)
                if (!(isShopHovered || isShopBigMenuHovered)) {
                    showShopBigMenu = false
                }
            }
        }

        if (isExploreHovered || isExploreBigMenuHovered) {
            exploreCloseScheduled?.cancel()
            showExploreMenu = true
        } else {
            exploreCloseScheduled = scope.launch {
                delay(500)
                if (!(isExploreHovered || isExploreBigMenuHovered)) {
                    showExploreMenu = false
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(ColorMode.current.toPalette().background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .padding(leftRight = 24.px)
                .gap(20.px)
                .display(DisplayStyle.Grid)
                .styleModifier { property("grid-template-columns", "1fr auto 1fr") }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .display(DisplayStyle.Flex)
                    .flexWrap(FlexWrap.Wrap)
                    .alignItems(AlignItems.Center)
                    .alignContent(AlignContent.Center)
                    .gap(16.px)
                    .ariaLabel("Primary Navigation")
            ) {
                if (isFullLayout) {
                    ListMenuItem(
                        text = getString(Strings.Shop),
                        hasDropdown = true,
                        hovered = isShopHovered || isShopBigMenuHovered,
                        onClick = { vm.trySend(NavbarContract.Inputs.OnStoreClicked) },
                        modifier = Modifier
                            .translateY((6).px)
                            .padding(topBottom = 24.px)
                            .onMouseEnter {
                                isShopHovered = true
                                showShopBigMenu = true
                                showExploreMenu = false
                            }
                            .onMouseLeave { isShopHovered = false }
                            .onFocusIn {
                                isShopHovered = true
                                showShopBigMenu = true
                                showExploreMenu = false
                            }
                            .onFocusOut { isShopHovered = false }
                    )
                    ListMenuItem(
                        text = getString(Strings.Explore),
                        hasDropdown = true,
                        hovered = isExploreHovered || isExploreBigMenuHovered,
                        onClick = { vm.trySend(NavbarContract.Inputs.OnExploreClicked) },
                        modifier = Modifier
                            .translateY((6).px)
                            .padding(topBottom = 6.px)
                            .onMouseEnter {
                                isExploreHovered = true
                                showExploreMenu = true
                                showShopBigMenu = false
                            }
                            .onMouseLeave { isExploreHovered = false }
                            .onFocusIn {
                                isExploreHovered = true
                                showExploreMenu = true
                                showShopBigMenu = false
                            }
                            .onFocusOut { isExploreHovered = false }
                    )
                }
            }
            Logo(
                onClick = { vm.trySend(NavbarContract.Inputs.OnLogoClick) },
                modifier = Modifier.margin(leftRight = 1.em)
            )
            RightSection(
                vm = vm,
                state = state,
                isFullLayout = isFullLayout,
                basketCount = basketCount,
            )
        }
        if (showShopBigMenu) {
            ShopBigMenu(
                vm = vm,
                state = state,
                modifier = Modifier
                    .onMouseOver { isShopBigMenuHovered = true }
                    .onMouseOut {
                        isShopBigMenuHovered = false
                        scheduleCloseMenu()
                    }
            )
        }
        if (showExploreMenu) {
            ExploreBigMenu(
                vm = vm,
                state = state,
                modifier = Modifier
                    .onMouseOver { isExploreBigMenuHovered = true }
                    .onMouseOut {
                        isExploreBigMenuHovered = false
                        scheduleCloseMenu()
                    }
            )
        }
    }
}

@Composable
private fun RightSection(
    vm: NavbarViewModel,
    state: NavbarContract.State,
    isFullLayout: Boolean,
    modifier: Modifier = Modifier,
    basketCount: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier.color(ColorMode.current.toPalette().color)
    ) {
        if (isFullLayout) {
            AppIconButton(
                onClick = { vm.trySend(NavbarContract.Inputs.OnSearchClicked) },
                icon = { MdiSearch(style = it) }
            )
            if (!state.isCheckAuthLoading) {
                AppIconButton(
                    onClick = { vm.trySend(NavbarContract.Inputs.OnUserClicked) },
                    icon = { MdiPerson2(style = it) }
                )
            } else {
                ShimmerHeader(Modifier.aspectRatio(1))
            }
            var colorMode by ColorMode.currentState
            AppIconButton(
                onClick = { colorMode = colorMode.opposite },
                icon = {
                    if (colorMode.isLight) MdiLightMode(style = it)
                    else MdiModeNight(style = it)
                }
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.px)
                        .backgroundColor(ColorMode.current.toPalette().background)
                        .borderRadius(50.percent)
                        .translateY(if (basketCount > 0) (-24).px else 0.px)
                        .opacity(if (basketCount > 0) 1.0 else 0.0)
                        .border(
                            width = 1.px,
                            color = ColorMode.current.toPalette().background,
                            style = LineStyle.Solid
                        )
                        .transition(
                            CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                        )
                ) {
                    SpanText(
                        text = basketCount.toString(),
                        modifier = Modifier
                            .fontSize(12.px)
                            .fontWeight(FontWeight.SemiBold)
                            .color(ColorMode.current.toPalette().background)
                    )
                }
                AppIconButton(
                    onClick = { vm.trySend(NavbarContract.Inputs.OnCartClick) },
                    icon = { MdiShoppingCart(style = it) }
                )
            }
        }
    }
}

@Composable
fun AppMenu(
    modifier: Modifier = Modifier,
    open: Boolean,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    itemsGap: CSSLengthOrPercentageNumericValue = 0.px,
) {
    Box(
        modifier = modifier
            .position(Position.Absolute)
            .zIndex(5)
            .width(200.px)
            .backgroundColor(ColorMode.current.toPalette().background)
            .opacity(if (open) 1.0 else 0.0)
            .visibility(if (open) Visibility.Visible else Visibility.Hidden)
            .userSelect(UserSelect.None)
            .border(
                width = 1.px,
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
            .translate(
                tx = (-12).px,
                ty = if (open) (-5).px else 20.px,
            )
            .transition(
                CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("top", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("visibility", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
            )
            .tabIndex(0)
    ) {
        Column(
            modifier = Modifier
                .gap(itemsGap)
                .fontWeight(400)
                .fontSize(14.px)
                .lineHeight(18.px)
                .padding(leftRight = 20.px, topBottom = 16.px)
        ) {
            items.forEach { item ->
                MenuItem(
                    onStoreMenuItemSelected = onItemSelected,
                    item = item,
                )
            }
        }
    }
}

@Composable
fun ShopBigMenu(vm: NavbarViewModel, state: NavbarContract.State, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .gap(2.em)
                .display(DisplayStyle.Flex)
                .padding(top = 2.em, bottom = 4.em, leftRight = 4.em)
        ) {
            Column(
                modifier = Modifier
                    .gap(1.em)
                    .margin(right = 50.px)
            ) {
                SpanText(
                    text = getString(Strings.Shop).uppercase(),
                    modifier = Modifier
//                        .roleStyle(MaterialTheme.typography.titleMedium)
                        .fontWeight(FontWeight.SemiBold)
                )
                Column(
                    modifier = Modifier
//                        .roleStyle(MaterialTheme.typography.bodyMedium)
                        .gap(1.em)
                ) {
                    TextLink(
                        text = getString(Strings.NewArrivals),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnNewArrivalsClicked) }
                    )
                    TextLink(
                        text = getString(Strings.AllCollections),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnAllCollectionsClicked) }
                    )
                    TextLink(
                        text = getString(Strings.OurFavourites),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnOurFavouritesClicked) }
                    )
                    TextLink(
                        text = getString(Strings.SummerDeals),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnSummerDealsClicked) }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .gap(1.em)
            ) {
                state.recommendedProducts
                    .take(4)
                    .forEach { product ->
                        RecommendedProductItem(
                            name = product.name,
                            currency = state.currency,
                            regularPrice = product.regularPrice.toString(),
                            salePrice = product.salePrice.toString(),
                            media = product.media,
                            onClick = { vm.trySend(NavbarContract.Inputs.OnRecommendedProductClicked(product.id)) }
                        )
                    }
            }
        }
        ContactSection(vm, state)
    }
}

@Composable
fun ExploreBigMenu(vm: NavbarViewModel, state: NavbarContract.State, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = gridModifier(4, gap = 2.em)
                .maxWidth(oneLayoutMaxWidth)
                .padding(top = 2.em, bottom = 4.em, leftRight = 4.em)
        ) {
            Image(
                src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/slidefour.jpg?v=1613676525&width=500",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .gap(1.em)
            ) {
                SpanText(
                    text = getString(Strings.Explore).uppercase(),
                    modifier = Modifier
//                        .roleStyle(MaterialTheme.typography.titleMedium)
                        .fontWeight(FontWeight.SemiBold)
                )
                Column(
                    modifier = Modifier.gap(1.em)
//                        .roleStyle(MaterialTheme.typography.bodyMedium)
                ) {
                    TextLink(
                        text = getString(Strings.NewArrivals),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnNewArrivalsClicked) }
                    )
                    TextLink(
                        text = getString(Strings.ShopTheLatest),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnShopTheLatestClicked) }
                    )
                    TextLink(
                        text = getString(Strings.WeLoved),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnWeLoveClicked) }
                    )
                    TextLink(
                        text = getString(Strings.Collections),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnCollectionsClicked) }
                    )
                }
            }
            Column(
                modifier = Modifier.gap(1.em)
            ) {
                SpanText(
                    text = getString(Strings.ShopByType).uppercase(),
                    modifier = HeadlineStyle.toModifier(H3Variant)
                        .fontWeight(FontWeight.SemiBold)
                )
                Column(
                    modifier = Modifier
                        .gap(1.em)
                ) {
                    TextLink(
                        text = getString(Strings.Tops),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnTopsClicked) }
                    )
                    TextLink(
                        text = getString(Strings.Bottoms),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnBottomsClicked) }
                    )
                    TextLink(
                        text = getString(Strings.Dresses),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnDressesClicked) }
                    )
                }
            }
            Column(
                modifier = Modifier.gap(1.em)
            ) {
                SpanText(
                    text = getString(Strings.QuickLinks).uppercase(),
                    modifier = HeadlineStyle.toModifier(H3Variant)
                        .fontWeight(FontWeight.SemiBold)
                )
                Column(
                    modifier = HeadlineStyle.toModifier(H3Variant)
                        .gap(1.em)
                ) {
                    TextLink(
                        text = getString(Strings.Delivery),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnDeliveryClicked) }
                    )
                    TextLink(
                        text = getString(Strings.Returns),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnReturnsClicked) }
                    )
                    TextLink(
                        text = getString(Strings.Contact),
                        onClick = { vm.trySend(NavbarContract.Inputs.OnContactClicked) }
                    )
                }
            }
        }
        ContactSection(vm, state)
    }
}

@Composable
private fun RecommendedProductItem(
    name: String,
    currency: Currency,
    regularPrice: String,
    salePrice: String?,
    media: List<GetRecommendedProductsQuery.Medium>,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .display(DisplayStyle.Flex)
            .gap(0.5.em)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .maxHeight(400.px)
                .fillMaxHeight()
                .aspectRatio(.6f)
                .onMouseOver { hovered = true }
                .onMouseLeave { hovered = false }
                .onFocusIn { hovered = true }
                .onFocusOut { hovered = false }
                .cursor(Cursor.Pointer)
                .tabIndex(0)
                .onClick { onClick() }
                .onEnterKeyDown(onClick)
                .overflow(Overflow.Hidden)
                .userSelect(UserSelect.None)
                .draggable(false)
                .scale(if (hovered) 1.02f else 1f)
                .transition(
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("transform", 0.2.s, TransitionTimingFunction.Ease)
                )
        ) {
            media.firstOrNull()?.let { firstMedia ->
                val secondMedia = media.getOrNull(1)
                val currentMedia = secondMedia?.let { if (hovered) it else firstMedia } ?: firstMedia

                Image(
                    src = currentMedia.url,
                    alt = firstMedia.alt,
                    modifier = Modifier
                        .fillMaxSize()
                        .objectFit(ObjectFit.Cover)
                )
            }
        }
        SpanText(
            text = name,
            modifier = Modifier
        )
        ProductPrice(
            regularPrice = regularPrice,
            salePrice = salePrice,
            currency = currency,
        )
    }
}

@Composable
private fun ContactSection(vm: NavbarViewModel, state: NavbarContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        AppDividerHorizontal()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .padding(topBottom = 1.em)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .gap(1.em)
            ) {
                MdiFacebook()
                MdiFacebook()
                MdiFacebook()
            }
            AppDividerVertical()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                TextLink(
                    text = getString(Strings.CustomerService),
                    onClick = { vm.trySend(NavbarContract.Inputs.OnCustomerServiceClicked) },
                )
            }
        }
        AppDividerHorizontal()
    }
}

@Composable
private fun MenuItem(
    onStoreMenuItemSelected: (String) -> Unit,
    item: String,
    modifier: Modifier = Modifier,
) {
    var itemHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onClick { onStoreMenuItemSelected(item) }
            .cursor(Cursor.Pointer)
            .padding(leftRight = 12.px)
            .onMouseEnter {
                itemHovered = true
            }
            .onMouseLeave { itemHovered = false }
            .tabIndex(0)
            .onEnterKeyDown { onStoreMenuItemSelected(item) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .gap(8.px)
        ) {
            SpanText(
                text = item,
                modifier = Modifier
                    .whiteSpace(WhiteSpace.NoWrap)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .onClick { onStoreMenuItemSelected(item) }
                    .height(2.px)
                    .fillMaxWidth(if (itemHovered) 100.percent else 0.percent)
                    .backgroundColor(ColorMode.current.toPalette().color)
                    .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
            )
        }
    }
}

@Composable
private fun ListMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    hovered: Boolean,
    hasDropdown: Boolean = false,
    contentColor: CSSColorValue = ColorMode.current.toPalette().color,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { onClick() }
            .gap(4.px)
            .cursor(Cursor.Pointer)
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(12.px)
        ) {
            SpanText(
                text = text.uppercase(),
                modifier = Modifier
                    .color(contentColor)
                    .whiteSpace(WhiteSpace.NoWrap)
            )
            if (hasDropdown) {
                RotatableChevron(
                    hovered = hovered,
                    open = hovered,
                    color = contentColor,
                    modifier = Modifier
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                .backgroundColor(contentColor)
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}

@Composable
fun TextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: CSSColorValue = ColorMode.current.toPalette().color,
) {
    var hovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { if (enabled) onClick() }
            .gap(4.px)
            .cursor(if (enabled) Cursor.Pointer else Cursor.Auto)
            .onEnterKeyDown(onClick)
            .onMouseOver { if (enabled) hovered = true }
            .onMouseOut { hovered = false }
            .onFocusIn { if (enabled) hovered = true }
            .onFocusOut { hovered = false }
            .tabIndex(0)
            .onEnterKeyDown { if (enabled) onClick() }
            .opacity(if (enabled) 1.0 else 0.6)
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        SpanText(
            text = text.uppercase(),
            modifier = textModifier
                .color(color)
                .thenIf(!enabled) { Modifier.textDecorationLine(TextDecorationLine.LineThrough) }
                .whiteSpace(WhiteSpace.NoWrap)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered && enabled) 100.percent else 0.percent)
                .backgroundColor(color)
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}
