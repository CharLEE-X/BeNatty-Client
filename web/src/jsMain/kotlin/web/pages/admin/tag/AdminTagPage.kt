package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.thenIf
import core.models.PageScreenState
import feature.admin.tag.page.AdminTagPageContract
import feature.admin.tag.page.AdminTagPageViewModel
import feature.admin.tag.page.adminTagPageStrings
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.DetailPageLayout
import web.components.layouts.ImproveWithButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.SaveButton
import web.util.onEnterKeyDown

@Composable
fun AdminTagPage(
    id: String?,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToTagList: () -> Unit,
    goToUserPage: (String) -> Unit,
    goToTagPage: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminTagPageViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goToTagList = goToTagList,
            goToUser = goToUserPage,
            goToTag = goToTagPage,
        )
    }
    val state by vm.observeStates().collectAsState()

    val title = if (state.pageScreenState is PageScreenState.New) {
        adminTagPageStrings.createTag
    } else {
        adminTagPageStrings.category
    }

    AdminLayout(
        title = title,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited || state.pageScreenState is PageScreenState.New,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = adminTagPageStrings.unsavedChanges,
        saveText = adminTagPageStrings.save,
        discardText = adminTagPageStrings.discard,
        onCancel = { vm.trySend(AdminTagPageContract.Inputs.OnClick.CancelEdit) },
        onSave = { vm.trySend(AdminTagPageContract.Inputs.OnClick.SaveEdit) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminTagPageStrings.unsavedChanges,
                saveText = adminTagPageStrings.save,
                resetText = adminTagPageStrings.dismiss,
                onSave = { vm.trySend(AdminTagPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminTagPageContract.Inputs.OnClick.CancelEdit) },
            )
        }
    ) {
        DetailPageLayout(
            title = title,
            subtitle = if (state.pageScreenState == PageScreenState.Existing) state.original.id else null,
            showDelete = state.pageScreenState !is PageScreenState.New,
            deleteText = adminTagPageStrings.delete,
            createdAtText = adminTagPageStrings.createdAt,
            updatedAtText = adminTagPageStrings.lastUpdatedAt,
            createdAtValue = state.current.createdAt,
            updatedAtValue = state.current.updatedAt,
            onDeleteClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.Delete) },
            onGoBack = adminRoutes.goBack,
        ) {
            CardSection(title = adminTagPageStrings.details) {
                AppOutlinedTextField(
                    value = state.current.name,
                    onValueChange = { vm.trySend(AdminTagPageContract.Inputs.Set.Name(it)) },
                    label = adminTagPageStrings.name,
                    errorText = state.nameError,
                    leadingIcon = null,
                    shake = state.shakeName,
                    required = true,
                    trailingIcon = {
                        ImproveWithButton(
                            tooltipText = adminTagPageStrings.improveWithAi,
                            onImproveClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.ImproveName) }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .thenIf(
                            state.pageScreenState is PageScreenState.New,
                            Modifier.onEnterKeyDown { vm.trySend(AdminTagPageContract.Inputs.OnClick.Create) }
                        )
                )
                if (state.pageScreenState is PageScreenState.New) {
                    SaveButton(
                        text = adminTagPageStrings.create,
                        disabled = false,
                        onClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.Create) },
                    )
                } else {
                    CreatorSection(
                        title = adminTagPageStrings.createdBy,
                        creatorName = state.current.creator.name,
                        onClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.GotToUserCreator) },
                    )
                }
            }
        }
    }
}
