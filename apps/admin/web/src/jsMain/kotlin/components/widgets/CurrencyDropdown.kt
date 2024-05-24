package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowDropDown
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import web.H1Variant
import web.HeadlineStyle

@Composable
fun CurrencyDropdown(
    modifier: Modifier = Modifier,
    countryText: String,
    countryImageUrl: String,
    countryImageAlt: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .gap(6.px)
            .onClick { onClick() },
    ) {
        Image(
            src = countryImageUrl,
            alt = countryImageAlt,
            width = 16,
            height = 16,
            modifier = Modifier.borderRadius(50.percent),
        )
        SpanText(
            text = countryText,
            modifier = HeadlineStyle.toModifier(H1Variant)
        )
        MdiArrowDropDown()
    }
}

@Composable
fun SimpleDropdown(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .gap(6.px)
            .onClick { onClick() },
    ) {
        SpanText(
            text = text,
            modifier = HeadlineStyle.toModifier(H1Variant)
        )
        MdiArrowDropDown()
    }
}
