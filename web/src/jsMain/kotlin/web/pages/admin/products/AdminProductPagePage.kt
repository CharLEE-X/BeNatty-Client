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
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import data.type.CatalogVisibility
import data.type.PostStatus
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
import web.compose.material3.component.ChipSet
import web.compose.material3.component.FilterChip
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.Radio
import web.compose.material3.component.Switch

@Composable
fun AdminProductPagePage(
    productId: String?,
    onError: suspend (String) -> Unit,
    goToProductList: () -> Unit,
    goToCreateCategory: suspend () -> Unit,
    goToCreateTag: suspend () -> Unit,
    goToUserDetails: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goToProductList = goToProductList,
            goToCreateCategory = goToCreateCategory,
            goToCreateTag = goToCreateTag,
            goToUserDetails = goToUserDetails,
        )
    }
    val state by vm.observeStates().collectAsState()

    DetailPageLayout(
        title = if (state.screenState is AdminTagPageContract.ScreenState.New) {
            state.strings.createProduct
        } else {
            state.strings.product
        },
        id = state.id,
        name = state.name.ifEmpty { null },
        showDelete = state.screenState is AdminProductPageContract.ScreenState.Existing,
        deleteText = state.strings.delete,
        cancelText = state.strings.cancel,
        createdAtText = state.strings.createdAt,
        updatedAtText = state.strings.updatedAt,
        createdAtValue = state.createdAt,
        updatedAtValue = state.updatedAt,
        onDeleteClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Delete) },
    ) {
        CommonDetails(vm, state)
        Data(vm, state)
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
                edit = { vm.trySend(AdminProductPageContract.Inputs.OnClick.EditDetails) },
                cancel = { vm.trySend(AdminProductPageContract.Inputs.OnClick.CancelEditDetails) },
            )
        }
    }
    Name(state, vm)
    if (state.screenState is AdminProductPageContract.ScreenState.New) {
        SaveButton(
            text = state.strings.create,
            disabled = state.isCreateProductButtonDisabled,
            onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Create) },
        )
    } else {
        ShortDescription(state, vm)
        IsFeatured(vm, state)
        AllowReviews(vm, state)
        CatalogVisibility(state, vm)
        Categories(state, vm)
        Tags(state)
        Creator(state, vm)
        PostStatus(state, vm)

        SaveButton(
            text = state.strings.save,
            disabled = state.isSaveCommonDetailsButtonDisabled,
            onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.SaveCommonDetails) },
        )
    }
}

@Composable
private fun Data(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SectionHeader(
        text = state.strings.data,
    ) {
        if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
            EditCancelButton(
                isEditing = state.isDataEditing,
                editText = state.strings.edit,
                cancelText = state.strings.cancel,
                edit = { vm.trySend(AdminProductPageContract.Inputs.OnClick.EditData) },
                cancel = { vm.trySend(AdminProductPageContract.Inputs.OnClick.CancelEditData) },
            )
        }
    }
    Description(state, vm)
    IsPurchasable(vm, state)

    SaveButton(
        text = state.strings.save,
        disabled = state.isSaveDataButtonDisabled,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.SaveDataDetails) },
    )
}

@Composable
fun IsPurchasable(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .thenIf(state.isDataEditing) {
                Modifier
                    .onClick { vm.trySend(AdminProductPageContract.Inputs.Set.IsPurchasable(!state.isPurchasable)) }
                    .cursor(Cursor.Pointer)
            }
    ) {
        Switch(
            selected = state.isPurchasable,
            disabled = !state.isDataEditing,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.isPurchasable)
    }
}


