package web.compose.extras.text

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLElement
import theme.MaterialTheme
import theme.TypeScaleTokens
import theme.TypeScaleTokens.Companion.applyStyle

@Composable
fun Text(
    role: TypeScaleTokens.Role,
    inline: Boolean = false,
    content: ContentBuilder<HTMLElement>? = null
) {
    if (inline)
        Span(
            {
                style {
                    applyStyle(role)
                }
            },
            content = content
        )
    else
        Div(
            {
                style {
                    applyStyle(role)
                }
            },
            content = content
        )
}

@Composable
fun Text(
    role: TypeScaleTokens.Role,
    inline: Boolean = false,
    value: String
) {
    Text(role, inline) {
        org.jetbrains.compose.web.dom.Text(value)
    }
}

@Composable
fun LargeDisplay(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.displayLarge, inline, value)

@Composable
fun MediumDisplay(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.displayMedium, inline, value)

@Composable
fun SmallDisplay(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.displaySmall, inline, value)

@Composable
fun LargeHeadline(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.headlineLarge, inline, value)

@Composable
fun MediumHeadline(value: String, inline: Boolean = false) =
    Text(MaterialTheme.typography.headlineMedium, inline, value)

@Composable
fun SmallHeadline(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.headlineSmall, inline, value)

@Composable
fun LargeTitle(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.titleLarge, inline, value)

@Composable
fun MediumTitle(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.titleMedium, inline, value)

@Composable
fun SmallTitle(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.titleSmall, inline, value)

@Composable
fun LargeBody(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.bodyLarge, inline, value)

@Composable
fun LargeBody(inline: Boolean = false, content: ContentBuilder<HTMLElement>) =
    Text(MaterialTheme.typography.bodyLarge, inline, content)

@Composable
fun MediumBody(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.bodyMedium, inline, value)

@Composable
fun MediumBody(inline: Boolean = false, content: ContentBuilder<HTMLElement>) =
    Text(MaterialTheme.typography.bodyMedium, inline, content)

@Composable
fun SmallBody(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.bodySmall, inline, value)

@Composable
fun SmallBody(inline: Boolean = false, content: ContentBuilder<HTMLElement>) =
    Text(MaterialTheme.typography.bodySmall, inline, content)

@Composable
fun LargeLabel(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.labelLarge, inline, value)

@Composable
fun MediumLabel(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.labelMedium, inline, value)

@Composable
fun SmallLabel(value: String, inline: Boolean = false) = Text(MaterialTheme.typography.labelSmall, inline, value)
