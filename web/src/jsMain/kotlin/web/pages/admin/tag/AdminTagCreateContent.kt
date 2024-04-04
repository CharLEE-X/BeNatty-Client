package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import feature.admin.tag.create.AdminTagCreateContract
import feature.admin.tag.create.AdminTagCreateViewModel
import feature.admin.tag.create.adminTagCreateStrings
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.DetailPageLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.SaveButton
import web.util.onEnterKeyDown

@Composable
fun AdminTagCreateContent(
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToTag: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminTagCreateViewModel(
            scope = scope,
            onError = onError,
            goBack = adminRoutes.goBack,
            goToTag = goToTag,
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = adminTagCreateStrings.createTag,
        isLoading = state.isLoading,
        showEditedButtons = false,
        isSaveEnabled = false,
        unsavedChangesText = "",
        saveText = "",
        discardText = "",
        onCancel = { },
        onSave = { },
        adminRoutes = adminRoutes,
        overlay = { }
    ) {
        DetailPageLayout(
            title = adminTagCreateStrings.createTag,
            subtitle = null,
            showDelete = false,
            deleteText = "",
            createdAtText = "",
            updatedAtText = "",
            createdAtValue = "",
            updatedAtValue = "",
            onDeleteClick = { },
            onGoBack = adminRoutes.goBack,
        ) {
            CardSection(title = adminTagCreateStrings.details) {
                AppOutlinedTextField(
                    value = state.name,
                    onValueChange = { vm.trySend(AdminTagCreateContract.Inputs.SetName(it)) },
                    label = adminTagCreateStrings.name,
                    errorText = state.nameError,
                    error = state.nameError != null,
                    leadingIcon = null,
                    shake = state.shakeName,
                    required = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onEnterKeyDown { vm.trySend(AdminTagCreateContract.Inputs.OnCreateClick) }
                )
                SaveButton(
                    text = adminTagCreateStrings.create,
                    disabled = false,
                    onClick = { vm.trySend(AdminTagCreateContract.Inputs.OnCreateClick) },
                )
            }
        }
    }
}
