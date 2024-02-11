package web.pages.admin.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.thenIf
import feature.admin.tag.page.AdminTagPageContract
import feature.admin.tag.page.AdminTagPageViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.DetailPageLayout
import web.components.layouts.ImproveWithAiRow
import web.components.widgets.CardSection
import web.components.widgets.CommonTextField
import web.components.widgets.CreatorSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.SaveButton
import web.util.onEnterKeyDown

@Composable
fun AdminTagPage(
    router: RouterViewModel,
    id: String?,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminTagPageViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goToUserList = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminTagList.matcher.routeFormat
                    )
                )
            },
            goToUser = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminUserPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
            goToTag = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminTagPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    val title = if (state.screenState is AdminTagPageContract.ScreenState.New) {
        state.strings.createTag
    } else {
        state.strings.category
    }

    AdminLayout(
        title = title,
        router = router,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.save,
                resetText = state.strings.reset,
                onSave = { vm.trySend(AdminTagPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminTagPageContract.Inputs.OnClick.CancelEdit) },
            )
        }
    ) {
        DetailPageLayout(
            title = title,
            id = state.current.id.toString(),
            name = state.current.name.ifEmpty { null },
            showDelete = state.screenState !is AdminTagPageContract.ScreenState.New,
            deleteText = state.strings.delete,
            cancelText = state.strings.cancel,
            createdAtText = state.strings.createdAt,
            updatedAtText = state.strings.lastUpdatedAt,
            createdAtValue = state.current.createdAt,
            updatedAtValue = state.current.updatedAt,
            onDeleteClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.Delete) },
        ) {
            CardSection(title = state.strings.details) {
                ImproveWithAiRow(
                    tooltipText = state.strings.improveWithAi,
                    onImproveClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.ImproveName) }
                ) {
                    CommonTextField(
                        value = state.current.name,
                        onValueChange = { vm.trySend(AdminTagPageContract.Inputs.Set.Name(it)) },
                        label = state.strings.name,
                        errorMsg = state.nameError,
                        icon = null,
                        shake = state.shakeName,
                        required = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .thenIf(
                                state.screenState is AdminTagPageContract.ScreenState.New,
                                Modifier.onEnterKeyDown { vm.trySend(AdminTagPageContract.Inputs.OnClick.Create) }
                            )
                    )
                }
                if (state.screenState is AdminTagPageContract.ScreenState.New) {
                    SaveButton(
                        text = state.strings.create,
                        disabled = false,
                        onClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.Create) },
                    )
                } else {
                    CreatorSection(
                        title = state.strings.createdBy,
                        creatorName = state.current.creator.name,
                        onClick = { vm.trySend(AdminTagPageContract.Inputs.OnClick.GotToUserCreator) },
                    )
                }
            }
        }
    }
}
