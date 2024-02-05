package web.pages.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import feature.admin.category.page.AdminCategoryPageContract
import feature.admin.category.page.AdminCategoryPageViewModel
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.value
import theme.MaterialTheme
import web.components.layouts.DetailPageLayout
import web.components.widgets.CommonTextfield
import web.components.widgets.EditCancelButton
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.FilledButton
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.Switch

@Composable
fun AdminCategoryPage(
    id: String?,
    onError: suspend (String) -> Unit,
    goToList: suspend () -> Unit,
    goToUserDetail: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminCategoryPageViewModel(
            id = id,
            scope = scope,
            onError = onError,
            goToUserList = goToList,
            goToUser = goToUserDetail,
        )
    }
    val state by vm.observeStates().collectAsState()

    DetailPageLayout(
        title = if (state.screenState is AdminCategoryPageContract.ScreenState.Create) {
            state.strings.createCategory
        } else {
            state.strings.category
        },
        id = state.id,
        name = state.name.ifEmpty { null },
        showDelete = state.screenState !is AdminCategoryPageContract.ScreenState.Create,
        deleteText = state.strings.delete,
        cancelText = state.strings.cancel,
        createdAtText = state.strings.createdAt,
        updatedAtText = state.strings.updatedAt,
        createdAtValue = state.createdAt,
        updatedAtValue = state.updatedAt,
        onDeleteClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Delete) },
    ) {
        Details(vm, state)
    }
}

@Composable
private fun Details(vm: AdminCategoryPageViewModel, state: AdminCategoryPageContract.State) {
    SectionHeader(
        text = state.strings.details,
    ) {
        println("state.screenState: ${state.screenState}")
        if (state.screenState is AdminCategoryPageContract.ScreenState.Existing) {
            EditCancelButton(
                isEditing = state.isDetailsEditing,
                editText = state.strings.edit,
                cancelText = state.strings.cancel,
                edit = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Edit) },
                cancel = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Cancel) },
            )
        }
    }
    CommonTextfield(
        value = state.name,
        onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Name(it)) },
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

    if (state.screenState is AdminCategoryPageContract.ScreenState.Create) {
        SaveButton(
            text = state.strings.create,
            disabled = state.isCreateButtonDisabled,
            onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.CreateNew) },
        )
    } else {
        CommonTextfield(
            value = state.description,
            onValueChange = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Description(it)) },
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
                .onClick { vm.trySend(AdminCategoryPageContract.Inputs.Set.Display(!state.display)) }
                .cursor(Cursor.Pointer)
                .margin(bottom = 1.em),
        ) {
            Switch(
                selected = state.display,
                onClick = { vm.trySend(AdminCategoryPageContract.Inputs.Set.Display(!state.display)) },
                modifier = Modifier.gap(1.em),
            )
            SpanText(text = state.strings.display)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(1.em)
        ) {
            SpanText(text = "${state.strings.parentCategory}:")
            state.parent?.let { parent ->
                OutlinedButton(
                    onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Parent) },
                ) {
                    SpanText(text = parent.name)
                }
            } ?: SpanText(text = state.strings.none)
        }
        if (state.isDetailsEditing) {
            CategoryPicker(state, vm)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.gap(1.em)
        ) {
            SpanText(text = "${state.strings.createdBy}:")
            OutlinedButton(
                onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.Creator) },
            ) {
                SpanText(text = state.creator.name)
            }
        }

        SaveButton(
            text = state.strings.save,
            disabled = state.isSaveButtonDisabled,
            onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.SaveDetails) },
        )
    }
}

@Composable
private fun CategoryPicker(
    state: AdminCategoryPageContract.State,
    vm: AdminCategoryPageViewModel
) {
    if (state.categories.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.em)
                .gap(1.em)
                .border(
                    width = 1.px,
                    color = MaterialTheme.colors.mdSysColorSurface.value(),
                    style = LineStyle.Solid,
                )
                .borderRadius(2.em)
        ) {
            state.categories.forEach { category ->
                if (state.parent?.id == category.id) { // current parent
                    FilledButton(
                        onClick = {
                            vm.trySend(AdminCategoryPageContract.Inputs.OnClick.ParentPicker(category))
                        },
                    ) {
                        SpanText(text = category.name)
                    }
                } else {
                    OutlinedButton(
                        onClick = { vm.trySend(AdminCategoryPageContract.Inputs.OnClick.ParentPicker(category)) },
                    ) {
                        SpanText(text = category.name)
                    }
                }
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            SpanText(text = state.strings.noOtherCategoriesToChooseFrom)
        }
    }
}
