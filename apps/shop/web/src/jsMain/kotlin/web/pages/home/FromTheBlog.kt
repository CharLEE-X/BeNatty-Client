package web.pages.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
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
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
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
fun FromTheBlog(vm: HomeViewModel, state: HomeContract.State) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(AppColors.lightBg)
            .margin(top = 2.em)
    ) {
        SpanText(
            text = state.fromTheBlog.title.uppercase(),
            modifier = HeadlineStyle.toModifier(H2Variant)
                .padding(topBottom = 1.5.em)
        )
        state.fromTheBlog.items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .maxWidth(oneLayoutMaxWidth)
            ) {
                Image(
                    src = item.url,
                    alt = item.title,
                    modifier = Modifier.fillMaxWidth(60.percent)
                )
                Column(
                    modifier = Modifier
                        .padding(2.5.em)
                        .gap(1.em)
                        .backgroundColor(AppColors.lightBg)
                        .translateX((-80).px)
                ) {
                    SpanText(
                        text = item.title,
                        modifier = HeadlineStyle.toModifier(H2Variant)
                            .fontWeight(FontWeight.SemiBold)
                    )
                    SpanText(
                        text = item.description,
                    )
                    SpanText(
                        text = item.date,
                        modifier = SubtitleStyle.toModifier()
                    )
                    Row(
                        modifier = Modifier.gap(0.5.em)
                    ) {
                        item.links.forEach { link ->
                            AppTextButton(
                                onClick = {
                                    // link.url
                                },
                                content = { SpanText(link.title) },
                            )
                        }
                    }
                }
            }
        }
    }
}
