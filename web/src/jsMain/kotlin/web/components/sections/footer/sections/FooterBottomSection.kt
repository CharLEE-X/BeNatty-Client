package web.components.sections.footer.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.sections.footer.FooterContract
import web.components.sections.footer.FooterViewModel
import web.compose.material3.component.TextButton

@Composable
fun FooterBottomSection(
    vm: FooterViewModel,
    state: FooterContract.State,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(topBottom = 1.em, leftRight = 3.em),
    ) {
        SpanText(
            text = "© ${state.year} ${state.strings.companyName}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium),
        )
        Spacer()
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnPrivacyPolicyClicked) },
        ) {
            SpanText(
                text = state.strings.privacyPolicy,
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium),
            )
        }
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnTermsOfServiceClicked) },
        ) {
            SpanText(
                text = state.strings.termsOfService,
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium),
            )
        }
        TextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnAccessibilityClicked) },
        ) {
            SpanText(
                text = state.strings.accessibility,
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium),
            )
        }
    }
}
