package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import feature.admin.product.page.AdminProductPageContract
import feature.admin.product.page.AdminProductPageViewModel
import org.jetbrains.compose.web.css.em
import web.components.widgets.CommonTextfield
import web.components.widgets.EditCancelButton
import web.components.widgets.PageHeader
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.pages.admin.users.DeleteButtonWithConfirmation

@Composable
fun AdminProductPagePage(
    productId: String?,
    onError: suspend (String) -> Unit,
    goToProductList: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goToProductList = goToProductList,
        )
    }
    val state by vm.observeStates().collectAsState()

    PageHeader(
        title = if (state.screenState is AdminProductPageContract.ScreenState.New) {
            "Create Product"
        } else {
            "Product: ${state.id}"
        }
    ) {
        if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
            DeleteButtonWithConfirmation(
                deleteText = state.strings.delete,
                cancelText = state.strings.cancel,
                onDelete = { vm.trySend(AdminProductPageContract.Inputs.DeleteProduct) },
            )
        }
    }
    CommonDetails(vm, state)
    Divider(modifier = Modifier.margin(topBottom = 1.em))
}

@Composable
private fun CommonDetails(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SectionHeader(
        text = state.strings.details,
    ) {
        if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
            EditCancelButton(
                isEditing = state.isCommonDetailsEditing,
                editText = state.strings.edit,
                cancelText = state.strings.cancel,
                edit = { vm.trySend(AdminProductPageContract.Inputs.SetCommonDetailsEditable(true)) },
                cancel = { vm.trySend(AdminProductPageContract.Inputs.SetCommonDetailsEditable(false)) },
            )
        }
    }
    CommonTextfield(
        value = state.name,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetName(it)) },
        label = state.strings.name,
        errorMsg = state.nameError,
        icon = { MdiPerson() },
        shake = state.shakeName,
        isEditing = if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
            state.isCommonDetailsEditing
        } else {
            true
        },
        modifier = Modifier.fillMaxWidth(),
    )

    if (state.screenState is AdminProductPageContract.ScreenState.New) {
        SaveButton(
            text = state.strings.create,
            disabled = state.isCreateProductButtonDisabled,
            onClick = { vm.trySend(AdminProductPageContract.Inputs.CreateNewProduct) },
        )
    } else {
        CommonTextfield(
            value = state.shortDescription,
            onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetShortDescription(it)) },
            label = state.strings.shortDescription,
            errorMsg = state.shortDescriptionError,
            icon = { MdiPerson() },
            shake = state.shakeShortDescription,
            isEditing = state.isCommonDetailsEditing,
            modifier = Modifier.fillMaxWidth(),
        )

        SaveButton(
            text = state.strings.save,
            disabled = state.isSaveCommonDetailsButtonDisabled,
            onClick = { vm.trySend(AdminProductPageContract.Inputs.SaveCommonDetails) },
        )
    }
}
