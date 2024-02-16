package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.rotateZ
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowDropDown
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowDropUp
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowUpward
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBrokenImage
import com.varabyte.kobweb.silk.components.icons.mdi.MdiTune
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibility
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.text.SpanText
import data.type.SortDirection
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.admin.list.ListItem
import feature.admin.list.PageInfo
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import org.jetbrains.compose.web.dom.Span
import theme.MaterialTheme
import web.components.widgets.NoItemsListAction
import web.components.widgets.PageHeader
import web.components.widgets.SearchBar
import web.compose.material3.component.CircularProgress
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledButton
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.labs.Menu
import web.compose.material3.component.labs.MenuItem
import web.compose.material3.component.labs.OutlinedSegmentedButtonSet
import web.compose.material3.component.labs.SegmentedButton

@Composable
fun ListPageLayout(
    state: AdminListContract.State,
    vm: AdminListViewModel,
) {
    with(state) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            PageHeader(strings.title) {
                FilledButton(
                    onClick = { vm.trySend(AdminListContract.Inputs.OnCreateClick) },
                    leadingIcon = { MdiAdd() },
                ) {
                    SpanText(text = strings.create)
                }
            }
            Actions(state, vm)

            val gridContainerModifier = Modifier
                .fillMaxWidth()
                .padding(topBottom = 0.5.em, leftRight = 1.em)
                .borderRadius(1.em)
                .display(DisplayStyle.Grid)
                .gap(10.px)
                .gridTemplateColumns { repeat(autoFit) { minmax(120.px, 1.fr) } }

            ListTopBar(
                state = state,
                vm = vm,
                gridContainerModifier = gridContainerModifier
            )
            if (showList) {
                items.forEach { item ->
                    Item(
                        state = state,
                        vm = vm,
                        item = item,
                        gridContainerModifier = gridContainerModifier
                    )
                }
                Spacer()
                PageNavigator(
                    modifier = Modifier.margin(topBottom = 2.em),
                    info = info,
                    pagesNumbers = pagesNumbers,
                    prevText = strings.previous,
                    nextText = strings.next,
                    showPrevious = showPrevious,
                    showNext = showNext,
                    onPageClick = { vm.trySend(AdminListContract.Inputs.OnPageClick(it)) },
                    onPreviousPageClick = { vm.trySend(AdminListContract.Inputs.OnPreviousPageClick) },
                    onNextPageClick = { vm.trySend(AdminListContract.Inputs.OnNextPageClick) },
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (showNoItems) {
                    NoItemsListAction(
                        pressText = strings.press,
                        createText = strings.create,
                        toStartText = strings.toStart,
                        onClick = { vm.trySend(AdminListContract.Inputs.OnCreateClick) }
                    )
                }
                if (isLoading) {
                    CircularProgress(
                        intermediate = true,
                        fourColor = true,
                    )
                }
            }
        }
    }
}

