package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
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
import com.varabyte.kobweb.silk.components.text.SpanText
import core.util.enumCapitalized
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.StockStatus
import feature.admin.product.page.AdminProductPageContract
import feature.admin.product.page.AdminProductPageViewModel
import feature.admin.tag.page.AdminTagPageContract
import feature.router.RouterScreen
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.em
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.DetailPageLayout
import web.components.widgets.CommonTextField
import web.components.widgets.HasChangesWidget
import web.components.widgets.SaveButton
import web.components.widgets.SectionHeader
import web.compose.material3.component.ChipSet
import web.compose.material3.component.FilterChip
import web.compose.material3.component.OutlinedButton
import web.compose.material3.component.Radio
import web.compose.material3.component.Switch
import web.compose.material3.component.TextFieldType

@Composable
fun AdminProductPagePage(
    productId: String?,
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goToProductList = {
                router.trySend(RouterContract.Inputs.GoBack())
            },
            goToCreateCategory = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.AdminCategoryPageNew.matcher.routeFormat)
                )
            },
            goToCreateTag = {
                router.trySend(
                    RouterContract.Inputs.GoToDestination(RouterScreen.AdminTagPageNew.matcher.routeFormat)
                )
            },
            goToUserDetails = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminUserPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    AdminLayout(
        title = state.strings.createProduct,
        router = router,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.saveChanges,
                resetText = state.strings.reset,
                onSave = { vm.trySend(AdminProductPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminProductPageContract.Inputs.OnClick.CancelEdit) },
            )
        }
    ) {
        DetailPageLayout(
            title = if (state.screenState is AdminTagPageContract.ScreenState.New) {
                state.strings.createProduct
            } else {
                state.strings.product
            },
            id = state.current.product.id.toString(),
            name = state.current.product.common.name.ifEmpty { null },
            showDelete = state.screenState is AdminProductPageContract.ScreenState.Existing,
            deleteText = state.strings.delete,
            cancelText = state.strings.cancel,
            createdAtText = state.strings.createdAt,
            updatedAtText = state.strings.updatedAt,
            createdAtValue = state.current.product.common.createdAt,
            updatedAtValue = state.current.product.common.updatedAt,
            onDeleteClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Delete) },
        ) {
            CommonDetails(vm, state)
            Data(vm, state)
            Inventory(vm, state)
            PriceSection(state, vm)
            ShippingSection(state, vm)
        }
    }
}

@Composable
fun ShippingSection(state: AdminProductPageContract.State, vm: AdminProductPageViewModel) {
    SectionHeader(
        text = state.strings.shipping,
    )

    Weight(vm, state)
    Length(vm, state)
    Width(vm, state)
    Height(vm, state)
}

@Composable
fun Height(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Height(it)) },
        label = state.strings.height,
        errorMsg = state.heightError,
        shake = state.shakeHeight,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun Width(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Width(it)) },
        label = state.strings.width,
        errorMsg = state.widthError,
        shake = state.shakeWidth,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun Length(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Length(it)) },
        label = state.strings.length,
        errorMsg = state.lengthError,
        shake = state.shakeLength,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun Weight(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Weight(it)) },
        label = state.strings.weight,
        errorMsg = state.weightError,
        shake = state.shakeWeight,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun PriceSection(state: AdminProductPageContract.State, vm: AdminProductPageViewModel) {
    SectionHeader(
        text = state.strings.price,
    )

    Price(vm, state)
    RegularPrice(vm, state)
    SalePrice(vm, state)
    OnSale(vm, state)
    SaleStart(vm, state)
    SaleEnd(vm, state)
}

@Composable
fun SaleStart(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(
        text = "SaleStart: ${state.current.product.price.saleStart} TODO: Implement date picker",
    )
}

@Composable
fun SaleEnd(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(
        text = "SaleEnd: ${state.current.product.price.saleEnd} TODO: Implement date picker",
    )
}

