package web.pages.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
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
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAir
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCheck
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDryCleaning
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEco
import com.varabyte.kobweb.silk.components.icons.mdi.MdiInfo
import com.varabyte.kobweb.silk.components.icons.mdi.MdiIron
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalShipping
import com.varabyte.kobweb.silk.components.icons.mdi.MdiWash
import com.varabyte.kobweb.silk.components.icons.mdi.MdiWavingHand
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import data.AdminProductGetByIdQuery
import data.type.Size
import data.type.Trait
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import feature.shop.cart.CartContract
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.H1Variant
import web.HeadlineStyle
import web.components.widgets.AppTooltip
import web.pages.home.gridModifier
import web.pages.product.catalogue.ProductSizeItem
import web.util.descriptionString
import web.util.onEnterKeyDown
import web.util.titleString

@Composable
fun ProductInfo(
    vm: ProductPageViewModel,
    productPageState: ProductPageContract.State,
    cartState: CartContract.State,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Vendor(productPageState)
        Name(productPageState)
        Price(productPageState, cartState)
        Stock(productPageState)
        SpendMore(
            showSpendMore = cartState.showSpendMore,
            currency = cartState.currency,
            spendMoreValue = cartState.spendMoreValue,
            spendMoreKey = cartState.spendMoreKey,
        )
        Box(Modifier.size(0.5.em))
        Traits(productPageState)
        Box(Modifier.size(0.5.em))
        ColorsSection(vm, productPageState)
        SizesSection(vm, productPageState)
    }
}

@Composable
fun SizesSection(
    vm: ProductPageViewModel,
    state: ProductPageContract.State,
) {
    if (state.sizes.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gap(0.5.em)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.gap(0.5.em)
            ) {
                SpanText(
                    text = getString(Strings.Size).uppercase(),
                    modifier = Modifier
                        .color(ColorMode.current.toPalette().color)
                        .fontWeight(FontWeight.SemiBold)
                )
                var hovered by remember { mutableStateOf(false) }
                MdiInfo(
                    style = IconStyle.OUTLINED,
                    modifier = Modifier
                        .onClick { vm.trySend(ProductPageContract.Inputs.OnSizeGuideClicked) }
                        .onEnterKeyDown { vm.trySend(ProductPageContract.Inputs.OnSizeGuideClicked) }
                        .fontSize(16.px)
                        .onMouseOver { hovered = true }
                        .onMouseLeave { hovered = false }
                        .onFocusIn { hovered = true }
                        .onFocusOut { hovered = false }
                        .tabIndex(0)
                        .cursor(Cursor.Pointer)
                        .opacity(if (hovered) 1.0 else 0.5)
                        .transition(Transition.of("opacity", 0.3.s, TransitionTimingFunction.Ease))
                )
            }
            Row(
                modifier = gridModifier(4, rowMinHeight = 30.px, gap = 0.5.em)
            ) {
                state.sizes.forEach { size ->
                    ProductSizeItem(
                        size = Size.valueOf(size),
                        selected = state.selectedSize == size,
                        available = size in state.sizesForColor,
                        onClick = { vm.trySend(ProductPageContract.Inputs.OnSizeClicked(size)) },
                    )
                }
            }
        }
    }
}

@Composable
fun ColorsSection(vm: ProductPageViewModel, state: ProductPageContract.State) {
    if (state.colors.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gap(0.5.em)
        ) {
            SpanText(
                text = getString(Strings.Color).uppercase(),
                modifier = Modifier
                    .color(ColorMode.current.toPalette().color)
                    .fontWeight(FontWeight.SemiBold)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(0.5.em)
            ) {
                state.colors.forEach { color ->
                    ColorMiniatureItem(
                        media = color.media?.let {
                            AdminProductGetByIdQuery.Medium(
                                keyName = it.keyName,
                                url = it.url,
                                alt = it.alt,
                                type = it.type,
                            )
                        },
                        selected = state.selectedColor?.contains(color.value) == true,
                        onClick = { vm.trySend(ProductPageContract.Inputs.OnColorClicked(color.value)) },
                    )
                    AppTooltip(color.value)
                }
            }
        }
    }
}

@Composable
private fun ColorMiniatureItem(
    media: AdminProductGetByIdQuery.Medium?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(80.px)
            .aspectRatio(.6f)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .cursor(if (!selected) Cursor.Pointer else Cursor.Auto)
            .tabIndex(0)
            .onClick { if (!selected) onClick() }
            .onEnterKeyDown { if (!selected) onClick() }
            .overflow(Overflow.Hidden)
            .userSelect(UserSelect.None)
            .draggable(false)
            .border(
                width = 4.px,
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
            .scale(if (hovered) 1.02f else 1f)
            .transition(
                Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("border", 0.3.s, TransitionTimingFunction.Ease)
            )
    ) {
        media?.let {
            Image(
                src = media.url,
                alt = media.alt,
                modifier = Modifier
                    .fillMaxSize()
                    .objectFit(ObjectFit.Cover)
                    .draggable(false)
            )
        }
    }
}

