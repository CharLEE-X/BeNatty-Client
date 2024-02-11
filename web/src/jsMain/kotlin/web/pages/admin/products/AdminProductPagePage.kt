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
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.text.SpanText
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.StockStatus
import feature.admin.product.page.AdminProductPageContract
import feature.admin.product.page.AdminProductPageViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import org.jetbrains.compose.web.css.em
import web.components.layouts.AdminLayout
import web.components.layouts.DetailPageLayout
import web.components.layouts.ImproveWithAiRow
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CommonTextField
import web.components.widgets.CreatorSection
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.SaveButton
import web.components.widgets.SwitchSection
import web.compose.material3.component.Divider
import web.compose.material3.component.TextFieldType
import web.util.onEnterKeyDown

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
            goToProduct = { id ->
                router.trySend(
                    RouterContract.Inputs.GoToDestination(
                        RouterScreen.AdminProductPageExisting.directions()
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
            title = if (state.screenState is AdminProductPageContract.ScreenState.New) {
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
            updatedAtText = state.strings.lastUpdatedAt,
            createdAtValue = state.current.product.common.createdAt,
            updatedAtValue = state.current.product.common.updatedAt,
            onDeleteClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Delete) },
        ) {
            CardSection(title = state.strings.details) {
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
                    Divider()
                    CatalogVisibility(state, vm)
                    Divider()
                    Categories(state, vm)
                    Divider()
                    Tags(vm, state)
                    Divider()
                    Creator(state, vm)
                }
            }

            if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
                CardSection(title = state.strings.data) {
                    PostStatus(state, vm)
                    Divider()
                    Description(state, vm)
                    IsPurchasable(vm, state)
                    Images(state)
                }
                CardSection(title = state.strings.inventory) {
                    OnePerOrder(vm, state)
                    Divider()
                    BackorderStatus(vm, state)
                    Divider()
                    CanBackorder(vm, state)
                    IsOnBackorder(vm, state)
                    LowStockThreshold(vm, state)
                    RemainingStock(vm, state)
                    Divider()
                    StatusOfStock(vm, state)
                    Divider()
                    TrackInventory(vm, state)
                }
                CardSection(title = state.strings.price) {
                    Price(vm, state)
                    RegularPrice(vm, state)
                    OnSale(vm, state)
                    SalePrice(vm, state)
                    SaleStart(state)
                    SaleEnd(state)
                }
                CardSection(title = state.strings.shipping) {
                    ShippingPreset(vm, state)
                    Weight(vm, state)
                    Length(vm, state)
                    Width(vm, state)
                    Height(vm, state)
                }
            }
        }
    }
}

@Composable
private fun ShippingPreset(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    val categories = state.current.product.common.categories
        .map { it.toString() }
        .mapNotNull { state.allCategories.firstOrNull { it.id.toString() == it.id.toString() }?.name }
    FilterChipSection(
        title = state.strings.shippingPreset,
        chips = categories,
        selectedChips = state.current.product.common.categories
            .map { it.toString() }
            .mapNotNull { id -> state.allCategories.firstOrNull { id == it.id.toString() }?.name },
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.PresetSelected(it)) },
        noChipsText = state.strings.noOtherCategoriesToChooseFrom,
        createText = state.strings.createCategory,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToCreateCategory) },
        afterTitle = { AppTooltip(state.strings.shippingPresetDesc) },
    )
    Box(Modifier.size(0.5.em))
}

@Composable
private fun Height(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Height(it)) },
        label = state.strings.height,
        errorMsg = state.heightError,
        shake = state.shakeHeight,
        type = TextFieldType.NUMBER,
        disabled = state.current.product.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.heightDesc)
}

@Composable
private fun Width(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Width(it)) },
        label = state.strings.width,
        errorMsg = state.widthError,
        shake = state.shakeWidth,
        type = TextFieldType.NUMBER,
        disabled = state.current.product.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.widthDesc)
}

@Composable
private fun Length(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Length(it)) },
        label = state.strings.length,
        errorMsg = state.lengthError,
        shake = state.shakeLength,
        type = TextFieldType.NUMBER,
        disabled = state.current.product.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.lengthDesc)
}

