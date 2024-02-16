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
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotateZ
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowUpward
import com.varabyte.kobweb.silk.components.icons.mdi.MdiBrokenImage
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibility
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVisibilityOff
import com.varabyte.kobweb.silk.components.text.SpanText
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
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.widgets.AppOutlinedSegmentedButtonSet
import web.components.widgets.AppSegmentedButton
import web.components.widgets.NoItemsListAction
import web.compose.material3.component.CircularProgress
import web.compose.material3.component.Divider

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
                        pressText = strings.press,
                        createText = strings.create,
                        toStartText = strings.toStart,
                        onClick = { vm.trySend(AdminListContract.Inputs.Click.Create) },
                        modifier = Modifier.margin(4.em)
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
            .gap(0.5.em)
            .margin(topBottom = 0.5.em)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = gridContainerModifier
        ) {
            when (state.dataType) {
                AdminListContract.DataType.USER -> {
                    AdminListContract.UserSlot.entries.forEach { slot ->
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
            .borderRadius(12.px)
            .onClick { vm.trySend(AdminListContract.Inputs.Click.Item(item.id)) }
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
                val url = item.slot2
                if (url.isNullOrEmpty()) {
                    MdiBrokenImage(Modifier.size(30.px))
                } else {
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
                    .thenIf(hovered) { Modifier.color(MaterialTheme.colors.mdSysColorSecondary.value()) }
                    .transition(CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease))
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
                        CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("rotate", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
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
            AppOutlinedSegmentedButtonSet(Modifier) {
                if (showPrevious) {
                    AppSegmentedButton(
                        label = prevText,
                        selected = false,
                        onClick = { prev?.let { onPreviousPageClick(it) } },
                    )
                }
                pagesNumbers.forEach { page ->
                    AppSegmentedButton(
                        label = page.toString(),
                        selected = page == info.count,
                        onClick = { onPageClick(page) },
                        modifier = Modifier
                            .disabled(page == info.count)
                    )
                }
                if (showNext) {
                    AppSegmentedButton(
                        label = nextText,
                        selected = false,
                        onClick = { next?.let { onNextPageClick(it) } },
                    )
                }
            }
        }
    }
}
