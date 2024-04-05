package web.pages.admin.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.text.SpanText
import data.AdminProductGetByIdQuery
import data.type.BackorderStatus
import data.type.MediaType
import data.type.PostStatus
import data.type.StockStatus
import feature.admin.customer.edit.adminCustomerEditStrings
import feature.admin.product.edit.AdminProductEditContract
import feature.admin.product.edit.AdminProductEditViewModel
import feature.admin.product.edit.adminProductPageStrings
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.w3c.dom.url.URL
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneThirdLayout
import web.components.widgets.AppFilledButton
import web.components.widgets.AppOutlinedTextField
import web.components.widgets.AppTooltip
import web.components.widgets.CardSection
import web.components.widgets.CreatorSection
import web.components.widgets.FilterChipSection
import web.components.widgets.HasChangesWidget
import web.components.widgets.ImagePreviewDialog
import web.components.widgets.MediaSlot
import web.components.widgets.SwitchSection
import web.components.widgets.TakeActionDialog
import web.compose.material3.component.Divider
import web.compose.material3.component.TextButton
import web.compose.material3.component.TextFieldType
import web.util.convertBase64ToFile
import web.util.convertImageToBase64

@Composable
fun AdminProductEditContent(
    productId: String,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCreateCategory: () -> Unit,
    goToCreateTag: () -> Unit,
    goToCustomer: (String) -> Unit,
    goToProduct: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductEditViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goBack = adminRoutes.goBack,
            goToCreateCategory = goToCreateCategory,
            goToCreateTag = goToCreateTag,
            goToCustomer = goToCustomer,
            goToProduct = goToProduct,
        )
    }
    val state by vm.observeStates().collectAsState()

    var previewDialogImage: AdminProductGetByIdQuery.Medium? by remember { mutableStateOf(null) }
    var previewDialogOpen by remember { mutableStateOf(false) }

    var deleteProductDialogOpen by remember { mutableStateOf(false) }
    var deleteProductDialogClosing by remember { mutableStateOf(false) }

    var deleteProductImageDialogOpen by remember { mutableStateOf(false) }
    var deleteProductImageDialogClosing by remember { mutableStateOf(false) }
    var deleteProductImageDialogImageId: String? by remember { mutableStateOf(null) }

    AdminLayout(
        title = adminProductPageStrings.products,
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = adminProductPageStrings.unsavedChanges,
        saveText = adminProductPageStrings.save,
        discardText = adminProductPageStrings.discard,
        onCancel = { if (state.wasEdited) vm.trySend(AdminProductEditContract.Inputs.OnDiscardClick) },
        onSave = { vm.trySend(AdminProductEditContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminProductPageStrings.unsavedChanges,
                saveText = adminProductPageStrings.saveChanges,
                resetText = adminProductPageStrings.dismiss,
                onSave = { vm.trySend(AdminProductEditContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminProductEditContract.Inputs.OnDiscardClick) },
            )
            if (previewDialogOpen) {
                previewDialogImage?.let { image ->
                    ImagePreviewDialog(
                        open = previewDialogOpen,
                        imageUrl = image.url,
                        alt = image.alt,
                        onClose = { previewDialogOpen = false }
                    )
                }
            }
            TakeActionDialog(
                open = deleteProductDialogOpen && !deleteProductDialogClosing,
                closing = deleteProductDialogClosing,
                title = "${adminCustomerEditStrings.delete} ${state.original.name}",
                actionYesText = adminProductPageStrings.delete,
                actionNoText = adminProductPageStrings.discard,
                contentText = adminProductPageStrings.deleteExplain,
                onOpen = { deleteProductDialogOpen = it },
                onClosing = { deleteProductDialogClosing = it },
                onYes = { vm.trySend(AdminProductEditContract.Inputs.OnDeleteClick) },
                onNo = { deleteProductDialogClosing = true },
            )
            TakeActionDialog(
                open = deleteProductImageDialogOpen && !deleteProductImageDialogClosing,
                closing = deleteProductImageDialogClosing,
                title = adminProductPageStrings.delete,
                actionYesText = adminProductPageStrings.delete,
                actionNoText = adminProductPageStrings.discard,
                contentText = adminProductPageStrings.deleteExplain,
                onOpen = { deleteProductImageDialogOpen = it },
                onClosing = { deleteProductImageDialogClosing = it },
                onYes = {
                    deleteProductImageDialogImageId?.let {
                        vm.trySend(AdminProductEditContract.Inputs.OnDeleteImageClick(it))
                    }
                },
                onNo = { deleteProductImageDialogClosing = true },
            )
        },
    ) {
        OneThirdLayout(
            title = state.original.name,
            subtitle = state.original.id,
            onGoBack = adminRoutes.goBack,
            hasBackButton = true,
            actions = {
                AppFilledButton(
                    onClick = { deleteProductDialogOpen = !deleteProductDialogOpen },
                    leadingIcon = { MdiDelete() },
                    containerColor = MaterialTheme.colors.error,
                ) {
                    SpanText(text = adminProductPageStrings.delete)
                }
            },
            content = {
                CardSection(title = null) {
                    Name(state, vm)
                    Description(state, vm)
                }
                CardSection(title = adminProductPageStrings.media) {
                    Media(
                        vm = vm,
                        state = state,
                        onImageClick = {
                            previewDialogImage = it
                            previewDialogOpen = true
                        },
                        onImageDeleteClick = {
                            deleteProductImageDialogImageId = it
                            deleteProductImageDialogOpen = !deleteProductImageDialogOpen
                        },
                    )
                }
                CardSection(title = adminProductPageStrings.price) {
                    Price(vm, state)
                    RegularPrice(vm, state)
                    ChargeTax(vm, state)
                }
                CardSection(title = adminProductPageStrings.variants) {
                    ManageVariantsSection(vm, state)
                }
                CardSection(title = adminProductPageStrings.inventory) {
                    TrackQuantity(vm, state)
                    UseGlobalTracking(vm, state)
                    RemainingStock(vm, state)
                    LowStockThreshold(vm, state)
                    BackorderStatus(vm, state)
                    StatusOfStock(vm, state)
                }
                CardSection(title = adminProductPageStrings.shipping) {
                    ShippingPreset(vm, state)
                    Weight(vm, state)
                    Length(vm, state)
                    Width(vm, state)
                    Height(vm, state)
                }
            },
            contentThird = {
                CardSection(title = adminProductPageStrings.status) {
                    PostStatus(vm, state)
                }
                CardSection(title = null) {
                    IsFeatured(vm, state)
                    AllowReviews(vm, state)
                }
                CardSection(title = adminProductPageStrings.insights) {
                    SpanText(text = adminProductPageStrings.noInsights)
                }
                CardSection(title = adminProductPageStrings.productOrganization) {
                    CategoryId(vm, state)
                    Divider()
                    Tags(vm, state)
                }
                CardSection(title = adminProductPageStrings.info) {
                    Creator(vm, state)
                    CreatedAt(state)
                    UpdatedAt(state)
                }
            }
        )
    }
}

