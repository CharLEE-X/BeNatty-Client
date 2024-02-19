package web.components.sections.footer.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.CurrencyDropdown
import web.components.widgets.SimpleDropdown

@Composable
fun FooterDeliverTo(
    modifier: Modifier = Modifier,
    currentCountryText: String,
    countryImageUrl: String,
    languageText: String,
    currentLanguageText: String,
    deliverToText: String,
    onCurrencyClick: () -> Unit,
    onLanguageClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = deliverToText,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 1.em),
        )
        CurrencyDropdown(
            countryText = currentCountryText,
            countryImageUrl = countryImageUrl,
            countryImageAlt = currentCountryText,
            onClick = onCurrencyClick,
        )
        SpanText(
            text = languageText,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(top = 2.em, bottom = 1.em),
        )
        SimpleDropdown(
            text = currentLanguageText,
            onClick = onLanguageClick,
        )
    }
}
