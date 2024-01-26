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
import web.components.sections.desktopNav.DesktopNavContract
import web.components.sections.footer.FooterContract
import web.components.sections.footer.FooterViewModel
import web.components.widgets.CurrencyDropdown
import web.components.widgets.SimpleDropdown

@Composable
fun FooterDeliverTo(
    vm: FooterViewModel,
    state: FooterContract.State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = state.strings.deliverTo,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 1.em),
        )
        CurrencyDropdown(
            countryText = DesktopNavContract.Strings.currencyEnUs,
            countryImageUrl = state.languageImageUrl,
            countryImageAlt = DesktopNavContract.Strings.currencyEnUs,
            onClick = { vm.trySend(FooterContract.Inputs.OnCurrencyClick) },
        )
        SpanText(
            text = state.strings.language,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(top = 2.em, bottom = 1.em),
        )
        SimpleDropdown(
            text = state.strings.english,
            onClick = { vm.trySend(FooterContract.Inputs.OnLanguageClick) },
        )
    }
}
