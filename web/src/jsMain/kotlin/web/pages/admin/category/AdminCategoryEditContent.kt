package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.getString
import feature.admin.category.edit.AdminCategoryEditContract
import feature.admin.category.edit.AdminCategoryEditViewModel
import kotlinx.browser.document
import kotlinx.coroutines.launch
import kotlinx.dom.clear
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.get
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.DeleteButton
import web.components.widgets.HasChangesWidget
import web.components.widgets.MediaSlot
import web.components.widgets.SwitchSection
import web.components.widgets.TakeActionDialog
import web.util.convertImageToBase64

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
            actions = { DeleteButton { deleteDialogOpen = !deleteDialogOpen } },
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
                CardSection(title = getString(Strings.Media)) {
                    Media(
                        vm = vm,
                        state = state,
                    )
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
private fun Media(
    vm: AdminCategoryEditViewModel,
    state: AdminCategoryEditContract.State,
) {
    val scope = rememberCoroutineScope()
    MediaSlot(
        url = state.current.mediaUrl,
        alt = null,
        isImagesLoading = state.loading,
        errorText = state.imageDropError,
        isImageClickable = false,
        hasDeleteButton = false,
        hasEditButton = true,
        onFileDropped = { file ->
            scope.launch {
                convertImageToBase64(file)?.let { imageString ->
                    vm.trySend(AdminCategoryEditContract.Inputs.AddMedia(imageString))
                } ?: vm.trySend(AdminCategoryEditContract.Inputs.SetImageDropError(error = "Not a PNG?"))
            }
        },
        onEditClick = {
            val input = document.createElement("input") as HTMLInputElement
            input.type = "file"
            input.accept = "image/*"
            input.style.display = "none"
            input.addEventListener("change", { event: Event ->
                val files = (event.target as HTMLInputElement).files
                if (files != null && files.length > 0) {
                    val file = files[0]
                    scope.launch {
                        file?.let { file ->
                            convertImageToBase64(file)?.let { imageString ->
                                vm.trySend(AdminCategoryEditContract.Inputs.AddMedia(imageString))
                            } ?: vm.trySend(AdminCategoryEditContract.Inputs.SetImageDropError(error = "Not a PNG?"))
                        }
                    }
                }
                input.clear()
            })
            document.body?.appendChild(input)
            input.click()
            document.body?.removeChild(input)
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
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
        )
    }
}

@Composable
fun CreatedAt(state: AdminCategoryEditContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.CreatedAt)}: ${state.current.createdAt}",
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
//        type = InputType.Number,
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
//        type = TextFieldType.NUMBER,
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
//        type = TextFieldType.NUMBER,
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
//        type = TextFieldType.NUMBER,
        suffixText = getString(Strings.Cm),
        modifier = Modifier.fillMaxWidth(),
    )
}
