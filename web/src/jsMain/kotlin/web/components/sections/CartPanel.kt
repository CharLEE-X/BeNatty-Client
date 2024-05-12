package web.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiClose
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.icons.mdi.MdiRemove
import com.varabyte.kobweb.silk.components.icons.mdi.MdiShoppingCart
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.GetTopSellingProductsQuery
import data.GetUserCartQuery
import feature.shop.cart.CartContract
import feature.shop.cart.CartContract.Inputs.OnDecrementClicked
import feature.shop.cart.CartContract.Inputs.OnIncrementClicked
import feature.shop.cart.CartContract.Inputs.OnRemoveClicked
import feature.shop.cart.CartViewModel
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.H1Variant
import web.H2Variant
import web.H3Variant
import web.HeadlineStyle
import web.SubtitleStyle
import web.components.widgets.AppFilledButton
import web.components.widgets.AppIconButton
import web.components.widgets.AppTextButton
import web.components.widgets.FlexSpacer
import web.components.widgets.Spacer
import web.components.widgets.themeScrollbarStyle
import web.pages.shop.home.gridModifier
import web.pages.shop.product.page.ProductPrice
import web.pages.shop.product.page.SpendMore
import web.shadow
import web.util.onEnterKeyDown

@Composable
fun BoxScope.CartPanel(vm: CartViewModel, state: CartContract.State, zIndex: Int) {
    val sidebarWidth = 500.px

    var open by remember { mutableStateOf(false) }
    var opened by remember { mutableStateOf(false) }

    LaunchedEffect(state.showSidebar) {
        if (state.showSidebar != open) {
            if (state.showSidebar) { // Open -> Close
                open = true
                delay(10)
                opened = true
            } else { // Close -> Open
                opened = false
                delay(350)
                open = false
            }
        }
    }

    if (open) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(zIndex)
                .onClick { vm.trySend(CartContract.Inputs.HideCart) }
                .opacity(if (opened) 1f else 0f)
                .transition(CSSTransition("opacity", 0.6.s, TransitionTimingFunction.Ease))
        )
        Box(
            modifier = Modifier
                .zIndex(zIndex + 100)
                .align(Alignment.CenterEnd)
                .position(Position.Fixed)
                .top(0.px)
                .right(0.px)
                .bottom(0.px)
                .fillMaxHeight()
                .width(sidebarWidth)
                .boxShadow(
                    offsetX = 0.px,
                    offsetY = 0.px,
                    blurRadius = 20.px,
                    color = shadow(),
                )
                .translateX(if (opened) 0.px else sidebarWidth)
                .transition(CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            CloseButton(
                onCloseCLick = { vm.trySend(CartContract.Inputs.HideCart) }
            )
            if (state.cart.items.isEmpty()) {
                EmptyBasketSection(
                    vm = vm,
                    state = state,
                    onContinueShoppingClick = { vm.trySend(CartContract.Inputs.HideCart) },
                    modifier = Modifier.margin(top = 2.5.em)
                )
            } else {
                BasketContent(
                    vm = vm,
                    state = state,
                )
            }
        }
    }
}

@Composable
private fun BoxScope.CloseButton(onCloseCLick: () -> Unit) {
    AppIconButton(
        onClick = { onCloseCLick() },
        modifier = Modifier
            .align(Alignment.TopEnd)
            .onEnterKeyDown(onCloseCLick)
            .margin(1.em)
    ) {
        MdiClose(
            style = IconStyle.OUTLINED,
        )
    }
}

