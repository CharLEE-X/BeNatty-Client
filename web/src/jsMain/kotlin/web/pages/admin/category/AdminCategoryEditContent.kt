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
import component.localization.Strings
import component.localization.getString
import feature.admin.category.page.AdminCategoryEditContract
import feature.admin.category.page.AdminCategoryEditViewModel
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
fun AdminCategoryEditContent(
    id: String,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCustomers: () -> Unit,
    goToCustomer: (String) -> Unit,
    goToCreateCategory: () -> Unit,
    goToCategory: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryEditViewModel(
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

    var deleteDialogOpen by remember { mutableStateOf(false) }
    var deleteDialogClosing by remember { mutableStateOf(false) }

    AdminLayout(
        title = getString(Strings.CreateCategory),
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = getString(Strings.UnsavedChanges),
        onCancel = { vm.trySend(AdminCategoryEditContract.Inputs.OnCancelEditClick) },
        onSave = { vm.trySend(AdminCategoryEditContract.Inputs.OnSaveEditClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = getString(Strings.UnsavedChanges),
                onSave = { vm.trySend(AdminCategoryEditContract.Inputs.OnSaveEditClick) },
                onCancel = { vm.trySend(AdminCategoryEditContract.Inputs.OnCancelEditClick) },
            )
            TakeActionDialog(
                open = deleteDialogOpen && !deleteDialogClosing,
                closing = deleteDialogClosing,
                title = "${getString(Strings.Delete)} ${state.original.name}",
                contentText = getString(Strings.DeleteExplain),
                onOpen = { deleteDialogOpen = it },
                onClosing = { deleteDialogClosing = it },
                onYes = { vm.trySend(AdminCategoryEditContract.Inputs.OnDeleteClick) },
                onNo = { deleteDialogClosing = true },
            )
        }
    ) {
        OneThirdLayout(
            title = state.original.name,
            subtitle = state.original.id,
            hasBackButton = true,
            actions = {
                AppFilledButton(
                    onClick = { deleteDialogOpen = !deleteDialogOpen },
                    leadingIcon = { MdiDelete() },
                    containerColor = MaterialTheme.colors.error,
                ) {
                    SpanText(text = getString(Strings.Delete))
                }
            },
            onGoBack = adminRoutes.goBack,
            content = {
                CardSection(title = null) {
                    Name(vm, state)
                }
                CardSection(title = getString(Strings.Shipping)) {
                    ShippingPreset(vm, state)
                }
            },
            contentThird = {
                CardSection(title = null) {
                    Status(vm, state)
                }
                CardSection(title = getString(Strings.Insights)) {
                    SpanText(text = getString(Strings.NoInsights))
                }
                CardSection(title = getString(Strings.CategoryOrganization)) {
                    ParentCategory(vm, state)
                }
                CardSection(title = getString(Strings.Info)) {
                    Creator(vm, state)
                    CreatedAt(state)
                    UpdatedAt(state)
                }
            }
        )
    }
}

@Composable
private fun Name(vm: AdminCategoryEditViewModel, state: AdminCategoryEditContract.State) {
    AppOutlinedTextField(
        value = state.current.name,
        onValueChange = { vm.trySend(AdminCategoryEditContract.Inputs.SetName(it)) },
        label = getString(Strings.Name),
        errorText = state.nameError,
        leadingIcon = null,
        shake = state.shakeName,
        required = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Description(vm: AdminCategoryEditViewModel, state: AdminCategoryEditContract.State) {
    AppOutlinedTextField(
        value = state.current.description ?: "",
        onValueChange = { vm.trySend(AdminCategoryEditContract.Inputs.SetDescription(it)) },
        label = getString(Strings.Description),
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
fun Status(vm: AdminCategoryEditViewModel, state: AdminCategoryEditContract.State) {
    SwitchSection(
        title = getString(Strings.Status),
        selected = state.current.display,
        onClick = { vm.trySend(AdminCategoryEditContract.Inputs.SetDisplay(!state.current.display)) },
    )
}

@Composable
private fun Creator(vm: AdminCategoryEditViewModel, state: AdminCategoryEditContract.State) {
    CreatorSection(
        title = getString(Strings.CreatedBy),
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminCategoryEditContract.Inputs.OnGoToUserCreatorClick) },
        afterTitle = { AppTooltip(getString(Strings.CreatedByDesc)) },
    )
}

@Composable
fun UpdatedAt(state: AdminCategoryEditContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.LastUpdatedAt)}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
fun CreatedAt(state: AdminCategoryEditContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.CreatedAt)}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminCategoryEditViewModel, state: AdminCategoryEditContract.State) {
    SwitchSection(
        title = getString(Strings.IsPhysicalProduct),
        selected = state.current.shippingPreset?.isPhysicalProduct ?: false,
        onClick = {
            vm.trySend(
                AdminCategoryEditContract.Inputs.SetRequiresShipping(
                    !(state.current.shippingPreset?.isPhysicalProduct ?: false)
                )
            )
        },
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.weight ?: "",
        onValueChange = { vm.trySend(AdminCategoryEditContract.Inputs.SetWeight(it)) },
        label = getString(Strings.Weight),
        errorText = state.weightError,
        leadingIcon = null,
        shake = state.shakeWeight,
        required = state.current.shippingPreset?.isPhysicalProduct == true,
        type = TextFieldType.NUMBER,
        suffixText = getString(Strings.Kg),
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.length ?: "",
        onValueChange = { vm.trySend(AdminCategoryEditContract.Inputs.SetLength(it)) },
        label = getString(Strings.Length),
        errorText = state.lengthError,
        leadingIcon = null,
        shake = state.shakeLength,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = getString(Strings.Cm),
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.width ?: "",
        onValueChange = { vm.trySend(AdminCategoryEditContract.Inputs.SetWidth(it)) },
        label = getString(Strings.Width),
        errorText = state.widthError,
        leadingIcon = null,
        shake = state.shakeWidth,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = getString(Strings.Cm),
        modifier = Modifier.fillMaxWidth(),
    )
    AppOutlinedTextField(
        value = state.current.shippingPreset?.height ?: "",
        onValueChange = { vm.trySend(AdminCategoryEditContract.Inputs.SetHeight(it)) },
        label = getString(Strings.Height),
        errorText = state.heightError,
        leadingIcon = null,
        shake = state.shakeHeight,
        required = state.current.shippingPreset?.isPhysicalProduct ?: false,
        type = TextFieldType.NUMBER,
        suffixText = getString(Strings.Cm),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ParentCategory(
    vm: AdminCategoryEditViewModel,
    state: AdminCategoryEditContract.State,
) {
    SpanText(text = getString(Strings.ParentCategory))
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.parent?.let { listOf(it.name) } ?: emptyList(),
        onChipClick = { vm.trySend(AdminCategoryEditContract.Inputs.OnParentCategoryClick(it)) },
        onCreateClick = { vm.trySend(AdminCategoryEditContract.Inputs.OnGoToCreateCategoryClick) },
        noChipsText = getString(Strings.NoCategories),
        createText = getString(Strings.CreateCategory),
    )
}
