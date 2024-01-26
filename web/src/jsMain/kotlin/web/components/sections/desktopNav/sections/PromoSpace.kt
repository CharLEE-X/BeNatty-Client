package web.components.sections.desktopNav.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalShipping
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.components.sections.desktopNav.DesktopNavContract
import web.components.widgets.CurrencyDropdown

@Composable
fun PromoSpace(
    currentLanguageImageUrl: String,
    onHelpAndFaqClick: () -> Unit,
    onCurrentAndLanguageClick: () -> Unit,
    bgColor: CSSColorValue,
    contentColor: CSSColorValue,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(topBottom = 0.25.em)
            .backgroundColor(bgColor)
            .color(contentColor)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(70.percent)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Center)
                    .gap(10.px),
            ) {
                MdiLocalShipping(
                    modifier = Modifier.fontSize(20.px)
                )
                SpanText(
                    text = DesktopNavContract.Strings.promoText,
                    modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .gap(16.px),
            ) {
                SpanText(
                    text = DesktopNavContract.Strings.helpAndFaq,
                    modifier = Modifier
                        .onClick { onHelpAndFaqClick() }
                        .roleStyle(MaterialTheme.typography.labelLarge)
                )

                CurrencyDropdown(
                    countryText = DesktopNavContract.Strings.currencyEnUs,
                    countryImageUrl = currentLanguageImageUrl,
                    countryImageAlt = DesktopNavContract.Strings.currencyEnUs,
                    onClick = onCurrentAndLanguageClick
                )
            }
        }
    }
}
