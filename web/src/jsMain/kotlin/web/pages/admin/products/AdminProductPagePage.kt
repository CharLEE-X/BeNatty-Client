package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.icons.mdi.MdiPerson
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import data.type.CatalogVisibility
import feature.admin.product.page.AdminProductPageContract
import feature.admin.product.page.AdminProductPageViewModel
import feature.admin.tag.page.AdminTagPageContract
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.DetailPageLayout
import web.components.widgets.CommonTextfield
import web.components.widgets.EditCancelButton
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.Divider
import web.compose.material3.component.Radio
import web.compose.material3.component.Switch

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

    DetailPageLayout(
        title = if (state.screenState is AdminTagPageContract.ScreenState.New) {
            state.strings.createProduct
        } else {
            "${state.strings.product}: ${state.id}"
        },
        showDelete = state.screenState is AdminProductPageContract.ScreenState.Existing,
        deleteText = state.strings.delete,
        cancelText = state.strings.cancel,
        onDeleteClick = { vm.trySend(AdminProductPageContract.Inputs.DeleteProduct) },
    ) {
        CommonDetails(vm, state)
        Divider(modifier = Modifier.margin(topBottom = 1.em))
    }
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .onClick { vm.trySend(AdminProductPageContract.Inputs.SetIsFeatured(!state.isFeatured)) }
                .cursor(Cursor.Pointer)
        ) {
            Switch(
                selected = state.isFeatured,
                modifier = Modifier.gap(1.em),
            )
            SpanText(text = state.strings.isFeatured)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .gap(1.em)
                .onClick { vm.trySend(AdminProductPageContract.Inputs.SetAllowReviews(!state.allowReviews)) }
                .cursor(Cursor.Pointer)
        ) {
            Switch(
                selected = state.allowReviews,
                modifier = Modifier.gap(1.em),
            )
            SpanText(text = state.strings.allowReviews)
        }
        Column(
            modifier = Modifier
                .gap(0.5.em)
                .margin(topBottom = 1.em)
        ) {
            SpanText(
                text = state.strings.catalogVisibility,
                modifier = Modifier.roleStyle(MaterialTheme.typography.headlineMedium)
            )
            CatalogVisibility.entries
                .filter { it != CatalogVisibility.UNKNOWN__ }
                .forEach { catalogVisibility ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(left = 2.em)
                            .gap(1.em)
                            .onClick {
                                vm.trySend(
                                    AdminProductPageContract.Inputs.SetCatalogVisibility(
                                        catalogVisibility
                                    )
                                )
                            }
                            .cursor(Cursor.Pointer)
                    ) {
                        Radio(
                            checked = state.catalogVisibility == catalogVisibility,
                            name = "role",
                            value = catalogVisibility.name,
                        )
                        SpanText(text = catalogVisibility.name.enumCapitalized())
                    }
                }
        }

        SaveButton(
            text = state.strings.save,
            disabled = state.isSaveCommonDetailsButtonDisabled,
            onClick = { vm.trySend(AdminProductPageContract.Inputs.SaveCommonDetails) },
        )
    }
}
