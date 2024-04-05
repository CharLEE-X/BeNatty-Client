package web.pages.admin.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import feature.admin.product.create.AdminProductCreateContract
import feature.admin.product.create.AdminProductCreateViewModel
import feature.admin.product.create.adminProductCreateStrings
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreateButton
import web.components.widgets.TrailingIconSubmit
import web.util.onEnterKeyDown

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

    var nameFocused by remember { mutableStateOf(false) }

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
                AppOutlinedTextField(
                    value = state.name,
                    onValueChange = { vm.trySend(AdminProductCreateContract.Inputs.SetName(it)) },
                    label = adminProductCreateStrings.name,
                    errorText = state.nameError,
                    error = state.nameError != null,
                    required = true,
                    trailingIcon = { TrailingIconSubmit(show = nameFocused && state.nameError == null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onEnterKeyDown { vm.trySend(AdminProductCreateContract.Inputs.OnCreateClick) }
                        .onFocusIn { nameFocused = true }
                        .onFocusOut { nameFocused = false }
                )
                CreateButton(
                    onClick = { vm.trySend(AdminProductCreateContract.Inputs.OnCreateClick) },
                )
            }
        }
    }
}
