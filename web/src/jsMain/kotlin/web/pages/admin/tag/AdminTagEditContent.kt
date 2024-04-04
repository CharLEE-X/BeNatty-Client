package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import feature.admin.tag.edit.AdminTagEditContract
import feature.admin.tag.edit.AdminTagEditViewModel
import feature.admin.tag.edit.adminTagEditStrings
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.DetailPageLayout
import web.components.layouts.ImproveWithButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.HasChangesWidget

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
        }
    ) {
        DetailPageLayout(
            title = state.original.name,
            subtitle = state.original.id,
            showDelete = true,
            deleteText = adminTagEditStrings.delete,
            createdAtText = adminTagEditStrings.createdAt,
            updatedAtText = adminTagEditStrings.lastUpdatedAt,
            createdAtValue = state.current.createdAt,
            updatedAtValue = state.current.updatedAt,
            onDeleteClick = { vm.trySend(AdminTagEditContract.Inputs.OnDeleteClick) },
            onGoBack = adminRoutes.goBack,
        ) {
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
        }
    }
}
