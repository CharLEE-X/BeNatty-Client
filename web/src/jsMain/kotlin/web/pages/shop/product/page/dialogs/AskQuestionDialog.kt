package web.pages.shop.product.page.dialogs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Text
import web.compose.material3.component.Dialog

@Composable
fun AskQuestionDialog(
    vm: ProductPageViewModel,
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
                    .padding(1.em)
            ) {

            }
        }
    }
}
