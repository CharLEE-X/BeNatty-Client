package web.pages.admin.category

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
import feature.admin.category.page.AdminCategoryPageContract
import feature.admin.category.page.AdminCategoryPageViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import web.components.layouts.AdminLayout
import web.components.layouts.DetailPageLayout
import web.components.layouts.ImproveWithAiRow
import web.components.widgets.CardSection
import web.components.widgets.CommonTextField
import web.components.widgets.CreatorSection
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.SaveButton
import web.components.widgets.SwitchSection
import web.compose.material3.component.TextFieldType

@Composable
fun AdminCategoryPage(
    router: RouterViewModel,
    id: String?,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryPageViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goToUserList = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminCategoryList.matcher.routeFormat
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
            goToCreateCategory = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.AdminCategoryPageNew.matcher.routeFormat)
                )
            },
            goToCategory = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminCategoryPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    val title = if (state.screenState is AdminCategoryPageContract.ScreenState.New) {
        state.strings.createCategory
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
                onSave = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.CancelEdit) },
            )
        }
    ) {
        DetailPageLayout(
            title = title,
            id = state.current.id.toString(),
            name = state.current.name.ifEmpty { null },
            showDelete = state.screenState !is AdminCategoryPageContract.ScreenState.New,
            deleteText = state.strings.delete,
            cancelText = state.strings.cancel,
            createdAtText = state.strings.createdAt,
            updatedAtText = state.strings.lastUpdatedAt,
            createdAtValue = state.current.createdAt,
            updatedAtValue = state.current.updatedAt,
            onDeleteClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Delete) },
        ) {
            Details(vm, state)
            if (state.screenState !is AdminCategoryPageContract.ScreenState.New) {
                ShippingPreset(vm, state)
            }
        }
    }
}

@Composable
private fun ShippingPreset(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    CardSection(title = state.strings.shipping) {
        SwitchSection(
            title = state.strings.requiresShipping,
            selected = state.current.shippingPreset?.requiresShipping ?: false,
            onClick = {
                vm.trySend(
                    AdminCategoryPageContract.Inputs.Set.RequiresShipping(
                        !(state.current.shippingPreset?.requiresShipping ?: false)
                    )
                )
            },
        )
        CommonTextField(
            value = state.current.shippingPreset?.weight ?: "",
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Weight(it)) },
            label = state.strings.weight,
            errorMsg = state.weightError,
            icon = null,
            shake = state.shakeWeight,
            required = state.current.shippingPreset?.requiresShipping == true,
            type = TextFieldType.NUMBER,
            suffixText = state.strings.kg,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = state.current.shippingPreset?.length ?: "",
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Length(it)) },
            label = state.strings.length,
            errorMsg = state.lengthError,
            icon = null,
            shake = state.shakeLength,
            required = state.current.shippingPreset?.requiresShipping ?: false,
            type = TextFieldType.NUMBER,
            suffixText = state.strings.cm,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = state.current.shippingPreset?.width ?: "",
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Width(it)) },
            label = state.strings.width,
            errorMsg = state.widthError,
            icon = null,
            shake = state.shakeWidth,
            required = state.current.shippingPreset?.requiresShipping ?: false,
            type = TextFieldType.NUMBER,
            suffixText = state.strings.cm,
            modifier = Modifier.fillMaxWidth(),
        )
        CommonTextField(
            value = state.current.shippingPreset?.height ?: "",
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Height(it)) },
            label = state.strings.height,
            errorMsg = state.heightError,
            icon = null,
            shake = state.shakeHeight,
            required = state.current.shippingPreset?.requiresShipping ?: false,
            type = TextFieldType.NUMBER,
            suffixText = state.strings.cm,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun Details(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    CardSection(title = state.strings.details) {
        CommonTextField(
            value = state.current.name,
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Name(it)) },
            label = state.strings.name,
            errorMsg = state.nameError,
            icon = null,
            shake = state.shakeName,
            required = true,
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.screenState is AdminCategoryPageContract.ScreenState.New) {
            SaveButton(
                text = state.strings.create,
                disabled = false,
                onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Create) },
            )
        } else {
            ImproveWithAiRow(
                tooltipText = state.strings.improveWithAi,
                onImproveClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.ImproveDescription) }
            ) {
                CommonTextField(
                    value = state.current.description ?: "",
                    onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Description(it)) },
                    label = state.strings.description,
                    errorMsg = null,
                    icon = null,
                    shake = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            SwitchSection(
                title = state.strings.display,
                selected = state.current.display,
                errorText = state.displayError,
                disabled = state.displayError != null,
                onClick = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Display(!state.current.display)) },
            )
            ParentCategory(vm, state)

            CreatorSection(
                title = state.strings.createdBy,
                creatorName = state.current.creator.name,
                onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.GotToUserCreator) },
            )
        }
    }
}

@Composable
private fun ParentCategory(
    vm: AdminCategoryPageViewModel,
    state: AdminCategoryPageContract.State,
) {
    FilterChipSection(
        title = "${state.strings.parentCategory}: ${state.current.parent?.name ?: state.strings.none}",
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.parent?.let { listOf(it.name) } ?: emptyList(),
        onChipClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.ParentCategorySelected(it)) },
        onCreateClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.GoToCreateCategory) },
        noChipsText = state.strings.noOtherCategoriesToChooseFrom,
        createText = state.strings.createCategory,
    )
}
