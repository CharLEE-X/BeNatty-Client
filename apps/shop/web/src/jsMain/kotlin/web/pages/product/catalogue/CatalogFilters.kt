package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.dom.svg.Circle
import com.varabyte.kobweb.compose.dom.svg.Defs
import com.varabyte.kobweb.compose.dom.svg.LinearGradient
import com.varabyte.kobweb.compose.dom.svg.Stop
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.dom.svg.SvgId
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.forms.Checkbox
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.button
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.focusOutline
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import data.type.Size
import data.type.StockStatus
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.w3c.dom.Element
import web.AppColors
import web.SubtitleStyle
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.ExpandableSection
import web.components.widgets.themeScrollbarStyle
import web.pages.home.gridModifier
import web.util.onEnterKeyDown

@Composable
fun CatalogueFilters(
    modifier: Modifier,
    vm: CatalogViewModel,
    state: CatalogContract.State,
) {
    var columnElement: Element? by remember { mutableStateOf(null) }
    var windowHeight: Int by remember { mutableStateOf(window.innerHeight) }
    var filtersHeight: Int by remember { mutableStateOf(0) }
    var showScrollbar by remember { mutableStateOf(filtersHeight > windowHeight) }

    LaunchedEffect(window.innerHeight, columnElement?.scrollHeight) {
        windowHeight = window.innerHeight
        columnElement?.scrollHeight?.let {
            filtersHeight = it
            showScrollbar = it > window.innerHeight
        }
    }

    Column(
        modifier = themeScrollbarStyle.toModifier().then(modifier)
            .maxHeight(windowHeight.px)
            .position(Position.Sticky)
            .top(0.px)
            .bottom(0.px)
            .overflow {
                x(Overflow.Hidden)
                y(Overflow.Auto)
            }
            .padding(
                right = 1.em,
                top = if (showScrollbar) 1.em else 0.em,
                bottom = if (showScrollbar) 1.em else 0.em,
            )
            .attrsModifier {
                ref {
                    filtersHeight = it.scrollHeight
                    columnElement = it
                    onDispose { columnElement = null }
                }
            }
            .gap(if (showScrollbar) 1.em else 1.5.em)
            .transition(
                Transition.of("padding", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("gap", 0.3.s, TransitionTimingFunction.Ease)
            )
    ) {
        ExpandableSection(title = getString(Strings.Availability), openInitially = true) {
            StockStatus(vm, state)
        }
        ExpandableSection(title = getString(Strings.Price), openInitially = true) {
            PriceFilters(vm, state)
        }
        ExpandableSection(title = getString(Strings.Color), openInitially = true) {
            ColorFilters(vm, state)
        }
        ExpandableSection(title = getString(Strings.Size), openInitially = true) {
            SizeFilters(vm, state)
        }
        ExpandableSection(title = getString(Strings.ProductType), openInitially = true) {
            ProductTypeSection(vm, state)
        }
//        ExpandableSection(title = getString(Strings.Attributes), openInitially = false) {
//            AttributeFilters(vm, state)
//        } // TODO: Needs implementing
    }
}

@Composable
private fun PriceFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            state.currentVariantOptions.highestPrice?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .gap(0.5.em)
                ) {
                    SpanText(
                        text = getString(Strings.TheHighestPriceIs),
                        modifier = Modifier
                            .color(ColorMode.current.toPalette().color)
                    )
                    SpanText(
                        text = "£${it}", // TODO: Localize currency
                        modifier = Modifier
                            .color(ColorMode.current.toPalette().color)
                    )
                }
            } ?: Box(Modifier.weight(1f))
            ResetButton(
                show = state.priceFrom != null || state.priceTo != null,
                onClick = { vm.trySend(CatalogContract.Inputs.OnPriceResetClicked) },
                modifier = Modifier.translateY(2.px)
            )
        }
        Row(
            modifier = gridModifier(2, gap = 0.5.em)
        ) {
            AppOutlinedTextField(
                text = state.priceFrom ?: "",
                onTextChange = { vm.trySend(CatalogContract.Inputs.OnPriceFromChanged(it)) },
                placeholder = getString(Strings.From),
            )
            AppOutlinedTextField(
                text = state.priceTo ?: "",
                onTextChange = { vm.trySend(CatalogContract.Inputs.OnPriceToChanged(it)) },
                placeholder = getString(Strings.To),
            )
        }
    }
}

@Composable
fun AttributeFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .gap(0.5.em)
    ) {
        SpanText("FIXME: Implement attribute filters")
    }
//        ChipSet {
//            Trait.entries.toList().filter { it != Trait.UNKNOWN__ }.forEach { trait ->
//                FilterChip(
//                    label = trait.name.enumCapitalized(),
//                    selected = trait in state.selectedTraits,
//                    iconSize = 0.px.toString(),
//                    onClick = { vm.trySend(CatalogContract.Inputs.OnTraitClicked(trait)) },
//                )
//            }
//        }
}

