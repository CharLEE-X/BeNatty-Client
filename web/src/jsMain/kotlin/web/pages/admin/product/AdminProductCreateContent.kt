package web.pages.admin.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCreate
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.product.create.AdminProductCreateContract
import feature.admin.product.create.AdminProductCreateViewModel
import feature.admin.product.create.adminProductCreateStrings
import org.jetbrains.compose.web.css.px
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection

@Composable
fun AdminProductCreateContent(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToProduct: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductCreateViewModel(
            scope = scope,
            onError = onError,
            goBack = adminRoutes.goBack,
            goToProduct = goToProduct,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = adminProductCreateStrings.newProduct,
        isLoading = state.isLoading,
        showEditedButtons = false,
        isSaveEnabled = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        onCancel = { },
        onSave = { },
        adminRoutes = adminRoutes,
        overlay = {},
    ) {
        OneLayout(
            title = adminProductCreateStrings.newProduct,
            subtitle = null,
            onGoBack = adminRoutes.goBack,
            hasBackButton = true,
            actions = {},
        ) {
            CardSection(title = null) {
                Name(state, vm)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer()
                    AppFilledButton(
                        onClick = { vm.trySend(AdminProductCreateContract.Inputs.OnCreateClick) },
                        leadingIcon = { MdiCreate() },
                        modifier = Modifier.width(150.px)
                    ) {
                        SpanText(adminProductCreateStrings.create.uppercase())
                    }
                }
            }
        }
    }
}

@Composable
private fun Name(
    state: AdminProductCreateContract.State,
    vm: AdminProductCreateViewModel
) {
    AppOutlinedTextField(
        value = state.name,
        onValueChange = { vm.trySend(AdminProductCreateContract.Inputs.SetName(it)) },
        label = adminProductCreateStrings.name,
        errorText = state.nameError,
        error = state.nameError != null,
        required = true,
        modifier = Modifier.fillMaxWidth()
    )
}
