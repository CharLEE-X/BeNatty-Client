package web.pages.product.catalogue

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.product.catalog.CatalogContract
import feature.product.catalog.CatalogViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.H2Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppFilledButton

@Composable
fun WhoWeAre(
    vm: CatalogViewModel,
    state: CatalogContract.State,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .margin(top = 2.em)
            .fillMaxWidth()
            .gap(3.em)
            .padding(leftRight = 24.px)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .gap(3.em)
        ) {
            Image(
                src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks4.jpg?v=1614301039&width=1200",
                modifier = Modifier
                    .fillMaxWidth(66.percent)
                    .aspectRatio(1 / 2)
                    .overflow(Overflow.Clip)
                    .objectFit(ObjectFit.Cover)
            )
            Column(
                modifier = Modifier.gap(2.em)
            ) {
                SpanText(
                    text = "Who we are",
                    modifier = HeadlineStyle.toModifier(H2Variant)
                )
                SpanText(
                    text = "Our story began in 2001 in a small studio in the middle of nowhere. With only one desk and next to no free time," +
                        " our brand was born. Our passion for unique design and collaboration brought our vision, and products, to life.",
                )
                SpanText(
                    text = "Our products bring together the finest materials and stunning design to create something very special. " +
                        "We believe in quality, care, and creating unique products that everyone can enjoy. Colorful, creative, " +
                        "and inspired by what we see everyday, each product represents what we love about the world we live in. " +
                        "We hope they’ll inspire you too.",
                )
                AppFilledButton(
                    onClick = {}
                ) {
                    SpanText(getString(Strings.ReadMore))
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .gap(3.em)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .gap(2.em)
                    .textAlign(TextAlign.End)
            ) {
                SpanText(
                    text = "Our materials",
                    modifier = HeadlineStyle.toModifier(H2Variant)
                )
                SpanText(
                    text = "As you might have expected we are fair trade only.",
                )
                SpanText(
                    text = "Our values and principles are rooted in our beliefs at spreading love and opportunity to " +
                        "countries that are in crisis and need our help.",
                )
                AppFilledButton(
                    onClick = {}
                ) {
                    SpanText(getString(Strings.Learn))
                }
            }
            Image(
                src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks6.jpg?v=1614301039&width=1200",
                modifier = Modifier
                    .fillMaxWidth(66.percent)
                    .aspectRatio(1 / 2)
                    .overflow(Overflow.Clip)
                    .objectFit(ObjectFit.Cover)
            )
        }
    }
}
