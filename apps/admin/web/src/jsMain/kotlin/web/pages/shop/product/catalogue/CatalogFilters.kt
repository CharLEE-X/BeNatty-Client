package web.pages.shop.product.catalogue

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
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.functions.saturate
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
import com.varabyte.kobweb.compose.ui.modifiers.backdropFilter
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoRows
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
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
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBrokenImage
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.focusOutline
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import data.type.Size
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.w3c.dom.Element
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.ExpandableSection
import web.components.widgets.themeScrollbarStyle
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
            .fillMaxWidth()
            .position(Position.Sticky)
            .top(0.px)
            .bottom(0.px)
            .overflow {
                x(Overflow.Hidden)
                y(Overflow.Auto)
            }
            .flex("0 0 auto")
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
                CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("gap", 0.3.s, TransitionTimingFunction.Ease)
            )
    ) {
        HorizontalDivider()
        ExpandableSection(title = getString(Strings.ProductType), openInitially = true) {
            CategoryFilters(vm, state)
        }
        HorizontalDivider()
        ExpandableSection(title = getString(Strings.Color), openInitially = true) {
            ColorFilters(vm, state)
        }
        HorizontalDivider()
        ExpandableSection(title = getString(Strings.Size), openInitially = true) {
            SizeFilters(vm, state)
        }
        HorizontalDivider()
        ExpandableSection(title = getString(Strings.Price), openInitially = false) {
            PriceFilters(vm, state)
        }
        HorizontalDivider()
        ExpandableSection(title = getString(Strings.Attributes), openInitially = false) {
            AttributeFilters(vm, state)
        }
        HorizontalDivider()
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
            modifier = Modifier
                .fillMaxWidth()
                .gap(0.5.em)
        ) {
            AppOutlinedTextField(
                text = state.priceFrom ?: "",
                onTextChanged = { vm.trySend(CatalogContract.Inputs.OnPriceFromChanged(it)) },
                placeholder = getString(Strings.From),
                modifier = Modifier.weight(1f)
            )
            AppOutlinedTextField(
                text = state.priceTo ?: "",
                onTextChanged = { vm.trySend(CatalogContract.Inputs.OnPriceToChanged(it)) },
                placeholder = getString(Strings.To),
                modifier = Modifier.weight(1f)
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
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
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
                .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
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
private fun CategoryFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(3) { size(1.fr) } }
            .gridAutoRows { minmax(50.px, 1.fr) }
            .gap(0.5.em)
    ) {
        state.allCatalogFilterOptions.categories.forEach { category ->
            val enabled = category.id in state.currentVariantOptions.categories
            val selected = category.id in state.selectedCategoryIds
            var hovered by remember { mutableStateOf(false) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(0.25.em)
                    .onClick { vm.trySend(CatalogContract.Inputs.OnCategoryClicked(category.id)) }
                    .userSelect(UserSelect.None)
                    .cursor(Cursor.Pointer)
                    .onMouseOver { if (enabled) hovered = true }
                    .onMouseLeave { hovered = false }
                    .onFocusIn { if (enabled) hovered = true }
                    .onFocusOut { hovered = false }
                    .thenIf(enabled, Modifier.tabIndex(0))
                    .onEnterKeyDown { vm.trySend(CatalogContract.Inputs.OnCategoryClicked(category.id)) }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .border(
                            width = 1.px,
                            color = if (category.id in state.selectedCategoryIds)
                                ColorMode.current.toPalette().color else Colors.Transparent
                        )
                        .objectFit(ObjectFit.Fill)
                        .transition(
                            CSSTransition("transform", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
                        )
                ) {
                    category.mediaUrl?.let { image ->
                        Image(
                            src = image,
                            alt = category.name,
                            modifier = Modifier
                                .fillMaxSize(70.percent)
                                .border(
                                    width = 1.px,
                                    color = if (category.id in state.selectedCategoryIds)
                                        ColorMode.current.toPalette().color else Colors.Transparent
                                )
                                .scale(if (hovered && enabled) 1.1f else 1f)
                                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
                        )
                    } ?: run {
                        MdiBrokenImage(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .color(ColorMode.current.toPalette().color)
                        )
                    }
                    if (!enabled) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .backdropFilter(saturate(0.2))
                        )
                        Cross()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.px,
                                color = if (selected) ColorMode.current.toPalette().color else Colors.Transparent,
                                style = LineStyle.Solid
                            )
                            .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.px)
                                .border(
                                    width = 2.px,
                                    color =
                                    if (selected) ColorMode.current.toPalette().background else Colors.Transparent,
                                    style = LineStyle.Solid
                                )
                                .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
                        )
                    }
                }
                SpanText(
                    text = category.name,
                    modifier = Modifier
                        .fontWeight(if (hovered && enabled) FontWeight.SemiBold else FontWeight.Normal)
                        .transition(CSSTransition("font-weight", 0.3.s, TransitionTimingFunction.Ease))
                )
            }
        }
    }
}