@Composable
fun OnSale(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .onClick {
                vm.trySend(AdminProductPageContract.Inputs.Set.OnSale(!state.current.product.price.onSale))
            }
            .cursor(Cursor.Pointer)
    ) {
        Switch(
            selected = state.current.product.price.onSale,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.onSale)
    }
}

@Composable
fun SalePrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.price.salePrice.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.SalePrice(it)) },
        label = state.strings.salePrice,
        errorMsg = state.salePriceError,
        shake = state.shakeSalePrice,
        type = TextFieldType.NUMBER,
        required = state.current.product.price.onSale,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun RegularPrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.price.regularPrice.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.RegularPrice(it)) },
        label = state.strings.regularPrice,
        errorMsg = state.regularPriceError,
        shake = state.shakeRegularPrice,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun Price(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.price.price.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Price(it)) },
        label = state.strings.price,
        errorMsg = state.priceError,
        shake = state.shakePrice,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun CommonDetails(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SectionHeader(
        text = state.strings.details,
    )
    Name(state, vm)
    if (state.screenState is AdminProductPageContract.ScreenState.New) {
        SaveButton(
            text = state.strings.create,
            disabled = state.isCreateDisabled,
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
    }
}

@Composable
private fun Data(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SectionHeader(
        text = state.strings.data,
    )
    PostStatus(state, vm)
    Description(state, vm)
    IsPurchasable(vm, state)
    Images(vm, state)
}

@Composable
private fun Images(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = state.strings.images)
    state.current.product.data.images.forEach { image ->
        SpanText(text = "image.url: ${image.id}")
        SpanText(text = "image.url: ${image.url}")
        SpanText(text = "image.name: ${image.name}")
        SpanText(text = "image.altText: ${image.altText}")
    }
    if (state.current.product.data.images.isEmpty()) {
        SpanText(text = state.strings.noImages)
    }
}

@Composable
private fun Inventory(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SectionHeader(
        text = state.strings.inventory,
    )
    OnePerOrder(vm, state)
    BackorderStatus(vm, state)
    CanBackorder(vm, state)
    LowStockThreshold(vm, state)
    RemainingStock(vm, state)
    StatusOfStock(vm, state)
    TrackInventory(vm, state)
}

@Composable
fun TrackInventory(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .onClick {
                vm.trySend(AdminProductPageContract.Inputs.Set.TrackInventory(!state.current.product.inventory.trackInventory))
            }
            .cursor(Cursor.Pointer)
    ) {
        Switch(
            selected = state.current.product.inventory.trackInventory,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.trackInventory)
    }
}

@Composable
private fun StatusOfStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    Column(
        modifier = Modifier
            .gap(0.5.em)
            .margin(topBottom = 1.em)
    ) {
        SpanText(
            text = state.strings.stockStatus,
            modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
        )
        StockStatus.entries
            .filter { it != StockStatus.UNKNOWN__ }
            .forEach { stockStatus ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(left = 1.em)
                        .gap(1.em)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfStock(stockStatus))
                        }
                ) {
                    Radio(
                        checked = state.current.product.inventory.stockStatus == stockStatus,
                        name = "role",
                        value = stockStatus.name,
                    )
                    SpanText(text = stockStatus.name.enumCapitalized())
                }
            }
    }
}

@Composable
fun LowStockThreshold(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.inventory.lowStockThreshold.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.LowStockThreshold(it.toIntOrNull() ?: 0)) },
        label = state.strings.lowStockThreshold,
        errorMsg = state.lowStockThresholdError,
        shake = state.shakeLowStockThreshold,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun RemainingStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.inventory.remainingStock.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.RemainingStock(it.toIntOrNull() ?: 0)) },
        label = state.strings.remainingStock,
        errorMsg = state.remainingStockError,
        shake = state.shakeRemainingStock,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun CanBackorder(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .cursor(Cursor.Pointer)
            .onClick {
                vm.trySend(AdminProductPageContract.Inputs.Set.CanBackorder(!state.current.product.inventory.canBackorder))
            }
    ) {
        Switch(
            selected = state.current.product.inventory.canBackorder,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.canBackorder)
    }
}

