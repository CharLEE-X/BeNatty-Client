package web.pages.admin.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.GoToDestination
import com.copperleaf.ballast.navigation.routing.RouterContract.Inputs.ReplaceTopDestination
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.pathParameter
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
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
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAddAPhoto
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCloudUpload
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import data.GetProductByIdQuery
import data.type.BackorderStatus
import data.type.MediaType
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
import org.w3c.dom.url.URL
import org.w3c.files.File
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppFilledCard
import web.components.widgets.AppFilledTonalIconButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.ImagePicker
import web.components.widgets.ImagePreviewDialog
import web.components.widgets.SwitchSection
import web.components.widgets.TakeActionDialog
import web.compose.material3.component.Divider
import web.compose.material3.component.TextFieldType
import web.util.convertBase64ToFile
import web.util.convertImageToBase64

@Composable
fun AdminProductPagePage(
    productId: String?,
    router: RouterViewModel,
    onError: suspend (String) -> Unit,
    goBack: () -> Unit,
    goToAdminHome: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goBack = {
                println("DEBUG product page: $productId -> goBack")
                goBack()
            },
            goToCreateCategory = { router.trySend(GoToDestination(RouterScreen.AdminCategoryPageNew.route)) },
            goToCreateTag = {
                router.trySend(
                    GoToDestination(RouterScreen.AdminTagPageNew.route)
                )
            },
            goToUserDetails = { id ->
                router.trySend(
                    GoToDestination(
                        RouterScreen.AdminUserPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
            goToProductDetail = { id ->
                router.trySend(
                    ReplaceTopDestination(
                        RouterScreen.AdminProductPageExisting.directions()
                            .pathParameter("id", id)
                            .build()
                    )
                )
            },
        )
    }
    val state by vm.observeStates().collectAsState()

    var previewDialogImage: GetProductByIdQuery.Medium? by remember { mutableStateOf(null) }
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
//        showEditedButtons = state.wasEdited || state.screenState is AdminProductPageContract.ScreenState.New,
//        isSaveEnabled = state.wasEdited,
//        unsavedChangesText = state.strings.unsavedChanges,
//        saveText = state.strings.save,
//        discardText = state.strings.discard,
//        onCancel = { if (state.wasEdited) vm.trySend(AdminProductPageContract.Inputs.Discard) },
//        onSave = { vm.trySend(AdminProductPageContract.Inputs.Save) },
//        goToAdminHome = goToAdminHome,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = state.strings.unsavedChanges,
                saveText = state.strings.saveChanges,
                resetText = state.strings.dismiss,
                onSave = { vm.trySend(AdminProductPageContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminProductPageContract.Inputs.OnDiscardClick) },
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
            TakeActionDialog(
                open = deleteProductDialogOpen && !deleteProductDialogClosing,
                closing = deleteProductDialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.discard,
                contentText = state.strings.deleteExplain,
                onOpen = { deleteProductDialogOpen = it },
                onClosing = { deleteProductDialogClosing = it },
                onYes = { vm.trySend(AdminProductPageContract.Inputs.OnDeleteClick) },
                onNo = { deleteProductDialogClosing = true },
            )
            TakeActionDialog(
                open = deleteProductImageDialogOpen && !deleteProductImageDialogClosing,
                closing = deleteProductImageDialogClosing,
                title = state.strings.delete,
                actionYesText = state.strings.delete,
                actionNoText = state.strings.discard,
                contentText = state.strings.deleteExplain,
                onOpen = { deleteProductImageDialogOpen = it },
                onClosing = { deleteProductImageDialogClosing = it },
                onYes = {
                    deleteProductImageDialogImageId?.let {
                        vm.trySend(AdminProductPageContract.Inputs.OnDeleteMediaClick(it))
                    }
                },
                onNo = { deleteProductImageDialogClosing = true },
            )
        },
    ) {
        OneThirdLayout(
            title = if (state.screenState is AdminProductPageContract.ScreenState.New) {
                state.strings.createProduct
            } else {
                state.original.title
            },
            onGoBack = { vm.trySend(AdminProductPageContract.Inputs.OnBackButtonClick) },
            hasBackButton = true,
            actions = {
                if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
                    AppFilledButton(
                        onClick = { deleteProductDialogOpen = !deleteProductDialogOpen },
                        leadingIcon = { MdiDelete() },
                        containerColor = MaterialTheme.colors.mdSysColorError.value(),
                    ) {
                        SpanText(text = state.strings.delete)
                    }
                }
            },
            content = {
                CardSection(title = null) {
                    Title(state, vm)
                    Description(state, vm)
                }
                CardSection(title = state.strings.media) {
                    Media(
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
                CardSection(title = state.strings.price) {
                    Price(vm, state)
                    RegularPrice(vm, state)
                    ChargeTax(vm, state)
                }
                CardSection(title = state.strings.inventory) {
                    TrackQuantity(vm, state)
                    UseGlobalTracking(vm, state)
                    RemainingStock(vm, state)
                    LowStockThreshold(vm, state)
                    BackorderStatus(vm, state)
                    StatusOfStock(vm, state)
                }
                CardSection(title = state.strings.shipping) {
                    ShippingPreset(vm, state)
                    Weight(vm, state)
                    Length(vm, state)
                    Width(vm, state)
                    Height(vm, state)
                }
            },
            contentThird = {
                CardSection(title = state.strings.status) {
//                    PostStatus(vm, state)
                }
                CardSection(title = null) {
                    IsFeatured(vm, state)
                    AllowReviews(vm, state)
                }
                CardSection(title = state.strings.insights) {
                    SpanText(text = state.strings.noInsights)
                }
                CardSection(title = state.strings.productOrganization) {
                    CategoryId(vm, state)
                    Divider()
                    Tags(vm, state)
                }
                if (state.screenState is AdminProductPageContract.ScreenState.Existing) {
                    CardSection(title = state.strings.info) {
                        Creator(vm, state)
                        CreatedAt(state)
                        UpdatedAt(state)
                    }
                }
            }
        )
    }
}

