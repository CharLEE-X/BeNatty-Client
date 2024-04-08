package web.pages.admin.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridColumn
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onFocusIn
import com.varabyte.kobweb.compose.ui.modifiers.onFocusOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.mdi.IconStyle
import com.varabyte.kobweb.silk.components.icons.mdi.MdiAdd
import com.varabyte.kobweb.silk.components.icons.mdi.MdiCheck
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDelete
import com.varabyte.kobweb.silk.components.icons.mdi.MdiEdit
import com.varabyte.kobweb.silk.components.icons.mdi.MdiUndo
import com.varabyte.kobweb.silk.components.text.SpanText
import component.localization.Strings
import component.localization.Strings.Edit
import component.localization.getString
import data.AdminProductGetByIdQuery
import data.type.BackorderStatus
import data.type.MediaType
import data.type.PostStatus
import data.type.StockStatus
import data.type.VariantType
import feature.admin.product.edit.AdminProductEditContract
import feature.admin.product.edit.AdminProductEditViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.AutoComplete
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import org.w3c.dom.HTMLElement
import org.w3c.dom.url.URL
import theme.MaterialTheme
import theme.roleStyle
import web.components.layouts.AdminLayout
import web.components.layouts.AdminRoutes
import web.components.layouts.OneThirdLayout
import web.components.sections.desktopNav.AppMenu
import web.components.widgets.AppFilledButton
import web.components.widgets.AppFilledTonalButton
import web.components.widgets.AppOutlinedButton
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
import web.components.widgets.TrailingIconGoToNext
import web.compose.material3.component.Divider
import web.compose.material3.component.TextButton
import web.compose.material3.component.TextFieldType
import web.util.convertBase64ToFile
import web.util.convertImageToBase64
import web.util.cornerRadius
import web.util.onEnterKeyDown

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
        title = getString(Strings.Products),
        isLoading = state.isLoading,
        showEditedButtons = state.wasEdited,
        isSaveEnabled = state.wasEdited,
        unsavedChangesText = getString(Strings.UnsavedChanges),
        saveText = getString(Strings.Save),
        discardText = getString(Strings.Discard),
        onCancel = { if (state.wasEdited) vm.trySend(AdminProductEditContract.Inputs.OnDiscardClick) },
        onSave = { vm.trySend(AdminProductEditContract.Inputs.OnSaveClick) },
        adminRoutes = adminRoutes,
        overlay = {
            HasChangesWidget(
                messageText = getString(Strings.UnsavedChanges),
                hasChanges = state.wasEdited,
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
                title = "${getString(Strings.Delete)} ${state.original.name}",
                contentText = getString(Strings.DeleteExplain),
                onOpen = { deleteProductDialogOpen = it },
                onClosing = { deleteProductDialogClosing = it },
                onYes = { vm.trySend(AdminProductEditContract.Inputs.OnDeleteClick) },
                onNo = { deleteProductDialogClosing = true },
            )
            TakeActionDialog(
                open = deleteProductImageDialogOpen && !deleteProductImageDialogClosing,
                closing = deleteProductImageDialogClosing,
                title = getString(Strings.Delete),
                actionYesText = getString(Strings.Delete),
                contentText = getString(Strings.DeleteExplain),
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
                    SpanText(getString(Strings.Delete))
                }
            },
            content = {
                CardSection(
                    title = getString(Strings.Variants),
                    contentPadding = 0.em,
                    gap = 0.em,
                ) {
                    ManageOptions(vm, state)
                    InspectVariations(vm, state)
                    TotalInventory(state)
                }
                CardSection(title = null) {
                    Name(state, vm)
                    Description(state, vm)
                }
                CardSection(title = getString(Strings.Media)) {
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
                CardSection(title = getString(Strings.Price)) {
                    Price(vm, state)
                    RegularPrice(vm, state)
                    ChargeTax(vm, state)
                }
                CardSection(title = getString(Strings.Inventory)) {
                    TrackQuantity(vm, state)
                    UseGlobalTracking(vm, state)
                    RemainingStock(vm, state)
                    LowStockThreshold(vm, state)
                    BackorderStatus(vm, state)
                    StatusOfStock(vm, state)
                }
                CardSection(title = getString(Strings.Shipping)) {
                    ShippingPreset(vm, state)
                    Weight(vm, state)
                    Length(vm, state)
                    Width(vm, state)
                    Height(vm, state)
                }
            },
            contentThird = {
                CardSection(title = getString(Strings.Status)) {
                    PostStatus(vm, state)
                }
                CardSection(title = null) {
                    IsFeatured(vm, state)
                    AllowReviews(vm, state)
                }
                CardSection(title = getString(Strings.Insights)) {
                    SpanText(getString(Strings.NoInsights))
                }
                CardSection(title = getString(Strings.ProductOrganization)) {
                    CategoryId(vm, state)
                    Divider()
                    Tags(vm, state)
                }
                CardSection(title = getString(Strings.Info)) {
                    Creator(vm, state)
                    CreatedAt(state)
                    UpdatedAt(state)
                }
            }
        )
    }
}

