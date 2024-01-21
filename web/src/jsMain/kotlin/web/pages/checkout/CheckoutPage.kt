package web.pages.checkout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.text.SpanText

@Composable
fun CheckoutPage(
    goBack: () -> Unit,
    goToPayment: () -> Unit,
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
                onClick = { goBack() }
            ) {
                SpanText("Go Back")
            }
            Button(
                onClick = { goToPayment() }
            ) {
                SpanText("Go net to payment")
            }
        }
    }
}