@Composable
private fun ResetButton(
    modifier: Modifier = Modifier,
    show: Boolean = false,
    color: CSSColorValue = ColorMode.current.toPalette().color,
    onClick: () -> Unit
) {
    var hovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .onClick { onClick() }
            .gap(2.px)
            .cursor(if (show) Cursor.Pointer else Cursor.Auto)
            .thenIf(
                show,
                Modifier
                    .tabIndex(0)
                    .onEnterKeyDown(onClick)
            )
            .opacity(if (show) 1f else 0f)
            .onMouseOver { hovered = true }
            .onMouseOut { hovered = false }
            .transition(Transition.of("opacity", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        SpanText(
            text = getString(Strings.Reset).uppercase(),
            modifier = Modifier
                .color(color)
                .whiteSpace(WhiteSpace.NoWrap)
        )
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .height(2.px)
                .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                .backgroundColor(color)
                .transition(Transition.of("width", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}

@Composable
private fun Cross(
    modifier: Modifier = Modifier,
    color: CSSColorValue = ColorMode.current.toPalette().color
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .overflow(Overflow.Hidden)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(2.px)
                .height(100.percent)
                .backgroundColor(color)
                .rotate(45.deg)
                .scale(1.25)
        )
    }
}

@Composable
private fun StockStatus(vm: CatalogViewModel, state: CatalogContract.State) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        state.currentVariantOptions.stockStatuses.forEach { item ->
            CheckableItem(
                label = item.name,
                count = item.count,
                checked = item.name in state.selectedStockStatuses.map { it.name },
                onCheckedChange = {
                    vm.trySend(CatalogContract.Inputs.OnAvailabilityClicked(StockStatus.valueOf(item.name)))
                },
                onClick = { vm.trySend(CatalogContract.Inputs.OnAvailabilityClicked(StockStatus.valueOf(item.name))) },
            )
        }
    }
}

fun StockStatus.name(): String = when (this) {
    StockStatus.InStock -> getString(Strings.InStock)
    else -> getString(Strings.OutOfStock)
}

@Composable
fun CheckableItem(
    label: String,
    count: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .gap(0.5.em)
            .userSelect(UserSelect.None)
            .cursor(Cursor.Pointer)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .onEnterKeyDown { onClick() }
            .tabIndex(0)
            .fontWeight(if (checked || hovered) FontWeight.SemiBold else FontWeight.Normal)
            .transition(Transition.of("font-weight", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        SpanText(
            text = label,
            modifier = Modifier
                .onClick { onClick() }
        )
        SpanText("(${count})",
            modifier = SubtitleStyle.toModifier()
                .onClick { onClick() }
        )
    }
}

@Composable
private fun ProductTypeSection(vm: CatalogViewModel, state: CatalogContract.State) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        state.allCatalogFilterOptions.categories.forEach { category ->
            val currentCategory = state.currentVariantOptions.productTypes
                .find { it.id == category.id }

            currentCategory?.let {
                CheckableItem(
                    label = it.name,
                    count = it.count,
                    checked = category.id in state.selectedCategoryIds,
                    onCheckedChange = { vm.trySend(CatalogContract.Inputs.OnCategoryClicked(category.id)) },
                    onClick = { vm.trySend(CatalogContract.Inputs.OnCategoryClicked(category.id)) },
                )
            }
        }
    }
}

@Composable
private fun ColorFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = gridModifier(2, gap = 0.5.em, rowMinHeight = 20.px)
    ) {
        state.allCatalogFilterOptions.colors.forEach { option ->
            val enabled = option.color.name in state.currentVariantOptions.colors.map { it.name }
            val selected = option.color in state.selectedColors
            val currentColor = state.currentVariantOptions.colors
                .find { it.name == option.color.name }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.5.em)
                    .gap(0.5.em)
                    .border(
                        width = 2.px,
                        color = if (selected) ColorMode.current.toPalette().button.default else AppColors.divider(),
                        style = LineStyle.Solid
                    )
                    .userSelect(UserSelect.None)
                    .cursor(Cursor.Pointer)
                    .onClick { vm.trySend(CatalogContract.Inputs.OnColorClicked(option.color)) }
                    .onEnterKeyDown { vm.trySend(CatalogContract.Inputs.OnColorClicked(option.color)) }
                    .tabIndex(0)
                    .overflow(Overflow.Hidden)
            ) {
                ColorCircle(
                    color = option.color,
                    hex = option.hex,
                    enabled = enabled,
                )
                SpanText(
                    text = option.color.name,
                    modifier = Modifier
                        .whiteSpace(WhiteSpace.NoWrap)
                        .textOverflow(TextOverflow.Ellipsis)
                )
                SpanText(
                    text = "(${currentColor?.count ?: 0})",
                    modifier = SubtitleStyle.toModifier()
                )
            }
        }
    }
}

