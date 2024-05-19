package web.pages.shop.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoRows
import com.varabyte.kobweb.compose.ui.modifiers.gridColumn
import com.varabyte.kobweb.compose.ui.modifiers.gridRow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronRight
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import data.GetLandingConfigQuery
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import web.H1Variant
import web.HeadlineStyle
import web.components.widgets.AppFilledButton
import web.components.widgets.Shimmer
import web.util.onEnterKeyDown

fun gridModifier(
    columns: Int = 3,
    gap: CSSLengthOrPercentageNumericValue = 1.5.em,
    rowMinHeight: CSSLengthOrPercentageNumericValue = 200.px
) = Modifier
    .fillMaxWidth()
    .display(DisplayStyle.Grid)
    .gridTemplateColumns { repeat(columns) { size(1.fr) } }
    .gridAutoRows { minmax(rowMinHeight, 1.fr) }
    .gap(gap)

val CollageBigItemStyle by ComponentStyle.base {
    Modifier
        .gridColumn(1, 3)
        .gridRow(1, 3)
}

@Composable
fun Slideshow(
    vm: HomeViewModel,
    state: HomeContract.State,
    height: CSSLengthOrPercentageNumericValue = 800.px
) {
    val items = state.landingConfig.slideshowItems

    val slideshowModifier = Modifier
        .fillMaxWidth()
        .height(height)
        .margin(0.px)
        .userSelect(UserSelect.None)
        .overflow(Overflow.Hidden)

    if (state.isLoading) {
        Shimmer(slideshowModifier)
    } else {
        Box(
            modifier = slideshowModifier
        ) {
            items.firstOrNull()?.let { _ ->
                val scope = rememberCoroutineScope()
                var autoSlide: Job? by remember { mutableStateOf(null) }
                var jobInProgress by remember { mutableStateOf(false) }

                var bottomIndex by remember { mutableStateOf(0) }
                var topIndex by remember { mutableStateOf(bottomIndex) }
                var topVisible by remember { mutableStateOf(false) }
                var topShow by remember { mutableStateOf(true) }
                var showTitle by remember { mutableStateOf(false) }

                println("Job in progress: $jobInProgress")

                suspend fun nextSlide() {
                    jobInProgress = true
                    delay(500L)
                    showTitle = false
                    delay(500L)
                    items.getOrNull(bottomIndex + 1)
                        ?.let { bottomIndex += 1 }
                        ?: run { bottomIndex = 0 }
                    jobInProgress = false
                }

                fun prevSlide() {
                    items.getOrNull(bottomIndex - 1)?.let {
                        bottomIndex -= 1
                    } ?: run { bottomIndex = items.size - 1 }
                }

                DisposableEffect(bottomIndex) {
                    autoSlide?.cancel()
                    autoSlide = scope.launch {
                        showTitle = true
                        topVisible = false
                        delay(1000)
                        topShow = false
                        topIndex = bottomIndex
                        delay(1000)

                        jobInProgress = true
                        topShow = true
                        topVisible = true
                        delay(2000)

                        nextSlide()
                    }

                    onDispose {
                        autoSlide?.cancel()
                        autoSlide = null
                    }
                }

                items.getOrNull(bottomIndex)?.let { item ->
                    item.media?.url?.let {
                        Image(
                            src = it,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                if (topShow) {
                    items.getOrNull(topIndex)?.let { item ->
                        item.media?.url?.let {
                            Image(
                                src = it,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .opacity(if (topVisible) 1.0 else 0.0)
                                    .transition(CSSTransition("opacity", 0.75.s, TransitionTimingFunction.Ease))
                            )
                        }
                    }
                }
                items.getOrNull(bottomIndex)?.let { item ->
                    if (bottomIndex % 2 != 0) {
                        ColumnInfo(
                            item = item,
                            onClick = { vm.trySend(HomeContract.Inputs.OnCollageItemClick(item)) },
                            show = showTitle,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .translate(tx = 300.px, ty = 0.px)
                        )

                    } else {
                        ColumnInfo(
                            item = item,
                            onClick = { vm.trySend(HomeContract.Inputs.OnCollageItemClick(item)) },
                            show = showTitle,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .translate(tx = 0.px, ty = 100.px)
                        )
                    }
                }
                Navigator(
                    enabled = !jobInProgress,
                    onClick = { prevSlide() },
                    icon = { modifier -> MdiChevronLeft(modifier) },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                )
                Navigator(
                    enabled = !jobInProgress,
                    onClick = {
                        if (!jobInProgress) {
                            autoSlide?.cancel()
                            scope.launch {
                                nextSlide()
                            }
                        }
                    },
                    icon = { modifier -> MdiChevronRight(modifier) },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
private fun ColumnInfo(
    modifier: Modifier,
    item: GetLandingConfigQuery.SlideshowItem,
    onClick: () -> Unit,
    show: Boolean,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(64.px)
            .gap(1.em)
            .opacity(if (show) 1.0 else 0.0)
            .transition(CSSTransition("opacity", 0.5.s, TransitionTimingFunction.Ease))
    ) {
        item.title?.let {
            SpanText(
                text = it.uppercase(),
                modifier = HeadlineStyle.toModifier(H1Variant)
            )
        }
        item.description?.let {
            SpanText(text = it)
        }
        AppFilledButton(onClick = onClick) {
            SpanText(text = getString(Strings.ShopNow))
        }
    }
}

@Composable
fun Navigator(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Modifier) -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(16.px)
            .margin(16.px)
            .backgroundColor(if (hovered) Colors.Transparent else ColorMode.current.toPalette().background)
            .color(ColorMode.current.opposite.toPalette().background)
            .borderRadius(50.percent)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .tabIndex(0)
            .cursor(Cursor.Pointer)
            .onClick { if (enabled) onClick() }
            .onEnterKeyDown { if (enabled) onClick() }
            .transition(
                CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("color", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        icon(Modifier.fillMaxSize())
    }
}

@Composable
fun ShimmerCollageItem(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1.0),
    textPosition: TextPosition = TextPosition.LeftBottom,
    content: @Composable ColumnScope.() -> Unit,
) {
    Shimmer(
        textPosition = textPosition,
        modifier = modifier
            .padding(50.px)
    ) {
        content()
    }
}

enum class TextPosition { Center, LeftBottom, RightTop }
