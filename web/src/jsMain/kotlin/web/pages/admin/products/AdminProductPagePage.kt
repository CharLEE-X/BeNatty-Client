package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAddAPhoto
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCloudUpload
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import data.ProductGetByIdQuery
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.StockStatus
import feature.admin.product.page.AdminProductPageContract
import feature.admin.product.page.AdminProductPageViewModel
import feature.router.RouterScreen
import feature.router.RouterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.value
import org.w3c.files.File
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.DetailPageLayout
import web.components.layouts.ImproveWithAiRow
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CommonTextField
import web.components.widgets.CreatorSection
import web.components.widgets.DeleteDialog
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.ImagePicker
import web.components.widgets.ImagePreviewDialog
import web.components.widgets.SaveButton
import web.components.widgets.SwitchSection
import web.compose.material3.component.Divider
import web.compose.material3.component.TextFieldType
import web.compose.material3.component.TonalIconButton
import web.compose.material3.component.labs.FilledCard
import web.util.convertImageToBase64
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

    var previewDialogImage: ProductGetByIdQuery.Image? by remember { mutableStateOf(null) }
    var previewDialogOpen by remember { mutableStateOf(false) }

    var deleteProductDialogOpen by remember { mutableStateOf(false) }
    var deleteProductDialogClosing by remember { mutableStateOf(false) }

    var deleteProductImageDialogOpen by remember { mutableStateOf(false) }
    var deleteProductImageDialogClosing by remember { mutableStateOf(false) }
    var deleteProductImageDialogImageId: String? by remember { mutableStateOf(null) }

    AdminLayout(
        title = state.strings.createProduct,
        router = router,
        isLoading = state.isLoading,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.saveChanges,
                resetText = state.strings.reset,
                onSave = { vm.trySend(AdminProductPageContract.Inputs.OnClick.SaveEdit) },
                onCancel = { vm.trySend(AdminProductPageContract.Inputs.OnClick.CancelEdit) },
            )
            if (previewDialogOpen) {
                previewDialogImage?.let { image ->
                    ImagePreviewDialog(
                        open = previewDialogOpen,
                        imageUrl = image.url,
                        alt = image.altText,
                        onClose = { previewDialogOpen = false }
                    )
                }
            }
            DeleteDialog(
                open = deleteProductDialogOpen && !deleteProductDialogClosing,
                closing = deleteProductDialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.cancel,
                contentText = state.strings.deleteExplain,
                onOpen = { deleteProductDialogOpen = it },
                onClosing = { deleteProductDialogClosing = it },
                onYes = { vm.trySend(AdminProductPageContract.Inputs.OnClick.Delete) },
                onNo = { deleteProductDialogClosing = true },
            )
            DeleteDialog(
                open = deleteProductImageDialogOpen && !deleteProductImageDialogClosing,
                closing = deleteProductImageDialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.cancel,
                contentText = state.strings.deleteExplain,
                onOpen = { deleteProductImageDialogOpen = it },
                onClosing = { deleteProductImageDialogClosing = it },
                onYes = {
                    deleteProductImageDialogImageId?.let {
                        vm.trySend(AdminProductPageContract.Inputs.OnClick.DeleteImage(it))
                    }
                },
                onNo = { deleteProductImageDialogClosing = true },
            )
        },
    ) {
        DetailPageLayout(
            title = if (state.screenState is AdminProductPageContract.ScreenState.New) {
                state.strings.createProduct
            } else {
                state.strings.product
            },
            id = state.current.id.toString(),
            name = state.current.common.name.ifEmpty { null },
            showDelete = state.screenState is AdminProductPageContract.ScreenState.Existing,
            deleteText = state.strings.delete,
            createdAtText = state.strings.createdAt,
            updatedAtText = state.strings.lastUpdatedAt,
            createdAtValue = state.current.common.createdAt,
            updatedAtValue = state.current.common.updatedAt,
            onDeleteClick = { deleteProductDialogOpen = !deleteProductDialogOpen },
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
                    Description(state, vm)
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
                    IsPurchasable(vm, state)
                    Images(
                        vm = vm,
                        state = state,
                        onImageCLick = {
                            previewDialogImage = it
                            previewDialogOpen = true
                        },
                        onImageDeleteClick = {
                            deleteProductImageDialogImageId = it
                            deleteProductImageDialogOpen = !deleteProductImageDialogOpen
                        },
                    )
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
    val categories = state.current.common.categories
        .map { it.toString() }
        .mapNotNull { state.allCategories.firstOrNull { it.id.toString() == it.id.toString() }?.name }
    FilterChipSection(
        title = state.strings.shippingPreset,
        chips = categories,
        selectedChips = state.current.common.categories
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
        value = state.current.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Height(it)) },
        label = state.strings.height,
        errorMsg = state.heightError,
        shake = state.shakeHeight,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.heightDesc)
}

