package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.millisToTime
import feature.admin.category.page.AdminCategoryPageContract
import feature.admin.category.page.AdminCategoryPageViewModel
import feature.admin.product.page.AdminProductPageContract
import org.jetbrains.compose.web.css.em
import web.components.layouts.DetailPageLayout
import web.components.widgets.CommonTextfield
import web.components.widgets.EditCancelButton
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.Switch

@Composable
fun AdminCategoryPage(
    id: String?,
    onError: suspend (String) -> Unit,
    goToList: suspend () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryPageViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goToUserList = goToList,
        )
    }
    val state by vm.observeStates().collectAsState()

    DetailPageLayout(
        title = if (state.screenState is AdminCategoryPageContract.ScreenState.New) {
            state.strings.createCategory
        } else {
            "${state.strings.category}: ${state.id}"
        },
        showDelete = state.screenState !is AdminCategoryPageContract.ScreenState.New,
        deleteText = state.strings.delete,
        cancelText = state.strings.cancel,
        onDeleteClick = { vm.trySend(AdminCategoryPageContract.Inputs.Delete) },
    ) {
        Details(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
    }
}

@Composable
private fun Details(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    SectionHeader(
        text = state.strings.details,
    ) {
        if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
            EditCancelButton(
                isEditing = state.isDetailsEditing,
                editText = state.strings.edit,
                cancelText = state.strings.cancel,
                edit = { vm.trySend(AdminCategoryPageContract.Inputs.SetDetailsEditable(true)) },
                cancel = { vm.trySend(AdminCategoryPageContract.Inputs.SetDetailsEditable(false)) },
            )
        }
    }
    CommonTextfield(
        value = state.name,
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.SetName(it)) },
        label = state.strings.name,
        errorMsg = state.nameError,
        icon = null,
        shake = state.shakeName,
        isEditing = if (state.screenState is AdminCategoryPageContract.ScreenState.Existing) {
            state.isDetailsEditing
        } else {
            true
        },
        modifier = Modifier.fillMaxWidth(),
    )

    if (state.screenState is AdminCategoryPageContract.ScreenState.New) {
        SaveButton(
            text = state.strings.create,
            disabled = state.isCreateButtonDisabled,
            onClick = { vm.trySend(AdminCategoryPageContract.Inputs.CreateNew) },
        )
    } else {
        CommonTextfield(
            value = state.description,
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.SetDescription(it)) },
            label = state.strings.description,
            errorMsg = null,
            icon = null,
            shake = false,
            isEditing = state.isDetailsEditing,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .onClick { vm.trySend(AdminCategoryPageContract.Inputs.SetDisplay(!state.display)) }
                .cursor(Cursor.Pointer)
                .margin(bottom = 1.em),
        ) {
            Switch(
                selected = state.display,
                onClick = { vm.trySend(AdminCategoryPageContract.Inputs.SetDisplay(!state.display)) },
                modifier = Modifier.gap(1.em),
            )
            SpanText(text = state.strings.display)
        }

        SpanText(text = "${state.strings.parentId}: ${state.parentId}")
        SpanText(text = "${state.strings.createdBy}: ${state.createdBy}")
        SpanText(text = "${state.strings.createdAt}: ${millisToTime(state.createdAt)}")
        SpanText(text = "${state.strings.updatedAt}: ${millisToTime(state.updatedAt)}")

        SaveButton(
            text = state.strings.save,
            disabled = state.isSaveButtonDisabled,
            onClick = { vm.trySend(AdminCategoryPageContract.Inputs.SaveDetails) },
        )
    }
}