@Composable
fun TotalInventory(state: AdminProductEditContract.State) {
    if (state.localVariants.isNotEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .backgroundColor(MaterialTheme.colors.surfaceContainerHigh)
                .padding(1.em)
                .borderTop(
                    width = 1.px,
                    color = MaterialTheme.colors.surface,
                    style = LineStyle.Solid
                )
                .borderRadius(
                    bottomLeft = cornerRadius,
                    bottomRight = cornerRadius
                )
        ) {
            SpanText(
                text = getString(Strings.TotalInventory) + ": ${state.totalInventory} " +
                    getString(Strings.Available).lowercase(),
                modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
            )
        }
    }
}

@Composable
private fun ManageOptions(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(leftRight = 1.em, topBottom = 1.em)
    ) {
        if (state.showAddOptions) {
            TextButton(
                leadingIcon = { MdiAdd() },
                onClick = { vm.trySend(AdminProductEditContract.Inputs.OnAddOptionsClick) },
            ) {
                SpanText(getString(Strings.AddOptionsLikeSizeOrColor))
            }
        }
        Column(
            Modifier
                .fillMaxWidth()
                .borderRadius(cornerRadius)
                .border(
                    width = 1.px,
                    color = if (state.localOptions.isNotEmpty()) MaterialTheme.colors.surfaceContainerHighest
                    else Colors.Transparent,
                    style = LineStyle.Solid
                )
        ) {
            state.localOptions.forEachIndexed { index, localOption ->
                if (localOption.isEditing) {
                    LocalOptionBuilder(index, localOption, vm, state)
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .gap(1.em)
                            .padding(1.em)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .gap(1.em)
                                .weight(1f)
                        ) {
                            SpanText(
                                text = localOption.name,
                                modifier = Modifier.fontWeight(FontWeight.SemiBold)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.gap(0.5.em)
                                    .display(DisplayStyle.Flex)
                            ) {
                                localOption.attrs.forEach { attr ->
                                    OptionAttrItem(attr)
                                }
                            }
                        }
                        AppOutlinedButton(
                            onClick = { vm.trySend(AdminProductEditContract.Inputs.OnEditOptionClicked(index)) },
                            leadingIcon = { MdiEdit() },
                        ) {
                            SpanText(getString(Edit))
                        }
                    }
                    if (state.localOptions.size > 1 && index < state.localOptions.size - 1) {
                        Divider(modifier = Modifier.color(MaterialTheme.colors.surfaceContainerHighest))
                    }
                }
            }
            if (state.showAddAnotherOption) {
                Divider(modifier = Modifier.color(MaterialTheme.colors.surfaceContainerHighest))
                TextButton(
                    leadingIcon = { MdiAdd() },
                    onClick = { vm.trySend(AdminProductEditContract.Inputs.OnAddAnotherOptionClick) },
                    hoverContainerColor = MaterialTheme.colors.surfaceContainerHigh,
                    modifier = Modifier.margin(0.5.em)
                ) {
                    SpanText(getString(Strings.AddAnotherOption))
                }
            }
        }
    }
}

@Composable
private fun OptionAttrItem(attr: AdminProductEditContract.Attribute) {
    SpanText(
        text = attr.value, // TODO: Need to be localized
        modifier = Modifier
            .roleStyle(MaterialTheme.typography.bodyMedium)
            .padding(
                topBottom = 0.5.em,
                leftRight = 1.em,
            )
            .borderRadius(cornerRadius)
            .border(
                width = 1.px,
                color = MaterialTheme.colors.surface,
                style = LineStyle.Solid
            )
            .backgroundColor(MaterialTheme.colors.surfaceContainerHigh)
    )
}

