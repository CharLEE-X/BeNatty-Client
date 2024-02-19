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
fun FooterAboutUs(
    showAdminButton: Boolean,
    aboutUsSmallText: String,
    aboutUsText: String,
    careerText: String,
    cyberSecurityText: String,
    pressText: String,
    adminTextText: String,
    onAboutUsClick: () -> Unit,
    onCareerClick: () -> Unit,
    onCyberSecurityClick: () -> Unit,
    onPressClick: () -> Unit,
    onGoToAdminHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        SpanText(
            text = aboutUsSmallText,
            modifier = Modifier
                .roleStyle(MaterialTheme.typography.bodyMedium)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 1.em, left = 1.em),
        )
        AppTextButton(
            onClick = { onAboutUsClick() },
        ) {
            SpanText(aboutUsText)
        }
        AppTextButton(
            onClick = { onCareerClick() },
        ) {
            SpanText(careerText)
        }
        AppTextButton(
            onClick = { onCyberSecurityClick() },
        ) {
            SpanText(cyberSecurityText)
        }
        AppTextButton(
            onClick = { onPressClick() },
        ) {
            SpanText(pressText)
        }
        if (showAdminButton) {
            AppTextButton(
                onClick = { onGoToAdminHome() },
            ) {
                SpanText(adminTextText)
            }
        }
    }
}
