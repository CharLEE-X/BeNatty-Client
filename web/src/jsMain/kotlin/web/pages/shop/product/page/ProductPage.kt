package web.pages.shop.product.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.px
import web.components.layouts.MainRoutes
import web.components.layouts.ShopMainLayout
import web.components.layouts.oneLayoutMaxWidth

@Composable
fun ProductPage(
    productId: String,
    mainRoutes: MainRoutes,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        ProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
        )
    }
    val state by vm.observeStates().collectAsState()

    ShopMainLayout(
        title = state.strings.productPage,
        mainRoutes = mainRoutes,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .margin(0.px)
                .padding(leftRight = 20.px, top = 100.px)
        ) {
            SpanText("Product Page")
            SpanText("Product: $productId")
        }
    }
}
