package web.pages.auth.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.H1Variant
import web.H2Variant
import web.HeadlineStyle

@Composable
fun LogoSection(
    logoText: String,
    appName: String,
    appMotto: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.gap(1.em)
            .margin(bottom = 1.em)
    ) {
        Image(
            src = "/logo.png",
            alt = logoText,
            modifier = Modifier.size(8.em)
        )
        Column {
            SpanText(
                text = appName,
                modifier = HeadlineStyle.toModifier(H1Variant)
                    .fontWeight(FontWeight.SemiBold)
            )
            SpanText(
                text = appMotto,
                modifier = HeadlineStyle.toModifier(H2Variant)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}