@Composable
private fun ColorFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(5) { size(1.fr) } }
            .gridAutoRows { minmax(40.px, 1.fr) }
            .gap(0.5.em)
    ) {
        state.allCatalogFilterOptions.colors.forEachIndexed { index, colorOption ->
            val enabled = colorOption.color in state.currentVariantOptions.colors
            val selected = colorOption.color in state.selectedColors
            var hovered by remember { mutableStateOf(false) }

            val circleModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(
                    width = if (colorOption.color == data.type.Color.WHITE || colorOption.color == data.type.Color.YELLOW)
                        1.px else 0.px,
                    color = ColorMode.current.toPalette().focusOutline,
                    style = LineStyle.Solid
                )
                .onClick { vm.trySend(CatalogContract.Inputs.OnColorClicked(colorOption.color)) }
                .userSelect(UserSelect.None)
                .cursor(Cursor.Pointer)
                .tabIndex(0)
                .onEnterKeyDown { vm.trySend(CatalogContract.Inputs.OnColorClicked(colorOption.color)) }
                .onMouseOver { hovered = true }
                .onMouseOut { hovered = false }
                .scale(if (hovered) 1.1f else 1f)
                .translateX(if (hovered && (index % 5 == 0)) 4.px else 0.px)
                .transition(
                    CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                )

            if (colorOption.color == data.type.Color.MULTICOLOR) {
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
                    modifier = circleModifier.backgroundColor(Color(colorOption.hex))
                ) {
                    if (!enabled) {
                        Cross(
                            color = when (colorOption.color) {
                                data.type.Color.BLACK,
                                data.type.Color.NAVY,
                                data.type.Color.BLUE,
                                data.type.Color.RED,
                                data.type.Color.PURPLE -> ColorMode.current.toPalette().background

                                else -> ColorMode.current.toPalette().color
                            },
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 2.px,
                                color = if (selected) ColorMode.current.toPalette().color else Colors.Transparent,
                                style = LineStyle.Solid
                            )
                            .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.px)
                                .border(
                                    width = 2.px,
                                    color = if (selected) ColorMode.current.toPalette().background else Colors.Transparent,
                                    style = LineStyle.Solid
                                )
                                .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SizeFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(2) { size(1.fr) } }
            .gridAutoRows { minmax(40.px, 1.fr) }
            .gap(0.5.em)
    ) {
        state.allCatalogFilterOptions.sizes.forEachIndexed { index, size ->
            ProductSizeItem(
                size = size,
                selected = size in state.selectedSizes,
                available = size in state.currentVariantOptions.sizes,
                isLeft = index % 2 == 0,
                onClick = { vm.trySend(CatalogContract.Inputs.OnSizeClicked(size)) },
            )
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
                CSSTransition("translate", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        SpanText(
            text = size.name,
            modifier = Modifier
                .textDecorationLine(if (!available) TextDecorationLine.LineThrough else TextDecorationLine.None)
                .fontWeight(if (!available) FontWeight.Normal else FontWeight.SemiBold)
                .transition(
                    CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("font-weight", 0.3.s, TransitionTimingFunction.Ease),
                )
        )
    }
}
