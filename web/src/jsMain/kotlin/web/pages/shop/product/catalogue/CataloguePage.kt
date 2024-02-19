package web.pages.shop.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import web.components.layouts.MainParams
import web.components.layouts.ShopMainLayout

@Composable
fun CataloguePage(
    mainParams: MainParams,
    goToProduct: (String) -> Unit,
) {
    ShopMainLayout(
        title = "Catalogue",
        mainParams = mainParams,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpanText("Catalogue")
                Button(
                    onClick = { goToProduct("1") }
                ) {
                    SpanText("Product 1")
                }
            }
        }
    }
}
