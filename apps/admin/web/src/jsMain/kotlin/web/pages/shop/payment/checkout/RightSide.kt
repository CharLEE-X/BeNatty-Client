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
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.border
import com.varabyte.kobweb.silk.theme.colors.palette.toPalette
import component.localization.Strings
import component.localization.getString
import core.models.Currency
import feature.checkout.CheckoutContract
import feature.checkout.CheckoutViewModel
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.H3Variant
import web.HeadlineStyle
import web.components.widgets.FlexSpacer
import web.components.widgets.Spacer

@Composable
fun RightSide(vm: CheckoutViewModel, state: CheckoutContract.State) {
    Column(
        modifier = Modifier
            .width(50.percent)
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
                modifier = HeadlineStyle.toModifier(H3Variant)
                    .fontWeight(FontWeight.SemiBold)
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.gap(0.5.em)
            ) {
                SpanText(
                    text = state.currency.code,
                )
                SpanText(
                    text = "${state.currency.symbol}${state.totalPrice}",
                    modifier = HeadlineStyle.toModifier(H3Variant)
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
                .border(
                    width = 1.px,
                    color = ColorMode.current.toPalette().border,
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
                    .fontWeight(FontWeight.SemiBold)
            )
            item.attrs.forEach { attr ->
                SpanText(
                    text = "${attr.key}: ${attr.value}",
                )
            }
        }
        FlexSpacer()
        SpanText(
            text = "${currency.symbol}${item.price}",
        )
    }
}