@Composable
fun UpdatedAt(state: AdminProductPageContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${state.strings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
fun CreatedAt(state: AdminProductPageContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${state.strings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = state.strings.shippingPreset)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let {
            listOf(state.allCategories.first { it.id == it }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnPresetSelected(it)) },
        noChipsText = state.strings.noCategories,
        createText = state.strings.createCategory,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(state.strings.shippingPresetDesc) },
    )
    Box(Modifier.size(0.5.em))
}

@Composable
private fun Height(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Height(it)) },
        label = state.strings.height,
        errorText = state.heightError,
        shake = state.heightShake,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.heightDesc)
}

@Composable
private fun Width(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Width(it)) },
        label = state.strings.width,
        errorText = state.widthError,
        shake = state.widthShake,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.widthDesc)
}

@Composable
private fun Length(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Length(it)) },
        label = state.strings.length,
        errorText = state.lengthError,
        shake = state.lengthShake,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.lengthDesc)
}

@Composable
private fun Weight(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Weight(it)) },
        label = state.strings.weight,
        errorText = state.weightError,
        shake = state.weightShake,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.weightDesc)
}

@Composable
private fun RegularPrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.regularPrice.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.RegularPrice(it)) },
        label = state.strings.regularPrice,
        errorText = state.regularPriceError,
        shake = state.regularPriceShake,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.regularPriceDesc)
}

@Composable
private fun Price(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.price.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Price(it)) },
        label = state.strings.price,
        errorText = state.priceError,
        shake = state.priceShake,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.priceDesc)
}

@Composable
private fun Media(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State,
    onImageCLick: (GetProductByIdQuery.Medium) -> Unit,
    onImageDeleteClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var convertedMedia by remember { mutableStateOf(emptyList<GetProductByIdQuery.Medium>()) }

    LaunchedEffect(state.localMedia) {
        convertedMedia = state.localMedia.mapIndexed { index, medium ->
            val file = convertBase64ToFile(medium.url, index.toString())
            val url = URL.createObjectURL(file)
            GetProductByIdQuery.Medium(
                id = medium.id,
                altText = medium.altText,
                url = url,
                mediaType = MediaType.Image,
                createdAt = "",
                updatedAt = "",
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .flexWrap(FlexWrap.Wrap)
            .gap(1.em)
    ) {
        val media = if (state.screenState is AdminProductPageContract.ScreenState.New) {
            state.localMedia
        } else {
            state.current.media
        }
        media.forEach { image ->
            MediaSlot(
                url = image.url,
                alt = image.altText,
                errorText = null,
                onFileDropped = {},
                onImageClick = { onImageCLick(image) },
                onDeleteClick = { onImageDeleteClick(image.id.toString()) },
            )
        }
        MediaSlot(
            url = null,
            alt = null,
            isImagesLoading = state.isImagesLoading,
            errorText = state.imageDropError,
            onFileDropped = { file ->
                scope.launch {
                    convertImageToBase64(file)?.let { imageString ->
                        vm.trySend(AdminProductPageContract.Inputs.AddMedia(imageString))
                    } ?: vm.trySend(AdminProductPageContract.Inputs.Set.ImageDropError(error = "Not a PNG?"))
                }
            },
            onImageClick = {},
            onDeleteClick = {},
        )
    }
}

@Composable
private fun MediaSlot(
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

    AppFilledCard(
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
                AppFilledTonalIconButton(
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
private fun TrackQuantity(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.trackQuantity,
        selected = state.current.inventory.trackQuantity,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.TrackQuantity(!state.current.inventory.trackQuantity)) },
    )
    AppTooltip(state.strings.trackQuantityDesc)
}

@Composable
private fun UseGlobalTracking(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.useGlobalTracking,
        selected = state.current.inventory.useGlobalTracking,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.UseGlobalTracking(!state.current.inventory.useGlobalTracking)) },
    )
    AppTooltip(state.strings.useGlobalTrackingDesc)
}