@Composable
fun ListTopBar(
    state: AdminListContract.State,
    vm: AdminListViewModel,
    gridContainerModifier: Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
            .margin(topBottom = 1.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = gridContainerModifier
        ) {
            when (state.dataType) {
                AdminListContract.DataType.USER -> {
                    AdminListContract.UserSlot.entries.forEach { slot ->
                        SlotBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.OnTopBarSlotClick(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.PRODUCT -> {
                    AdminListContract.ProductSlot.entries.forEach { slot ->
                        SlotBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = slot != AdminListContract.ProductSlot.Image,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.OnTopBarSlotClick(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.ORDER -> {
                    AdminListContract.OrderSlot.entries.forEach { slot ->
                        SlotBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.OnTopBarSlotClick(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.CATEGORY -> {
                    AdminListContract.CategorySlot.entries.forEach { slot ->
                        SlotBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.OnTopBarSlotClick(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.TAG -> {
                    AdminListContract.TagSlot.entries.forEach { slot ->
                        SlotBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.OnTopBarSlotClick(slot.name)) },
                        )
                    }
                }
            }
        }
        Divider()
    }
}

@Composable
fun Item(
    state: AdminListContract.State,
    vm: AdminListViewModel,
    item: ListItem,
    gridContainerModifier: Modifier
) {
    var isHovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = gridContainerModifier
            .onMouseEnter { isHovered = true }
            .onMouseLeave { isHovered = false }
            .thenIf(isHovered) { Modifier.backgroundColor(MaterialTheme.colors.mdSysColorSurfaceContainerHigh.value()) }
            .thenIf(isHovered) { Modifier.color(MaterialTheme.colors.mdSysColorOnSurfaceVariant.value()) }
            .onClick { vm.trySend(AdminListContract.Inputs.OnItemClick(item.id)) }
            .cursor(Cursor.Pointer)
            .transition(
                CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        when (state.dataType) {
            AdminListContract.DataType.USER -> {
                SpanText(item.slot1)
                SpanText(item.slot2 ?: "N/A")
                SpanText(item.slot3 ?: "N/A")
                SpanText(item.slot4 ?: "N/A")
            }

            AdminListContract.DataType.PRODUCT -> {
                SpanText(item.slot1)
                item.slot2?.let { url -> MiniImage(url) } ?: MdiBrokenImage(Modifier.size(30.px))
                SpanText(item.slot3 ?: "N/A")
                SpanText(item.slot4 ?: "N/A")
                SpanText(item.slot5 ?: "N/A")
                SpanText(item.slot6 ?: "N/A")
            }

            AdminListContract.DataType.ORDER -> {
                SpanText(item.slot1)
                SpanText(item.slot2 ?: "N/A")
                SpanText(item.slot3 ?: "N/A")
                SpanText(item.slot4 ?: "N/A")
            }

            AdminListContract.DataType.CATEGORY -> {
                SpanText(item.slot1)
                SpanText(item.slot2 ?: "N/A")
                SpanText(item.slot3 ?: "N/A")
                if (item.slot4.toBoolean()) MdiVisibility() else MdiVisibilityOff()
                SpanText(item.slot5 ?: "N/A")
            }

            AdminListContract.DataType.TAG -> {
                SpanText(item.slot1)
                SpanText(item.slot2 ?: "N/A")
                SpanText(item.slot3 ?: "N/A")
            }
        }
    }
}

@Composable
private fun MiniImage(url: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(0.25.em)
            .overflow(Overflow.Hidden)
    ) {
        var hovered by remember { mutableStateOf(false) }
        Image(
            src = url,
            modifier = Modifier
                .size(40.px)
                .borderRadius(5.px)
                .objectFit(ObjectFit.Cover)
                .onMouseEnter { hovered = true }
                .onMouseLeave { hovered = false }
                .scale(if (hovered) 4 else 1.0)
                .transition(CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease))
        )
    }
}

@Composable
private fun SlotBarItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isSortable: Boolean,
    sortDirection: SortDirection,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .onClick { if (isSortable) onClick() }
            .thenIf(isSortable) { Modifier.cursor(Cursor.Pointer) }
            .gap(0.25.em)
    ) {
        SpanText(
            text = text,
            modifier = Modifier
                .userSelect(UserSelect.None)
        )
        if (isSortable) {
            MdiArrowUpward(
                modifier = Modifier
                    .scale(if (isSelected) 1.0 else 0.0)
                    .opacity(if (isSelected) 1.0 else 0.0)
                    .rotateZ(if (sortDirection == SortDirection.ASC) 0.deg else 180.deg)
                    .transition(
                        CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("rotate", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                    )
            )
        }
    }
}

@Composable
fun Actions(
    state: AdminListContract.State,
    vm: AdminListViewModel,
) {
    var isPerPageMenuOpen by remember { mutableStateOf(false) }
    var isFiltersOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .margin(topBottom = 1.em)
            .gap(1.em)
    ) {
        SearchBar(
            value = state.searchValue,
            onValueChange = { vm.trySend(AdminListContract.Inputs.OnChange.Search(it)) },
            placeholder = state.strings.search,
            onEnterPress = { vm.trySend(AdminListContract.Inputs.SendSearch) },
            onSearchIconClick = { },
            containerShape = 30.px,
            modifier = Modifier.height(50.px)
        )
        OutlinedButton(
            onClick = { isFiltersOpen = !isFiltersOpen },
            trailingIcon = { MdiTune() },
            modifier = Modifier.height(50.px)
        ) {
            SpanText(text = state.strings.filter)
        }
        Span(Modifier.position(Position.Relative).toAttrs()) {
            "per-page-anchor".let { anchor ->
                OutlinedButton(
                    onClick = { isPerPageMenuOpen = !isPerPageMenuOpen },
                    trailingIcon = { if (isPerPageMenuOpen) MdiArrowDropUp() else MdiArrowDropDown() },
                    modifier = Modifier
                        .id(anchor)
                        .height(50.px)
                ) {
                    SpanText(text = "${state.strings.show}: ${state.perPage}")
                }
                Menu(
                    anchor = anchor,
                    open = isPerPageMenuOpen,
                    onClosed = { isPerPageMenuOpen = false },
                ) {
                    state.perPageOptions.forEach { option ->
                        MenuItem(
                            onCLick = { vm.trySend(AdminListContract.Inputs.OnChange.PerPage(option)) },
                            selected = state.perPage == option,
                        ) {
                            SpanText(option.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PageNavigator(
    modifier: Modifier,
    info: PageInfo,
    pagesNumbers: List<Int>,
    prevText: String,
    nextText: String,
    showPrevious: Boolean,
    showNext: Boolean,
    onPageClick: (Int) -> Unit,
    onPreviousPageClick: (Int) -> Unit,
    onNextPageClick: (Int) -> Unit,
) {
    with(info) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            OutlinedSegmentedButtonSet(Modifier) {
                if (showPrevious) {
                    SegmentedButton(
                        label = prevText,
                        selected = false,
                        onClick = { prev?.let { onPreviousPageClick(it) } },
                    )
                }
                pagesNumbers.forEach { page ->
                    SegmentedButton(
                        label = page.toString(),
                        selected = page == info.count,
                        onClick = { onPageClick(page) },
                        modifier = Modifier
                            .disabled(page == info.count)
                    )
                }
                if (showNext) {
                    SegmentedButton(
                        label = nextText,
                        selected = false,
                        onClick = { next?.let { onNextPageClick(it) } },
                    )
                }
            }
        }
    }
}
