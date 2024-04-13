package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
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
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backdropFilter
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoRows
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBrokenImage
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.ExpandableSection
import web.compose.material3.component.Divider
import web.util.cornerRadius
import web.util.onEnterKeyDown

@Composable
fun CatalogueFilters(
    modifier: Modifier,
    vm: CatalogViewModel,
    state: CatalogContract.State,
) {
    Column(
        modifier = modifier
            .gap(2.em)
    ) {
        Divider(modifier = Modifier.color(MaterialTheme.colors.outline))
        ExpandableSection(title = getString(Strings.ProductType), openInitially = true) {
            ProductTypeFilters(vm, state)
        }
        Divider(modifier = Modifier.color(MaterialTheme.colors.outline))
        ExpandableSection(title = getString(Strings.Color), openInitially = true) {
            ColorFilters(vm, state)
        }
        Divider(modifier = Modifier.color(MaterialTheme.colors.outline))
        ExpandableSection(title = getString(Strings.Size), openInitially = true) {
            SizeFilters(vm, state)
        }
        Divider(modifier = Modifier.color(MaterialTheme.colors.outline))
        ExpandableSection(title = getString(Strings.Price), openInitially = true) {
            PriceFilters(
                highestPrice = state.variantOptions.highestPrice,
                priceFrom = state.priceFrom,
                priceTo = state.priceTo,
                onPriceFromChanged = { vm.trySend(CatalogContract.Inputs.OnPriceFromChanged(it)) },
                onPriceToChanged = { vm.trySend(CatalogContract.Inputs.OnPriceToChanged(it)) },
                onResetClicked = { vm.trySend(CatalogContract.Inputs.OnPriceResetClicked) }
            )
        }
        Divider(modifier = Modifier.color(MaterialTheme.colors.outline))
    }
}

@Composable
private fun PriceFilters(
    highestPrice: Double?,
    priceFrom: String?,
    priceTo: String?,
    onPriceFromChanged: (String) -> Unit,
    onPriceToChanged: (String) -> Unit,
    onResetClicked: () -> Unit
) {
    Column(
        modifier = Modifier.gap(0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            highestPrice?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .gap(0.5.em)
                ) {
                    SpanText(
                        text = getString(Strings.TheHighestPriceIs),
                        modifier = Modifier
                            .color(MaterialTheme.colors.onSurface)
                            .roleStyle(MaterialTheme.typography.bodyMedium)
                    )
                    SpanText(
                        text = "£$highestPrice", // TODO: Localize currency
                        modifier = Modifier
                            .color(MaterialTheme.colors.onSurface)
                            .roleStyle(MaterialTheme.typography.bodyMedium)
                    )
                }
            } ?: Box(Modifier.weight(1f))
            ResetButton(
                show = priceFrom != null || priceTo != null,
                onClick = onResetClicked,
                modifier = Modifier.translateY(2.px)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .gap(0.5.em)
        ) {
            AppOutlinedTextField(
                placeholder = getString(Strings.From),
                value = priceFrom ?: "",
                onValueChange = onPriceFromChanged,
                prefixText = "£",
                modifier = Modifier.weight(1f)
            )
            AppOutlinedTextField(
                placeholder = getString(Strings.To),
                value = priceTo ?: "",
                onValueChange = onPriceToChanged,
                prefixText = "£",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ResetButton(
    modifier: Modifier = Modifier,
    show: Boolean = false,
    color: CSSColorValue = MaterialTheme.colors.onSurface,
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
                .roleStyle(MaterialTheme.typography.bodyMedium)
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
private fun SizeFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(2) { size(1.fr) } }
            .gridAutoRows { minmax(40.px, 1.fr) }
            .gap(0.5.em)
    ) {
        state.variantOptions.sizes.forEach { sizeFilter ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .minHeight(50.px)
                    .backgroundColor(
                        if (sizeFilter.size in state.selectedSizes)
                            MaterialTheme.colors.onSurface else Colors.Transparent
                    )
                    .borderRadius(40.px)
                    .border(
                        width = 1.px,
                        color = MaterialTheme.colors.outline,
                        style = LineStyle.Solid
                    )
                    .padding(leftRight = 16.px, topBottom = 10.px)
                    .onClick { if (sizeFilter.enabled) vm.trySend(CatalogContract.Inputs.OnSizeClicked(sizeFilter.size)) }
                    .userSelect(UserSelect.None)
                    .cursor(if (sizeFilter.enabled) Cursor.Pointer else Cursor.Auto)
                    .thenIf(sizeFilter.enabled, Modifier.tabIndex(0))
                    .onEnterKeyDown {
                        if (sizeFilter.enabled) vm.trySend(CatalogContract.Inputs.OnSizeClicked(sizeFilter.size))
                    }
                    .transition(
                        CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease)
                    )
            ) {
                SpanText(
                    text = sizeFilter.size.name,
                    modifier = Modifier
                        .textDecorationLine(
                            if (!sizeFilter.enabled) TextDecorationLine.LineThrough else TextDecorationLine.None
                        )
                        .fontWeight(if (!sizeFilter.enabled) FontWeight.Light else FontWeight.Normal)
                        .roleStyle(MaterialTheme.typography.bodyMedium)
                        .color(
                            if (sizeFilter.size in state.selectedSizes)
                                MaterialTheme.colors.surface else MaterialTheme.colors.onSurface
                        )
                        .transition(CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease))
                )
            }
        }
    }
}

