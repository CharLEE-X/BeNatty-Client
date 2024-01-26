package web.pages.product

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.router.RouterViewModel

@Composable
fun ProductPage(
    router: RouterViewModel,
    id: String,
    onError: suspend (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText("ProductDetails: $id")
            Button(
                onClick = { router.trySend(RouterContract.Inputs.GoBack()) }
            ) {
                SpanText("Go back")
            }
        }
    }
}
