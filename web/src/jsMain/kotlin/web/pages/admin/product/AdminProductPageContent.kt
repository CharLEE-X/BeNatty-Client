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
import core.models.PageScreenState
import data.AdminProductGetByIdQuery
import data.type.BackorderStatus
import data.type.MediaType
import data.type.PostStatus
import data.type.StockStatus
import feature.admin.customer.page.adminCustomerPageStrings
import feature.admin.product.page.AdminProductPageContract
import feature.admin.product.page.AdminProductPageViewModel
import feature.admin.product.page.adminProductPageStrings
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
fun AdminProductPageContent(
    productId: String?,
    onError: suspend (String) -> Unit,
    adminRoutes: AdminRoutes,
    goToCreateCategory: () -> Unit,
    goToCreateTag: () -> Unit,
    goToCustomer: (String) -> Unit,
    goToProduct: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val vm = remember(scope) {
        AdminProductPageViewModel(
            productId = productId,
            scope = scope,
            onError = onError,
            goBackToProducts = adminRoutes.goBack,
            goToCreateCategory = goToCreateCategory,
            goToCreateTag = goToCreateTag,
            goToCustomerDetails = goToCustomer,
            goToProductDetail = goToProduct,
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
        showEditedButtons = state.wasEdited || state.pageScreenState is PageScreenState.New,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = adminProductPageStrings.unsavedChanges,
        saveText = adminProductPageStrings.save,
        discardText = adminProductPageStrings.discard,
        onCancel = { if (state.wasEdited) vm.trySend(AdminProductPageContract.Inputs.OnDiscardClick) },
        onSave = { vm.trySend(AdminProductPageContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                hasChanges = state.wasEdited,
                messageText = adminProductPageStrings.unsavedChanges,
                saveText = adminProductPageStrings.saveChanges,
                resetText = adminProductPageStrings.dismiss,
                onSave = { vm.trySend(AdminProductPageContract.Inputs.OnSaveClick) },
                onCancel = { vm.trySend(AdminProductPageContract.Inputs.OnDiscardClick) },
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
                title = "${adminCustomerPageStrings.delete} ${state.original.title}",
                actionYesText = adminProductPageStrings.delete,
                actionNoText = adminProductPageStrings.discard,
                contentText = adminProductPageStrings.deleteExplain,
                onOpen = { deleteProductDialogOpen = it },
                onClosing = { deleteProductDialogClosing = it },
                onYes = { vm.trySend(AdminProductPageContract.Inputs.OnDeleteClick) },
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
                        vm.trySend(AdminProductPageContract.Inputs.OnDeleteImageClick(it))
                    }
                },
                onNo = { deleteProductImageDialogClosing = true },
            )
        },
    ) {
        OneThirdLayout(
            title = if (state.pageScreenState is PageScreenState.New) {
                adminProductPageStrings.createProduct
            } else {
                state.original.title
            },
            subtitle = if (state.pageScreenState == PageScreenState.Existing) state.original.id else null,
            onGoBack = adminRoutes.goBack,
            hasBackButton = true,
            actions = {
                if (state.pageScreenState is PageScreenState.Existing) {
                    AppFilledButton(
                        onClick = { deleteProductDialogOpen = !deleteProductDialogOpen },
                        leadingIcon = { MdiDelete() },
                        containerColor = MaterialTheme.colors.error,
                    ) {
                        SpanText(text = adminProductPageStrings.delete)
                    }
                }
            },
            content = {
                CardSection(title = null) {
                    Title(state, vm)
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
//                    PostStatus(vm, state)
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
                if (state.pageScreenState is PageScreenState.Existing) {
                    CardSection(title = adminProductPageStrings.info) {
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
private fun ManageVariantsSection(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    if (state.current.variants.isEmpty()) {
        TextButton(
            leadingIcon = { MdiAdd() },
            onClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateVariantClick) },
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
fun UpdatedAt(state: AdminProductPageContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${adminProductPageStrings.lastUpdatedAt}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
fun CreatedAt(state: AdminProductPageContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${adminProductPageStrings.createdAt}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = adminProductPageStrings.shippingPreset)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let { categoryId ->
            listOf(state.allCategories.first { it.id == categoryId }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnPresetSelected(it)) },
        noChipsText = adminProductPageStrings.noCategories,
        createText = adminProductPageStrings.createCategory,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.shippingPresetDesc) },
    )
    Box(Modifier.size(0.5.em))
}

@Composable
private fun Height(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetHeight(it)) },
        label = adminProductPageStrings.height,
        errorText = state.heightError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.heightDesc)
}

@Composable
private fun Width(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetWidth(it)) },
        label = adminProductPageStrings.width,
        errorText = state.widthError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.widthDesc)
}

@Composable
private fun Length(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetLength(it)) },
        label = adminProductPageStrings.length,
        errorText = state.lengthError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.lengthDesc)
}

@Composable
private fun Weight(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetWeight(it)) },
        label = adminProductPageStrings.weight,
        errorText = state.weightError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.weightDesc)
}

@Composable
private fun RegularPrice(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.regularPrice.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetRegularPrice(it)) },
        label = adminProductPageStrings.regularPrice,
        errorText = state.regularPriceError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.regularPriceDesc)
}

@Composable
private fun Price(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.price.toString(),
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetPrice(it)) },
        label = adminProductPageStrings.price,
        errorText = state.priceError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(adminProductPageStrings.priceDesc)
}