@Composable
fun InspectVariations(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    val gridContainerModifier = Modifier
        .fillMaxWidth()
        .display(DisplayStyle.Grid)
        .gap(1.em)
        .gridTemplateColumns { repeat(4) { size(1.fr) } }

    println(
        """
        AllLocalOptions:\n${state.localOptions.map { "$it\n" }}
        AllLocalVariants:\n${state.localVariants.map { "$it\n" }}
        """.trimIndent()
    )

    if (state.localVariants.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 1.em)
                .gap(0.5.em)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(bottom = 1.em)
                    .backgroundColor(MaterialTheme.colors.surfaceContainerHigh)
                    .borderTop(
                        width = 1.px,
                        color = MaterialTheme.colors.surface,
                        style = LineStyle.Solid
                    )
                    .borderBottom(
                        width = 1.px,
                        color = MaterialTheme.colors.surface,
                        style = LineStyle.Solid
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = gridContainerModifier
                        .padding(topBottom = 1.5.em)
                        .padding(left = 2.em, right = 1.em)
                        .roleStyle(MaterialTheme.typography.bodyMedium)
                ) {
                    SpanText(
                        text = getString(Strings.Variant),
                        modifier = Modifier.gridColumn(1, 3)
                    )
                    SpanText(
                        text = getString(Strings.Price),
                        modifier = Modifier.gridColumn(3, 4)
                    )
                    SpanText(
                        text = getString(Strings.Available),
                        modifier = Modifier.gridColumn(4, 5)
                    )
                }
                Box(modifier = Modifier.size(48.px).padding(right = 1.em))
            }
            state.localVariants.forEachIndexed { index, variant ->
                LocalVariantItem(
                    variant = variant,
                    disabled = !state.variantEditingEnabled,
                    onPriceChanged = { vm.trySend(AdminProductEditContract.Inputs.OnVariantPriceChanged(index, it)) },
                    onQuantityChanged = {
                        vm.trySend(AdminProductEditContract.Inputs.OnVariantQuantityChanged(index, it))
                    },
                    onDeleteClick = { vm.trySend(AdminProductEditContract.Inputs.OnDeleteVariantClicked(index)) },
                    onUndoDeleteClick = {
                        vm.trySend(AdminProductEditContract.Inputs.OnUndoDeleteVariantClicked(index))
                    },
                    modifier = gridContainerModifier
                        .padding(left = 2.em, right = 1.em)
                        .thenIf(index != state.localVariants.size - 1) {
                            Modifier.borderBottom(
                                width = 1.px,
                                color = MaterialTheme.colors.surface,
                                style = LineStyle.Solid
                            )
                        }
                )
            }
        }
    }
}

@Composable
private fun LocalVariantItem(
    modifier: Modifier,
    variant: AdminProductEditContract.LocalVariant,
    disabled: Boolean,
    onPriceChanged: (String) -> Unit,
    onQuantityChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onUndoDeleteClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier.roleStyle(MaterialTheme.typography.bodyMedium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .gap(0.5.em)
                    .gridColumn(1, 3)
            ) {
                variant.attrs.forEachIndexed { index, attr ->
                    OptionAttrItem(AdminProductEditContract.Attribute(attr))
                    if (index < variant.attrs.size - 1) {
                        Box(
                            modifier = Modifier
                                .size(6.px)
                                .borderRadius(50.percent)
                                .color(MaterialTheme.colors.surface)
                        )
                    }
                }
            }
            if (variant.enabled) {
                AppOutlinedTextField(
                    value = variant.price,
                    onValueChange = onPriceChanged,
                    placeholder = "100.00",
                    prefixText = "£", // TODO: Localize
                    error = variant.priceError != null,
                    errorText = variant.priceError,
                    hasTrailingIcon = false,
                    rows = 1,
                    disabled = disabled,
                    modifier = Modifier.gridColumn(3, 4)
                )
                if (disabled) AppTooltip(getString(Strings.FinishAddingOptionToEnableEditing))
                AppOutlinedTextField(
                    value = variant.quantity,
                    onValueChange = onQuantityChanged,
                    error = variant.quantityError != null,
                    errorText = variant.quantityError,
                    placeholder = "0",
                    hasTrailingIcon = false,
                    rows = 1,
                    disabled = disabled,
                    modifier = Modifier.gridColumn(4, 5)
                )
                if (disabled) AppTooltip(getString(Strings.FinishAddingOptionToEnableEditing))
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridColumn(3, 5)
                        .height(4.em)
                ) {
                    Spacer()
                    SpanText(
                        text = getString(Strings.ThisVariantWontBeCreated),
                        modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
                    )
                }
            }
        }
        if (variant.enabled) {
            DeleteButton(
                modifier = Modifier.padding(right = 1.em),
                onClick = { onDeleteClick() },
                disabled = disabled
            )
        } else {
            UndoButton(
                modifier = Modifier.padding(right = 1.em),
                onClick = onUndoDeleteClick
            )
        }
    }
}

