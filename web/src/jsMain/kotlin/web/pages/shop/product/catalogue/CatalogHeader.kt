package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import data.type.ProductsSort
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import theme.MaterialTheme
import theme.roleStyle
import web.components.sections.desktopNav.AppMenu
import web.components.widgets.ShimmerButton
import web.components.widgets.ShimmerText
import web.util.glossy

@Composable
fun CatalogueHeader(vm: CatalogViewModel, state: CatalogContract.State) {
    val scope = rememberCoroutineScope()
    var scheduled: Job? = null

    var isFiltersButtonHovered by remember { mutableStateOf(false) }
    var isFiltersButtonFocused by remember { mutableStateOf(false) }
    var isMenuFocused by remember { mutableStateOf(false) }
    var isMenuHovered by remember { mutableStateOf(isFiltersButtonHovered) }
    var open by remember { mutableStateOf(isFiltersButtonHovered) }

    fun scheduleCloseMenu() {
        scheduled = scope.launch {
            delay(1000)
            if (!(isFiltersButtonHovered || isMenuHovered)) {
                open = false
            }
        }
    }

    LaunchedEffect(isFiltersButtonHovered, isMenuHovered) {
        if (isFiltersButtonHovered || isMenuHovered) {
            scheduled?.cancel()
            open = true
        } else {
            scheduleCloseMenu()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (!state.isCatalogConfigLoading) {
            SpanText(
                text = "${state.currentVariantOptions.total} products",
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodyLarge)
                    .color(MaterialTheme.colors.onSurface)
            )
        } else {
            ShimmerText(Modifier.width(100.px))
        }
        Spacer()
        Span(
            Modifier
                .position(Position.Relative)
                .toAttrs()
        ) {
            if (!state.isCatalogConfigLoading) {
                FiltersButton(
                    sortByText = getString(Strings.SortBy),
                    currentFilter = state.sortBy.rawValue, // TODO: Localize
                    menuOpened = open,
                    onClick = { open = !open },
                    modifier = Modifier
                        .onMouseEnter { isFiltersButtonHovered = true }
                        .onMouseLeave { isFiltersButtonHovered = false }
                        .onFocusIn { isFiltersButtonFocused = true }
                        .onFocusOut { isFiltersButtonFocused = false }
                        .tabIndex(0)
                )
            } else {
                ShimmerButton(Modifier.width(170.px))
            }
            AppMenu(
                open = open || isFiltersButtonFocused || isMenuHovered || isMenuFocused,
                items = ProductsSort.knownEntries.map { it.rawValue },
                onItemSelected = {
                    open = false
                    vm.trySend(CatalogContract.Inputs.OnSortBySelected(it))
                },
                modifier = Modifier
                    .margin(top = 10.px)
                    .onMouseOver { isMenuHovered = true }
                    .onMouseOut {
                        isMenuHovered = false
                        scheduleCloseMenu()
                    }
                    .onFocusIn { isMenuFocused = true }
                    .onFocusOut { isMenuFocused = false }
                    .tabIndex(0)
            )
        }
    }
}

@Composable
private fun FiltersButton(
    modifier: Modifier,
    sortByText: String,
    currentFilter: String,
    menuOpened: Boolean,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
    val borderColor = when {
        hovered || menuOpened -> MaterialTheme.colors.onSecondaryContainer
        else -> MaterialTheme.colors.secondaryContainer
    }

    Box(
        modifier = modifier
            .padding(left = 1.em, right = 0.5.em, top = 0.5.em, bottom = 0.5.em)
            .glossy(
                color = MaterialTheme.colors.secondaryContainer,
                borderRadius = 36.px
            )
            .border(
                width = 1.px,
                color = borderColor,
                style = LineStyle.Solid
            )
            .cursor(Cursor.Pointer)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .transition(CSSTransition.group(listOf("border", "background-color"), 0.3.s, TransitionTimingFunction.Ease))
            .onClick { onClick() }
            .userSelect(UserSelect.None)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(0.25.em)
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
                    modifier = Modifier
                        .fontWeight(FontWeight.Bold)
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
