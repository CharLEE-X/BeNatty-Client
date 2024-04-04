package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import core.models.PageScreenState
import feature.admin.category.page.AdminCategoryPageContract
import feature.admin.category.page.AdminCategoryPageViewModel
import feature.admin.category.page.adminCategoryPageStrings
import feature.admin.customer.page.adminCustomerPageStrings
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
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
    id: String?,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCustomers: () -> Unit,
    goToCustomer: (String) -> Unit,
    goToCreateCategory: () -> Unit,
    goToCategory: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryPageViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goToUserList = goToCustomers,
            goToUser = goToCustomer,
            goToCreateCategory = goToCreateCategory,
            goToCategory = goToCategory,
        )
    }
    val state by vm.observeStates().collectAsState()

    val title = if (state.pageScreenState is PageScreenState.New) {
        adminCategoryPageStrings.createCategory
    } else {
        adminCategoryPageStrings.category
    }

    var deleteDialogOpen by remember { mutableStateOf(false) }
    var deleteDialogClosing by remember { mutableStateOf(false) }

    AdminLayout(
        title = title,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited || state.pageScreenState is PageScreenState.New,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = adminCategoryPageStrings.unsavedChanges,
        saveText = adminCategoryPageStrings.save,
        discardText = adminCategoryPageStrings.discard,
        onCancel = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.CancelEdit) },
        onSave = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.SaveEdit) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminCategoryPageStrings.unsavedChanges,
                saveText = adminCategoryPageStrings.save,
                resetText = adminCategoryPageStrings.dismiss,
                onSave = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.CancelEdit) },
            )
            TakeActionDialog(
                open = deleteDialogOpen && !deleteDialogClosing,
                closing = deleteDialogClosing,
                title = "${adminCustomerPageStrings.delete} ${state.original.name}",
                actionYesText = adminCategoryPageStrings.delete,
                actionNoText = adminCategoryPageStrings.discard,
                contentText = adminCategoryPageStrings.deleteExplain,
                onOpen = { deleteDialogOpen = it },
                onClosing = { deleteDialogClosing = it },
                onYes = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Delete) },
                onNo = { deleteDialogClosing = true },
            )
        }
    ) {
        OneThirdLayout(
            title = if (state.pageScreenState is PageScreenState.New) {
                adminCategoryPageStrings.createCategory
            } else {
                state.original.name
            },
            subtitle = if (state.pageScreenState == PageScreenState.Existing) state.original.id else null,
            hasBackButton = true,
            actions = {
                if (state.pageScreenState is PageScreenState.Existing) {
                    AppFilledButton(
                        onClick = { deleteDialogOpen = !deleteDialogOpen },
                        leadingIcon = { MdiDelete() },
                        containerColor = MaterialTheme.colors.error,
                    ) {
                        SpanText(text = adminCategoryPageStrings.delete)
                    }
                }
            },
            onGoBack = adminRoutes.goBack,
            content = {
                CardSection(title = null) {
                    Title(vm, state)
                    Description(vm, state)
                }
                if (state.pageScreenState !is PageScreenState.New) {
                    CardSection(title = adminCategoryPageStrings.shipping) {
                        ShippingPreset(vm, state)
                    }
                }
            },
            contentThird = {
                CardSection(title = null) {
                    Status(vm, state)
                }
                CardSection(title = adminCategoryPageStrings.insights) {
                    SpanText(text = adminCategoryPageStrings.noInsights)
                }
                CardSection(title = adminCategoryPageStrings.categoryOrganization) {
                    ParentCategory(vm, state)
                }
                if (state.pageScreenState is PageScreenState.Existing) {
                    CardSection(title = adminCategoryPageStrings.info) {
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
        label = adminCategoryPageStrings.name,
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
        label = adminCategoryPageStrings.description,
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
        title = adminCategoryPageStrings.status,
        selected = state.current.display,
        onClick = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Display(!state.current.display)) },
    )
}

@Composable
private fun Creator(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    CreatorSection(
        title = adminCategoryPageStrings.createdBy,
        creatorName = "${state.current.creator.firstName} ${state.current.creator.lastName}",
        onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.GoToUserCreator) },
        afterTitle = { AppTooltip(adminCategoryPageStrings.createdByDesc) },
    )
}

@Composable
fun UpdatedAt(state: AdminCategoryPageContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${adminCategoryPageStrings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
fun CreatedAt(state: AdminCategoryPageContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${adminCategoryPageStrings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    SwitchSection(
        title = adminCategoryPageStrings.isPhysicalProduct,
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
        label = adminCategoryPageStrings.weight,
        errorText = state.weightError,
        leadingIcon = null,
        shake = state.shakeWeight,
        required = state.current.shippingPreset?.isPhysicalProduct == true,
        type = TextFieldType.NUMBER,
        suffixText = adminCategoryPageStrings.kg,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.length ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Length(it)) },
        label = adminCategoryPageStrings.length,
        errorText = state.lengthError,
        leadingIcon = null,
        shake = state.shakeLength,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = adminCategoryPageStrings.cm,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.width ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Width(it)) },
        label = adminCategoryPageStrings.width,
        errorText = state.widthError,
        leadingIcon = null,
        shake = state.shakeWidth,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = adminCategoryPageStrings.cm,
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.height ?: "",
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Height(it)) },
        label = adminCategoryPageStrings.height,
        errorText = state.heightError,
        leadingIcon = null,
        shake = state.shakeHeight,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = adminCategoryPageStrings.cm,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ParentCategory(
    vm: AdminCategoryPageViewModel,
    state: AdminCategoryPageContract.State,
) {
    SpanText(text = adminCategoryPageStrings.parentCategory)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.parent?.let { listOf(it.firstName).mapNotNull { it } } ?: emptyList(),
        onChipClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.ParentCategorySelected(it)) },
        onCreateClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.GoToCreateCategory) },
        noChipsText = adminCategoryPageStrings.noCategories,
        createText = adminCategoryPageStrings.createCategory,
    )
}
