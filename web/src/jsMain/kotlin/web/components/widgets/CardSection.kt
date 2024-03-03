package web.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle


@Composable
fun CardSection(
    title: String?,
    description: String? = null,
    elevation: Int? = null,
    color: CSSColorValue? = MaterialTheme.colors.surfaceContainerLow,
    content: @Composable () -> Unit,
) {
    AppElevatedCard(
        elevation = elevation,
        color = color,
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
                        .backgroundColor(MaterialTheme.colors.primaryContainer)
                        .borderRadius(topLeft = 12.px, topRight = 12.px)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(0.5.em)
                    ) {
                        SpanText(
                            text = title,
                            modifier = Modifier
                                .roleStyle(MaterialTheme.typography.headlineSmall)
                                .fontWeight(FontWeight.Bold)
                                .userSelect(UserSelect.None)
                        )
                        description?.let {
                            SpanText(
                                text = it,
                                modifier = Modifier.roleStyle(MaterialTheme.typography.labelLarge)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.em)
                    .gap(1.em)
            ) {
                content()
            }
        }
    }
}
