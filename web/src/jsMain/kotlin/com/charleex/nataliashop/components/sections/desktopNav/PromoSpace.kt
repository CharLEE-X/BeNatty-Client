package com.charleex.nataliashop.components.sections.desktopNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.charleex.nataliashop.toSitePalette
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowDropDown
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLocalShipping
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun PromoSpace(
    currentLanguageImageUrl: String,
    horizontalMargin: CSSSizeValue<CSSUnit.em>,
    onHelpAndFaqClick: () -> Unit,
    onCurrentAndLanguageClick: () -> Unit,
) {
    val colorMode by ColorMode.currentState

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(topBottom = 0.25.em, leftRight = horizontalMargin)
            .backgroundColor(colorMode.toSitePalette().nearBackground)
            .color(colorMode.toSitePalette().neutral700)
            .fontFamily("Inter-Regular", "Helvetica")
            .fontWeight(FontWeight.Normal)
            .fontSize(12.px)
            .whiteSpace(WhiteSpace.NoWrap),
    ) {
        Promo(
            text = "FREE SHIPPING ON ORDERS OVER $50", // TODO: Localize
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .gap(16.px),
        ) {
            HelpAndFaqButton(
                text = "Help & FAQ", // TODO: Localize
                tooltip = "Help & FAQ", // TODO: Localize
                onClick = onHelpAndFaqClick,
            )
            CurrencyAndLanguageButton(
                text = "EN, $", // TODO: Localize
                tooltip = "Change language and currency", // TODO: Localize
                imgUrl = currentLanguageImageUrl,
                onClick = onCurrentAndLanguageClick,
            )
        }
    }
}

@Composable
private fun BoxScope.Promo(
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .align(Alignment.Center)
            .gap(10.px),
    ) {
        MdiLocalShipping(
            modifier = Modifier.fontSize(12.px),
        )
        SpanText(text)
    }
}

@Composable
private fun HelpAndFaqButton(
    text: String,
    tooltip: String,
    onClick: () -> Unit,
) {
    SpanText(
        text = text,
        modifier = ClickableStyle.toModifier()
            .onClick { onClick() },
    )
    Tooltip(ElementTarget.PreviousSibling, tooltip, placement = PopupPlacement.BottomRight)
}

@Composable
private fun CurrencyAndLanguageButton(
    text: String,
    tooltip: String,
    imgUrl: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = ClickableStyle.toModifier()
            .gap(6.px)
            .onClick { onClick() },
    ) {
        Image(
            src = imgUrl,
            alt = text,
            width = 16,
            height = 16,
            modifier = Modifier.borderRadius(50.percent),
        )
        SpanText(text)
        MdiArrowDropDown()
    }
    Tooltip(ElementTarget.PreviousSibling, tooltip, placement = PopupPlacement.BottomRight)
}
