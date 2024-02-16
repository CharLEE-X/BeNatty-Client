package web.pages.payment.cart

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
import feature.router.Screen

@Suppress("UNUSED_PARAMETER")
@Composable
fun CartPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {

    val goBack = { router.trySend(RouterContract.Inputs.GoBack()) }
    val goToCheckout = {
        router.trySend(
            RouterContract.Inputs.GoToDestination(Screen.Checkout.matcher.routeFormat)
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText("Cart")
            Button(
                onClick = { goBack() }
            ) {
                SpanText("Go Back")
            }
            Button(
                onClick = { goToCheckout() }
            ) {
                SpanText("Go Next")
            }
        }
    }
}
