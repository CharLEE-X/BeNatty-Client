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
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.H2Variant
import web.H3Variant
import web.HeadlineStyle
import web.components.widgets.AppFilledButton

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
        modifier = HeadlineStyle.toModifier(H2Variant)
            .align(Alignment.Start)
            .margin(top = 1.5.em)
    )
    AppFilledButton(
        onClick = { onGoogleClick() },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        Image(
            src = "/google.png",
            alt = googleButtonText,
            modifier = Modifier.size(1.5.em)
        )
        SpanText(
            text = googleButtonText,
            modifier = HeadlineStyle.toModifier(H3Variant)
                .margin(topBottom = 0.5.em)
        )
    }
    AppFilledButton(
        onClick = { onFacebookClick() },
        modifier = Modifier
            .fillMaxWidth()
            .margin(top = 1.em)
    ) {
        Image(
            src = "/facebook.png",
            alt = facebookButtonText,
            modifier = Modifier.size(1.5.em)
        )
        SpanText(
            text = facebookButtonText,
            modifier = HeadlineStyle.toModifier(H3Variant)
                .margin(topBottom = 0.5.em)
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .margin(topBottom = 2.em)
    ) {
        HorizontalDivider()
        SpanText(
            text = orText,
            modifier = HeadlineStyle.toModifier(H3Variant)
                .opacity(0.5f)
                .margin(leftRight = 0.5.em)
        )
        HorizontalDivider()
    }
}