@Composable
private fun ColorCircle(
    color: data.type.Color,
    hex: String,
    enabled: Boolean,
) {

    val circleModifier = Modifier
        .size(
            if (color == data.type.Color.WHITE || color == data.type.Color.YELLOW)
                33.px else 34.px
        )
        .border(
            width = if (color == data.type.Color.WHITE || color == data.type.Color.YELLOW)
                1.px else 0.px,
            color = ColorMode.current.toPalette().focusOutline,
            style = LineStyle.Solid
        )
        .borderRadius(50.percent)

    if (color == data.type.Color.MULTI) {
        val id = SvgId("multicolor")
        var svgWidth: Int by remember { mutableStateOf(0) }
        var svgHeight: Int by remember { mutableStateOf(0) }

        Box(
            modifier = circleModifier
        ) {
            Svg(attrs = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .toAttrs {
                    ref {
                        svgWidth = it.width.baseVal.value.toInt()
                        svgHeight = it.height.baseVal.value.toInt()

                        onDispose {}
                    }
                }
            ) {
                Defs {
                    LinearGradient(id, attrs = {
                        x1(0.percent)
                        y1(0.percent)
                        x2(100.percent)
                        y2(0.percent)
                    }) {
                        Stop(0.percent, Colors.Red)
                        Stop(16.percent, Colors.Orange)
                        Stop(32.percent, Colors.Yellow)
                        Stop(48.percent, Colors.Green)
                        Stop(64.percent, Colors.Blue)
                        Stop(80.percent, Colors.Indigo)
                        Stop(90.percent, Colors.Violet)
                        Stop(95.percent, Colors.Pink)
                        Stop(100.percent, Colors.Cyan)
                    }
                }

                Circle {
                    cx(svgWidth / 2)
                    cy(svgHeight / 2)
                    r(svgWidth / 2)
                    fill(id)
                }
            }
            if (!enabled) {
                Cross(
                    color = ColorMode.current.toPalette().background,
                )
            }
        }
    } else {
        Box(
            modifier = circleModifier.backgroundColor(Color(hex))
        ) {
            if (!enabled) {
                Cross(
                    color = when (color) {
                        data.type.Color.BLACK,
                        data.type.Color.NAVY,
                        data.type.Color.BLUE,
                        data.type.Color.RED,
                        data.type.Color.PURPLE -> ColorMode.current.toPalette().background

                        else -> ColorMode.current.toPalette().color
                    },
                )
            }
        }
    }
}

@Composable
private fun SizeFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        state.allCatalogFilterOptions.sizes.forEach { size ->
            val currentSize = state.currentVariantOptions.sizes
                .find { it.name == size.name }

            currentSize?.let {
                CheckableItem(
                    label = it.name,
                    count = it.count,
                    checked = size in state.selectedSizes,
                    onCheckedChange = { vm.trySend(CatalogContract.Inputs.OnSizeClicked(size)) },
                    onClick = { vm.trySend(CatalogContract.Inputs.OnSizeClicked(size)) },
                )
            }
        }
    }
}

@Composable
fun ProductSizeItem(
    size: Size,
    selected: Boolean,
    available: Boolean,
    isLeft: Boolean = false,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .onMouseOver { if (available) hovered = true }
            .onMouseOut { hovered = false }
            .onFocusIn { if (available) hovered = true }
            .onFocusOut { hovered = false }
            .minHeight(50.px)
            .border(
                width = 2.px,
                color = when {
                    selected && available -> Colors.Transparent
                    selected && !available -> Colors.Red
                    else -> ColorMode.current.toPalette().focusOutline
                },
                style = LineStyle.Solid
            )
            .padding(leftRight = 16.px, topBottom = 10.px)
            .onClick { if (available && !selected) onClick() }
            .userSelect(UserSelect.None)
            .cursor(if (available && !selected) Cursor.Pointer else Cursor.Auto)
            .tabIndex(0)
            .onEnterKeyDown { if (available && !selected) onClick() }
            .scale(if (hovered && available) 1.02f else 1f)
            .translateX(if (hovered && isLeft && available) 4.px else 0.px)
            .transition(
                Transition.of("translate", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("background-color", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("border", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        SpanText(
            text = size.name,
            modifier = Modifier
                .textDecorationLine(if (!available) TextDecorationLine.LineThrough else TextDecorationLine.None)
                .fontWeight(if (!available) FontWeight.Normal else FontWeight.SemiBold)
                .transition(
                    Transition.of("color", 0.3.s, TransitionTimingFunction.Ease),
                    Transition.of("font-weight", 0.3.s, TransitionTimingFunction.Ease),
                )
        )
    }
}