@Composable
private fun ManageVariantsSection(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    if (state.current.variants.isEmpty()) {
        TextButton(
            leadingIcon = { MdiAdd() },
            onClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateVariantClick) },
        ) {
            val addVariantText = if (state.current.variants.isEmpty())
                adminProductPageStrings.addOptionsLikeSizeOrColor else adminProductPageStrings.addAnotherOption
            SpanText(text = addVariantText)
        }
    } else {
        state.current.variants.forEach { variant ->
            SpanText(text = variant.id)
        }
    }
}

@Composable
private fun UpdatedAt(state: AdminProductEditContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${adminProductPageStrings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun CreatedAt(state: AdminProductEditContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${adminProductPageStrings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(text = adminProductPageStrings.shippingPreset)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let { categoryId ->
            listOf(state.allCategories.first { it.id == categoryId }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.OnPresetSelected(it)) },
        noChipsText = adminProductPageStrings.noCategories,
        createText = adminProductPageStrings.createCategory,
        onCreateClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.shippingPresetDesc) },
    )
    Box(Modifier.size(0.5.em))
}

@Composable
private fun Height(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetHeight(it)) },
        label = adminProductPageStrings.height,
        errorText = state.heightError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.heightDesc)
}

@Composable
private fun Width(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetWidth(it)) },
        label = adminProductPageStrings.width,
        errorText = state.widthError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.widthDesc)
}

