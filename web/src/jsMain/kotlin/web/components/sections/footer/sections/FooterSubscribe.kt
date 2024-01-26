package web.components.sections.footer.sections

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
import web.components.sections.footer.FooterContract
import web.components.sections.footer.FooterViewModel
import web.compose.material3.component.IconButton
import web.compose.material3.component.OutlinedIconButton
import web.compose.material3.component.OutlinedTextField


@Composable
fun FooterSubscribe(
    modifier: Modifier = Modifier,
    state: FooterContract.State,
    vm: FooterViewModel,
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = state.strings.subscribe,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.labelLarge)
                .fontWeight(FontWeight.Bold)
        )
        OutlinedTextField(
            value = state.email,
            onInput = { vm.trySend(FooterContract.Inputs.SetEmail(it)) },
            label = state.strings.email,
            errorText = state.emailError,
            error = state.emailError != null,
            trailingIcon = {
                IconButton(
                    onClick = { vm.trySend(FooterContract.Inputs.OnEmailSend) },
                ) {
                    MdiSend()
                }
            },
            modifier = Modifier
//                .fillMaxWidth()
                .margin(top = 1.cssRem)
                .onKeyDown {
                    if (it.key == "Enter") {
                        vm.trySend(FooterContract.Inputs.OnEmailSend)
                    }
                }
        )
        Row(
            modifier = Modifier
                .gap(1.em)
                .margin(top = 1.em)
        ) {
            OutlinedIconButton(
                onClick = { },
            ) {
                Image(
                    src = "/facebook.png",
                    alt = "Facebook",
                    modifier = Modifier.size(1.em)
                )
            }
            OutlinedIconButton(
                onClick = { },
            ) {
                Image(
                    src = "/twitter.png",
                    alt = "Twitter",
                    modifier = Modifier.size(1.em)
                )
            }
            OutlinedIconButton(
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