@Composable
private fun LocalOptionBuilder(
    optionIndex: Int,
    variant: AdminProductEditContract.LocalOption,
    vm: AdminProductEditViewModel,
    state: AdminProductEditContract.State
) {
    var open by remember { mutableStateOf(false) }
    var menuFocused by remember { mutableStateOf(false) }

    var recognisedInputType by remember { mutableStateOf<VariantType?>(null) }

    LaunchedEffect(variant.name) {
        recognisedInputType = VariantType.knownEntries.firstOrNull { it.name == variant.name }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .gap(0.5.em)
            .padding(1.em)
    ) {
        SpanText(
            text = getString(Strings.OptionName),
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyMedium)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .gap(1.em)
        ) {
            Span(
                Modifier
                    .fillMaxWidth()
                    .position(Position.Relative)
                    .toAttrs()
            ) {
                var nameRef by remember { mutableStateOf<HTMLElement?>(null) }

                AppOutlinedTextField(
                    value = variant.name,
                    onValueChange = {
                        vm.trySend(
                            AdminProductEditContract.Inputs.OnOptionNameChanged(
                                optionIndex,
                                it
                            )
                        )
                    },
                    placeholder = getString(Strings.Size),
                    errorText = variant.nameError,
                    error = variant.nameError != null,
                    trailingIcon = { TrailingIconGoToNext(show = open || menuFocused) },
                    autoComplete = AutoComplete.off,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusIn { open = true }
                        .onFocusOut { open = false }
                )

                val optionsBeforeIndex = state.localOptions
                    .take(optionIndex)
                    .map { it.name }

                AppMenu(
                    open = open || menuFocused,
                    items = VariantType.knownEntries
                        .filter { it.name !in optionsBeforeIndex }
                        .map { it.name },
                    onItemSelected = {
                        open = false
                        vm.trySend(AdminProductEditContract.Inputs.OnOptionNameChanged(optionIndex, it))
                    },
                    fontSize = 18.px,
                    itemsGap = 0.5.em,
                    bgAlpha = 255,
                    modifier = Modifier
                        .margin(top = 10.px)
                        .translateX((-8).px)
                        .onFocusIn { menuFocused = true }
                        .onFocusOut { menuFocused = false }
                        .attrsModifier {
                            ref {
                                nameRef = it.unsafeCast<HTMLElement>()
                                onDispose { nameRef = null }
                            }
                        }
                        .tabIndex(0)
                )
            }
            DeleteButton { vm.trySend(AdminProductEditContract.Inputs.OnDeleteOptionClicked(optionIndex)) }
        }

        SpanText(
            text = getString(Strings.OptionValues),
            modifier = Modifier
                .margin(top = 1.em)
                .roleStyle(MaterialTheme.typography.bodyMedium)
        )
        variant.attrs.forEachIndexed { attrIndex, attr ->
            var isOptionFocused by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .gap(1.em)
            ) {
                AppOutlinedTextField(
                    value = attr.value,
                    onValueChange = {
                        vm.trySend(
                            AdminProductEditContract.Inputs.OnOptionAttrValueChanged(
                                optionIndex = optionIndex,
                                attrIndex = attrIndex,
                                value = it
                            )
                        )
                    },
                    placeholder = when (recognisedInputType) {
                        VariantType.Size -> getString(Strings.Small)
                        VariantType.Color -> getString(Strings.Black)
                        VariantType.Material -> getString(Strings.Cotton)
                        VariantType.Style -> getString(Strings.Classic)
                        else -> getString(Strings.AddAnotherValue)
                    },
                    errorText = attr.error,
                    error = attr.error != null,
                    trailingIcon = { TrailingIconGoToNext(show = isOptionFocused) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusIn { isOptionFocused = true }
                        .onFocusOut { isOptionFocused = false }
                )
                if (attr.value.isNotBlank()) {
                    DeleteButton {
                        vm.trySend(AdminProductEditContract.Inputs.OnDeleteOptionAttrClicked(optionIndex, attrIndex))
                    }
                } else {
                    Box(modifier = Modifier.size(24.px))
                }
            }
        }
        Spacer()
        AppFilledTonalButton(
            onClick = { vm.trySend(AdminProductEditContract.Inputs.OnOptionDoneClicked(optionIndex)) },
            leadingIcon = { MdiCheck() },
            disabled = variant.isDoneDisabled
        ) {
            SpanText(text = getString(Strings.Done))
        }
    }
}