@Composable
private fun Length(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetLength(it)) },
        label = adminProductPageStrings.length,
        errorText = state.lengthError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.lengthDesc)
}

@Composable
private fun Weight(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetWeight(it)) },
        label = adminProductPageStrings.weight,
        errorText = state.weightError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.weightDesc)
}

@Composable
private fun RegularPrice(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.regularPrice.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetRegularPrice(it)) },
        label = adminProductPageStrings.regularPrice,
        errorText = state.regularPriceError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.regularPriceDesc)
}

@Composable
private fun Price(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.price.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetPrice(it)) },
        label = adminProductPageStrings.price,
        errorText = state.priceError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.priceDesc)
}

@Composable
private fun Media(
    vm: AdminProductEditViewModel,
    state: AdminProductEditContract.State,
    onImageClick: (AdminProductGetByIdQuery.Medium) -> Unit,
    onImageDeleteClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var convertedMedia by remember { mutableStateOf(emptyList<AdminProductGetByIdQuery.Medium>()) }

    LaunchedEffect(state.localMedia) {
        convertedMedia = state.localMedia.mapIndexed { index, medium ->
            val file = convertBase64ToFile(medium.url, index.toString())
            val url = URL.createObjectURL(file)
            AdminProductGetByIdQuery.Medium(
                keyName = medium.keyName,
                alt = medium.alt,
                url = url,
                type = MediaType.Image,
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .flexWrap(FlexWrap.Wrap)
            .gap(1.em)
    ) {
        state.current.media.forEach { image ->
            MediaSlot(
                url = image.url,
                alt = image.alt,
                errorText = null,
                onFileDropped = {},
                onImageClick = { onImageClick(image) },
                onDeleteClick = { onImageDeleteClick(image.keyName) }
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
                        vm.trySend(AdminProductEditContract.Inputs.AddMedia(imageString))
                    } ?: vm.trySend(AdminProductEditContract.Inputs.SetImageDropError(error = "Not a PNG?"))
                }
            },
            onImageClick = {},
            onDeleteClick = {},
            modifier = Modifier.size(200.px)
        )
    }
}

@Composable
private fun TrackQuantity(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = adminProductPageStrings.trackQuantity,
        selected = state.current.inventory.trackQuantity,
        onClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetTrackQuantity(
                    !state.current.inventory.trackQuantity
                )
            )
        },
    )
    AppTooltip(adminProductPageStrings.trackQuantityDesc)
}

@Composable
private fun UseGlobalTracking(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = adminProductPageStrings.useGlobalTracking,
        selected = state.current.inventory.useGlobalTracking,
        onClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetUseGlobalTracking(
                    !state.current.inventory.useGlobalTracking
                )
            )
        },
    )
    AppTooltip(adminProductPageStrings.useGlobalTrackingDesc)
}

@Composable
private fun ChargeTax(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = adminProductPageStrings.chargeTax,
        selected = state.current.pricing.chargeTax,
        onClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetChargeTax(
                    !state.current.pricing.chargeTax
                )
            )
        },
    )
    AppTooltip(adminProductPageStrings.chargeTaxDesc)
}

@Composable
private fun StatusOfStock(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(text = adminProductPageStrings.stockStatus)
    FilterChipSection(
        chips = StockStatus.entries
            .filter { it != StockStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.inventory.stockStatus.name),
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.SetStatusOfStock(StockStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(adminProductPageStrings.stockStatusDesc) },
    )
}