@Composable
private fun Weight(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Weight(it)) },
        label = state.strings.weight,
        errorMsg = state.weightError,
        shake = state.shakeWeight,
        type = TextFieldType.NUMBER,
        disabled = state.current.product.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.weightDesc)
}

@Composable
private fun SaleStart(state: AdminProductPageContract.State) {
    SpanText(
        text = "SaleStart: ${state.current.product.price.saleStart} TODO: Implement date picker",
    )
    AppTooltip(state.strings.saleStartDesc)
}

@Composable
private fun SaleEnd(state: AdminProductPageContract.State) {
    SpanText(
        text = "SaleEnd: ${state.current.product.price.saleEnd} TODO: Implement date picker",
    )
    AppTooltip(state.strings.saleEndDesc)
}

@Composable
private fun OnSale(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.onSale,
        selected = state.current.product.price.onSale,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.OnSale(!state.current.product.price.onSale)) },
    )
    AppTooltip(state.strings.onSaleDesc)
}

@Composable
private fun SalePrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
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
    AppTooltip(state.strings.salePriceDesc)
}

@Composable
private fun RegularPrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.product.price.regularPrice.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.RegularPrice(it)) },
        label = state.strings.regularPrice,
        errorMsg = state.regularPriceError,
        shake = state.shakeRegularPrice,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.regularPriceDesc)
}

@Composable
private fun Price(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
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
    AppTooltip(state.strings.priceDesc)
}

@Composable
private fun Images(state: AdminProductPageContract.State) {
    SpanText(text = state.strings.images)
    AppTooltip(state.strings.imagesDesc)
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
private fun TrackInventory(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.trackInventory,
        selected = state.current.product.inventory.trackInventory,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.TrackInventory(!state.current.product.inventory.trackInventory)) },
    )
    AppTooltip(state.strings.trackInventoryDesc)
}

@Composable
private fun StatusOfStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    FilterChipSection(
        title = state.strings.stockStatus,
        chips = StockStatus.entries
            .filter { it != StockStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.product.inventory.stockStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfStock(StockStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(state.strings.stockStatusDesc) },
    )
}

@Composable
private fun LowStockThreshold(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
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
    AppTooltip(state.strings.lowStockThresholdDesc)
}

@Composable
private fun RemainingStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
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
    AppTooltip(state.strings.remainingStockDesc)
}

@Composable
private fun IsOnBackorder(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.isOnBackorder,
        selected = state.current.product.inventory.isOnBackorder,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsOnBackorder(!state.current.product.inventory.isOnBackorder)) },
    )
    AppTooltip(state.strings.isOnBackorderDesc)
}

@Composable
private fun CanBackorder(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.canBackorder,
        selected = state.current.product.inventory.canBackorder,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsOnBackorder(!state.current.product.inventory.canBackorder)) },
    )
    AppTooltip(state.strings.canBackorderDesc)
}

@Composable
private fun BackorderStatus(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    FilterChipSection(
        title = state.strings.backorderStatus,
        chips = BackorderStatus.entries
            .filter { it != BackorderStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.product.inventory.backorderStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfBackorder(BackorderStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(state.strings.backorderStatusDesc) },
    )
}

@Composable
private fun IsPurchasable(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.isPurchasable,
        selected = state.current.product.data.isPurchasable,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsPurchasable(!state.current.product.data.isPurchasable)) },
    )
    AppTooltip(state.strings.isPurchasableDesc)
}


@Composable
private fun Name(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    ImproveWithAiRow(
        tooltipText = state.strings.improveWithAi,
        onImproveClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.ImproveName) }
    ) {
        CommonTextField(
            value = state.current.product.common.name,
            onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Name(it)) },
            label = state.strings.name,
            errorMsg = state.nameError,
            shake = state.shakeName,
            required = true,
            modifier = Modifier
                .weight(1f)
                .thenIf(
                    state.screenState is AdminProductPageContract.ScreenState.New,
                    Modifier.onEnterKeyDown { vm.trySend(AdminProductPageContract.Inputs.OnClick.Create) }
                )
        )
    }
}