@Composable
private fun Cross(
    modifier: Modifier = Modifier,
    color: CSSColorValue = MaterialTheme.colors.onSurface
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
private fun ColorFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(5) { size(1.fr) } }
            .gridAutoRows { minmax(40.px, 1.fr) }
            .gap(0.5.em)
    ) {
        state.variantOptions.colors.forEach { color ->
            val selected = color.color in state.selectedColors
            var hovered by remember { mutableStateOf(false) }

            val circleModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .borderRadius(50.percent)
                .border(
                    width = if (color.color == data.type.Color.WHITE || color.color == data.type.Color.YELLOW)
                        1.px else 0.px,
                    color = MaterialTheme.colors.outline,
                    style = LineStyle.Solid
                )
                .onClick { if (color.enabled) vm.trySend(CatalogContract.Inputs.OnColorClicked(color.color)) }
                .userSelect(UserSelect.None)
                .cursor(if (color.enabled) Cursor.Pointer else Cursor.Auto)
                .thenIf(color.enabled, Modifier.tabIndex(0))
                .tabIndex(0)
                .onEnterKeyDown { if (color.enabled) vm.trySend(CatalogContract.Inputs.OnColorClicked(color.color)) }
                .onMouseOver { hovered = true }
                .onMouseOut { hovered = false }
                .scale(if (hovered && color.enabled) 1.1f else 1f)
                .transition(
                    CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
                    CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                )

            if (color.color == data.type.Color.MULTICOLOR) {
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
                    if (!color.enabled) {
                        Cross(
                            color = MaterialTheme.colors.background,
                            modifier = Modifier.borderRadius(50.percent)
                        )
                    }
                }
            } else {
                Box(
                    modifier = circleModifier.backgroundColor(Color(color.hex))
                ) {
                    if (!color.enabled) {
                        Cross(
                            color = when (color.color) {
                                data.type.Color.BLACK,
                                data.type.Color.NAVY,
                                data.type.Color.BLUE,
                                data.type.Color.RED,
                                data.type.Color.PURPLE -> MaterialTheme.colors.background

                                else -> MaterialTheme.colors.onSurface
                            },
                            modifier = Modifier.borderRadius(50.percent)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .borderRadius(50.percent)
                            .border(
                                width = if (selected) 2.px else 1.px,
                                color = if (selected) MaterialTheme.colors.onSurface else Colors.Transparent,
                                style = LineStyle.Solid
                            )
                            .transition(CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.px)
                                .borderRadius(50.percent)
                                .border(
                                    width = 2.px,
                                    color = if (selected) MaterialTheme.colors.background else Colors.Transparent,
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
private fun ProductTypeFilters(vm: CatalogViewModel, state: CatalogContract.State) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { repeat(3) { size(1.fr) } }
            .gridAutoRows { minmax(50.px, 1.fr) }
            .gap(0.5.em)
    ) {
        state.variantOptions.categories.forEach { category ->
            var hovered by remember { mutableStateOf(false) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(0.25.em)
                    .onClick { vm.trySend(CatalogContract.Inputs.OnCategoryClicked(category.id)) }
                    .userSelect(UserSelect.None)
                    .cursor(if (category.enabled) Cursor.Pointer else Cursor.Auto)
                    .onMouseOver { if (category.enabled) hovered = true }
                    .onMouseOver { if (category.enabled) hovered = false }
                    .thenIf(category.enabled, Modifier.tabIndex(0))
                    .onEnterKeyDown {
                        if (category.enabled) vm.trySend(CatalogContract.Inputs.OnCategoryClicked(category.id))
                    }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .borderRadius(cornerRadius)
                        .border(
                            width = 1.px,
                            color = if (category.id in state.selectedCategoryIds)
                                MaterialTheme.colors.onSurface else Colors.Transparent
                        )
                        .backgroundColor(MaterialTheme.colors.surfaceContainerHighest)
                        .scale(if (hovered && category.enabled) 1.05f else 1f)
                        .objectFit(ObjectFit.Fill)
                        .transition(
                            CSSTransition("transform", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
                        )
                ) {
                    category.imageUrl?.let { image ->
                        Image(
                            src = image,
                            alt = category.name,
                            modifier = Modifier
                                .border(
                                    width = 1.px,
                                    color = if (category.id in state.selectedCategoryIds)
                                        MaterialTheme.colors.onSurface else Colors.Transparent
                                )
                        )
                    } ?: run {
                        MdiBrokenImage(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .color(MaterialTheme.colors.onSurface)
                        )
                    }
                    if (!category.enabled) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .borderRadius(cornerRadius)
                                .backdropFilter(saturate(0.2))
                        )
                        Cross(Modifier.borderRadius(cornerRadius))
                    }
                }
                SpanText(
                    text = category.name,
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.bodyMedium)
                        .fontWeight(if (hovered && category.enabled) FontWeight.SemiBold else FontWeight.Normal)
                        .transition(CSSTransition("font-weight", 0.3.s, TransitionTimingFunction.Ease))
                )
            }
        }
    }
}
