package web.pages.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
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
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.em
import web.H3Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppFilledButton

@Composable
fun BlogFeatured(vm: HomeViewModel, state: HomeContract.State) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = gridModifier(2)
                .maxWidth(oneLayoutMaxWidth)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1 / 2)
            ) {
                Image(
                    src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/icon__detail--image-1.jpg?v=1635975757&width=800",
                    alt = "Blog Featured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .objectFit(ObjectFit.Cover)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .margin(4.em)
                        .gap(1.em)
                ) {
                    SpanText(
                        text = "In great detail".uppercase(),
                        modifier = HeadlineStyle.toModifier(H3Variant)
                    )
                    SpanText("The story behind the brand")
                    AppFilledButton(
                        onClick = { /* TODO */ },
                    ) {
                        SpanText(getString(Strings.ReadMore))
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1 / 2)
                    .overflow(Overflow.Hidden)
            ) {
                Image(
                    src = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/icon__detail--image-2.jpg?v=1635975799&width=800",
                    alt = "Blog Featured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .objectFit(ObjectFit.Cover)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .margin(2.em)
                        .gap(1.em)
                ) {
                    SpanText(
                        text = "Beach vibes".uppercase(),
                        modifier = HeadlineStyle.toModifier(H3Variant)
                    )
                    SpanText("For Vacay or Staycay")
                    AppFilledButton(
                        onClick = { /* TODO */ },
                    ) {
                        SpanText(getString(Strings.ReadMore))
                    }
                }
            }
        }
    }
}
