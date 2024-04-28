package web.pages.shop.payment.checkout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import feature.checkout.CheckoutContract
import feature.checkout.CheckoutViewModel
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.FlexSpacer
import web.components.widgets.Spacer
import web.util.cornerRadius

@Composable
fun RightSide(vm: CheckoutViewModel, state: CheckoutContract.State) {
    Column(
        modifier = Modifier
            .width(50.percent)
            .backgroundColor(MaterialTheme.colors.surface.toRgb().copy(alpha = 50))
            .padding(2.em)
            .gap(0.5.em)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em)
        ) {
            state.items.forEach { item ->
                CartItem(
                    item = item,
                    currency = state.currency
                )
            }
        }
        Spacer(0.5.em)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .roleStyle(MaterialTheme.typography.bodyMedium)
        ) {
            SpanText(text = getString(Strings.Subtotal))
            SpanText(
                text = "${state.currency.symbol}${state.subtotal}",
                modifier = Modifier.fontWeight(FontWeight.SemiBold)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .roleStyle(MaterialTheme.typography.bodyMedium)
        ) {
            SpanText(text = getString(Strings.Shipping))
            SpanText(
                text = state.shippingMessage,
                modifier = Modifier.fontWeight(FontWeight.SemiBold)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            SpanText(
                text = getString(Strings.Total),
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.headlineMedium)
                    .fontWeight(FontWeight.SemiBold)
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.gap(0.5.em)
            ) {
                SpanText(
                    text = state.currency.code,
                    modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
                )
                SpanText(
                    text = "${state.currency.symbol}${state.totalPrice}",
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.headlineMedium)
                        .fontWeight(FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
private fun CartItem(
    item: CheckoutContract.CartItem,
    currency: Currency,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Image(
            src = item.mediaUrl ?: "",
            alt = item.name,
            modifier = Modifier
                .aspectRatio(1f)
                .maxHeight(100.px)
                .objectFit(ObjectFit.Cover)
                .borderRadius(cornerRadius)
                .border(
                    width = 1.px,
                    color = MaterialTheme.colors.surface,
                    style = LineStyle.Solid
                )
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .gap(0.25.em)
        ) {
            SpanText(
                text = item.name.uppercase(),
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.bodyMedium)
                    .fontWeight(FontWeight.SemiBold)
            )
            item.attrs.forEach { attr ->
                SpanText(
                    text = "${attr.key}: ${attr.value}",
                    modifier = Modifier.roleStyle(MaterialTheme.typography.bodySmall)
                )
            }
        }
        FlexSpacer()
        SpanText(
            text = "${currency.symbol}${item.price}",
        )
    }
}