@Composable
private fun EmptyBasketSection(
    modifier: Modifier,
    vm: CartViewModel,
    state: CartContract.State,
    onContinueShoppingClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        FlexSpacer()
        MdiShoppingCart(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .fontSize(64.px)
        )
        Spacer(0.5.em)
        SpanText(
            text = getString(Strings.YourCartIsEmpty),
            modifier = HeadlineStyle.toModifier(H1Variant)
        )
        Spacer(1.em)
        AppFilledButton(
            onClick = onContinueShoppingClick,
        ) {
            SpanText(text = getString(Strings.ContinueShopping))
        }
        Spacer(2.em)
        SpanText(
            text = "${getString(Strings.AlreadyHaveAnAccount)}?",
            modifier = HeadlineStyle.toModifier(H1Variant)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            AppTextButton(
                onClick = { vm.trySend(CartContract.Inputs.OnLoginClicked) },
            ) {
                SpanText(text = getString(Strings.LogIn))
            }
            SpanText(
                text = "${getString(Strings.ToCheckOutFaster)}."
            )
        }
        FlexSpacer()
        BottomSection(vm, state)
    }
}

@Composable
fun BottomSection(vm: CartViewModel, state: CartContract.State) {
    HorizontalDivider()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.5.em)
            .margin(bottom = 1.5.em)
            .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        SpanText(
            text = getString(Strings.TopProductsOfThisWeek).uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
        )
        Spacer()
        Row(
            modifier = gridModifier(4)
        ) {
            state.topSellingProducts.forEach { product ->
                TopProductItem(
                    name = product.name,
                    currency = state.currency,
                    regularPrice = product.regularPrice.toString(),
                    salePrice = product.salePrice?.toString(),
                    media = product.media.first(),
                    onClick = { vm.trySend(CartContract.Inputs.OnTopProductClicked(product.id)) }
                )
            }
        }
    }
}

@Composable
private fun TopProductItem(
    name: String,
    currency: Currency,
    regularPrice: String,
    salePrice: String?,
    media: GetTopSellingProductsQuery.Medium,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
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
            Image(
                src = media.url,
                alt = media.alt,
                modifier = Modifier
                    .fillMaxSize()
                    .objectFit(ObjectFit.Cover)
            )
        }
        Spacer()
        SpanText(
            text = name,
            modifier = SubtitleStyle.toModifier()
                .overflow(Overflow.Hidden)
        )
        Spacer(0.5.em)
        ProductPrice(
            regularPrice = regularPrice,
            salePrice = salePrice,
            currency = currency,
        )
    }
}

@Composable
private fun BasketContent(
    vm: CartViewModel,
    state: CartContract.State
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 2.em,
                    bottom = 1.5.em,
                    leftRight = 1.5.em
                )
                .gap(1.em)
        ) {
            SpanText(
                text = getString(Strings.YourCart).uppercase(),
                modifier = HeadlineStyle.toModifier(H1Variant)
            )
            SpendMore(
                showSpendMore = state.showSpendMore,
                currency = state.currency,
                spendMoreValue = state.spendMoreValue,
                spendMoreKey = state.spendMoreKey,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = themeScrollbarStyle.toModifier()
                .gap(1.em)
                .fillMaxSize()
                .padding(
                    bottom = 1.em,
                    leftRight = 1.5.em
                )
                .gap(1.em)
                .overflow {
                    x(Overflow.Hidden)
                    y(Overflow.Auto)
                }
        ) {
            state.cart.items.forEach { item ->
                CartItem(
                    item = item,
                    currency = state.currency,
                    onDecrementClick = { vm.trySend(OnDecrementClicked(item.productId, item.variantId)) },
                    onIncrementClick = { vm.trySend(OnIncrementClicked(item.productId, item.variantId)) },
                    onRemoveClick = { vm.trySend(OnRemoveClicked(item.productId, item.variantId)) },
                )
            }
        }
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 1.5.em,
                    bottom = 2.em,
                    leftRight = 1.5.em
                )
        ) {
            Row(
                modifier = HeadlineStyle.toModifier(H1Variant)
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                SpanText(
                    text = getString(Strings.Subtotal).uppercase(),
                )
                FlexSpacer()
                SpanText(text = "${state.currency.symbol}${state.subtotal}")
            }
            Row(
                verticalAlignment = Alignment.Top,
                modifier = HeadlineStyle.toModifier(H1Variant)
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                SpanText(
                    text = getString(Strings.TaxesAndShippingCalculatedAtCheckout),
                    modifier = SubtitleStyle.toModifier()
                )
                FlexSpacer()
                state.saved?.let {
                    SpanText(
                        text = "${getString(Strings.Saved)}: ${state.currency.symbol}${state.saved}",
                        modifier = HeadlineStyle.toModifier(H1Variant)
                            .color(Colors.Red)
                    )
                }
            }
            Spacer()
            AppFilledButton(
                onClick = { vm.trySend(CartContract.Inputs.OnGoToCheckoutClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.px)
            ) {
                SpanText(
                    text = getString(Strings.CheckOut).uppercase(),
                    modifier = HeadlineStyle.toModifier(H3Variant)
                )
            }
        }
    }
}

