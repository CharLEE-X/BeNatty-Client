package web.pages.shop.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.draggable
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridColumn
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiZoomIn
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import data.AdminProductGetByIdQuery
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.w3c.dom.HTMLElement
import web.components.widgets.themeScrollbarStyle
import web.pages.shop.home.gridModifier
import web.util.onEnterKeyDown

@Composable
fun ProductMedia(vm: ProductPageViewModel, state: ProductPageContract.State) {
    var mainImageElement: HTMLElement? by remember { mutableStateOf(null) }
    var mainImageHeight by remember { mutableStateOf(5000) }

    val media = state.product.media + state.product.variants
        .flatMap { it.media }
        .map {
            AdminProductGetByIdQuery.Medium(
                keyName = it.keyName,
                url = it.url,
                alt = it.alt,
                type = it.type,
            )
        }

    LaunchedEffect(state) {
        mainImageElement?.scrollHeight?.let {
            if (it > 0) {
                mainImageHeight = it
            }
        }
        println("mainImageHeight: $mainImageHeight")
    }

    Row(
        modifier = gridModifier(columns = 6, gap = 1.em)
            .position(Position.Sticky)
            .top(40.px)
            .transition(
                CSSTransition("top", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("position", 0.3.s, TransitionTimingFunction.Ease),
            )
    ) {
        Column(
            modifier = themeScrollbarStyle.toModifier()
                .gridColumn(1, 2)
                .fillMaxHeight()
                .maxHeight(mainImageHeight.px)
                .overflow(Overflow.Auto)
                .flex("0 0 auto")
                .gap(0.5.em)
                .padding(right = 10.px)
        ) {
            media.forEach { media ->
                ProductDetailMiniatureItem(
                    media = media,
                    selected = state.selectedMedia?.keyName == media.keyName,
                    onClick = { vm.trySend(ProductPageContract.Inputs.OnGalleryMiniatureClicked(media)) },
                )
            }
        }
        MainImage(
            media = state.selectedMedia,
            onClick = { vm.trySend(ProductPageContract.Inputs.OnMainImageClicked) },
            modifier = Modifier
                .gridColumn(2, 7)
                .attrsModifier {
                    ref {
                        mainImageElement = it as HTMLElement
                        onDispose {}
                    }
                }
        )
    }
}

@Composable
private fun ProductDetailMiniatureItem(
    media: AdminProductGetByIdQuery.Medium,
    selected: Boolean,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(100.px)
            .aspectRatio(.6f)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .cursor(Cursor.Pointer)
            .tabIndex(0)
            .onClick { onClick() }
            .onEnterKeyDown(onClick)
            .overflow(Overflow.Hidden)
            .userSelect(UserSelect.None)
            .draggable(false)
            .scale(if (hovered) 1.05f else 1f)
            .translateX(if (selected) 4.px else 0.px)
            .border(
                width = 4.px,
                color = ColorMode.current.toPalette().border,
                style = LineStyle.Solid
            )
            .transition(
                CSSTransition("border", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                CSSTransition("translate", 0.2.s, TransitionTimingFunction.Ease)
            )
    ) {
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

@Composable
private fun MainImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    media: AdminProductGetByIdQuery.Medium?,
) {
    var hovered by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onClick { onClick() }
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { focused = true }
            .onFocusOut { focused = false }
            .cursor(Cursor.Pointer)
            .overflow(Overflow.Hidden)
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    ) {
        val imageModifier = Modifier
            .fillMaxWidth()
            .objectFit(ObjectFit.Cover)
        media?.let { media ->
            Image(
                src = media.url,
                alt = media.alt,
                modifier = imageModifier
                    .draggable(false)
            )

            MdiZoomIn(
                style = IconStyle.OUTLINED,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.px)
                    .scale(1.5f)
                    .color(ColorMode.current.toPalette().color)
                    .opacity(if (hovered) 1f else 0.6f)
                    .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
            )
        } ?: Box(
            modifier = imageModifier
        )
    }
}
