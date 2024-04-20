package web.pages.shop.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAddShoppingCart
import com.varabyte.kobweb.silk.components.icons.mdi.MdiContactSupport
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppFilledButton
import web.pages.shop.home.gridModifier
import web.pages.shop.product.page.dialogs.AskQuestionDialog
import web.pages.shop.product.page.dialogs.SizeGuideDialog
import web.util.glossy
import web.util.onEnterKeyDown

@Composable
fun ProductPage(
    productId: String,
    mainRoutes: MainRoutes,
    onError: suspend (String) -> Unit,
    goToProduct: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goToProduct = goToProduct,
        )
    }
    val state by vm.observeStates().collectAsState()

    var sizeGuideDialogOpen by remember { mutableStateOf(false) }
    var sizeGuideDialogClosing by remember { mutableStateOf(false) }

    var askQuestionDialogOpen by remember { mutableStateOf(false) }
    var askQuestionDialogClosing by remember { mutableStateOf(false) }

    ShopMainLayout(
        title = getString(Strings.ProductPage),
        mainRoutes = mainRoutes,
        overlay = {
            SizeGuideDialog(
                state = state,
                open = sizeGuideDialogOpen && !sizeGuideDialogClosing,
                closing = sizeGuideDialogClosing,
                title = getString(Strings.SizeGuide).uppercase(),
                onOpen = { sizeGuideDialogOpen = it },
                onClosing = { sizeGuideDialogClosing = it },
            )
            AskQuestionDialog(
                vm = vm,
                state = state,
                open = askQuestionDialogOpen && !askQuestionDialogClosing,
                closing = askQuestionDialogClosing,
                title = "${getString(Strings.HaveAQuestion)}?".uppercase(),
                onOpen = { askQuestionDialogOpen = it },
                onClosing = { askQuestionDialogClosing = it },
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .margin(0.px)
                .padding(20.px)
                .glossy()
                .gap(2.em)
        ) {
            Row(
                gridModifier(
                    columns = 2,
                    gap = 2.em,
                )
            ) {
                ProductMedia(vm, state)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ProductInfo(
                        vm = vm,
                        state = state,
                        onOpenSizeGuideClicked = { sizeGuideDialogOpen = true },
                    )
                    AddToCartButton(vm, state)
                    Box(Modifier.size(1.em))
                    AskQuestionButton(
                        vm = vm,
                        state = state,
                        onAskQuestionClicked = { askQuestionDialogOpen = true }
                    )
                    Box(Modifier.size(1.em))
                    SimilarProducts(
                        vm = vm,
                        state = state,
                        modifier = Modifier
                    )
                }
            }
            YouMayAlsoLike(vm, state)
        }
    }
}

@Composable
fun AskQuestionButton(
    vm: ProductPageViewModel,
    state: ProductPageContract.State,
    onAskQuestionClicked: () -> Unit
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(0.5.em)
            .color(MaterialTheme.colors.onBackground)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .tabIndex(0)
            .onClick { onAskQuestionClicked() }
            .onEnterKeyDown(onAskQuestionClicked)
            .cursor(Cursor.Pointer)
    ) {
        MdiContactSupport(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .size(24.px)
                .opacity(if (hovered) 1f else 0.5f)
        )
        Box {
            SpanText(
                text = getString(Strings.AskQuestion).uppercase(),
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .translateY((26).px)
                    .height(2.px)
                    .fillMaxWidth(if (hovered) 100.percent else 0.percent)
                    .backgroundColor(MaterialTheme.colors.onSurface)
                    .transition(CSSTransition("width", 0.3.s, TransitionTimingFunction.Ease))
            )
        }
    }
}

@Composable
private fun AddToCartButton(vm: ProductPageViewModel, state: ProductPageContract.State) {
    AppFilledButton(
        disabled = !state.isAddToCartButtonEnabled,
        onClick = { vm.trySend(ProductPageContract.Inputs.OnAddToCartClicked) },
        leadingIcon = { MdiAddShoppingCart() },
        containerShape = 32.px,
        modifier = Modifier.fillMaxWidth()
    ) {
        SpanText(
            text = getString(Strings.AddToCart).uppercase(),
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.titleMedium)
                .padding(8.px)
        )
    }
}