@Composable
private fun ShortDescription(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    ImproveWithAiRow(
        tooltipText = state.strings.improveWithAi,
        onImproveClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.ImproveShortDescription) }
    ) {
        CommonTextField(
            value = state.current.product.common.shortDescription ?: "",
            onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.ShortDescription(it)) },
            label = state.strings.productShortDescription,
            errorMsg = state.shortDescriptionError,
            shake = state.shakeShortDescription,
            modifier = Modifier.fillMaxWidth(),
        )
        AppTooltip(state.strings.productShortDescriptionDesc)
    }
}

@Composable
private fun Description(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {

    ImproveWithAiRow(
        tooltipText = state.strings.improveWithAi,
        onImproveClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.ImproveDescription) }
    ) {
        CommonTextField(
            value = state.current.product.data.description ?: "",
            onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Description(it)) },
            label = state.strings.description,
            errorMsg = state.descriptionError,
            shake = state.shakeDescription,
            modifier = Modifier.fillMaxWidth(),
        )
        AppTooltip(state.strings.descriptionDesc)
    }
}

@Composable
private fun IsFeatured(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.isFeatured,
        selected = state.current.product.common.isFeatured,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsFeatured(!state.current.product.common.isFeatured)) },
    )
    AppTooltip(state.strings.isFeaturedDesc)
}

@Composable
private fun AllowReviews(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State
) {
    SwitchSection(
        title = state.strings.allowReviews,
        selected = state.current.product.common.allowReviews,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.AllowReviews(!state.current.product.common.allowReviews)) },
    )
    AppTooltip(state.strings.allowReviewsDesc)
}

@Composable
private fun OnePerOrder(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.onePerOrder,
        selected = state.current.product.inventory.onePerOrder,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.OnePerOrder(!state.current.product.inventory.onePerOrder)) },
    )
    AppTooltip(state.strings.onePerOrderDesc)
}

@Composable
private fun CatalogVisibility(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    FilterChipSection(
        title = state.strings.catalogVisibility,
        chips = CatalogVisibility.entries
            .filter { it != CatalogVisibility.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.product.common.catalogVisibility.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.Set.VisibilityInCatalog(CatalogVisibility.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(state.strings.catalogVisibilityDesc) },
    )
}

@Composable
private fun Categories(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    FilterChipSection(
        title = state.strings.categories,
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.product.common.categories
            .map { it.toString() }
            .mapNotNull { id -> state.allCategories.firstOrNull { id == it.id.toString() }?.name },
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.CategorySelected(it)) },
        noChipsText = state.strings.noOtherCategoriesToChooseFrom,
        createText = state.strings.createCategory,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToCreateCategory) },
        afterTitle = { AppTooltip(state.strings.categoriesDesc) },
    )
}

@Composable
private fun Tags(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    ImproveWithAiRow(
        tooltipText = state.strings.improveWithAi,
        onImproveClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.ImproveTags) }
    ) {
        FilterChipSection(
            title = state.strings.tags,
            chips = state.allTags.map { it.name },
            selectedChips = state.current.product.common.tags
                .map { it.toString() }
                .mapNotNull { id -> state.allTags.firstOrNull { id == it.id.toString() }?.name },
            onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.TagSelected(it)) },
            canBeEmpty = true,
            noChipsText = state.strings.noTags,
            createText = state.strings.createTag,
            onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToCreateTag) },
            afterTitle = { AppTooltip(state.strings.tagsDesc) },
        )
    }
}

@Composable
private fun Creator(state: AdminProductPageContract.State, vm: AdminProductPageViewModel) {
    CreatorSection(
        title = state.strings.createdBy,
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToUserCreator) },
        afterTitle = { AppTooltip(state.strings.createdByDesc) },
    )
}

@Composable
private fun PostStatus(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    FilterChipSection(
        title = state.strings.postStatus,
        chips = PostStatus.entries
            .filter { it != PostStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.product.data.postStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfPost(PostStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(state.strings.postStatusDesc) },
    )
}
