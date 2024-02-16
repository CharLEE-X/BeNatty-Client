package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.category.page.AdminCategoryPageContract
import feature.admin.category.page.AdminCategoryPageViewModel
import feature.router.RouterViewModel
import feature.router.Screen
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.SwitchSection
import web.components.widgets.TakeActionDialog
import web.compose.material3.component.TextFieldType

@Composable
fun AdminCategoryPage(
    router: RouterViewModel,
    id: String?,
    onError: suspend (String) -> Unit,
    goToAdminHome: () -> Unit,
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
                        Screen.AdminCategoryList.matcher.routeFormat
                    )
                )
            },
            goToUser = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        Screen.AdminCustomerProfile.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
            goToCreateCategory = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(Screen.AdminCategoryPageNew.matcher.routeFormat)
                )
            },
            goToCategory = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        Screen.AdminCategoryPageExisting.directions()
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

    var deleteDialogOpen by remember { mutableStateOf(false) }
    var deleteDialogClosing by remember { mutableStateOf(false) }

    AdminLayout(
        title = title,
        router = router,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited || state.screenState is AdminCategoryPageContract.ScreenState.New,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = state.strings.unsavedChanges,
        saveText = state.strings.save,
        discardText = state.strings.discard,
        onCancel = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.CancelEdit) },
        onSave = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.SaveEdit) },
        goToAdminHome = goToAdminHome,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.save,
                resetText = state.strings.dismiss,
                onSave = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.CancelEdit) },
            )
            TakeActionDialog(
                open = deleteDialogOpen && !deleteDialogClosing,
                closing = deleteDialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.discard,
                contentText = state.strings.deleteExplain,
                onOpen = { deleteDialogOpen = it },
                onClosing = { deleteDialogClosing = it },
                onYes = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Delete) },
                onNo = { deleteDialogClosing = true },
            )
        }
    ) {
        OneThirdLayout(
            title = if (state.screenState is AdminCategoryPageContract.ScreenState.New) {
                state.strings.createCategory
            } else {
                state.original.name
            },
            hasBackButton = true,
            actions = {
                if (state.screenState is AdminCategoryPageContract.ScreenState.Existing) {
                    AppFilledButton(
                        onClick = { deleteDialogOpen = !deleteDialogOpen },
                        leadingIcon = { MdiDelete() },
                        containerColor = MaterialTheme.colors.mdSysColorError.value(),
                    ) {
                        SpanText(text = state.strings.delete)
                    }
                }
            },
            onGoBack = { router.trySend(RouterContract.Inputs.GoBack()) },
            content = {
                CardSection(title = null) {
                    Title(vm, state)
                    Description(vm, state)
                }
                if (state.screenState !is AdminCategoryPageContract.ScreenState.New) {
                    CardSection(title = state.strings.shipping) {
                        ShippingPreset(vm, state)
                    }
                }
            },
            contentThird = {
                CardSection(title = null) {
                    Status(vm, state)
                }
                CardSection(title = state.strings.insights) {
                    SpanText(text = state.strings.noInsights)
                }
                CardSection(title = state.strings.categoryOrganization) {
                    ParentCategory(vm, state)
                }
                if (state.screenState is AdminCategoryPageContract.ScreenState.Existing) {
                    CardSection(title = state.strings.info) {
                        Creator(vm, state)
                        CreatedAt(state)
                        UpdatedAt(state)
                    }
                }
            }
        )
    }
}

@Composable
private fun Title(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    AppOutlinedTextField(
        value = state.current.name,
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Name(it)) },
        label = state.strings.name,
        errorText = state.nameError,
        leadingIcon = null,
        shake = state.shakeName,
        required = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Description(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    AppOutlinedTextField(
        value = state.current.description ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Description(it)) },
        label = state.strings.description,
        errorText = null,
        leadingIcon = null,
        shake = false,
        type = TextFieldType.TEXTAREA,
        rows = 3,
        modifier = Modifier
            .fillMaxWidth()
            .resize(Resize.Vertical)
    )
}

@Composable
fun Status(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    SwitchSection(
        title = state.strings.status,
        selected = state.current.display,
        onClick = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Display(!state.current.display)) },
    )
}

@Composable
private fun Creator(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    CreatorSection(
        title = state.strings.createdBy,
        creatorName = "${state.current.creator.firstName} ${state.current.creator.lastName}",
        onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.GoToUserCreator) },
        afterTitle = { AppTooltip(state.strings.createdByDesc) },
    )
}

@Composable
fun UpdatedAt(state: AdminCategoryPageContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${state.strings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
fun CreatedAt(state: AdminCategoryPageContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${state.strings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    SwitchSection(
        title = state.strings.isPhysicalProduct,
        selected = state.current.shippingPreset?.isPhysicalProduct ?: false,
        onClick = {
            vm.trySend(
                AdminCategoryPageContract.Inputs.Set.RequiresShipping(
                    !(state.current.shippingPreset?.isPhysicalProduct ?: false)
                )
            )
        },
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.weight ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Weight(it)) },
        label = state.strings.weight,
        errorText = state.weightError,
        leadingIcon = null,
        shake = state.shakeWeight,
        required = state.current.shippingPreset?.isPhysicalProduct == true,
        type = TextFieldType.NUMBER,
        suffixText = state.strings.kg,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.length ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Length(it)) },
        label = state.strings.length,
        errorText = state.lengthError,
        leadingIcon = null,
        shake = state.shakeLength,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = state.strings.cm,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.width ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Width(it)) },
        label = state.strings.width,
        errorText = state.widthError,
        leadingIcon = null,
        shake = state.shakeWidth,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = state.strings.cm,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.height ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Height(it)) },
        label = state.strings.height,
        errorText = state.heightError,
        leadingIcon = null,
        shake = state.shakeHeight,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = state.strings.cm,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ParentCategory(
    vm: AdminCategoryPageViewModel,
    state: AdminCategoryPageContract.State,
) {
    SpanText(text = state.strings.parentCategory)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.parent?.let { listOf(it.firstName).mapNotNull { it } } ?: emptyList(),
        onChipClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.ParentCategorySelected(it)) },
        onCreateClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.GoToCreateCategory) },
        noChipsText = state.strings.noCategories,
        createText = state.strings.createCategory,
    )
}
