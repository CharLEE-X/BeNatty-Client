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
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle

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
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.displayLarge)
                    .fontWeight(FontWeight.SemiBold)
            )
            SpanText(
                text = appMotto,
                modifier = Modifier
                    .roleStyle(MaterialTheme.typography.labelLarge)
                    .fontWeight(FontWeight.SemiBold)
            )
        }
    }
}
