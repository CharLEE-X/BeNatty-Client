package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import web.H1Variant
import web.H3Variant
import web.HeadlineStyle

@Composable
fun CardSection(
    title: String?,
    description: String? = null,
    contentPadding: CSSLengthOrPercentageNumericValue = 1.em,
    gap: CSSLengthOrPercentageNumericValue = 1.em,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .gap(0.5.em)
        ) {
            title?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(leftRight = 1.em, topBottom = 0.5.em)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(0.5.em)
                    ) {
                        SpanText(
                            text = title,
                            modifier = HeadlineStyle.toModifier(H3Variant)
                        )
                        description?.let {
                            SpanText(
                                text = it,
                                modifier = HeadlineStyle.toModifier(H1Variant)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .gap(gap)
            ) {
                content()
            }
        }
    }
}