@Composable
private fun Width(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Width(it)) },
        label = state.strings.width,
        errorMsg = state.widthError,
        shake = state.shakeWidth,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.widthDesc)
}

@Composable
private fun Length(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Length(it)) },
        label = state.strings.length,
        errorMsg = state.lengthError,
        shake = state.shakeLength,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.lengthDesc)
}

@Composable
private fun Weight(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Weight(it)) },
        label = state.strings.weight,
        errorMsg = state.weightError,
        shake = state.shakeWeight,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.weightDesc)
}

@Composable
private fun SaleStart(state: AdminProductPageContract.State) {
    SpanText(
        text = "SaleStart: ${state.current.price.saleStart} TODO: Implement date picker",
    )
    AppTooltip(state.strings.saleStartDesc)
}

@Composable
private fun SaleEnd(state: AdminProductPageContract.State) {
    SpanText(
        text = "SaleEnd: ${state.current.price.saleEnd} TODO: Implement date picker",
    )
    AppTooltip(state.strings.saleEndDesc)
}

@Composable
private fun OnSale(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.onSale,
        selected = state.current.price.onSale,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.OnSale(!state.current.price.onSale)) },
    )
    AppTooltip(state.strings.onSaleDesc)
}

@Composable
private fun SalePrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.price.salePrice.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.SalePrice(it)) },
        label = state.strings.salePrice,
        errorMsg = state.salePriceError,
        shake = state.shakeSalePrice,
        type = TextFieldType.NUMBER,
        required = state.current.price.onSale,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.salePriceDesc)
}

@Composable
private fun RegularPrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CommonTextField(
        value = state.current.price.regularPrice.toString(),
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
        value = state.current.price.price.toString(),
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
private fun Images(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State,
    onImageCLick: (ProductGetByIdQuery.Image) -> Unit,
    onImageDeleteClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    SpanText(text = state.strings.images)
    AppTooltip(state.strings.imagesDesc)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .flexWrap(FlexWrap.Wrap)
            .gap(1.em)
    ) {
        state.current.data.images.forEachIndexed { index, image ->
            ImageSlot(
                url = image.url,
                alt = image.altText,
                errorText = null,
                onFileDropped = {},
                onImageClick = { onImageCLick(image) },
                onDeleteClick = { onImageDeleteClick(image.id.toString()) },
            )
        }
        ImageSlot(
            url = null,
            alt = null,
            isImagesLoading = state.isImagesLoading,
            errorText = state.imageDropError,
            onFileDropped = { file ->
                scope.launch {
                    convertImageToBase64(file)?.let { imageString ->
                        vm.trySend(AdminProductPageContract.Inputs.UploadImage(imageString))
                    } ?: vm.trySend(AdminProductPageContract.Inputs.Set.ImageDropError(error = "Not a PNG?"))
                }
            },
            onImageClick = {},
            onDeleteClick = {},
        )
    }
}