@Composable
private fun DeleteButton(
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    MdiDelete(
        style = IconStyle.OUTLINED,
        modifier = modifier
            .onClick { if (!disabled) onClick() }
            .onEnterKeyDown { if (!disabled) onClick() }
            .cursor(if (disabled) Cursor.NotAllowed else Cursor.Pointer)
            .disabled(disabled)
            .onMouseOver { hovered = true }
            .onMouseOut { hovered = false }
            .opacity(if (hovered && !disabled) 1.0 else 0.5)
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
            .tabIndex(0)
    )
}

@Composable
private fun UndoButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }

    MdiUndo(
        style = IconStyle.OUTLINED,
        modifier = modifier
            .onClick { onClick() }
            .cursor(Cursor.Pointer)
            .onMouseOver { hovered = true }
            .onMouseOut { hovered = false }
            .opacity(if (hovered) 1.0 else 0.5)
            .transition(CSSTransition("opacity", 0.3.s, TransitionTimingFunction.Ease))
            .tabIndex(0)
            .onEnterKeyDown(onClick)
    )
}

@Composable
private fun UpdatedAt(state: AdminProductEditContract.State) {
    if (state.current.updatedAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.LastUpdatedAt)}: ${state.current.updatedAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun CreatedAt(state: AdminProductEditContract.State) {
    if (state.current.createdAt.isNotEmpty()) {
        SpanText(
            text = "${getString(Strings.CreatedAt)}: ${state.current.createdAt}",
            modifier = Modifier.roleStyle(MaterialTheme.typography.bodyLarge)
        )
    }
}

@Composable
private fun ShippingPreset(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(getString(Strings.ShippingPreset))
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let { categoryId ->
            listOf(state.allCategories.first { it.id == categoryId }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.OnPresetSelected(it)) },
        noChipsText = getString(Strings.NoCategories),
        createText = getString(Strings.CreateCategory),
        onCreateClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(getString(Strings.ShippingPresetDesc)) },
    )
    Box(Modifier.size(0.5.em))
}

@Composable
private fun Height(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.height.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetHeight(it)) },
        label = getString(Strings.Height),
        errorText = state.heightError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.HeightDesc))
}

@Composable
private fun Width(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.width.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetWidth(it)) },
        label = getString(Strings.Width),
        errorText = state.widthError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.WidthDesc))
}

@Composable
private fun Length(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.length.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetLength(it)) },
        label = getString(Strings.Length),
        errorText = state.lengthError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.LengthDesc))
}

@Composable
private fun Weight(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.shipping.weight.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetWeight(it)) },
        label = getString(Strings.Weight),
        errorText = state.weightError,
        type = TextFieldType.NUMBER,
        disabled = state.current.shipping.presetId != null,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.WeightDesc))
}

@Composable
private fun RegularPrice(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.regularPrice.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetRegularPrice(it)) },
        label = getString(Strings.RegularPrice),
        errorText = state.regularPriceError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.RegularPriceDesc))
}

@Composable
private fun Price(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    AppOutlinedTextField(
        value = state.current.pricing.price.toString(),
        onValueChange = { vm.trySend(AdminProductEditContract.Inputs.SetPrice(it)) },
        label = getString(Strings.Price),
        errorText = state.priceError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.PriceDesc))
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
        title = getString(Strings.TrackQuantity),
        selected = state.current.inventory.trackQuantity,
        onClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetTrackQuantity(
                    !state.current.inventory.trackQuantity
                )
            )
        },
    )
    AppTooltip(getString(Strings.TrackQuantityDesc))
}