@Composable
private fun CartItem(
    item: GetUserCartQuery.Item,
    currency: Currency,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
            .padding(1.em)
    ) {
        Image(
            src = item.mediaUrl ?: "",
            alt = item.name,
            modifier = Modifier
                .aspectRatio(1f)
                .maxHeight(100.px)
                .objectFit(ObjectFit.Cover)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .gap(0.25.em)
        ) {
            SpanText(
                text = item.vendor,
                modifier = SubtitleStyle.toModifier()
            )
            SpanText(
                text = item.name.uppercase(),
                modifier = Modifier
                    .fontWeight(FontWeight.SemiBold)
            )
            item.attrs.forEach { attr ->
                SpanText(
                    text = "${attr.key}: ${attr.value}",
                    modifier = SubtitleStyle.toModifier()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .gap(1.em)
                    .margin(top = 0.5.em)
            ) {
                IncDecButton(
                    quantity = item.quantity,
                    onIncrementClick = onIncrementClick,
                    onDecrementClick = onDecrementClick,
                )
                AppIconButton(
                    onClick = { onRemoveClick() },
                    icon = { MdiDelete(style = IconStyle.OUTLINED) },
                )
            }
        }
        ProductPrice(
            regularPrice = item.regularPrice.toString(),
            salePrice = item.salePrice?.toString(),
            currency = currency,
            containerModifier = Modifier.whiteSpace(WhiteSpace.NoWrap),
            initialIsOnSale = item.salePrice != null
        )
    }
}

@Composable
private fun IncDecButton(
    quantity: Int,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
) {
    var containerHovered by remember { mutableStateOf(false) }
    var minusHovered = containerHovered
    var plusHovered = containerHovered

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(topBottom = 0.35.em, leftRight = 1.em)
            .gap(0.75.em)
            .border(
                width = 2.px,
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
            .onMouseOver { containerHovered = true }
            .onMouseLeave { containerHovered = false }
            .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        MdiRemove(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .tabIndex(0)
                .onClick { onDecrementClick() }
                .onEnterKeyDown(onDecrementClick)
                .onMouseOver { minusHovered = true }
                .onMouseLeave { minusHovered = false }
                .userSelect(UserSelect.None)
                .draggable(false)
                .opacity(if (minusHovered) 1f else 0.6f)
                .scale(if (minusHovered) 1.2f else 1f)
                .transition(
                    CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease)
                )
        )
        SpanText(
            text = quantity.toString(),
        )
        MdiAdd(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .tabIndex(0)
                .onClick { onIncrementClick() }
                .onEnterKeyDown(onIncrementClick)
                .onMouseOver { plusHovered = true }
                .onMouseLeave { plusHovered = false }
                .userSelect(UserSelect.None)
                .draggable(false)
                .opacity(if (plusHovered) 1f else 0.6f)
                .scale(if (plusHovered) 1.2f else 1f)
                .transition(
                    CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease)
                )
        )
    }
}
