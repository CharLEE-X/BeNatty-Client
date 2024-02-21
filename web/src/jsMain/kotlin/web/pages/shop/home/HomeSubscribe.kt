package web.pages.shop.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSend
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.AppIconButton
import web.components.widgets.AppOutlinedIconButton
import web.components.widgets.SearchBar

@Composable
fun HomeSubscribe(
    modifier: Modifier = Modifier,
    subscribeText: String,
    emailText: String,
    emailPlaceholder: String,
    onEmailSend: () -> Unit,
    onEmailChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = subscribeText,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.labelLarge)
                .fontWeight(FontWeight.Bold)
        )
        SearchBar(
            value = emailText,
            onValueChange = onEmailChange,
            placeholder = emailPlaceholder,
            onEnterPress = onEmailSend,
            leadingIcon = {
                Image(
                    src = "/email.png",
                    alt = "Email",
                    modifier = Modifier.size(1.em)
                )
            },
            trailingIcon = {
                AppIconButton(
                    onClick = { onEmailSend() },
                ) {
                    MdiSend()
                }
            },
            modifier = Modifier
                .margin(top = 1.cssRem)
                .onKeyDown {
                    if (it.key == "Enter") {
                        onEmailSend()
                    }
                }
        )
        Row(
            modifier = Modifier
                .gap(1.em)
                .margin(top = 1.em)
        ) {
            AppOutlinedIconButton(
                onClick = { },
            ) {
                Image(
                    src = "/facebook.png",
                    alt = "Facebook",
                    modifier = Modifier.size(1.em)
                )
            }
            AppOutlinedIconButton(
                onClick = { },
            ) {
                Image(
                    src = "/twitter.png",
                    alt = "Twitter",
                    modifier = Modifier.size(1.em)
                )
            }
            AppOutlinedIconButton(
                onClick = { },
            ) {
                Image(
                    src = "/instagram.png",
                    alt = "Instagram",
                    modifier = Modifier.size(1.em)
                )
            }
        }
    }
}