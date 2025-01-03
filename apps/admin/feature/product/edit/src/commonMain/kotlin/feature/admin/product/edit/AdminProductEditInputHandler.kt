package feature.admin.product.edit

import com.apollographql.apollo3.api.Optional
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.mapToUiMessage
import core.util.millisToTime
import data.AdminProductGetByIdQuery
import data.GetCategoryByIdQuery
import data.service.CategoryService
import data.service.ProductService
import data.service.TagService
import data.type.BackorderStatus
import data.type.BlobInput
import data.type.MediaInput
import data.type.MediaType
import data.type.ProductUpdateVariantInput
import data.type.StockStatus
import data.type.Trait
import data.type.VariantItemInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminProductEditContract.Inputs, AdminProductEditContract.Events, AdminProductEditContract.State>

internal class AdminProductEditInputHandler :
    KoinComponent,
    InputHandler<AdminProductEditContract.Inputs, AdminProductEditContract.Events, AdminProductEditContract.State> {

    private val productService by inject<ProductService>()
    private val inputValidator by inject<InputValidator>()
    private val categoryService: CategoryService by inject()
    private val tagService: TagService by inject()

    override suspend fun InputScope.handleInput(input: AdminProductEditContract.Inputs) = when (input) {
        is AdminProductEditContract.Inputs.Init -> handleInit(input.productId)
        is AdminProductEditContract.Inputs.UploadMedia -> handleUploadImage(input.mediaString)
        is AdminProductEditContract.Inputs.AddMedia -> handleAddMedia(input.mediaString)
        is AdminProductEditContract.Inputs.DeleteMedia -> handleDeleteMedia(input.index)

        is AdminProductEditContract.Inputs.GetProductById -> handleGetProductById(input.id)
        AdminProductEditContract.Inputs.GetAllCategories -> handleGetAllCategories()
        AdminProductEditContract.Inputs.GetAllTags -> handleGetAllTags()
        is AdminProductEditContract.Inputs.GetPresetCategory -> handleGetPresetCategory(input.categoryId)

        AdminProductEditContract.Inputs.OnDeleteClick -> handleDelete()
        AdminProductEditContract.Inputs.OnSaveClick -> handleSave()
        AdminProductEditContract.Inputs.OnDiscardClick -> handleOnDiscardClick()
        is AdminProductEditContract.Inputs.OnCategorySelected -> handleOnClickCategory(input.categoryName)
        is AdminProductEditContract.Inputs.OnTagSelected -> handleTagClick(input.tagName)
        is AdminProductEditContract.Inputs.OnDeleteImageClick -> handleDeleteImage(input.imageId)
        is AdminProductEditContract.Inputs.OnUserCreatorClick -> handelOnUserCreatorClick()
        AdminProductEditContract.Inputs.OnCreateTagClick -> postEvent(AdminProductEditContract.Events.GoToCreateTag)

        is AdminProductEditContract.Inputs.OnPresetSelected -> handlePresetClick(input.preset)
        AdminProductEditContract.Inputs.OnCreateCategoryClick ->
            postEvent(AdminProductEditContract.Events.GoToCreateCategory)

        AdminProductEditContract.Inputs.OnCreateVariantClick -> updateState { it.copy(showAddAnotherOption = true) }

        is AdminProductEditContract.Inputs.OnOptionNameChanged ->
            handleOnOptionNameChanged(input.optionIndex, input.name)

        is AdminProductEditContract.Inputs.OnOptionAttrValueChanged ->
            handleOnOptionAttrValueChanged(input.optionIndex, input.attrIndex, input.value)

        is AdminProductEditContract.Inputs.OnOptionDoneClicked -> handleOnOptionDoneClicked(input.optionIndex)
        is AdminProductEditContract.Inputs.OnEditOptionClicked -> handleOnEditOptionClicked(input.optionIndex)
        is AdminProductEditContract.Inputs.OnDeleteOptionClicked -> handleOnDeleteOptionClicked(input.optionIndex)
        is AdminProductEditContract.Inputs.OnDeleteOptionAttrClicked ->
            handleOnDeleteOptionAttrClicked(input.optionIndex, input.attrIndex)

        is AdminProductEditContract.Inputs.OnVariantPriceChanged ->
            handleOnVariantPriceChanged(input.variantIndex, input.price)

        is AdminProductEditContract.Inputs.OnVariantQuantityChanged ->
            handleOnVariantQuantityChanged(input.variantIndex, input.quantity)

        is AdminProductEditContract.Inputs.OnDeleteVariantClicked -> handleOnDeleteVariantClicked(input.variantIndex)

        is AdminProductEditContract.Inputs.OnUndoDeleteVariantClicked ->
            handleOnUndoDeleteVariantClicked(input.deletedVariantIndex)

        is AdminProductEditContract.Inputs.OnLocalVariantsChanged ->
            handleOnLocalVariantsChanged(input.localVariants)

        is AdminProductEditContract.Inputs.SetAllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminProductEditContract.Inputs.SetAllTags -> updateState { it.copy(allTags = input.tags) }
        is AdminProductEditContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductEditContract.Inputs.SetImagesLoading ->
            updateState { it.copy(isImagesLoading = input.isImagesLoading) }

        is AdminProductEditContract.Inputs.SetOriginal ->
            updateState { it.copy(original = input.product).wasEdited() }

        is AdminProductEditContract.Inputs.SetCurrent ->
            updateState {
                it.copy(current = input.product).wasEdited()
                    .also { println("AllLocalVariants variants:\n${it.current.variants.map { "$it\n" }}") }
            }

        is AdminProductEditContract.Inputs.SetId -> handleSetId(input.id)
        is AdminProductEditContract.Inputs.SetName -> handleSetName(input.title)

        is AdminProductEditContract.Inputs.SetIsFeatured ->
            updateState { it.copy(current = it.current.copy(isFeatured = input.isFeatured)).wasEdited() }

        is AdminProductEditContract.Inputs.SetAllowReviews ->
            updateState { it.copy(current = it.current.copy(allowReviews = input.allowReviews)).wasEdited() }

        is AdminProductEditContract.Inputs.SetStatusOfPost ->
            updateState { it.copy(current = it.current.copy(postStatus = input.postStatus)).wasEdited() }

        is AdminProductEditContract.Inputs.SetDescription ->
            updateState { it.copy(current = it.current.copy(description = input.description)).wasEdited() }

        is AdminProductEditContract.Inputs.SetCategoryId ->
            updateState { it.copy(current = it.current.copy(categoryId = input.categoryId)).wasEdited() }

        is AdminProductEditContract.Inputs.SetCreatedAt -> handleSetCreatedAt(input.createdAt)
        is AdminProductEditContract.Inputs.SetCreator -> handleSetCreator(input.creator)
        is AdminProductEditContract.Inputs.SetUpdatedAt -> handleSetUpdatedAt(input.updatedAt)
        is AdminProductEditContract.Inputs.SetStatusOfBackorder -> handleSetStatusOfBackorder(input.backorderStatus)
        is AdminProductEditContract.Inputs.SetMedia -> handleSetMedia(input.media)
        is AdminProductEditContract.Inputs.SetLowStockThreshold -> handleSetLowStockThreshold(input.lowStockThreshold)
        is AdminProductEditContract.Inputs.SetSalePrice -> handleSetSalePrice(input.price)
        is AdminProductEditContract.Inputs.SetRegularPrice -> handleSetRegularPrice(input.regularPrice)
        is AdminProductEditContract.Inputs.SetRemainingStock -> handleSetRemainingStock(input.remainingStock)
        is AdminProductEditContract.Inputs.SetIsPhysicalProduct -> handleSetIsPhysicalProduct(input.isPhysicalProduct)
        is AdminProductEditContract.Inputs.SetStatusOfStock -> handleSetStatusOfStock(input.stockStatus)
        is AdminProductEditContract.Inputs.SetTrackQuantity -> handleSetTrackQuantity(input.trackQuantity)
        is AdminProductEditContract.Inputs.SetWeight -> handleSetWeight(input.weight)
        is AdminProductEditContract.Inputs.SetLength -> handleSetLength(input.length)
        is AdminProductEditContract.Inputs.SetWidth -> handleSetWidth(input.width)
        is AdminProductEditContract.Inputs.SetHeight -> handleSetHeight(input.height)
        is AdminProductEditContract.Inputs.SetPresetCategory -> handleSetPresetCategory(input.category)
        is AdminProductEditContract.Inputs.SetShippingPresetId -> handleSetShippingPresetId(input.presetId)
        is AdminProductEditContract.Inputs.SetImageDropError -> updateState { it.copy(imageDropError = input.error) }
        is AdminProductEditContract.Inputs.SetChargeTax -> handleSetChargeTax(input.chargeTax)
        is AdminProductEditContract.Inputs.SetUseGlobalTracking -> handleSetUseGlobalTracking(input.useGlobalTracking)
        is AdminProductEditContract.Inputs.SetLocalMedia -> updateState { it.copy(localMedia = input.media) }

        // Variants
        AdminProductEditContract.Inputs.OnAddOptionsClick -> handleOnAddOptionsClick()
        AdminProductEditContract.Inputs.OnAddAnotherOptionClick -> handleOnAddAnotherOptionClick()
        is AdminProductEditContract.Inputs.OnLocalOptionsChanged -> handleOnLocalOptionsChanged(input)

        is AdminProductEditContract.Inputs.SetShowAddOptions -> updateState { it.copy(showAddOptions = input.show) }
        is AdminProductEditContract.Inputs.SetShowAddAnotherOption -> updateState { it.copy(showAddAnotherOption = input.show) }
        is AdminProductEditContract.Inputs.SetLocalOptions -> updateState {
            it.copy(
                showAddOptions = input.options.isEmpty(),
                showAddAnotherOption = input.options.size in 1..2,
                localOptions = input.options,
                variantEditingEnabled = input.options
                    .none { variant -> variant.isEditing },
            )
        }

        is AdminProductEditContract.Inputs.SetLocalVariants ->
            updateState { it.copy(localVariants = input.localVariants) }

        is AdminProductEditContract.Inputs.SetTotalInventory -> updateState { it.copy(totalInventory = input.total) }
        is AdminProductEditContract.Inputs.OnTraitClick -> handleOnTraitClick(input.trait)
        is AdminProductEditContract.Inputs.SetVendor ->
            updateState { it.copy(current = it.current.copy(vendor = input.vendor)).wasEdited() }
    }

    private suspend fun InputScope.handleOnTraitClick(trait: Trait) {
        updateState {
            it.copy(
                current = it.current.copy(
                    traits = if (trait in it.current.traits) it.current.traits - trait
                    else it.current.traits + trait
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnDiscardClick() {
        updateState {
            val localOptions = getLocalOptions(it.original.variants)
            val localVariants = it.original.variants.map {
                AdminProductEditContract.LocalVariant(
                    id = it.id,
                    attrs = it.attrs.map { attr -> attr.value },
                    quantity = it.quantity.toString(),
                    enabled = true
                )
            }
            val totalInventory = getTotalInventory(localVariants)

            it.copy(
                current = it.original,
                localVariants = localVariants,
                totalInventory = totalInventory,
                localOptions = localOptions,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnLocalVariantsChanged(localVariants: List<AdminProductEditContract.LocalVariant>) {
        val state = getCurrentState()
        sideJob("handleOnLocalVariantsChanged") {

            // TODO: copy values from previous variants to new variants so we dont lose them

            val variants = localVariants
                .filter { variant ->
                    variant.attrs.isNotEmpty() &&
                        variant.quantity.isNotBlank() &&
                        variant.enabled
                }
                .map { variant ->
                    AdminProductGetByIdQuery.Variant(
                        id = variant.id ?: "",
                        attrs = variant.attrs.mapIndexed { index, attr ->
                            AdminProductGetByIdQuery.Attr(state.localOptions[index].name, attr)
                        },
                        media = emptyList(),
                        quantity = variant.quantity.toInt(),
                    )
                }

            postInput(AdminProductEditContract.Inputs.SetLocalVariants(localVariants))
            postInput(AdminProductEditContract.Inputs.SetTotalInventory(countTotalInventory(localVariants)))
            postInput(AdminProductEditContract.Inputs.SetCurrent(state.current.copy(variants = variants)))
        }
    }

    private suspend fun InputScope.handleOnUndoDeleteVariantClicked(variantIndex: Int) {
        val newVariants = getCurrentState().localVariants.toMutableList()
        newVariants[variantIndex] = newVariants[variantIndex].copy(enabled = true)
        postInput(AdminProductEditContract.Inputs.OnLocalVariantsChanged(newVariants))
    }

    private suspend fun InputScope.handleOnDeleteVariantClicked(variantIndex: Int) {
        val newVariants = getCurrentState().localVariants.toMutableList()
        newVariants[variantIndex] = newVariants[variantIndex].copy(enabled = false)
        postInput(AdminProductEditContract.Inputs.OnLocalVariantsChanged(newVariants))
    }

    private suspend fun InputScope.handleOnVariantQuantityChanged(variantIndex: Int, quantity: String) {
        val newVariants = getCurrentState().localVariants.toMutableList()
        newVariants[variantIndex] = newVariants[variantIndex].copy(
            quantity = quantity,
            quantityError = inputValidator.validateNumberPositive(quantity.toInt())
        )
        postInput(AdminProductEditContract.Inputs.OnLocalVariantsChanged(newVariants))
    }

    private suspend fun InputScope.handleOnVariantPriceChanged(variantIndex: Int, price: String) {
        val newVariants = getCurrentState().localVariants.toMutableList()
        newVariants[variantIndex] = newVariants[variantIndex].copy(
            priceError = inputValidator.validateIsPriceDouble(price)
        )
        postInput(AdminProductEditContract.Inputs.OnLocalVariantsChanged(newVariants))
    }

    private suspend fun InputScope.handleOnLocalOptionsChanged(
        input: AdminProductEditContract.Inputs.OnLocalOptionsChanged
    ) {
        val state = getCurrentState()
        sideJob("handleOnLocalOptionsChanged") {
            val localVariants =
                handleGenerateLocalVariants(state.current.variants, input.options)
            postInput(AdminProductEditContract.Inputs.OnLocalVariantsChanged(localVariants))
            postInput(AdminProductEditContract.Inputs.SetLocalOptions(input.options))
        }
    }

    private fun countTotalInventory(variants: List<AdminProductEditContract.LocalVariant>): Int = variants
        .mapNotNull {
            try {
                it.quantity.toInt()
            } catch (e: NumberFormatException) {
                null
            }
        }
        .sum()

    private fun handleGenerateLocalVariants(
        currentVariants: List<AdminProductGetByIdQuery.Variant>,
        localOptions: List<AdminProductEditContract.LocalOption>
    ): List<AdminProductEditContract.LocalVariant> {
        val allOptions = localOptions.map {
            it.attrs.filter { attr -> attr.value.isNotBlank() }
        }
        val allVariants = mutableListOf<AdminProductEditContract.LocalVariant>()

        if (allOptions.isNotEmpty()) {
            val firstOption = allOptions.first().map { listOf(it) }
            val restOptions = allOptions.drop(1)
            val restVariants = restOptions.fold(firstOption) { acc, option ->
                acc.flatMap { a -> option.map { b -> a + b } }
            }
            allVariants.addAll(
                restVariants.map { newVariantAttrs ->
                    val existingVariant = currentVariants
                        .find { variant ->
                            variant.attrs.map { it.value } == newVariantAttrs.map { it.value }
                        }
                    if (existingVariant != null) {
                        // If the variant already exists, use its values
                        AdminProductEditContract.LocalVariant(
                            id = existingVariant.id,
                            attrs = existingVariant.attrs.map { it.value },
                            quantity = existingVariant.quantity.toString(),
                            enabled = true
                        )
                    } else if (currentVariants.isEmpty()) {
                        AdminProductEditContract.LocalVariant(
                            attrs = newVariantAttrs.map { it.value },
                            enabled = true,
                        )
                    } else {
                        // If the variant does not exist, create a new one with enabled set to false
                        AdminProductEditContract.LocalVariant(
                            attrs = newVariantAttrs.map { it.value },
                            enabled = false,
                        )
                    }
                }
            )
        }

        return allVariants
    }

    private suspend fun InputScope.handleOnDeleteOptionAttrClicked(optionIndex: Int, attrIndex: Int) {
        val state = getCurrentState()
        val newOptions = state.localOptions.toMutableList()
        val newAttrs = newOptions[optionIndex].attrs.toMutableList()
        newAttrs.removeAt(attrIndex)
        newOptions[optionIndex] = newOptions[optionIndex].copy(attrs = newAttrs)
        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newOptions))
    }

    private suspend fun InputScope.handleOnDeleteOptionClicked(optionIndex: Int) {
        val state = getCurrentState()
        val newOptions = state.localOptions.toMutableList()
        newOptions.removeAt(optionIndex)
        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newOptions))
    }

    private suspend fun InputScope.handleOnOptionNameChanged(optionIndex: Int, name: String) {
        val state = getCurrentState()
        val newOptions = state.localOptions.toMutableList()

        var nameError = inputValidator.validateText(name, 1)
        val allValues = state.localOptions.map { it.name }
        if (nameError == null && name in allValues) nameError = "Value must be unique"

        val anyAttrHasError = state.localOptions.none { it.attrs.any { it.error != null } }

        newOptions[optionIndex] = newOptions[optionIndex].copy(
            name = name,
            nameError = nameError,
            isDoneDisabled = nameError != null || anyAttrHasError
        )
        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newOptions))
    }

    private suspend fun InputScope.handleOnEditOptionClicked(optionIndex: Int) {
        val state = getCurrentState()
        val newOptions = state.localOptions.toMutableList()
        newOptions[optionIndex] = newOptions[optionIndex].copy(
            isEditing = true,
            isDoneDisabled = false,
            attrs = newOptions[optionIndex].attrs.toMutableList().apply {
                if (isEmpty() || last().value.isNotEmpty()) add(AdminProductEditContract.Attribute())
            }
        )
        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newOptions))
    }

    private suspend fun InputScope.handleOnOptionDoneClicked(optionIndex: Int) {
        val state = getCurrentState()
        val newOptions = state.localOptions.toMutableList()
        val option = newOptions[optionIndex]
            .copy(
                attrs = newOptions[optionIndex].attrs
                    .map { AdminProductEditContract.Attribute(it.value.trim()) }
                    .filter { it.value.isNotBlank() }
            )
        if (option.name.isNotBlank() && option.attrs.isNotEmpty()) {
            newOptions[optionIndex] = option.copy(isEditing = false)
            postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newOptions))
        } else noOp()
    }

    private suspend fun InputScope.handleOnOptionAttrValueChanged(optionIndex: Int, attrIndex: Int, value: String) {
        val state = getCurrentState()

        val newOptions = state.localOptions.toMutableList().apply {
            val isLastAttr = attrIndex == state.localOptions[optionIndex].attrs.size - 1
            val isValueNotEmpty = value.isNotEmpty()

            val option = this[optionIndex]
            val newAttrs = option.attrs.toMutableList()

            var valueError = inputValidator.validateText(value, 1)
            val allValues = option.attrs.map { it.value }
            if (valueError == null && value in allValues) valueError = "Value must be unique"

            newAttrs[attrIndex] = newAttrs[attrIndex].copy(
                value = value,
                error = valueError,
            )
            if (isLastAttr && isValueNotEmpty) newAttrs.add(AdminProductEditContract.Attribute())

            val anyAttrHasError = option.attrs.any { it.error != null }
            val allAttrEmpty = (option.attrs + AdminProductEditContract.Attribute(value))
                .all { it.value.isEmpty() }
            val nameError = option.nameError != null

            this[optionIndex] = option.copy(
                attrs = newAttrs,
                isDoneDisabled = valueError != null || anyAttrHasError || allAttrEmpty || nameError
            )
        }

        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newOptions))
    }

    private suspend fun InputScope.handleOnAddOptionsClick() {
        val variants = listOf(AdminProductEditContract.LocalOption())
        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(variants))
    }

    private suspend fun InputScope.handleOnAddAnotherOptionClick() {
        val state = getCurrentState()
        val newVariants = state.localOptions + AdminProductEditContract.LocalOption()
        postInput(AdminProductEditContract.Inputs.OnLocalOptionsChanged(newVariants))
    }

    private suspend fun InputScope.handelOnUserCreatorClick() {
        getCurrentState().original.creator.id.let { userId ->
            postEvent(AdminProductEditContract.Events.GoToUserDetails(userId))
        }
    }

    private suspend fun InputScope.handleAddMedia(mediaString: String) {
        sideJob("handleAddMedia") {
            postInput(AdminProductEditContract.Inputs.UploadMedia(mediaString))
        }
    }

    private suspend fun InputScope.handleDeleteMedia(index: Int) {
        val state = getCurrentState()
        sideJob("handleDeleteMedia") {
            state.current.let {
                val id = it.media[index].keyName
                postInput(AdminProductEditContract.Inputs.OnDeleteImageClick(id))
            }
        }
    }

    private suspend fun InputScope.handleSetUseGlobalTracking(useGlobalTracking: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(useGlobalTracking = useGlobalTracking))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetChargeTax(chargeTax: Boolean) {
        updateState {
            it.copy(current = it.current.copy(pricing = it.current.pricing.copy(chargeTax = chargeTax))).wasEdited()
        }
    }

    private suspend fun InputScope.handleOnClickCategory(categoryName: String) {
        updateState { state ->
            state.allCategories
                .find { category -> category.name == categoryName }
                ?.let { chosenCategory ->
                    val currentCategoryId = state.current.categoryId
                    val newCategoryId = if (chosenCategory.id == currentCategoryId) null else chosenCategory.id
                    state.copy(current = state.current.copy(categoryId = newCategoryId)).wasEdited()
                } ?: state
        }
    }

    private suspend fun InputScope.handleTagClick(name: String) {
        updateState { state ->
            state.allTags
                .find { tag -> tag.name == name }
                ?.let { chosenTag ->
                    val id = chosenTag.id
                    state.current.tags.let { userTags ->
                        val newTags = userTags.toMutableList()

                        if (id in userTags) newTags.remove(id) else newTags.add(id)

                        state.copy(current = state.current.copy(tags = newTags)).wasEdited()
                    }
                } ?: state
        }
    }

    private suspend fun InputScope.handleSetShippingPresetId(presetId: String?) {
        updateState {
            it.copy(current = it.current.copy(shipping = it.current.shipping.copy(presetId = presetId))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPresetCategory(category: GetCategoryByIdQuery.GetCategoryById?) {
        updateState {
            it.copy(
                presetCategory = category,
                current = it.current.copy(
                    shipping = it.current.shipping.copy(
                        presetId = category?.id,
                        weight = category?.shippingPreset?.weight ?: it.current.shipping.weight,
                        length = category?.shippingPreset?.length ?: it.current.shipping.length,
                        width = category?.shippingPreset?.width ?: it.current.shipping.width,
                        height = category?.shippingPreset?.height ?: it.current.shipping.height,
                        isPhysicalProduct = category?.shippingPreset?.isPhysicalProduct
                            ?: it.current.shipping.isPhysicalProduct,
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleGetPresetCategory(categoryId: String) {
        sideJob("handleGetPresetCategory") {
            categoryService.getById(categoryId).fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    postInput(AdminProductEditContract.Inputs.SetShippingPresetId(categoryId))
                    postInput(AdminProductEditContract.Inputs.SetPresetCategory(data.getCategoryById))
                },
            )
        }
    }

    private suspend fun InputScope.handlePresetClick(presetId: String) {
        val state = getCurrentState()
        sideJob("handlePresetClick") {
            if (state.current.shipping.presetId == presetId) {
                postInput(AdminProductEditContract.Inputs.SetPresetCategory(null))
            } else {
                postInput(AdminProductEditContract.Inputs.GetPresetCategory(presetId))
            }
        }
    }

    private suspend fun InputScope.handleSetStatusOfBackorder(backorderStatus: BackorderStatus) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(backorderStatus = backorderStatus))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetStatusOfStock(stockStatus: StockStatus) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(stockStatus = stockStatus))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetLowStockThreshold(lowStockThreshold: Int) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(lowStockThreshold = lowStockThreshold)),
                lowStockThresholdError = if (it.wasEdited) inputValidator.validateNumberPositive(lowStockThreshold) else null,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetRemainingStock(remainingStock: Int) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(remainingStock = remainingStock)),
                remainingStockError = if (it.wasEdited) inputValidator.validateNumberPositive(remainingStock) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetTrackQuantity(trackQuantity: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(trackQuantity = trackQuantity))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetSalePrice(price: String) {
        updateState {
            it.copy(
                current = it.current.copy(pricing = it.current.pricing.copy(salePrice = price.toDouble())),
                salePriceError = if (it.wasEdited) inputValidator.validateNumberPositive(price.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetRegularPrice(regularPrice: String) {
        updateState {
            it.copy(
                current = it.current.copy(pricing = it.current.pricing.copy(regularPrice = regularPrice.toDouble())),
                regularPriceError = if (it.wasEdited) inputValidator.validateNumberPositive(regularPrice.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetHeight(height: String) {
        updateState {
            it.copy(
                current = it.current.copy(shipping = it.current.shipping.copy(height = height)),
                heightError = if (it.wasEdited) inputValidator.validateNumberPositive(height.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetLength(length: String) {
        updateState {
            it.copy(
                current = it.current.copy(shipping = it.current.shipping.copy(length = length)),
                lengthError = if (it.wasEdited) inputValidator.validateNumberPositive(length.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetWeight(weight: String) {
        updateState {
            it.copy(
                current = it.current.copy(shipping = it.current.shipping.copy(weight = weight)),
                weightError = if (it.wasEdited) inputValidator.validateNumberPositive(weight.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetWidth(width: String) {
        updateState {
            it.copy(
                current = it.current.copy(shipping = it.current.shipping.copy(width = width)),
                widthError = if (it.wasEdited) inputValidator.validateNumberPositive(width.toInt()) else null
            ).wasEdited()

        }
    }

    private suspend fun InputScope.handleSetIsPhysicalProduct(isPhysicalProduct: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(shipping = it.current.shipping.copy(isPhysicalProduct = isPhysicalProduct))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetMedia(media: List<AdminProductGetByIdQuery.Medium>) {
        updateState { it.copy(current = it.current.copy(media = media)).wasEdited() }
    }

    private suspend fun InputScope.handleSetId(id: String) {
        updateState { it.copy(current = it.current.copy(id = id)).wasEdited() }
    }

    private suspend fun InputScope.handleSetUpdatedAt(updatedAt: String) {
        updateState { it.copy(current = it.current.copy(updatedAt = updatedAt)).wasEdited() }
    }

    private suspend fun InputScope.handleSetCreator(creator: AdminProductGetByIdQuery.Creator) {
        updateState { it.copy(current = it.current.copy(creator = creator)).wasEdited() }
    }

    private suspend fun InputScope.handleSetCreatedAt(createdAt: String) {
        updateState { it.copy(current = it.current.copy(createdAt = createdAt)).wasEdited() }
    }

    private suspend fun InputScope.handleInit(id: String) {
        sideJob("handleInit") {
            postInput(AdminProductEditContract.Inputs.SetLoading(isLoading = true))
            postInput(AdminProductEditContract.Inputs.GetProductById(id))
            postInput(AdminProductEditContract.Inputs.GetAllCategories)
            postInput(AdminProductEditContract.Inputs.GetAllTags)
            postInput(AdminProductEditContract.Inputs.SetLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleGetAllCategories() {
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminProductEditContract.Inputs.SetAllCategories(it.getAllCategoriesAsMinimal)) },
            )
        }
    }

    private suspend fun InputScope.handleGetAllTags() {
        sideJob("handleGetAllTags") {
            tagService.getTagsAllMinimal().fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminProductEditContract.Inputs.SetAllTags(it.getAllTagsAsMinimal)) },
            )
        }
    }

    private suspend fun InputScope.handleGetProductById(id: String) {
        sideJob("handleGetProduct") {
            productService.getProductById(id).fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                {
                    println("getProductById: $it")
                    with(it.getProductById) {
                        val createdAt = millisToTime(createdAt.toLong())
                        val updatedAt = millisToTime(updatedAt.toLong())
                        val product = copy(
                            name = name,
                            description = description,
                            postStatus = postStatus,
                            media = media,
                            categoryId = categoryId,
                            tags = tags,
                            isFeatured = isFeatured,
                            allowReviews = allowReviews,
                            creator = creator.copy(
                                id = creator.id,
                                name = creator.name,
                            ),
                            createdAt = createdAt,
                            updatedAt = updatedAt,
                            pricing = pricing.copy(
                                regularPrice = pricing.regularPrice,
                                salePrice = pricing.salePrice,
                                chargeTax = pricing.chargeTax,
                            ),
                            variants = variants
                        )

                        val localVariants = variants.map {
                            AdminProductEditContract.LocalVariant(
                                id = it.id,
                                attrs = it.attrs.map { attr -> attr.value },
                                quantity = it.quantity.toString(),
                                enabled = true
                            )
                        }
                        postInput(AdminProductEditContract.Inputs.SetLocalVariants(localVariants))

                        val totalInventory = getTotalInventory(localVariants)
                        postInput(AdminProductEditContract.Inputs.SetTotalInventory(totalInventory))

                        val localOptions = getLocalOptions(variants)
                        postInput(AdminProductEditContract.Inputs.SetLocalOptions(localOptions))

                        postInput(AdminProductEditContract.Inputs.SetOriginal(product))
                        postInput(AdminProductEditContract.Inputs.SetCurrent(product))
                        postInput(AdminProductEditContract.Inputs.SetShowAddOptions(product.variants.isEmpty()))
                        postInput(
                            AdminProductEditContract.Inputs.SetShowAddAnotherOption(product.variants.isNotEmpty())
                        )
                    }
                },
            )
        }
    }

    private fun priceWithDecimals(price: Double): String {
        val priceString = if (price.toString().contains(".")) {
            when {
                price.toString().endsWith(".") -> price.toString() + "00"
                price.toString().split(".")[1].length == 1 -> price.toString() + "0"
                else -> price.toString()
            }
        } else {
            "$price.00"
        }
        return priceString
    }

    private fun getTotalInventory(localVariants: List<AdminProductEditContract.LocalVariant>) =
        localVariants.sumOf { it.quantity.toInt() }

    private fun getLocalOptions(variants: List<AdminProductGetByIdQuery.Variant>): List<AdminProductEditContract.LocalOption> {
        val localOptions = mutableListOf<AdminProductEditContract.LocalOption>()

        variants
            .flatMap { it.attrs }
            .forEach { attr ->
                val key = attr.key
                if (key !in localOptions.map { it.name }) {
                    localOptions.add(
                        AdminProductEditContract.LocalOption(
                            name = key,
                            isEditing = false,
                            attrs = listOf(AdminProductEditContract.Attribute(attr.value))
                        )
                    )
                } else {
                    localOptions
                        .firstOrNull { it.name == key }
                        ?.let { option ->
                            val index = localOptions.indexOf(option)
                            val newAttrs = option.attrs.toMutableSet()
                            newAttrs.add(AdminProductEditContract.Attribute(attr.value))
                            localOptions[index] = option.copy(attrs = newAttrs.toList())
                        }
                }
            }
        return localOptions
    }

    private suspend fun InputScope.handleDeleteImage(imageId: String) {
        val state = getCurrentState()
        state.current.let { current ->
            sideJob("handleDeleteImage") {
                postInput(AdminProductEditContract.Inputs.SetImagesLoading(isImagesLoading = true))
                productService.deleteImage(current.id, imageId).fold(
                    { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                    {
                        val media = state.current.media.filter { it.keyName != imageId }
                        val updatedProduct = state.current.copy(media = media)
                        postInput(AdminProductEditContract.Inputs.SetOriginal(updatedProduct))
                        postInput(AdminProductEditContract.Inputs.SetCurrent(updatedProduct))
                    },
                )
                postInput(AdminProductEditContract.Inputs.SetImagesLoading(isImagesLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleUploadImage(imageString: String) {
        val current = getCurrentState().current
        sideJob("handleSaveDetailsUploadImage") {
            postInput(AdminProductEditContract.Inputs.SetImageDropError(error = null))
            postInput(AdminProductEditContract.Inputs.SetImagesLoading(isImagesLoading = true))

            productService.uploadImage(
                productId = current.id,
                blobInput = BlobInput(imageString),
                mediaType = MediaType.Image
            ).fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val media = data.uploadProductMedia.media.map {
                        AdminProductGetByIdQuery.Medium(
                            keyName = it.keyName,
                            url = it.url,
                            type = it.type,
                            alt = it.alt,
                        )
                    }
                    val updatedProduct = current.copy(media = media)
                    postInput(AdminProductEditContract.Inputs.SetOriginal(updatedProduct))
                    postInput(AdminProductEditContract.Inputs.SetCurrent(updatedProduct))
                },
            )
            postInput(AdminProductEditContract.Inputs.SetImagesLoading(isImagesLoading = false))
        }
    }

    private suspend fun InputScope.handleUpdateProduct() {
        val current = getCurrentState().current
        val original = getCurrentState().original
        sideJob("handleUpdateProduct") {
            productService.updateProduct(
                id = original.id,
                name = if (current.name != original.name) current.name else null,
                description = if (current.description != original.description)
                    current.description else null,
                isFeatured = if (current.isFeatured != original.isFeatured) {
                    current.isFeatured
                } else null,
                allowReviews = if (current.allowReviews != original.allowReviews) {
                    current.allowReviews
                } else null,
                categoryId = if (current.categoryId != original.categoryId) current.categoryId else null,
                tags = if (current.tags.map { it } != original.tags.map { it }) {
                    current.tags.map { it }
                } else null,
                postStatus = if (current.postStatus != original.postStatus) {
                    current.postStatus
                } else null,
                backorderStatus = if (current.inventory.backorderStatus != original.inventory.backorderStatus) {
                    current.inventory.backorderStatus
                } else null,
                lowStockThreshold = if (current.inventory.lowStockThreshold != original.inventory.lowStockThreshold) {
                    current.inventory.lowStockThreshold
                } else null,
                remainingStock = if (current.inventory.remainingStock != original.inventory.remainingStock) {
                    current.inventory.remainingStock
                } else null,
                stockStatus = if (current.inventory.stockStatus != original.inventory.stockStatus) {
                    current.inventory.stockStatus
                } else null,
                trackQuantity = if (current.inventory.trackQuantity != original.inventory.trackQuantity) {
                    current.inventory.trackQuantity
                } else null,
                traits = if (current.traits != original.traits) current.traits else null,
                salePrice = if (current.pricing.salePrice != original.pricing.salePrice) current.pricing.salePrice else null,
                regularPrice = if (current.pricing.regularPrice != original.pricing.regularPrice) {
                    current.pricing.regularPrice
                } else null,
                chargeTax = if (current.pricing.chargeTax != original.pricing.chargeTax) {
                    current.pricing.chargeTax
                } else null,
                presetId = if (current.shipping.presetId != original.shipping.presetId) current.shipping.presetId else null,
                isPhysicalProduct = if (current.shipping.isPhysicalProduct != original.shipping.isPhysicalProduct) {
                    current.shipping.isPhysicalProduct
                } else null,
                height = if (current.shipping.height != original.shipping.height) current.shipping.height else null,
                length = if (current.shipping.length != original.shipping.length) current.shipping.length else null,
                weight = if (current.shipping.weight != original.shipping.weight) current.shipping.weight else null,
                width = if (current.shipping.width != original.shipping.width) current.shipping.width else null,
                variants = if (current.variants.map { it } != original.variants.map { it }) {
                    current.variants.map { variant ->
                        ProductUpdateVariantInput(
                            id = if (variant.id.isEmpty()) Optional.absent() else Optional.present(variant.id),
                            attrs = variant.attrs.map { attr ->
                                VariantItemInput(
                                    key = attr.key,
                                    value = attr.value
                                )
                            },
                            media = variant.media.map { medium ->
                                MediaInput(
                                    keyName = medium.keyName,
                                    url = medium.url,
                                    alt = medium.alt,
                                    type = medium.type,
                                )
                            },
                            quantity = variant.quantity,
                        )
                    }
                } else null,
            ).fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val localVariants = data.updateProduct.variants.map {
                        AdminProductEditContract.LocalVariant(
                            id = it.id,
                            attrs = it.attrs.map { attr -> attr.value },
                            quantity = it.quantity.toString(),
                            enabled = true
                        )
                    }
                    postInput(AdminProductEditContract.Inputs.SetLocalVariants(localVariants))

                    val totalInventory = getTotalInventory(localVariants)
                    postInput(AdminProductEditContract.Inputs.SetTotalInventory(totalInventory))

                    val variants = data.updateProduct.variants.map {
                        AdminProductGetByIdQuery.Variant(
                            id = it.id,
                            attrs = it.attrs.map {
                                AdminProductGetByIdQuery.Attr(
                                    key = it.key,
                                    value = it.value
                                )
                            },
                            media = it.media.map {
                                AdminProductGetByIdQuery.Medium1(
                                    keyName = it.keyName,
                                    url = it.url,
                                    alt = it.alt,
                                    type = it.type,
                                )
                            },
                            quantity = it.quantity,
                        )
                    }
                    val localOptions = getLocalOptions(variants)
                    postInput(AdminProductEditContract.Inputs.SetLocalOptions(localOptions))

                    postInput(
                        AdminProductEditContract.Inputs.SetOriginal(
                            product = AdminProductGetByIdQuery.GetProductById(
                                id = data.updateProduct.id,
                                name = data.updateProduct.name,
                                description = data.updateProduct.description,
                                sold = data.updateProduct.sold,
                                media = current.media, // Updating this elsewhere
                                postStatus = data.updateProduct.postStatus,
                                categoryId = data.updateProduct.categoryId,
                                tags = data.updateProduct.tags,
                                isFeatured = data.updateProduct.isFeatured,
                                allowReviews = data.updateProduct.allowReviews,
                                vendor = data.updateProduct.vendor,
                                traits = data.updateProduct.traits,
                                pricing = AdminProductGetByIdQuery.Pricing(
                                    salePrice = data.updateProduct.pricing.salePrice,
                                    regularPrice = data.updateProduct.pricing.regularPrice,
                                    chargeTax = data.updateProduct.pricing.chargeTax,
                                ),
                                inventory = AdminProductGetByIdQuery.Inventory(
                                    trackQuantity = data.updateProduct.inventory.trackQuantity,
                                    useGlobalTracking = data.updateProduct.inventory.useGlobalTracking,
                                    backorderStatus = data.updateProduct.inventory.backorderStatus,
                                    lowStockThreshold = data.updateProduct.inventory.lowStockThreshold,
                                    remainingStock = data.updateProduct.inventory.remainingStock,
                                    stockStatus = data.updateProduct.inventory.stockStatus,
                                ),
                                shipping = AdminProductGetByIdQuery.Shipping(
                                    presetId = data.updateProduct.shipping.presetId,
                                    isPhysicalProduct = data.updateProduct.shipping.isPhysicalProduct,
                                    height = data.updateProduct.shipping.height,
                                    length = data.updateProduct.shipping.length,
                                    weight = data.updateProduct.shipping.weight,
                                    width = data.updateProduct.shipping.width,
                                ),
                                variants = data.updateProduct.variants.map {
                                    AdminProductGetByIdQuery.Variant(
                                        id = it.id,
                                        attrs = it.attrs.map {
                                            AdminProductGetByIdQuery.Attr(
                                                key = it.key,
                                                value = it.value
                                            )
                                        },
                                        media = it.media.map {
                                            AdminProductGetByIdQuery.Medium1(
                                                keyName = it.keyName,
                                                url = it.url,
                                                alt = it.alt,
                                                type = it.type,
                                            )
                                        },
                                        quantity = it.quantity,
                                    )
                                },
                                creator = original.creator,
                                reviews = original.reviews,
                                totalInWishlist = original.totalInWishlist,
                                createdAt = original.createdAt,
                                updatedAt = original.updatedAt,
                            ),
                        )
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleDelete() {
        val id = getCurrentState().original.id
        sideJob("handleDeleteProduct") {
            productService.delete(id).fold(
                { postEvent(AdminProductEditContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminProductEditContract.Events.GoBackToProducts) },
            )
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        updateState {
            it.copy(
                current = it.current.copy(name = name),
                titleError = inputValidator.validateText(name),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSave() {
        with(getCurrentState()) {
            val isTitleError = titleError != null
            val isDescriptionsError = descriptionError != null
            val isLowStockThresholdError = lowStockThresholdError != null
            val isRemainingStockError = remainingStockError != null
            val isPriceError = salePriceError != null
            val isRegularPriceError = regularPriceError != null
            val isWeightError = weightError != null
            val isLengthError = lengthError != null
            val isWidthError = widthError != null
            val isHeightError = heightError != null
            val isNoError = !isTitleError && !isDescriptionsError &&
                !isLowStockThresholdError && !isRemainingStockError && !isPriceError &&
                !isRegularPriceError && !isWeightError && !isLengthError &&
                !isWidthError && !isHeightError

            if (isNoError) {
                handleUpdateProduct()
            } else noOp()
        }
    }
}

private fun AdminProductEditContract.State.wasEdited(): AdminProductEditContract.State =
    copy(wasEdited = current != original)
