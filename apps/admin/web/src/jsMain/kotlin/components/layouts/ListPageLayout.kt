package web.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotateZ
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowUpward
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBrokenImage
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibility
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import data.type.SortDirection
import feature.admin.list.AdminListContract
import feature.admin.list.AdminListViewModel
import feature.admin.list.ListItem
import feature.admin.list.PageInfo
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.components.widgets.AppFilledButton
import web.util.onEnterKeyDown

@Composable
fun ListPageLayout(
    state: AdminListContract.State,
    vm: AdminListViewModel,
) {
    with(state) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.px)
        ) {
            val gridContainerModifier = Modifier
                .fillMaxWidth()
                .padding(topBottom = 0.5.em, leftRight = 1.em)
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
                    prevText = getString(Strings.Previous),
                    nextText = getString(Strings.Next),
                    showPrevious = showPrevious,
                    showNext = showNext,
                    onPageClick = { vm.trySend(AdminListContract.Inputs.Click.Page(it)) },
                    onPreviousPageClick = { vm.trySend(AdminListContract.Inputs.Click.PreviousPage) },
                    onNextPageClick = { vm.trySend(AdminListContract.Inputs.Click.NextPage) },
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (showNoItems) {
                    NoItemsListAction(
                        pressText = getString(Strings.Press),
                        createText = getString(Strings.Create),
                        toStartText = getString(Strings.ToStart),
                        onClick = { vm.trySend(AdminListContract.Inputs.Click.Create) },
                        modifier = Modifier.margin(4.em)
                    )
                }
                if (isLoading) {
                    SpanText("Loading...")
                }
            }
        }
    }
}

@Composable
private fun NoItemsListAction(
    modifier: Modifier,
    pressText: String,
    createText: String,
    toStartText: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.gap(0.5.em)
    ) {
        SpanText(text = pressText)
        AppFilledButton(
            onClick = { onClick() },
            modifier = Modifier
                .tabIndex(0)
                .onEnterKeyDown(onClick)
        ) {
            SpanText(text = createText.lowercase())
        }
        SpanText(text = toStartText.lowercase())
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
            .gap(0.5.em)
            .margin(topBottom = 0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = gridContainerModifier
        ) {
            when (state.dataType) {
                AdminListContract.DataType.Customer -> {
                    AdminListContract.CustomerSlot.entries.forEach { slot ->
                        TopBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.Click.Slot(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.PRODUCT -> {
                    AdminListContract.ProductSlot.entries.forEach { slot ->
                        TopBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = slot != AdminListContract.ProductSlot.Image,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.Click.Slot(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.ORDER -> {
                    AdminListContract.OrderSlot.entries.forEach { slot ->
                        TopBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.Click.Slot(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.CATEGORY -> {
                    AdminListContract.CategorySlot.entries.forEach { slot ->
                        TopBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.Click.Slot(slot.name)) },
                        )
                    }
                }

                AdminListContract.DataType.TAG -> {
                    AdminListContract.TagSlot.entries.forEach { slot ->
                        TopBarItem(
                            text = slot.asString(),
                            isSelected = state.sortBy == slot.name,
                            isSortable = true,
                            sortDirection = state.sortDirection,
                            onClick = { vm.trySend(AdminListContract.Inputs.Click.Slot(slot.name)) },
                        )
                    }
                }
            }
        }
        HorizontalDivider()
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
            .onClick { vm.trySend(AdminListContract.Inputs.Click.Item(item.id)) }
            .cursor(Cursor.Pointer)
            .transition(
                Transition.of("background-color", 0.3.s, TransitionTimingFunction.Ease),
                Transition.of("color", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        when (state.dataType) {
            AdminListContract.DataType.Customer -> {
                SpanText(item.slot1)
                SpanText(item.slot2 ?: "N/A")
                SpanText(item.slot3 ?: "N/A")
                SpanText(item.slot4 ?: "N/A")
            }

            AdminListContract.DataType.PRODUCT -> {
                SpanText(item.slot1)
                val url = item.slot2
                if (url.isNullOrEmpty()) MdiBrokenImage(Modifier.size(30.px)) else MiniImage(url)
                SpanText(item.slot3 ?: "N/A")
                SpanText(item.slot4 ?: "N/A")
                SpanText(item.slot5 ?: "N/A")
            }

            AdminListContract.DataType.ORDER -> {
                SpanText(item.slot1)
                SpanText(item.slot2 ?: "N/A")
                SpanText(item.slot3 ?: "N/A")
                SpanText(item.slot4 ?: "N/A")
            }

            AdminListContract.DataType.CATEGORY -> {
                SpanText(item.slot1)
                val url = item.slot2
                if (url.isNullOrEmpty()) MdiBrokenImage(Modifier.size(30.px)) else MiniImage(url)
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
    var hovered by remember { mutableStateOf(false) }

    Image(
        src = url,
        modifier = Modifier
            .size(40.px)
            .objectFit(ObjectFit.Cover)
            .onMouseEnter { hovered = true }
            .onMouseLeave { hovered = false }
//            .overflow(Overflow.Hidden)
            .scale(if (hovered) 4 else 1.0)
            .transition(Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease))
    )
}

@Composable
private fun TopBarItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    isSortable: Boolean,
    sortDirection: SortDirection,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .gap(0.25.em)
            .onClick { if (isSortable) onClick() }
            .userSelect(UserSelect.None)
            .thenIf(isSortable) {
                Modifier
                    .cursor(Cursor.Pointer)
                    .onMouseEnter { hovered = true }
                    .onMouseLeave { hovered = false }
                    .transition(Transition.of("color", 0.3.s, TransitionTimingFunction.Ease))
            }
    ) {
        SpanText(text)
        if (isSortable) {
            MdiArrowUpward(
                modifier = Modifier
                    .scale(if (isSelected) 1.0 else 0.0)
                    .opacity(if (isSelected) 1.0 else 0.0)
                    .rotateZ(if (sortDirection == SortDirection.ASC) 0.deg else 180.deg)
                    .transition(
                        Transition.of("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        Transition.of("rotate", 0.3.s, TransitionTimingFunction.Ease),
                        Transition.of("scale", 0.3.s, TransitionTimingFunction.Ease),
                    )
            )
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
            SpanText("FIXME: Implement PageNavigator")
//            AppOutlinedSegmentedButtonSet(Modifier) {
//                if (showPrevious) {
//                    AppSegmentedButton(
//                        label = prevText,
//                        selected = false,
//                        onClick = { prev?.let { onPreviousPageClick(it) } },
//                    )
//                }
//                pagesNumbers.forEach { page ->
//                    AppSegmentedButton(
//                        label = page.toString(),
//                        selected = page == info.count,
//                        onClick = { onPageClick(page) },
//                        modifier = Modifier
//                            .disabled(page == info.count)
//                    )
//                }
//                if (showNext) {
//                    AppSegmentedButton(
//                        label = nextText,
//                        selected = false,
//                        onClick = { next?.let { onNextPageClick(it) } },
//                    )
//                }
//            }
        }
    }
}