@Composable
private fun ChargeTax(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.chargeTax,
        selected = state.current.pricing.chargeTax,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.ChargeTax(!state.current.pricing.chargeTax)) },
    )
    AppTooltip(state.strings.chargeTaxDesc)
}

@Composable
private fun StatusOfStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = state.strings.stockStatus)
    FilterChipSection(
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
    AppOutlinedTextField(
        value = state.current.inventory.lowStockThreshold.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductPageContract.Inputs.Set.LowStockThreshold(
                    it.toIntOrNull() ?: 0
                )
            )
        },
        label = state.strings.lowStockThreshold,
        errorText = state.lowStockThresholdError,
        shake = state.lowStockThresholdShake,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.lowStockThresholdDesc)
}

@Composable
private fun RemainingStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.inventory.remainingStock.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductPageContract.Inputs.Set.RemainingStock(
                    it.toIntOrNull() ?: 0
                )
            )
        },
        label = state.strings.remainingStock,
        errorText = state.remainingStockError,
        shake = state.remainingStockShake,
        type = TextFieldType.NUMBER,
        required = true,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(state.strings.remainingStockDesc)
}

@Composable
private fun BackorderStatus(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = state.strings.backorderStatus)
    FilterChipSection(
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
private fun Title(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.title,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Title(it)) },
        label = state.strings.title,
        errorText = state.titleError,
        shake = state.titleShake,
        required = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Description(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.description,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.Set.Description(it)) },
        label = state.strings.description,
        errorText = state.descriptionError,
        shake = state.descriptionShake,
        type = TextFieldType.TEXTAREA,
        rows = 5,
        modifier = Modifier
            .fillMaxWidth()
            .resize(Resize.Vertical)
    )
    AppTooltip(state.strings.descriptionDesc)
}

@Composable
private fun IsFeatured(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = state.strings.isFeatured,
        selected = state.current.isFeatured,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.IsFeatured(!state.current.isFeatured)) },
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
        selected = state.current.allowReviews,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.Set.AllowReviews(!state.current.allowReviews)) },
    )
    AppTooltip(state.strings.allowReviewsDesc)
}

@Composable
private fun CategoryId(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = state.strings.categories)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let {
            listOf(state.allCategories.first { it.id == it }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnCategorySelected(it)) },
        noChipsText = state.strings.noCategories,
        createText = state.strings.createCategory,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(state.strings.categoriesDesc) },
    )
}

@Composable
private fun Tags(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = state.strings.tags)
    FilterChipSection(
        chips = state.allTags.map { it.name },
        selectedChips = state.current.tags
            .map { it.toString() }
            .mapNotNull { id -> state.allTags.firstOrNull { id == it.id.toString() }?.name },
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnTagSelected(it)) },
        canBeEmpty = true,
        noChipsText = state.strings.noTags,
        createText = state.strings.createTag,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateTagClick) },
        afterTitle = { AppTooltip(state.strings.tagsDesc) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Creator(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CreatorSection(
        title = state.strings.createdBy,
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreatorClick) },
        afterTitle = { AppTooltip(state.strings.createdByDesc) },
    )
}

@Composable
private fun PostStatus(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    FilterChipSection(
        chips = PostStatus.entries
            .filter { it != PostStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.postStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.Set.StatusOfPost(PostStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(state.strings.postStatusDesc) },
    )
}