@Composable
private fun ImageSlot(
    url: String?,
    alt: String?,
    errorText: String?,
    cornerRadius: CSSLengthOrPercentageNumericValue = 14.px,
    isImagesLoading: Boolean = false,
    onFileDropped: (File) -> Unit,
    onImageClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    var imageHovered by remember { mutableStateOf(false) }
    var addIconHovered by remember { mutableStateOf(false) }
    var deleteIconHovered by remember { mutableStateOf(false) }

    val size = 200.px

    FilledCard(
        modifier = Modifier
            .size(size)
            .borderRadius(cornerRadius)
            .overflow(Overflow.Hidden)
            .onMouseOver { imageHovered = true }
            .onMouseOut { imageHovered = false }
    ) {
        url?.let {
            Box {
                Image(
                    src = url,
                    alt = alt ?: "",
                    modifier = Modifier
                        .size(size)
                        .borderRadius(cornerRadius)
                        .onClick { onImageClick() }
                        .scale(if (imageHovered) 1.05 else 1.0)
                        .onMouseOver { imageHovered = true }
                        .onMouseOut { imageHovered = false }
                        .objectFit(ObjectFit.Cover)
                        .transition(CSSTransition("scale", 0.5.s, TransitionTimingFunction.Ease))
                )
                Box(
                    modifier = Modifier
                        .size(size)
                        .borderRadius(cornerRadius)
                        .objectFit(ObjectFit.Cover)
                        .backgroundColor(
//                            if (imageHovered || deleteIconHovered) {
                            MaterialTheme.colors.mdSysColorOnSurface.value()
//                            } else Colors.Transparent
                        )
                        .transition(CSSTransition("backgroundColor", 0.3.s, TransitionTimingFunction.Ease))
                ) {}
                TonalIconButton(
                    onClick = { onDeleteClick() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .margin(0.5.em)
                        .onMouseOver { deleteIconHovered = true }
                        .onMouseOut { deleteIconHovered = false }
                        .opacity(if (imageHovered || deleteIconHovered) 1.0 else 0.0)
                        .scale(if (deleteIconHovered) 1.05 else 1.0)
                        .transition(
                            CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                        )
                ) {
                    MdiDelete()
                }
            }
        } ?: Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .borderRadius(cornerRadius)
                .border(
                    width = 2.px,
                    color = MaterialTheme.colors.mdSysColorInverseSurface.value(),
                    style = LineStyle.Dashed,
                )
                .size(size)
                .backgroundColor(
                    if (imageHovered || addIconHovered) {
                        MaterialTheme.colors.mdSysColorSurfaceContainer.value()
                    } else MaterialTheme.colors.mdSysColorSurface.value()
                )
                .transition(CSSTransition("background-color", 0.3.s, TransitionTimingFunction.Ease))
        ) {
            if (isImagesLoading) {
                var opacity by remember { mutableStateOf(1.0) }
                LaunchedEffect(isImagesLoading) {
                    while (isImagesLoading) {
                        opacity = 0.5
                        delay(600)
                        opacity = 1.0
                        delay(600)
                    }
                }
                MdiCloudUpload(
                    modifier = Modifier
                        .opacity(opacity)
                        .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
                )
            } else {
                ImagePicker(
                    onFileSelected = { onFileDropped(it) },
                    modifier = Modifier
                        .size(size)
                        .onMouseOver { imageHovered = true }
                        .onMouseOut { imageHovered = false }
                )
                MdiAddAPhoto(
                    modifier = Modifier
                        .onMouseEnter { addIconHovered = true }
                        .onMouseLeave { addIconHovered = false }
                        .opacity(if (imageHovered || addIconHovered) 1.0 else 0.5)
                        .scale(if (imageHovered || addIconHovered) 1.05 else 1.0)
                        .transition(
                            CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease),
                            CSSTransition("scale", 0.3.s, TransitionTimingFunction.Ease),
                        )
                )
                errorText?.let { errorText ->
                    SpanText(
                        text = errorText,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .roleStyle(MaterialTheme.typography.labelSmall)
                            .color(MaterialTheme.colors.mdSysColorError.value())
                            .margin(0.5.em)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrackInventory(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.trackInventory,
        selected = state.current.inventory.trackInventory,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.TrackInventory(!state.current.inventory.trackInventory)) },
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
        selectedChips = listOf(state.current.inventory.stockStatus.name),
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
        value = state.current.inventory.lowStockThreshold.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductPageContract.Inputs.Set.LowStockThreshold(
                    it.toIntOrNull() ?: 0
                )
            )
        },
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
        value = state.current.inventory.remainingStock.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductPageContract.Inputs.Set.RemainingStock(
                    it.toIntOrNull() ?: 0
                )
            )
        },
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
        selected = state.current.inventory.isOnBackorder,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsOnBackorder(!state.current.inventory.isOnBackorder)) },
    )
    AppTooltip(state.strings.isOnBackorderDesc)
}

@Composable
private fun CanBackorder(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.canBackorder,
        selected = state.current.inventory.canBackorder,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsOnBackorder(!state.current.inventory.canBackorder)) },
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
        selectedChips = listOf(state.current.inventory.backorderStatus.name),
        onChipClick = {
            vm.trySend(
                AdminProductPageContract.Inputs.Set.StatusOfBackorder(
                    BackorderStatus.valueOf(
                        it
                    )
                )
            )
        },
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
        selected = state.current.data.isPurchasable,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsPurchasable(!state.current.data.isPurchasable)) },
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
            value = state.current.common.name,
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
            value = state.current.common.shortDescription ?: "",
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
            value = state.current.data.description ?: "",
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
        selected = state.current.common.isFeatured,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsFeatured(!state.current.common.isFeatured)) },
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
        selected = state.current.common.allowReviews,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.AllowReviews(!state.current.common.allowReviews)) },
    )
    AppTooltip(state.strings.allowReviewsDesc)
}

@Composable
private fun OnePerOrder(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.onePerOrder,
        selected = state.current.inventory.onePerOrder,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.OnePerOrder(!state.current.inventory.onePerOrder)) },
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
        selectedChips = listOf(state.current.common.catalogVisibility.name),
        onChipClick = {
            vm.trySend(
                AdminProductPageContract.Inputs.Set.VisibilityInCatalog(
                    CatalogVisibility.valueOf(
                        it
                    )
                )
            )
        },
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
        selectedChips = state.current.common.categories
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
            selectedChips = state.current.common.tags
                .map { it.toString() }
                .mapNotNull { id -> state.allTags.firstOrNull { id == it.id.toString() }?.name },
            onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.TagSelected(it)) },
            canBeEmpty = true,
            noChipsText = state.strings.noTags,
            createText = state.strings.createTag,
            onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnClick.GoToCreateTag) },
            afterTitle = { AppTooltip(state.strings.tagsDesc) },
            modifier = Modifier.weight(1f),
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
        selectedChips = listOf(state.current.data.postStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfPost(PostStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(state.strings.postStatusDesc) },
    )
}
