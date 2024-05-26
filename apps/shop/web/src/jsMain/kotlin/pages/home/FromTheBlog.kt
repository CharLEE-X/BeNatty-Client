package pages.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.shop.home.HomeContract
import feature.shop.home.HomeViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.AppColors
import web.H2Variant
import web.HeadlineStyle
import web.SubtitleStyle
import web.components.layouts.oneLayoutMaxWidth
import web.components.widgets.AppTextButton

@Composable
fun FromTheBlog(viewModel: HomeViewModel, state: HomeContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(AppColors.lightBg)
            .margin(top = 2.em)
    ) {
        SpanText(
            text = getString(Strings.FromTheBlog).uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
                .padding(topBottom = 1.5.em)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
        ) {
            Image(
                src = "https://icon-shopify-theme.myshopify.com/cdn/shop/articles/blog-two.jpg?v=1562342340&width=800",
                alt = "",
                modifier = Modifier
                    .fillMaxWidth(60.percent)
            )
            Column(
                modifier = Modifier
                    .padding(2.5.em)
                    .gap(1.em)
                    .backgroundColor(AppColors.lightBg)
                    .translateX((-80).px)
            ) {
                SpanText(
                    text = "Color pop",
                    modifier = HeadlineStyle.toModifier(H2Variant)
                        .fontWeight(FontWeight.SemiBold)
                )
                SpanText(
                    text = "As much as we live and breath monochrome styles (and we can all agree a majority of our " +
                        "daily wear is crisp whites and deep blacks), a gals gotta have a...",
                )
                SpanText(
                    text = "Posted on Apr 28, 2024",
                    modifier = SubtitleStyle.toModifier()
                )
                Row(
                    modifier = Modifier.gap(0.5.em)
                ) {
                    AppTextButton(
                        onClick = {},
                        content = { SpanText("Clothing") },
                    )
                    AppTextButton(
                        onClick = {},
                        content = { SpanText("Fashion") },
                    )
                }
            }
        }
        Box(Modifier.size(4.em))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
        ) {
            Column(
                modifier = Modifier
                    .padding(2.5.em)
                    .gap(1.em)
                    .backgroundColor(AppColors.lightBg)
                    .translateX(40.px)
            ) {
                SpanText(
                    text = "Summer look book",
                    modifier = HeadlineStyle.toModifier(H2Variant)
                        .fontWeight(FontWeight.SemiBold)
                )
                SpanText(
                    text = "Take a look at our favorite pieces from the latest summer collection in our hand- picked " +
                        "summer look book. Our inspiration for each piece was light, fun and an essential piece of every...",
                )
                SpanText(
                    text = "Posted on Mar 06, 2024",
                    modifier = SubtitleStyle.toModifier()
                )
                Row(
                    modifier = Modifier.gap(0.5.em)
                ) {
                    AppTextButton(
                        onClick = {},
                        content = { SpanText("Clothing") },
                    )
                    AppTextButton(
                        onClick = {},
                        content = { SpanText("Fashion") },
                    )
                }
            }
            Image(
                src = "https://icon-shopify-theme.myshopify.com/cdn/shop/articles/blog-one.jpg?v=1562342357&width=800",
                alt = "",
                modifier = Modifier
                    .fillMaxWidth(60.percent)
            )
            Box(Modifier.size(4.em))
        }
    }
}