@Composable
private fun UseGlobalTracking(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = getString(Strings.UseGlobalTracking),
        selected = state.current.inventory.useGlobalTracking,
        onClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetUseGlobalTracking(
                    !state.current.inventory.useGlobalTracking
                )
            )
        },
    )
    AppTooltip(getString(Strings.UseGlobalTrackingDesc))
}

@Composable
private fun ChargeTax(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = getString(Strings.ChargeTax),
        selected = state.current.pricing.chargeTax,
        onClick = {
            vm.trySend(
                AdminProductEditContract.Inputs.SetChargeTax(
                    !state.current.pricing.chargeTax
                )
            )
        },
    )
    AppTooltip(getString(Strings.ChargeTaxDesc))
}

@Composable
private fun StatusOfStock(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(getString(Strings.StockStatus))
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
        afterTitle = { AppTooltip(getString(Strings.StockStatusDesc)) },
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
        label = getString(Strings.LowStockThreshold),
        errorText = state.lowStockThresholdError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.LowStockThresholdDesc))
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
        label = getString(Strings.RemainingStock),
        errorText = state.remainingStockError,
        type = TextFieldType.NUMBER,
        modifier = Modifier.fillMaxWidth(),
    )
    AppTooltip(getString(Strings.RemainingStockDesc))
}

@Composable
private fun BackorderStatus(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(getString(Strings.BackorderStatus))
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
        afterTitle = { AppTooltip(getString(Strings.BackorderStatusDesc)) },
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
        label = getString(Strings.Name),
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
        label = getString(Strings.Description),
        errorText = state.descriptionError,
        type = TextFieldType.TEXTAREA,
        rows = 5,
        modifier = Modifier
            .fillMaxWidth()
            .resize(Resize.Vertical)
    )
    AppTooltip(getString(Strings.DescriptionDesc))
}

@Composable
private fun IsFeatured(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SwitchSection(
        title = getString(Strings.IsFeatured),
        selected = state.current.isFeatured,
        onClick = { vm.trySend(AdminProductEditContract.Inputs.SetIsFeatured(!state.current.isFeatured)) },
    )
    AppTooltip(getString(Strings.IsFeaturedDesc))
}

@Composable
private fun AllowReviews(
    vm: AdminProductEditViewModel,
    state: AdminProductEditContract.State
) {
    SwitchSection(
        title = getString(Strings.AllowReviews),
        selected = state.current.allowReviews,
        onClick = {
            vm.trySend(AdminProductEditContract.Inputs.SetAllowReviews(!state.current.allowReviews))
        },
    )
    AppTooltip(getString(Strings.AllowReviewsDesc))
}

@Composable
private fun CategoryId(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(getString(Strings.Categories))
    FilterChipSection(
        chips = state.allCategories.map { it.name },
        selectedChips = state.current.categoryId?.let { categoryId ->
            listOf(state.allCategories.first { it.id == categoryId }.name)
        } ?: emptyList(),
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.OnCategorySelected(it)) },
        noChipsText = getString(Strings.NoCategories),
        createText = getString(Strings.CreateCategory),
        onCreateClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateCategoryClick) },
        afterTitle = { AppTooltip(getString(Strings.CategoriesDesc)) },
    )
}

@Composable
private fun Tags(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    SpanText(getString(Strings.Tags))
    FilterChipSection(
        chips = state.allTags.map { it.name },
        selectedChips = state.current.tags
            .mapNotNull { id -> state.allTags.firstOrNull { id == it.id }?.name },
        onChipClick = { vm.trySend(AdminProductEditContract.Inputs.OnTagSelected(it)) },
        canBeEmpty = true,
        noChipsText = getString(Strings.NoTags),
        createText = getString(Strings.CreateTag),
        onCreateClick = { vm.trySend(AdminProductEditContract.Inputs.OnCreateTagClick) },
        afterTitle = { AppTooltip(getString(Strings.TagsDesc)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun Creator(vm: AdminProductEditViewModel, state: AdminProductEditContract.State) {
    CreatorSection(
        title = getString(Strings.CreatedBy),
        creatorName = state.current.creator.name,
        onClick = { vm.trySend(AdminProductEditContract.Inputs.OnUserCreatorClick) },
        afterTitle = { AppTooltip(getString(Strings.CreatedByDesc)) },
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
        afterTitle = { AppTooltip(getString(Strings.PostStatusDesc)) },
    )
}
