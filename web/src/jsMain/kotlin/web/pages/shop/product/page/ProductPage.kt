package web.pages.shop.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAddShoppingCart
import com.varabyte.kobweb.silk.components.icons.mdi.MdiContactSupport
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import kotlinx.browser.window
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.Element
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppFilledButton
import web.components.widgets.FlexSpacer
import web.components.widgets.RotatableChevron
import web.components.widgets.Spacer
import web.compose.material3.component.Divider
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

    var sizeGuideDialogOpen by remember { mutableStateOf(false) }
    var sizeGuideDialogClosing by remember { mutableStateOf(false) }

    var askQuestionDialogOpen by remember { mutableStateOf(false) }
    var askQuestionDialogClosing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goToProduct = goToProduct,
            openAskQuestionDialog = { askQuestionDialogOpen = true },
            openSizeGuideDialog = { sizeGuideDialogOpen = true },
        )
    }
    val state by vm.observeStates().collectAsState()

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
                onSend = { vm.trySend(ProductPageContract.Inputs.OnSendQuestionClicked) },
                onCancel = { askQuestionDialogOpen = false },
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
                    )
                    Spacer(2.em)
                    AddToCartButton(vm, state)
                    Spacer()
                    AskQuestionButton(vm)
                    Spacer()
                    DescriptionsSection(state)
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
private fun DescriptionsSection(state: ProductPageContract.State) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Block)
            .margin(top = 1.em)
    ) {
        Divider()
        ExpandableSection(
            title = getString(Strings.Description),
            enabled = state.product.description.isNotEmpty(),
        ) {
            SpanText(
                text = state.product.description,
                modifier = Modifier.color(MaterialTheme.colors.onBackground)
            )
        }
        Divider()
        ExpandableSection(
            title = getString(Strings.Materials),
            enabled = true,
        ) {
            SpanText(
                text = """
                    OUTER SHELL: 100% cotton
                    LINING: 100% cotton
                """.trimIndent(),
                modifier = Modifier.color(MaterialTheme.colors.onBackground)
            )
        }
        Divider()
        ExpandableSection(
            title = getString(Strings.Care),
            enabled = true,
        ) {
            SpanText(
                text = """
                    Caring for your clothes is caring for the environment.
                    To keep your jackets and coats clean, just freshen them out and wipe them with a cloth or clothing brush. This is a more delicate process with fabrics and additionally avoids water and energy consumption from washing processes.

                    • Hand wash max 30ºC/86ºF
                    • Do not use bleach / whitener
                    • Iron maximum 110ºC/230ºF
                    • Do not dry clean
                    • Do not tumble dry
                """.trimIndent(),
                modifier = Modifier.color(MaterialTheme.colors.onBackground)
            )
        }
        Divider()
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    var open by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }

    var contentElement: Element? by remember { mutableStateOf(null) }
    var scrollHeight: Int by remember { mutableStateOf(300) }

    LaunchedEffect(window.innerHeight, contentElement?.scrollHeight) {
        if (contentElement != null) {
            if ((contentElement?.scrollHeight ?: 0) > 0) {
                scrollHeight = contentElement?.scrollHeight ?: 300
            }
        }
    }

    if (enabled) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .boxSizing(BoxSizing.BorderBox)
                .margin(top = 30.px)
                .listStyle(ListStyleType.None)
                .overflow(Overflow.Hidden)
                .maxHeight(scrollHeight.px)
                .attrsModifier {
                    ref {
//                        scrollHeight = it.scrollHeight
//                        contentElement = it
                        onDispose { contentElement = null }
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onMouseOver { if (enabled) hovered = true }
                    .onMouseLeave { hovered = false }
                    .onFocusIn { if (enabled) hovered = true }
                    .onFocusOut { hovered = false }
                    .tabIndex(0)
                    .onClick { if (enabled) open = !open }
                    .onEnterKeyDown { if (enabled) open = !open }
                    .cursor(if (enabled) Cursor.Pointer else Cursor.Default)
                    .display(DisplayStyle.Flex)
            ) {
                SpanText(
                    text = title.uppercase(),
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.titleMedium)
                        .color(MaterialTheme.colors.onBackground)
                )
                if (enabled) {
                    FlexSpacer()
                    RotatableChevron(
                        hovered = hovered,
                        open = open,
                        modifier = Modifier
                    )
                }
            }
//            if (open) {
            Span(
                Modifier
                    .padding(top = if (open) 1.em else 0.em)
                    .roleStyle(MaterialTheme.typography.bodyLarge)
                    .thenIf(!open, Modifier.height(0.px))
                    .opacity(if (open) 1f else 0f)
                    .display(DisplayStyle.ListItem)
                    .margin(bottom = 30.px)
                    .transition(
                        CSSTransition("visibility", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("height", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("max-height", 0.3.s, TransitionTimingFunction.Ease),
                        CSSTransition("padding", 0.3.s, TransitionTimingFunction.Ease),
                    )
                    .toAttrs()
            ) {
                content()
            }
//            }
        }
    }
}

@Composable
private fun AskQuestionButton(vm: ProductPageViewModel) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(2.px)
            .color(MaterialTheme.colors.onBackground)
            .onMouseOver { hovered = true }
            .onMouseLeave { hovered = false }
            .onFocusIn { hovered = true }
            .onFocusOut { hovered = false }
            .tabIndex(0)
            .onClick { vm.trySend(ProductPageContract.Inputs.OnAskQuestionClicked) }
            .onEnterKeyDown { vm.trySend(ProductPageContract.Inputs.OnAskQuestionClicked) }
            .cursor(Cursor.Pointer)
            .opacity(if (hovered) 1f else 0.85f)
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
    ) {
        MdiContactSupport(
            style = IconStyle.OUTLINED,
            modifier = Modifier
                .size(24.px)
                .opacity(if (hovered) 1f else 0.5f)
                .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
        )
        Box {
            SpanText(
                text = getString(Strings.AskQuestion).uppercase(),
                modifier = Modifier.roleStyle(MaterialTheme.typography.titleSmall)
            )
            Box(
                modifier = Modifier
                    .translateY(22.px)
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