@Composable
private fun BackorderStatus(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(
        text = state.strings.backorderStatus,
        modifier = Modifier.roleStyle(MaterialTheme.typography.titleMedium)
    )
    BackorderStatus.entries
        .filter { it != BackorderStatus.UNKNOWN__ }
        .forEach { backorderStatus ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(left = 1.em)
                    .gap(1.em)
                    .cursor(Cursor.Pointer)
                    .onClick { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfBackorder(backorderStatus)) }
            ) {
                Radio(
                    checked = state.current.product.inventory.backorderStatus == backorderStatus,
                    name = "role",
                    value = backorderStatus.name,
                )
                SpanText(text = backorderStatus.name.enumCapitalized())
            }
        }
}

@Composable
fun IsPurchasable(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .onClick {
                vm.trySend(AdminProductPageContract.Inputs.Set.IsPurchasable(!state.current.product.data.isPurchasable))
            }
            .cursor(Cursor.Pointer)
    ) {
        Switch(
            selected = state.current.product.data.isPurchasable,
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
    CommonTextField(
        value = state.current.product.common.name,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Name(it)) },
        label = state.strings.name,
        errorMsg = state.nameError,
        shake = state.shakeName,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun Description(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    CommonTextField(
        value = state.current.product.data.description ?: "",
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Description(it)) },
        label = state.strings.description,
        errorMsg = state.descriptionError,
        shake = state.shakeDescription,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ShortDescription(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    CommonTextField(
        value = state.current.product.common.shortDescription ?: "",
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.ShortDescription(it)) },
        label = state.strings.shortDescription,
        errorMsg = state.shortDescriptionError,
        shake = state.shakeShortDescription,
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
            .cursor(Cursor.Pointer)
            .onClick {
                vm.trySend(AdminProductPageContract.Inputs.Set.IsFeatured(!state.current.product.common.isFeatured))
            }
    ) {
        Switch(
            selected = state.current.product.common.isFeatured,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.isFeatured)
    }
}

@Composable
private fun OnePerOrder(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .gap(1.em)
            .cursor(Cursor.Pointer)
            .onClick {
                vm.trySend(AdminProductPageContract.Inputs.Set.OnePerOrder(!state.current.product.inventory.onePerOrder))
            }
    ) {
        Switch(
            selected = state.current.product.inventory.onePerOrder,
            modifier = Modifier.gap(1.em),
        )
        SpanText(text = state.strings.onePerOrder)
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
            .onClick {
                vm.trySend(
                    AdminProductPageContract.Inputs.Set.AllowReviews(
                        !state.current.product.common.allowReviews
                    )
                )
            }
            .cursor(Cursor.Pointer)
    ) {
        Switch(
            selected = state.current.product.common.allowReviews,
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
                        .padding(left = 1.em)
                        .gap(1.em)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            vm.trySend(AdminProductPageContract.Inputs.Set.VisibilityInCatalog(catalogVisibility))
                        }
                ) {
                    Radio(
                        checked = state.current.product.common.catalogVisibility == catalogVisibility,
                        name = "role",
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
                selected = category.id in state.current.product.common.categories,
                onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToCategory(category.id.toString())) },
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
            onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToCreator) },
        ) {
            SpanText(text = state.current.creator.name)
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
                        .padding(left = 1.em)
                        .gap(1.em)
                        .cursor(Cursor.Pointer)
                        .onClick { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfPost(postStatus)) }
                ) {
                    Radio(
                        checked = state.current.product.data.postStatus == postStatus,
                        name = "role",
                        value = postStatus.name,
                    )
                    SpanText(text = postStatus.name.enumCapitalized())
                }
            }
    }
}