@Composable
private fun Media(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State,
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
        val media = if (state.pageScreenState is PageScreenState.New) {
            state.localMedia
        } else {
            state.current.media
        }
        media.forEach { image ->
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
                        vm.trySend(AdminProductPageContract.Inputs.AddMedia(imageString))
                    } ?: vm.trySend(AdminProductPageContract.Inputs.SetImageDropError(error = "Not a PNG?"))
                }
            },
            onImageClick = {},
            onDeleteClick = {},
            modifier = Modifier.size(200.px)
        )
    }
}

@Composable
private fun TrackQuantity(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = adminProductPageStrings.trackQuantity,
        selected = state.current.inventory.trackQuantity,
        onClick = {
            vm.trySend(
                AdminProductPageContract.Inputs.SetTrackQuantity(
                    !state.current.inventory.trackQuantity
                )
            )
        },
    )
    AppTooltip(adminProductPageStrings.trackQuantityDesc)
}

@Composable
private fun UseGlobalTracking(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = adminProductPageStrings.useGlobalTracking,
        selected = state.current.inventory.useGlobalTracking,
        onClick = {
            vm.trySend(
                AdminProductPageContract.Inputs.SetUseGlobalTracking(
                    !state.current.inventory.useGlobalTracking
                )
            )
        },
    )
    AppTooltip(adminProductPageStrings.useGlobalTrackingDesc)
}

@Composable
private fun ChargeTax(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = adminProductPageStrings.chargeTax,
        selected = state.current.pricing.chargeTax,
        onClick = {
            vm.trySend(
                AdminProductPageContract.Inputs.SetChargeTax(
                    !state.current.pricing.chargeTax
                )
            )
        },
    )
    AppTooltip(adminProductPageStrings.chargeTaxDesc)
}

@Composable
private fun StatusOfStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = adminProductPageStrings.stockStatus)
    FilterChipSection(
        chips = StockStatus.entries
            .filter { it != StockStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.inventory.stockStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.SetStatusOfStock(StockStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(adminProductPageStrings.stockStatusDesc) },
    )
}

@Composable
private fun LowStockThreshold(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.inventory.lowStockThreshold.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductPageContract.Inputs.SetLowStockThreshold(
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
private fun RemainingStock(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    AppOutlinedTextField(
        value = state.current.inventory.remainingStock.toString(),
        onValueChange = {
            vm.trySend(
                AdminProductPageContract.Inputs.SetRemainingStock(
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
private fun BackorderStatus(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = adminProductPageStrings.backorderStatus)
    FilterChipSection(
        chips = BackorderStatus.entries
            .filter { it != BackorderStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.inventory.backorderStatus.name),
        onChipClick = {
            vm.trySend(
                AdminProductPageContract.Inputs.SetStatusOfBackorder(
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
private fun Title(
    state: AdminProductPageContract.State,
    vm: AdminProductPageViewModel
) {
    AppOutlinedTextField(
        value = state.current.title,
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetTitle(it)) },
        label = adminProductPageStrings.title,
        errorText = state.titleError,
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
        onValueChange = { vm.trySend(AdminProductPageContract.Inputs.SetDescription(it)) },
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
private fun IsFeatured(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SwitchSection(
        title = adminProductPageStrings.isFeatured,
        selected = state.current.isFeatured,
        onClick = { vm.trySend(AdminProductPageContract.Inputs.SetIsFeatured(!state.current.isFeatured)) },
    )
    AppTooltip(adminProductPageStrings.isFeaturedDesc)
}

@Composable
private fun AllowReviews(
    vm: AdminProductPageViewModel,
    state: AdminProductPageContract.State
) {
    SwitchSection(
        title = adminProductPageStrings.allowReviews,
        selected = state.current.allowReviews,
        onClick = {
            vm.trySend(AdminProductPageContract.Inputs.SetAllowReviews(!state.current.allowReviews))
        },
    )
    AppTooltip(adminProductPageStrings.allowReviewsDesc)
}

@Composable
private fun CategoryId(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = adminProductPageStrings.categories)
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let { categoryId ->
            listOf(state.allCategories.first { it.id == categoryId }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnCategorySelected(it)) },
        noChipsText = adminProductPageStrings.noCategories,
        createText = adminProductPageStrings.createCategory,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.categoriesDesc) },
    )
}

@Composable
private fun Tags(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    SpanText(text = adminProductPageStrings.tags)
    FilterChipSection(
        chips = state.allTags.map { it.name },
        selectedChips = state.current.tags
            .mapNotNull { id -> state.allTags.firstOrNull { id == it.id }?.name },
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.OnTagSelected(it)) },
        canBeEmpty = true,
        noChipsText = adminProductPageStrings.noTags,
        createText = adminProductPageStrings.createTag,
        onCreateClick = { vm.trySend(AdminProductPageContract.Inputs.OnCreateTagClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.tagsDesc) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Creator(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    CreatorSection(
        title = adminProductPageStrings.createdBy,
        creatorName = "${state.current.creator.firstName} ${state.current.creator.lastName}",
        onClick = { vm.trySend(AdminProductPageContract.Inputs.OnUserCreatorClick) },
        afterTitle = { AppTooltip(adminProductPageStrings.createdByDesc) },
    )
}

@Composable
private fun PostStatus(vm: AdminProductPageViewModel, state: AdminProductPageContract.State) {
    FilterChipSection(
        chips = PostStatus.entries
            .filter { it != PostStatus.UNKNOWN__ }
            .map { it.name },
        selectedChips = listOf(state.current.postStatus.name),
        onChipClick = { vm.trySend(AdminProductPageContract.Inputs.SetStatusOfPost(PostStatus.valueOf(it))) },
        canBeEmpty = false,
        noChipsText = "",
        createText = "",
        onCreateClick = {},
        afterTitle = { AppTooltip(adminProductPageStrings.postStatusDesc) },
    )
}