@Composable
private fun LowStockThreshold(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.inventory.lowStockThreshold.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetLowStockThreshold(
                    it.toIntOrNull() ?: 0
                )
            )
        },
        label = adminProductPageStrings.lowStockThreshold,
        errorText = state.lowStockThresholdError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.lowStockThresholdDesc)
}

@Composable
private fun RemainingStock(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.inventory.remainingStock.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetRemainingStock(
                    it.toIntOrNull() ?: 0
                )
            )
        },
        label = adminProductPageStrings.remainingStock,
        errorText = state.remainingStockError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.remainingStockDesc)
}

@Composable
private fun BackorderStatus(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(text = adminProductPageStrings.backorderStatus)
    FilterChipSection(
        chips = BackorderStatus.entries
            .filter { it != BackorderStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.inventory.backorderStatus.name),
        onChipClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetStatusOfBackorder(
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
        afterTitle = { AppTooltip(adminProductPageStrings.backorderStatusDesc) },
    )
}

@Composable
private fun Name(
    state: AdminProductEditContract.State,
    vm: AdminProductEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.name,
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetName(it)) },
        label = adminProductPageStrings.name,
        errorText = state.titleError,
        required = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Description(
    state: AdminProductEditContract.State,
    vm: AdminProductEditViewModel
) {
    AppOutlinedTextField(
        value = state.current.description,
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetDescription(it)) },
        label = adminProductPageStrings.description,
        errorText = state.descriptionError,
        type = TextFieldType.TEXTAREA,
        rows = 5,
        modifier = Modifier
            .fillMaxWidth()
            .resize(Resize.Vertical)
    )
    AppTooltip(adminProductPageStrings.descriptionDesc)
}

@Composable
private fun IsFeatured(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = adminProductPageStrings.isFeatured,
        selected = state.current.isFeatured,
        onClick = { vm.trySend(AdminProductEditContract.Inputs.SetIsFeatured(!state.current.isFeatured)) },
    )
    AppTooltip(adminProductPageStrings.isFeaturedDesc)
}

@Composable
private fun AllowReviews(
    vm: AdminProductEditViewModel,
    state: AdminProductEditContract.State
) {
    SwitchSection(
        title = adminProductPageStrings.allowReviews,
        selected = state.current.allowReviews,
        onClick = {
            vm.trySend(AdminProductEditContract.Inputs.SetAllowReviews(!state.current.allowReviews))
        },
    )
    AppTooltip(adminProductPageStrings.allowReviewsDesc)
}

@Composable
private fun CategoryId(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(text = adminProductPageStrings.categories)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let { categoryId ->
            listOf(state.allCategories.first { it.id == categoryId }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.OnCategorySelected(it)) },
        noChipsText = adminProductPageStrings.noCategories,
        createText = adminProductPageStrings.createCategory,
        onCreateClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.categoriesDesc) },
    )
}

@Composable
private fun Tags(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(text = adminProductPageStrings.tags)
    FilterChipSection(
        chips = state.allTags.map { it.name },
        selectedChips = state.current.tags
            .mapNotNull { id -> state.allTags.firstOrNull { id == it.id }?.name },
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.OnTagSelected(it)) },
        canBeEmpty = true,
        noChipsText = adminProductPageStrings.noTags,
        createText = adminProductPageStrings.createTag,
        onCreateClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateTagClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.tagsDesc) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Creator(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    CreatorSection(
        title = adminProductPageStrings.createdBy,
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminProductEditContract.Inputs.OnUserCreatorClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.createdByDesc) },
    )
}

@Composable
private fun PostStatus(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    FilterChipSection(
        chips = PostStatus.entries
            .filter { it != PostStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.postStatus.name),
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.SetStatusOfPost(PostStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(adminProductPageStrings.postStatusDesc) },
    )
}
