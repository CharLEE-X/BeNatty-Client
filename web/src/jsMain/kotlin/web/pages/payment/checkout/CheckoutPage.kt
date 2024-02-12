package web.pages.payment.checkout

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.router.RouterScreen
import feature.router.RouterViewModel

@Suppress("UNUSED_PARAMETER")
@Composable
fun CheckoutPage(
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpanText("Checkout")
            Button(
                onClick = {
                    router.trySend(RouterContract.Inputs.GoBack())
                }
            ) {
                SpanText("Go Back")
            }
            Button(
                onClick = {
                    router.trySend(
                        RouterContract.Inputs.GoToDestination(RouterScreen.Payment.matcher.routeFormat)
                    )
                }
            ) {
                SpanText("Go net to payment")
            }
        }
    }
}
