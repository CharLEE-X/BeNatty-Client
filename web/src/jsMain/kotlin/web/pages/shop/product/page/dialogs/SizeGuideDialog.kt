package web.pages.shop.product.page.dialogs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.product.page.ProductPageContract
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import theme.MaterialTheme
import web.compose.material3.component.Dialog
import web.util.cornerRadius

@Composable
fun SizeGuideDialog(
    state: ProductPageContract.State,
    open: Boolean,
    title: String,
    closing: Boolean,
    onOpen: (Boolean) -> Unit,
    onClosing: (Boolean) -> Unit,
) {
    if (open || closing) {
        Dialog(
            open = open && !closing,
            onClosed = {
                onOpen(false)
                onClosing(false)
            },
            onClosing = { onClosing(true) },
            headline = {
                Text(title)
            },
            actions = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.em)
            ) {
                val standardRowModifier = Modifier
                    .minWidth(500.px)
                    .display(DisplayStyle.Grid)
                    .gridTemplateColumns { repeat(2) { size(1.fr) } }
                    .padding(1.em)
                val bgRowModifier = standardRowModifier
                    .backgroundColor(MaterialTheme.colors.surfaceContainerHighest)
                    .borderRadius(cornerRadius)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = bgRowModifier
                ) {
                    SpanText("Size", modifier = Modifier.weight(1))
                    SpanText("UK", modifier = Modifier.weight(1))
                }
                state.sizeGuide.forEachIndexed { index, sizeGuide ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = if (index % 2 == 0) standardRowModifier else bgRowModifier
                    ) {
                        SpanText(sizeGuide.size, modifier = Modifier.weight(1))
                        SpanText(sizeGuide.uk, modifier = Modifier.weight(1))
                    }
                }
            }
        }
    }
}
