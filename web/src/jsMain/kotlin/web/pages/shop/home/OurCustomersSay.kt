package web.pages.shop.home

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
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rowGap
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiStarRate
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.AppColors
import web.H2Variant
import web.H3Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth

@Composable
fun OurCustomersSay(
    vm: HomeViewModel,
    state: HomeContract.State,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .maxWidth(oneLayoutMaxWidth)
            .padding(leftRight = 24.px)
            .margin(top = 2.em)
            .gap(3.em)
    ) {
        SpanText(
            text = getString(Strings.OurCustomerSay).uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
        )
        Row(
            modifier = gridModifier(3, gap = 1.5.em)
        ) {
            Item(
                title = "Great store",
                description = "Such a great variety and well priced too!",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks4.jpg?v=1614301039&width=500",
                rating = 5,
                author = "Stephanie, London"
            )
            Item(
                title = "Lovely team",
                description = "The team is always so helpful and willing to assist",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/slidefour.jpg?v=1613676525&width=500",
                rating = 5,
                author = "Jane, Manchester"
            )
            Item(
                title = "Fast shipping",
                description = "Always delivered so quickly, I am impressed",
                url = "https://icon-shopify-theme.myshopify.com/cdn/shop/files/looks6.jpg?v=1614301039&width=500",
                rating = 5,
                author = "Jane, Manchester"
            )
        }
    }
}

@Composable
private fun Item(
    title: String,
    description: String,
    url: String,
    rating: Int,
    author: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .backgroundColor(AppColors.lightBg)
            .padding(40.px)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .rowGap(0.25.em)
    ) {
        Image(
            src = url,
            modifier = Modifier
                .fillMaxWidth(50.percent)
                .borderRadius(50.percent)
                .aspectRatio(1f)
                .objectFit(ObjectFit.Cover)
                .styleModifier {
                    property("object-position", "center")
                }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(0.25.em)
        ) {
            repeat(rating) {
                MdiStarRate(
                    style = IconStyle.FILLED,
                    modifier = Modifier
                )
            }
            repeat(5 - rating) {
                MdiStarRate(
                    style = IconStyle.OUTLINED,
                    modifier = Modifier
                )
            }
        }
        SpanText(
            text = title,
            modifier = HeadlineStyle.toModifier(H3Variant)
                .fontWeight(FontWeight.Bold)
        )
        SpanText(text = description)
        SpanText(
            text = author,
            modifier = Modifier.fontWeight(FontWeight.SemiBold)
        )
    }
}