@Composable
fun Traits(state: ProductPageContract.State) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        state.product.traits.forEach { trait ->
            var hovered by remember { mutableStateOf(false) }
            println("trait: $trait hovered: $hovered")

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .gap(1.em)
                    .color(ColorMode.current.toPalette().color)
                    .onMouseOver { hovered = true }
                    .onMouseLeave { hovered = false }
                    .onFocusIn { hovered = true }
                    .onFocusOut { hovered = false }
                    .tabIndex(0)
                    .cursor(Cursor.Help)
            ) {
                trait.icon(
                    Modifier.fontSize(40.px)
                )
                SpanText(
                    text = trait.titleString(),
                    modifier = Modifier.fontSize(20.px)
                )
                MdiInfo(
                    style = IconStyle.OUTLINED,
                    modifier = Modifier
                        .fontSize(16.px)
                        .opacity(if (hovered) 1.0 else 0.5)
                        .transition(Transition.of("opacity", 0.3.s, TransitionTimingFunction.Ease))
                )
            }
            AppTooltip(trait.descriptionString())
        }
    }
}

@Composable
fun Trait.icon(modifier: Modifier) = when (this) {
    Trait.Handmade -> MdiWavingHand(modifier, IconStyle.OUTLINED)
    Trait.Organic -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.EcoFriendly -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Vegan -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Custom -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Unique -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Trending -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Popular -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Featured -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Recommended -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Special -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Exclusive -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Limited -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.NewArrival -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Seasonal -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Vintage -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Luxury -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Casual -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Formal -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.BusinessCasual -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Athletic -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Outdoor -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.WaterResistant -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Insulated -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.Breathable -> MdiAir(modifier, IconStyle.OUTLINED)
    Trait.Stretch -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.NonIron -> MdiIron(modifier, IconStyle.OUTLINED)
    Trait.EasyCare -> MdiEco(modifier, IconStyle.OUTLINED)
    Trait.MachineWashable -> MdiWash(modifier, IconStyle.OUTLINED)
    Trait.DryCleanOnly -> MdiDryCleaning(modifier, IconStyle.OUTLINED)
    Trait.UNKNOWN__ -> MdiDryCleaning(modifier, IconStyle.OUTLINED)
}

@Composable
fun SpendMore(
    showSpendMore: Boolean,
    currency: Currency,
    spendMoreValue: String,
    spendMoreKey: String,
    bgColor: Color = Colors.Transparent,
) {
    if (showSpendMore) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .gap(0.5.em)
                .fillMaxWidth()
                .border(
                    width = 2.px,
                    color = ColorMode.current.toPalette().border,
                    style = LineStyle.Solid
                )
                .backgroundColor(bgColor)
                .color(ColorMode.current.toPalette().color)
                .padding(0.5.em)
        ) {
            MdiLocalShipping(
                style = IconStyle.OUTLINED,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .gap(0.25.em)
                    .fontSize(16.px)
            ) {
                SpanText(
                    text = getString(Strings.Spend),
                    modifier = Modifier
                )
                SpanText(
                    text = "${currency.symbol}${spendMoreValue}".uppercase(),
                    modifier = Modifier.fontWeight(FontWeight.SemiBold)
                )
                SpanText(
                    text = getString(Strings.To).lowercase(),
                    modifier = Modifier
                )
                SpanText(
                    text = spendMoreKey.uppercase(),
                    modifier = Modifier.fontWeight(FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
private fun Name(state: ProductPageContract.State) {
    SpanText(
        text = state.product.name.uppercase(),
        modifier = HeadlineStyle.toModifier(H1Variant)
            .color(ColorMode.current.toPalette().color)
            .fontWeight(FontWeight.Bold)
    )
}

@Composable
private fun Vendor(state: ProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(0.25.em)
            .color(ColorMode.current.toPalette().color)
    ) {
        SpanText(
            text = "${getString(Strings.Vendor).uppercase()}:",
            modifier = Modifier.fontWeight(FontWeight.Bold)
        )
        SpanText(
            text = state.product.vendor.uppercase(),
            modifier = Modifier
        )
    }
}

@Composable
private fun Price(
    productPageState: ProductPageContract.State,
    cartState: CartContract.State,
) {
    ProductPrice(
        currentPrice = productPageState.product.pricing.regularPrice.toString(),
        salePrice = productPageState.product.pricing.salePrice?.toString(),
        currency = cartState.currency,
        containerModifier = Modifier
            .padding(bottom = if (productPageState.product.pricing.salePrice != null) 0.25.em else 0.em)
    )
}

@Composable
private fun Stock(state: ProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(0.5.em)
    ) {
        MdiCheck(
            modifier = Modifier
                .padding(4.px)
                .borderRadius(50.percent)
                .fontSize(16.px)
        )
        SpanText(
            text = "${state.stockStatusString}!",
            modifier = Modifier
                .fontWeight(FontWeight.Bold)
        )
    }
}
