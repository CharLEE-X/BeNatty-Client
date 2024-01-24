package web.pages.auth.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.compose.material3.component.Divider
import web.compose.material3.component.FilledTonalButton

@Composable
fun ColumnScope.SocialButtonsLoginSection(
    header: String,
    googleButtonText: String,
    facebookButtonText: String,
    orText: String,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
) {
    SpanText(
        text = header,
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.headlineMedium)
            .align(Alignment.Start)
            .margin(top = 1.5.em)
    )
    FilledTonalButton(
        onClick = { onGoogleClick() },
        leadingIcon = {
            Image(
                src = "/google.png",
                alt = googleButtonText,
                modifier = Modifier.size(1.5.em)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        SpanText(
            text = googleButtonText,
            modifier = Modifier
                .margin(topBottom = 0.5.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }
    FilledTonalButton(
        onClick = { onFacebookClick() },
        leadingIcon = {
            Image(
                src = "/facebook.png",
                alt = facebookButtonText,
                modifier = Modifier.size(1.5.em)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        SpanText(
            text = facebookButtonText,
            modifier = Modifier
                .margin(topBottom = 0.5.em)
                .roleStyle(MaterialTheme.typography.headlineSmall)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .margin(topBottom = 2.em)
    ) {
        Divider()
        SpanText(
            text = orText,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.headlineSmall)
                .opacity(0.5f)
                .margin(leftRight = 0.5.em)
        )
        Divider()
    }
}
