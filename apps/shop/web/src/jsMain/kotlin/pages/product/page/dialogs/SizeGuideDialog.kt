package pages.product.page.dialogs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.product.page.ProductPageContract

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
        SpanText("FIXME: Implement SizeGuideDialog")
//        Dialog(
//            open = open && !closing,
//            onClosed = {
//                onOpen(false)
//                onClosing(false)
//            },
//            onClosing = { onClosing(true) },
//            headline = {
//                Text(title)
//            },
//            actions = {}
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(3.em)
//            ) {
//                val standardRowModifier = Modifier
//                    .minWidth(500.px)
//                    .display(DisplayStyle.Grid)
//                    .gridTemplateColumns { repeat(2) { size(1.fr) } }
//                    .padding(1.em)
//                val bgRowModifier = standardRowModifier
//                    .backgroundColor(MaterialTheme.colors.surfaceContainerHighest)
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = bgRowModifier
//                ) {
//                    SpanText("Size", modifier = Modifier.weight(1))
//                    SpanText("UK", modifier = Modifier.weight(1))
//                }
//                state.sizeGuide.forEachIndexed { index, sizeGuide ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        modifier = if (index % 2 == 0) standardRowModifier else bgRowModifier
//                    ) {
//                        SpanText(sizeGuide.size, modifier = Modifier.weight(1))
//                        SpanText(sizeGuide.uk, modifier = Modifier.weight(1))
//                    }
//                }
//            }
//        }
    }
}
