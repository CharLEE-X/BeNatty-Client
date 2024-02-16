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
import web.components.widgets.AppTextButton

@Composable
fun FooterAboutUs(
    vm: FooterViewModel,
    state: FooterContract.State,
    modifier: Modifier = Modifier,
    onGoToAdminDashboard: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = state.strings.aboutUsSmall,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 1.em, left = 1.em),
        )
        AppTextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnAboutUsClick) },
        ) {
            SpanText(state.strings.aboutUs)
        }
        AppTextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnCareerClick) },
        ) {
            SpanText(state.strings.career)
        }
        AppTextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnCyberSecurityClick) },
        ) {
            SpanText(state.strings.cyberSecurity)
        }
        AppTextButton(
            onClick = { vm.trySend(FooterContract.Inputs.OnPressClick) },
        ) {
            SpanText(state.strings.press)
        }
        AppTextButton(
            onClick = { onGoToAdminDashboard() },
        ) {
            SpanText("Admin")
        }
    }
}
