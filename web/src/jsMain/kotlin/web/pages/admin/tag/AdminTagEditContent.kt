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
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.product.edit.adminProductPageStrings
import feature.admin.tag.edit.AdminTagEditContract
import feature.admin.tag.edit.AdminTagEditViewModel
import feature.admin.tag.edit.adminTagEditStrings
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.ImproveWithButton
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
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
        title = adminTagEditStrings.tag,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = adminTagEditStrings.unsavedChanges,
        saveText = adminTagEditStrings.save,
        discardText = adminTagEditStrings.discard,
        onCancel = { vm.trySend(AdminTagEditContract.Inputs.OnCancelEditClick) },
        onSave = { vm.trySend(AdminTagEditContract.Inputs.OnSaveEditClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminTagEditStrings.unsavedChanges,
                saveText = adminTagEditStrings.save,
                resetText = adminTagEditStrings.dismiss,
                onSave = { vm.trySend(AdminTagEditContract.Inputs.OnSaveEditClick) },
                onCancel = { vm.trySend(AdminTagEditContract.Inputs.OnCancelEditClick) },
            )
            TakeActionDialog(
                open = deleteProductDialogOpen && !deleteProductDialogClosing,
                closing = deleteProductDialogClosing,
                title = "${adminTagEditStrings.delete} ${state.original.name}",
                actionYesText = adminTagEditStrings.delete,
                actionNoText = adminTagEditStrings.discard,
                contentText = adminTagEditStrings.deleteExplain,
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
            actions = {
                AppFilledButton(
                    onClick = { deleteProductDialogOpen = !deleteProductDialogOpen },
                    leadingIcon = { MdiDelete() },
                    containerColor = MaterialTheme.colors.error,
                ) {
                    SpanText(text = adminProductPageStrings.delete)
                }
            },
            content = {
                CardSection(title = adminTagEditStrings.details) {
                    AppOutlinedTextField(
                        value = state.current.name,
                        onValueChange = { vm.trySend(AdminTagEditContract.Inputs.SetName(it)) },
                        label = adminTagEditStrings.name,
                        errorText = state.nameError,
                        leadingIcon = null,
                        shake = state.shakeName,
                        required = true,
                        trailingIcon = {
                            ImproveWithButton(
                                tooltipText = adminTagEditStrings.improveWithAi,
                                onImproveClick = { vm.trySend(AdminTagEditContract.Inputs.OnImproveNameClick) }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CreatorSection(
                        title = adminTagEditStrings.createdBy,
                        creatorName = state.current.creator.name,
                        onClick = { vm.trySend(AdminTagEditContract.Inputs.OnGotToUserCreatorClick) },
                    )
                }
            },
            contentThird = {
                CardSection(title = adminProductPageStrings.info) {
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
        title = adminProductPageStrings.createdBy,
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminTagEditContract.Inputs.OnUserCreatorClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.createdByDesc) },
    )
}

@Composable
private fun UpdatedAt(state: AdminTagEditContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${adminProductPageStrings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun CreatedAt(state: AdminTagEditContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${adminProductPageStrings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}
