package web.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowBack
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import theme.MaterialTheme
import theme.roleStyle
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedIconButton
import web.compose.material3.component.Divider
import web.util.glossy

@Composable
fun DetailPageLayout(
    title: String,
    showDelete: Boolean,
    deleteText: String,
    createdAtText: String,
    updatedAtText: String,
    createdAtValue: String,
    updatedAtValue: String,
    onDeleteClick: () -> Unit,
    onGoBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .flex("1 1")
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Stretch)
            .padding(2.em)
            .gap(1.em)
    ) {
        NavTopSection(
            title = title,
            onGoBack = onGoBack,
            hasBackButton = true,
            actions = {
                if (showDelete) {
                    AppFilledButton(
                        onClick = { onDeleteClick() },
                        leadingIcon = { MdiDelete() },
                        containerColor = MaterialTheme.colors.error,
                    ) {
                        SpanText(text = deleteText)
                    }
                }
            },
        )
        content()
        BottomSection(
            createdAtValue = createdAtValue,
            updatedAtValue = updatedAtValue,
            createdAtText = createdAtText,
            updatedAtText = updatedAtText
        )
    }
}

@Composable
fun BottomSection(
    createdAtValue: String,
    updatedAtValue: String,
    createdAtText: String,
    updatedAtText: String
) {
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

@Composable
fun NavTopSection(
    title: String,
    hasBackButton: Boolean,
    onGoBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.px)
            .glossy()
            .gap(1.em)
    ) {
        if (hasBackButton) {
            AppOutlinedIconButton(
                onClick = { onGoBack() },
                modifier = Modifier.size(3.em)
            ) {
                MdiArrowBack()
            }
        }

        SpanText(
            text = title,
            modifier = Modifier.roleStyle(MaterialTheme.typography.displaySmall)
        )
        Spacer()
        actions()
    }
}