@Composable
private fun Name(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    CommonTextfield(
        value = state.name,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Name(it)) },
        label = state.strings.name,
        errorMsg = state.nameError,
        shake = state.shakeName,
        isEditing = if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
            state.isCommonDetailsEditing
        } else {
            true
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Description(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    CommonTextfield(
        value = state.description ?: "",
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Description(it)) },
        label = state.strings.description,
        errorMsg = state.descriptionError,
        shake = state.shakeDescription,
        isEditing = state.isDataEditing,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ShortDescription(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    CommonTextfield(
        value = state.shortDescription,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.ShortDescription(it)) },
        label = state.strings.shortDescription,
        errorMsg = state.shortDescriptionError,
        shake = state.shakeShortDescription,
        isEditing = state.isCommonDetailsEditing,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun IsFeatured(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .thenIf(state.isCommonDetailsEditing) {
                Modifier
                    .cursor(Cursor.Pointer)
                    .onClick { vm.trySend(AdminProductPageContract.Inputs.Set.IsFeatured(!state.isFeatured)) }
            }
    ) {
        Switch(
            selected = state.isFeatured,
            disabled = !state.isCommonDetailsEditing,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.isFeatured)
    }
}

@Composable
private fun AllowReviews(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .onClick { vm.trySend(AdminProductPageContract.Inputs.Set.AllowReviews(!state.allowReviews)) }
            .cursor(Cursor.Pointer)
    ) {
        Switch(
            selected = state.allowReviews,
            disabled = !state.isCommonDetailsEditing,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.allowReviews)
    }
}

@Composable
private fun CatalogVisibility(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    Column(
        modifier = Modifier
            .gap(0.5.em)
            .margin(topBottom = 1.em)
    ) {
        SpanText(
            text = state.strings.catalogVisibility,
            modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
        )
        CatalogVisibility.entries
            .filter { it != CatalogVisibility.UNKNOWN__ }
            .forEach { catalogVisibility ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(left = 2.em)
                        .gap(1.em)
                        .thenIf(state.isCommonDetailsEditing) {
                            Modifier
                                .cursor(Cursor.Pointer)
                                .onClick {
                                    vm.trySend(
                                        AdminProductPageContract.Inputs.Set.VisibilityInCatalog(catalogVisibility)
                                    )
                                }
                        }
                ) {
                    Radio(
                        checked = state.catalogVisibility == catalogVisibility,
                        name = "role",
                        disabled = !state.isCommonDetailsEditing,
                        value = catalogVisibility.name,
                    )
                    SpanText(text = catalogVisibility.name.enumCapitalized())
                }
            }
    }
}

@Composable
private fun Categories(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    SpanText(
        text = state.strings.categories,
        modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
    )
    ChipSet {
        state.allCategories.forEach { category ->
            FilterChip(
                label = category.name,
                disabled = !state.isCommonDetailsEditing,
                selected = category.id in state.categories,
                onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Category(category.id.toString())) },
            )
        }
    }
}

@Composable
private fun Tags(state: AdminProductPageContract.State) {
    SpanText(
        text = state.strings.tags,
        modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
    )
    SpanText("TODO: Implement tags")
}

@Composable
private fun Creator(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.gap(1.em)
    ) {
        SpanText(text = "${state.strings.createdBy}:")
        OutlinedButton(
            onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Creator) },
        ) {
            SpanText(text = state.creator.name)
        }
    }
}

@Composable
private fun PostStatus(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    Column(
        modifier = Modifier
            .gap(0.5.em)
            .margin(topBottom = 1.em)
    ) {
        SpanText(
            text = state.strings.postStatus,
            modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
        )
        PostStatus.entries
            .filter { it != PostStatus.UNKNOWN__ }
            .forEach { postStatus ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(left = 2.em)
                        .gap(1.em)
                        .thenIf(state.isCommonDetailsEditing) {
                            Modifier
                                .cursor(Cursor.Pointer)
                                .onClick {
                                    vm.trySend(
                                        AdminProductPageContract.Inputs.Set.StatusOfPost(postStatus)
                                    )
                                }
                        }
                ) {
                    Radio(
                        checked = state.postStatus == postStatus,
                        name = "role",
                        disabled = !state.isCommonDetailsEditing,
                        value = postStatus.name,
                    )
                    SpanText(text = postStatus.name.enumCapitalized())
                }
            }
    }
}
