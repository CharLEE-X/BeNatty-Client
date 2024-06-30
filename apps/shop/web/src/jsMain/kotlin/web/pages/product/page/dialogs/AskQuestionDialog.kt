package web.pages.product.page.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.em
import web.components.widgets.AppOutlinedTextField

@Composable
fun AskQuestionDialog(
    vm: ProductPageViewModel,
    state: ProductPageContract.State,
    open: Boolean,
    title: String,
    sendText: String = getString(Strings.Send),
    cancelText: String = getString(Strings.Cancel),
    closing: Boolean,
    onOpen: (Boolean) -> Unit,
    onClosing: (Boolean) -> Unit,
    onSend: () -> Unit,
    onCancel: () -> Unit,
) {
    var nameFocused by remember { mutableStateOf(false) }
    var emailFocused by remember { mutableStateOf(false) }
    var questionFocused by remember { mutableStateOf(false) }

    if (open || closing) {
        SpanText("TODO: Implement AskQuestionDialog")
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
//            actions = {
//                AppFilledButton(
//                    onClick = {
//                        onCancel()
//                        onClosing(true)
//                    },
//                    leadingIcon = {
//                        MdiCancel(style = IconStyle.OUTLINED)
//                    },
//                    containerColor = MaterialTheme.colors.tertiary
//                ) {
//                    Text(cancelText)
//                }
//                AppFilledButton(
//                    onClick = {
//                        onSend()
//                        onClosing(true)
//                    },
//                    leadingIcon = { MdiSend(style = IconStyle.OUTLINED) },
//                ) {
//                    Text(sendText)
//                }
//            },
//        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 2.em, top = 1.em)
                .gap(1.em)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                AppOutlinedTextField(
                    text = state.askQuestionName,
                    onTextChange = { vm.trySend(ProductPageContract.Inputs.OnAskQuestionNameChanged(it)) },
                    placeholder = getString(Strings.Name),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusIn { nameFocused = true }
                        .onFocusOut { nameFocused = false }
                )
                AppOutlinedTextField(
                    text = state.askQuestionEmail,
                    onTextChange = { vm.trySend(ProductPageContract.Inputs.OnAskQuestionEmailChanged(it)) },
                    placeholder = getString(Strings.Email),
                    required = true,
                    valid = state.askQuestionEmailError == null,
                    modifier = Modifier
                        .weight(1f)
                        .resize(Resize.Vertical)
                        .onFocusIn { emailFocused = true }
                        .onFocusOut { emailFocused = false }
                )
            }
            AppOutlinedTextField(
                text = state.askQuestionQuestion,
                onTextChange = { vm.trySend(ProductPageContract.Inputs.OnAskQuestionQuestionChanged(it)) },
                placeholder = getString(Strings.Comment),
                required = true,
                valid = state.askQuestionQuestionError == null,
                modifier = Modifier.fillMaxWidth()
                    .resize(Resize.Vertical)
                    .onFocusIn { questionFocused = true }
                    .onFocusOut { questionFocused = false }
            )
        }
    }
}
