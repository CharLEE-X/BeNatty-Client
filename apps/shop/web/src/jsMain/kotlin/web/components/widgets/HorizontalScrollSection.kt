package web.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronLeft
import com.varabyte.kobweb.silk.components.icons.mdi.MdiChevronRight
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLElement
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions
import web.H2Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.pages.home.Navigator
import web.util.onEnterKeyDown

data class ScrollableItem(
    val id: String,
    val urls: List<String>,
    val title: String,
    val price: String,
    val sizes: String,
)

@Composable
fun HorizontalScrollSection(
    title: String,
    seeMoreTitle: String,
    items: List<ScrollableItem>,
    onItemClicked: (String) -> Unit,
    onSeeMoreClicked: () -> Unit,
) {
    var scrollable: HTMLElement? by remember { mutableStateOf(null) }
    val gap = 24.px
    val itemsInRow = 4
    var itemWidth by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(scrollable?.clientWidth) {
        itemWidth = scrollable?.clientWidth?.let { width ->
            (width - gap.value * (itemsInRow - 1)) / itemsInRow
        }?.toInt()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .gap(2.em)
            .margin(top = 2.em)
    ) {
        SpanText(
            text = title.uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .padding(leftRight = 24.px)
                .draggable(false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(670.px)
                    .overflow {
                        x(Overflow.Scroll)
                        y(Overflow.Hidden)
                    }
                    .styleModifier {
                        property("-ms-overflow-style", "none")
                        property("scrollbar-width", "0.px")
                        property("scrollbar-height", "0.px")
                        property("scrollbar-color", "transparent transparent")
                    }
                    .draggable(false)
                    .attrsModifier {
                        ref {
                            scrollable = it.unsafeCast<HTMLElement>()
                            onDispose { scrollable = null }
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .gap(gap)
                        .userSelect(UserSelect.None)
                        .padding(leftRight = gap)
                        .draggable(false)
                ) {
                    items.forEach { item ->
                        ScrollableItem(
                            urls = item.urls,
                            title = item.title,
                            price = item.price,
                            sizes = item.sizes,
                            onCLick = { onItemClicked(item.id) },
                            modifier = Modifier
                                .width(itemWidth?.px ?: 500.px)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .zIndex(10)
                    .fillMaxWidth()
                    .maxWidth(oneLayoutMaxWidth)
                    .padding(1.em)
            ) {
                Navigator(
                    enabled = true,
                    onClick = {
                        itemWidth?.toDouble()?.let { amount ->
                            scrollable?.scrollBy(
                                ScrollToOptions(
                                    left = -(amount + (gap.value / 2)),
                                    behavior = ScrollBehavior.SMOOTH
                                )
                            )
                        }
                    },
                    icon = { modifier -> MdiChevronLeft(modifier) },
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Navigator(
                    enabled = true,
                    onClick = {
                        itemWidth?.toDouble()?.let { amount ->
                            scrollable?.scrollBy(
                                ScrollToOptions(
                                    left = amount + (gap.value / 2),
                                    behavior = ScrollBehavior.SMOOTH
                                )
                            )
                        }
                    },
                    icon = { modifier -> MdiChevronRight(modifier) },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        AppOutlinedButton(
            onClick = { onSeeMoreClicked() },
            content = { SpanText(seeMoreTitle) }
        )
    }
}

@Composable
fun ScrollableItem(
    modifier: Modifier,
    urls: List<String>,
    title: String,
    price: String,
    sizes: String,
    onCLick: () -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    val url = if (hovered && urls.size > 1) urls[1] else urls[0]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .gap(0.5.em)
            .onClick { onCLick() }
            .onEnterKeyDown { onCLick() }
            .tabIndex(0)
            .draggable(false)
            .cursor(Cursor.Pointer)
            .onMouseOver { hovered = true }
            .onMouseOver { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusIn { hovered = false }
    ) {
        Image(
            src = url,
            alt = title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1 / 2)
                .objectFit(ObjectFit.Cover)
        )
        SpanText(text = title)
        SpanText(text = price)
        SpanText(text = sizes)
    }
}
