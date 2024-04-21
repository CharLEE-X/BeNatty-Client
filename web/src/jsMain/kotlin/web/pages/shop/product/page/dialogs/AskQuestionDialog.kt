package web.pages.shop.product.page.dialogs

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCancel
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSend
import component.localization.Strings
import component.localization.getString
import feature.product.page.ProductPageContract
import feature.product.page.ProductPageViewModel
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Text
import theme.MaterialTheme
import web.components.widgets.AppFilledButton
import web.components.widgets.AppFilledTonalButton
import web.components.widgets.SearchBar
import web.compose.material3.component.Dialog

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
            actions = {
                AppFilledTonalButton(
                    onClick = {
                        onCancel()
                        onClosing(true)
                    },
                    leadingIcon = {
                        MdiCancel(
                            style = IconStyle.OUTLINED,
                            modifier = Modifier.color(MaterialTheme.colors.onTertiary)
                        )
                    },
                    containerColor = MaterialTheme.colors.tertiary
                ) {
                    Text(cancelText)
                }
                AppFilledButton(
                    onClick = {
                        onSend()
                        onClosing(true)
                    },
                    leadingIcon = {
                        MdiSend(
                            style = IconStyle.OUTLINED,
                            modifier = Modifier.color(MaterialTheme.colors.onPrimary)
                        )
                    },
                ) {
                    Text(sendText)
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.em)
            ) {
                Row {
                    SearchBar(
                        value = state.askQuestionData.name,
                        onValueChange = {
                            vm.trySend(
                                ProductPageContract.Inputs.OnAskQuestionDataChanged(
                                    data = state.askQuestionData.copy(name = it)
                                )
                            )
                        },
                        placeholder = getString(Strings.Name),
                        onEnterPress = {},
                        modifier = Modifier.weight(1f)
                    )
                    SearchBar(
                        value = state.askQuestionData.phone,
                        placeholder = getString(Strings.Phone),
                        onValueChange = {
                            vm.trySend(
                                ProductPageContract.Inputs.OnAskQuestionDataChanged(
                                    data = state.askQuestionData.copy(phone = it)
                                )
                            )
                        },
                        onEnterPress = {},
                        modifier = Modifier.weight(1f)
                    )
                }
                SearchBar(
                    value = state.askQuestionData.email,
                    placeholder = getString(Strings.Email),
                    onValueChange = {
                        vm.trySend(
                            ProductPageContract.Inputs.OnAskQuestionDataChanged(
                                data = state.askQuestionData.copy(email = it)
                            )
                        )
                    },
                    onEnterPress = {},
                    modifier = Modifier.fillMaxWidth()
                )
                SearchBar(
                    value = state.askQuestionData.question,
                    placeholder = getString(Strings.Comment),
                    onValueChange = {
                        vm.trySend(
                            ProductPageContract.Inputs.OnAskQuestionDataChanged(
                                data = state.askQuestionData.copy(question = it)
                            )
                        )
                    },
                    onEnterPress = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
