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
import web.components.widgets.AppTextButton

@Composable
fun FooterHelp(
    modifier: Modifier = Modifier,
    helpText: String,
    trackOrderText: String,
    shippingText: String,
    returnsText: String,
    faQsText: String,
    contactUsText: String,
    onTrackOrderClick: () -> Unit,
    onShippingClick: () -> Unit,
    onReturnsClick: () -> Unit,
    onFAQsClick: () -> Unit,
    onContactUsClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = helpText,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 1.em, left = 1.em),
        )
        AppTextButton(
            onClick = { onTrackOrderClick() },
        ) {
            SpanText(trackOrderText)
        }
        AppTextButton(
            onClick = { onShippingClick() },
        ) {
            SpanText(shippingText)
        }
        AppTextButton(
            onClick = { onReturnsClick() },
        ) {
            SpanText(returnsText)
        }
        AppTextButton(
            onClick = { onFAQsClick() },
        ) {
            SpanText(faQsText)
        }
        AppTextButton(
            onClick = { onContactUsClick() },
        ) {
            SpanText(contactUsText)
        }
    }
}
