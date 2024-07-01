package web.components.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalShipping
import com.varabyte.kobweb.silk.components.icons.mdi.MdiRefresh
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSentimentSatisfied
import com.varabyte.kobweb.silk.components.icons.mdi.MdiVerifiedUser
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import web.H3Variant
import web.HeadlineStyle
import web.components.layouts.oneLayoutMaxWidth

@Composable
fun FreeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(oneLayoutMaxWidth)
                .display(DisplayStyle.Grid)
                .gridTemplateColumns { repeat(4) { size(1.fr) } }
                .padding(
                    top = 2.5.em,
                    bottom = 1.em,
                    leftRight = 24.px
                )
        ) {
            AddItem(
                title = getString(Strings.FreeShipping),
                description = getString(Strings.OnAllOrdersOver),
                icon = {
                    MdiLocalShipping(
                        style = IconStyle.OUTLINED,
                        modifier = Modifier.fontSize(32.px)
                    )
                }
            )
            AddItem(
                title = getString(Strings.FreeReturns),
                description = getString(Strings.OnAllUnopenedUnusedItems),
                icon = {
                    MdiRefresh(
                        style = IconStyle.OUTLINED,
                        modifier = Modifier.fontSize(32.px)
                    )
                }
            )
            AddItem(
                title = getString(Strings.SecureShipping),
                description = getString(Strings.ShopWithConfidence),
                icon = {
                    MdiVerifiedUser(
                        style = IconStyle.OUTLINED,
                        modifier = Modifier.fontSize(32.px)
                    )
                }
            )
            AddItem(
                title = getString(Strings.PercentSatisfaction),
                description = getString(Strings.RatedExcellentByCustomers),
                icon = {
                    MdiSentimentSatisfied(
                        style = IconStyle.OUTLINED,
                        modifier = Modifier.fontSize(32.px)
                    )
                }
            )
        }
    }
}

@Composable
private fun AddItem(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.gap(1.em)
    ) {
        icon()
        SpanText(
            text = title.uppercase(),
            modifier = HeadlineStyle.toModifier(H3Variant)
        )
        SpanText(text = description)
    }
}
