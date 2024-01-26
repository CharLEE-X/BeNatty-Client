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
import web.components.sections.footer.FooterContract
import web.components.sections.footer.FooterViewModel
import web.compose.material3.component.TextButton

@Composable
fun FooterHelp(vm: FooterViewModel, state: FooterContract.State, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = state.strings.help,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 1.em, left = 1.em),
        )
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnTrackOrderClick) },
        ) {
            SpanText(state.strings.trackOrder)
        }
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnShippingClick) },
        ) {
            SpanText(state.strings.shipping)
        }
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnReturnsClick) },
        ) {
            SpanText(state.strings.returns)
        }
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnFAQsClick) },
        ) {
            SpanText(state.strings.faQs)
        }
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnContactUsClick) },
        ) {
            SpanText(state.strings.contactUs)
        }
    }
}
