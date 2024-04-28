package web.pages.shop.payment.checkout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import data.type.PaymentType
import feature.checkout.CheckoutContract
import feature.checkout.CheckoutViewModel
import feature.checkout.toPaymentMethod
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import web.components.widgets.AppFilledButton
import web.compose.material3.component.Divider
import web.pages.shop.home.gridModifier

@Composable
fun LeftSide(vm: CheckoutViewModel, state: CheckoutContract.State) {
    Column(
        modifier = Modifier
            .width(50.percent)
            .padding(2.em)
    ) {
        ExpressCheckout(vm, state)
    }
}

@Composable
private fun ExpressCheckout(vm: CheckoutViewModel, state: CheckoutContract.State) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        SpanText(
            text = getString(Strings.ExpressCheckout),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = gridModifier(2)
                .fillMaxWidth()
                .gap(1.em)
        ) {
            state.paymentTypes.map { it.toPaymentMethod() }.forEach {
                AppFilledButton(
                    onClick = { vm.trySend(CheckoutContract.Inputs.OnPaymentMethodClicked(it.type)) },
                    containerColor = when (it.type) {
                        PaymentType.VISA -> Colors.GreenYellow
                        PaymentType.PAYPAL -> Colors.Yellow
                        PaymentType.UNKNOWN__,
                        PaymentType.APPLE_PAY,
                        PaymentType.GOOGLE_PAY -> Colors.Black
                    },

                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        src = it.iconUrl,
                        alt = it.name,
                        modifier = Modifier
                            .width(32.px)
                            .margin(leftRight = 2.em)
                            .scale(1.8f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em)
        ) {
            Divider()
            SpanText(
                text = getString(Strings.Or),
                modifier = Modifier.color(MaterialTheme.colors.onSurface)
            )
            Divider()
        }
    }
}

@Composable
private fun Section(
    title: String,
) {

}
