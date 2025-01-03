package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.admin.tag.edit.AdminTagEditContract
import feature.admin.tag.edit.AdminTagEditViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.EditButton
import web.components.widgets.HasChangesWidget
import web.components.widgets.TakeActionDialog

@Composable
fun AdminTagEditContent(
    id: String,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToUser: (String) -> Unit,
    goToTag: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminTagEditViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goBack = adminRoutes.goBack,
            goToUser = goToUser,
            goToTag = goToTag,
        )
    }
    val state by vm.observeStates().collectAsState()

    var deleteProductDialogOpen by remember { mutableStateOf(false) }
    var deleteProductDialogClosing by remember { mutableStateOf(false) }

    AdminLayout(
        title = getString(Strings.Tag),
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        onCancel = { vm.trySend(AdminTagEditContract.Inputs.OnCancelEditClick) },
        onSave = { vm.trySend(AdminTagEditContract.Inputs.OnSaveEditClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = getString(Strings.UnsavedChanges),
                onSave = { vm.trySend(AdminTagEditContract.Inputs.OnSaveEditClick) },
                onCancel = { vm.trySend(AdminTagEditContract.Inputs.OnCancelEditClick) },
            )
            TakeActionDialog(
                open = deleteProductDialogOpen && !deleteProductDialogClosing,
                closing = deleteProductDialogClosing,
                title = "${getString(Strings.Delete)} ${state.original.name}",
                contentText = getString(Strings.DeleteExplain),
                onOpen = { deleteProductDialogOpen = it },
                onClosing = { deleteProductDialogClosing = it },
                onYes = { vm.trySend(AdminTagEditContract.Inputs.OnDeleteClick) },
                onNo = { deleteProductDialogClosing = true },
            )
        }
    ) {
        OneThirdLayout(
            title = state.original.name,
            subtitle = state.original.id,
            onGoBack = adminRoutes.goBack,
            hasBackButton = true,
            actions = { EditButton { deleteProductDialogOpen = !deleteProductDialogOpen } },
            content = {
                CardSection(getString(Strings.Details)) {
                    AppOutlinedTextField(
                        text = state.current.name,
                        onTextChanged = { vm.trySend(AdminTagEditContract.Inputs.SetName(it)) },
                        placeholder = getString(Strings.Name),
                        required = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    CreatorSection(
                        title = getString(Strings.CreatedBy),
                        creatorName = state.current.creator.name,
                        onClick = { vm.trySend(AdminTagEditContract.Inputs.OnGotToUserCreatorClick) },
                    )
                }
            },
            contentThird = {
                CardSection(getString(Strings.Info)) {
                    Creator(vm, state)
                    CreatedAt(state)
                    UpdatedAt(state)
                }
            }
        )
    }
}

@Composable
private fun Creator(vm: AdminTagEditViewModel, state: AdminTagEditContract.State) {
    CreatorSection(
        title = getString(Strings.CreatedAt),
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminTagEditContract.Inputs.OnUserCreatorClick) },
        afterTitle = { AppTooltip(getString(Strings.CreatedByDesc)) },
    )
}

@Composable
private fun UpdatedAt(state: AdminTagEditContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.LastUpdatedAt)}: ${state.current.updatedAt}",
        )
    }
}

@Composable
private fun CreatedAt(state: AdminTagEditContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.CreatedAt)}: ${state.current.createdAt}",
        )
    }
}
