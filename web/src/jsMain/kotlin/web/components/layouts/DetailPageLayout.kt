package web.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.FONT_CUSTOM
import web.components.widgets.DeleteButtonWithConfirmation
import web.compose.material3.component.Divider

@Composable
fun DetailPageLayout(
    title: String,
    id: String?,
    name: String?,
    showDelete: Boolean,
    deleteText: String,
    cancelText: String,
    createdAtText: String,
    updatedAtText: String,
    createdAtValue: String,
    updatedAtValue: String,
    onDeleteClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(1.em)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em)
        ) {
            SpanText(
                text = title,
                modifier = Modifier.roleStyle(MaterialTheme.typography.displayMedium)
            )
            name?.let {
                SpanText(
                    text = "'${name}'",
                    modifier = Modifier
                        .roleStyle(MaterialTheme.typography.displayMedium)
                        .fontFamily(FONT_CUSTOM)
                )
            }
            Spacer()
            if (showDelete) {
                DeleteButtonWithConfirmation(
                    deleteText = deleteText,
                    cancelText = cancelText,
                    onDelete = onDeleteClick,
                )
            }
        }
        id?.let {
            SpanText(
                text = "ID: $id",
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineSmall)
            )
        }
        Divider(modifier = Modifier.margin(bottom = 1.em))
        content()
        if (createdAtValue.isEmpty() && updatedAtValue.isEmpty()) {
            Box(Modifier.margin(top = 1.em))
        } else {
            Divider(modifier = Modifier.margin(top = 1.em))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
                    .margin(bottom = 1.em)
            ) {
                if (createdAtValue.isNotEmpty()) {
                    SpanText(
                        text = "$createdAtText: $createdAtValue",
                        modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
                    )
                }

                if (updatedAtValue.isNotEmpty()) {
                    SpanText(
                        text = "$updatedAtText: $updatedAtValue",
                        modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
                    )
                }
            }
        }
    }
}
