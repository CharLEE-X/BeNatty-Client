package feature.admin.product.page

import com.apollographql.apollo3.api.Optional
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
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
import data.type.InventoryInput
import data.type.MediaType
import data.type.PricingCreateInput
import data.type.ProductCreateInput
import data.type.ShippingInput
import data.type.StockStatus
import data.type.VariantCreateInput
import data.type.VariantItemInput
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>

internal class AdminProductPageInputHandler :
    KoinComponent,
    InputHandler<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State> {

    private val productService by inject<ProductService>()
    private val inputValidator by inject<InputValidator>()
    private val categoryService: CategoryService by inject()
    private val tagService: TagService by inject()

    override suspend fun InputScope.handleInput(input: AdminProductPageContract.Inputs) = when (input) {
        is AdminProductPageContract.Inputs.Init -> handleInit(input.productId)
        is AdminProductPageContract.Inputs.UploadMedia -> handleUploadImage(input.mediaString)
        is AdminProductPageContract.Inputs.AddMedia -> handleAddMedia(input.mediaString)
        is AdminProductPageContract.Inputs.DeleteMedia -> handleDeleteMedia(input.index)

        is AdminProductPageContract.Inputs.GetProductById -> handleGetProductById(input.id)
        AdminProductPageContract.Inputs.GetAllCategories -> handleGetAllCategories()
        AdminProductPageContract.Inputs.GetAllTags -> handleGetAllTags()
        is AdminProductPageContract.Inputs.GetPresetCategory -> handleGetPresetCategory(input.categoryId)

        AdminProductPageContract.Inputs.OnDeleteClick -> handleDelete()
        AdminProductPageContract.Inputs.OnSaveClick -> handleSave()
        AdminProductPageContract.Inputs.OnDiscardClick -> updateState { it.copy(current = it.original).wasEdited() }
        is AdminProductPageContract.Inputs.OnCategorySelected -> handleOnClickCategory(input.categoryName)
        is AdminProductPageContract.Inputs.OnTagSelected -> handleTagClick(input.tagName)
        is AdminProductPageContract.Inputs.OnDeleteImageClick -> handleDeleteImage(input.imageId)
        is AdminProductPageContract.Inputs.OnUserCreatorClick -> handelOnUserCreatorClick()
        AdminProductPageContract.Inputs.OnCreateTagClick -> postEvent(AdminProductPageContract.Events.GoToCreateTag)

        is AdminProductPageContract.Inputs.OnPresetSelected -> handlePresetClick(input.preset)
        AdminProductPageContract.Inputs.OnCreateCategoryClick ->
            postEvent(AdminProductPageContract.Events.GoToCreateCategory)

        AdminProductPageContract.Inputs.OnCreateVariantClick -> updateState { it.copy(showAddVariantFields = true) }

        is AdminProductPageContract.Inputs.SetAllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminProductPageContract.Inputs.SetAllTags -> updateState { it.copy(allTags = input.tags) }
        is AdminProductPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductPageContract.Inputs.SetImagesLoading ->
            updateState { it.copy(isImagesLoading = input.isImagesLoading) }

        is AdminProductPageContract.Inputs.SetOriginalProduct ->
            updateState { it.copy(original = input.product).wasEdited() }

        is AdminProductPageContract.Inputs.SetCurrentProduct ->
            updateState { it.copy(current = input.product).wasEdited() }

        is AdminProductPageContract.Inputs.SetStateOfScreen -> updateState { it.copy(screenState = input.screenState) }
        is AdminProductPageContract.Inputs.SetId -> handleSetId(input.id)
        is AdminProductPageContract.Inputs.SetTitle -> handleSetTitle(input.title)

        is AdminProductPageContract.Inputs.SetIsFeatured ->
            updateState { it.copy(current = it.current.copy(isFeatured = input.isFeatured)).wasEdited() }

        is AdminProductPageContract.Inputs.SetAllowReviews ->
            updateState { it.copy(current = it.current.copy(allowReviews = input.allowReviews)).wasEdited() }

        is AdminProductPageContract.Inputs.SetStatusOfPost ->
            updateState { it.copy(current = it.current.copy(postStatus = input.postStatus)).wasEdited() }

        is AdminProductPageContract.Inputs.SetDescription ->
            updateState { it.copy(current = it.current.copy(description = input.description)).wasEdited() }

        is AdminProductPageContract.Inputs.SetCategoryId ->
            updateState { it.copy(current = it.current.copy(categoryId = input.categoryId)).wasEdited() }

        is AdminProductPageContract.Inputs.SetCreatedAt -> handleSetCreatedAt(input.createdAt)
        is AdminProductPageContract.Inputs.SetCreator -> handleSetCreator(input.creator)
        is AdminProductPageContract.Inputs.SetUpdatedAt -> handleSetUpdatedAt(input.updatedAt)
        is AdminProductPageContract.Inputs.SetStatusOfBackorder -> handleSetStatusOfBackorder(input.backorderStatus)
        is AdminProductPageContract.Inputs.SetMedia -> handleSetMedia(input.media)
        is AdminProductPageContract.Inputs.SetLowStockThreshold -> handleSetLowStockThreshold(input.lowStockThreshold)
        is AdminProductPageContract.Inputs.SetPrice -> handleSetPrice(input.price)
        is AdminProductPageContract.Inputs.SetRegularPrice -> handleSetRegularPrice(input.regularPrice)
        is AdminProductPageContract.Inputs.SetRemainingStock -> handleSetRemainingStock(input.remainingStock)
        is AdminProductPageContract.Inputs.SetIsPhysicalProduct -> handleSetIsPhysicalProduct(input.isPhysicalProduct)
        is AdminProductPageContract.Inputs.SetStatusOfStock -> handleSetStatusOfStock(input.stockStatus)
        is AdminProductPageContract.Inputs.SetTrackQuantity -> handleSetTrackQuantity(input.trackQuantity)
        is AdminProductPageContract.Inputs.SetWeight -> handleSetWeight(input.weight)
        is AdminProductPageContract.Inputs.SetLength -> handleSetLength(input.length)
        is AdminProductPageContract.Inputs.SetWidth -> handleSetWidth(input.width)
        is AdminProductPageContract.Inputs.SetHeight -> handleSetHeight(input.height)
        is AdminProductPageContract.Inputs.SetPresetCategory -> handleSetPresetCategory(input.category)
        is AdminProductPageContract.Inputs.SetShippingPresetId -> handleSetShippingPresetId(input.presetId)
        is AdminProductPageContract.Inputs.SetImageDropError -> updateState { it.copy(imageDropError = input.error) }
        is AdminProductPageContract.Inputs.SetChargeTax -> handleSetChargeTax(input.chargeTax)
        is AdminProductPageContract.Inputs.SetUseGlobalTracking -> handleSetUseGlobalTracking(input.useGlobalTracking)
        is AdminProductPageContract.Inputs.SetLocalMedia -> updateState { it.copy(localMedia = input.media) }
    }

    private suspend fun InputScope.handelOnUserCreatorClick() {
        getCurrentState().original.creator.id.let { userId ->
            postEvent(AdminProductPageContract.Events.GoToUserDetails(userId))
        }
    }

    private suspend fun InputScope.handleAddMedia(mediaString: String) {
        val state = getCurrentState()
        sideJob("handleAddMedia") {
            when (state.screenState) {
                AdminProductPageContract.ScreenState.Existing -> {
                    postInput(AdminProductPageContract.Inputs.UploadMedia(mediaString))
                }

                AdminProductPageContract.ScreenState.New -> {
                    val currentLocalMedia = state.localMedia
                    val newMedia = AdminProductGetByIdQuery.Medium(
                        id = currentLocalMedia.size.toString(),
                        url = mediaString,
                        mediaType = MediaType.Image,
                        altText = "",
                        updatedAt = "0",
                    )
                    postInput(AdminProductPageContract.Inputs.SetLocalMedia(currentLocalMedia + newMedia))
                }
            }
        }
    }

    private suspend fun InputScope.handleDeleteMedia(index: Int) {
        val state = getCurrentState()
        sideJob("handleDeleteMedia") {
            when (state.screenState) {
                AdminProductPageContract.ScreenState.Existing -> {
                    state.current.let {
                        val id = it.media[index].id
                        postInput(AdminProductPageContract.Inputs.OnDeleteImageClick(id))
                    }
                }

                AdminProductPageContract.ScreenState.New -> {
                    val newMedia = state.localMedia.toMutableList().drop(index)
                    postInput(AdminProductPageContract.Inputs.SetLocalMedia(newMedia))
                }
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
            categoryService.getById(categoryId).collect { result ->
                result.fold(
                    { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        postInput(AdminProductPageContract.Inputs.SetShippingPresetId(categoryId))
                        postInput(AdminProductPageContract.Inputs.SetPresetCategory(data.getCategoryById))
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handlePresetClick(presetId: String) {
        val state = getCurrentState()
        sideJob("handlePresetClick") {
            if (state.current.shipping.presetId == presetId) {
                postInput(AdminProductPageContract.Inputs.SetPresetCategory(null))
            } else {
                postInput(AdminProductPageContract.Inputs.GetPresetCategory(presetId))
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

    private suspend fun InputScope.handleSetPrice(price: String) {
        updateState {
            it.copy(
                current = it.current.copy(pricing = it.current.pricing.copy(price = price)),
                priceError = if (it.wasEdited) inputValidator.validateNumberPositive(price.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetRegularPrice(regularPrice: String) {
        updateState {
            it.copy(
                current = it.current.copy(pricing = it.current.pricing.copy(regularPrice = regularPrice)),
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

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminProductPageContract.Inputs.SetStateOfScreen(AdminProductPageContract.ScreenState.New))
            } else {
                postInput(AdminProductPageContract.Inputs.SetLoading(isLoading = true))
                postInput(AdminProductPageContract.Inputs.GetProductById(id))
                postInput(AdminProductPageContract.Inputs.SetStateOfScreen(AdminProductPageContract.ScreenState.Existing))
                postInput(AdminProductPageContract.Inputs.SetLoading(isLoading = false))
            }
            postInput(AdminProductPageContract.Inputs.GetAllCategories)
            postInput(AdminProductPageContract.Inputs.GetAllTags)
        }
    }

    private suspend fun InputScope.handleGetAllCategories() {
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminProductPageContract.Inputs.SetAllCategories(it.getAllCategoriesAsMinimal)) },
            )
        }
    }

    private suspend fun InputScope.handleGetAllTags() {
        sideJob("handleGetAllTags") {
            tagService.getTagsAllMinimal().fold(
                { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                { postInput(AdminProductPageContract.Inputs.SetAllTags(it.getAllTagsAsMinimal)) },
            )
        }
    }

    private suspend fun InputScope.handleGetProductById(id: String) {
        sideJob("handleGetProduct") {
            productService.getById(id).collect { result ->
                result.fold(
                    { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                    {
                        with(it.getProductById) {
                            val createdAt = millisToTime(createdAt.toLong())
                            val updatedAt = millisToTime(updatedAt.toLong())
                            val originalProduct = copy(
                                title = title,
                                description = description,
                                postStatus = postStatus,
                                media = media,
                                categoryId = categoryId,
                                tags = tags,
                                isFeatured = isFeatured,
                                allowReviews = allowReviews,
                                creator = creator.copy(
                                    id = creator.id,
                                    firstName = creator.firstName,
                                    lastName = creator.lastName,
                                ),
                                createdAt = createdAt,
                                updatedAt = updatedAt,
                                pricing = pricing.copy(
                                    price = pricing.price,
                                    regularPrice = pricing.regularPrice,
                                    chargeTax = pricing.chargeTax,
                                ),
                            )
                            postInput(AdminProductPageContract.Inputs.SetOriginalProduct(originalProduct))
                            postInput(AdminProductPageContract.Inputs.SetCurrentProduct(originalProduct))
                        }
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleCreateNewProduct() {
        val state = getCurrentState()
        state.current.let {
            val input = ProductCreateInput(
                title = it.title,
                description = it.description,
                postStatus = it.postStatus,
                media = it.media.map { media -> media.id },
                categoryId = Optional.present(it.categoryId),
                tags = it.tags,
                isFeatured = it.isFeatured,
                allowReviews = it.allowReviews,
                pricing = PricingCreateInput(
                    price = it.pricing.price ?: "",
                    regularPrice = it.pricing.regularPrice ?: "",
                    chargeTax = it.pricing.chargeTax,
                ),
                inventory = InventoryInput(
                    trackQuantity = it.inventory.trackQuantity,
                    useGlobalTracking = it.inventory.useGlobalTracking,
                    remainingStock = it.inventory.remainingStock,
                    stockStatus = it.inventory.stockStatus,
                    backorderStatus = it.inventory.backorderStatus,
                    lowStockThreshold = it.inventory.lowStockThreshold,
                ),
                shipping = ShippingInput(
                    isPhysicalProduct = it.shipping.isPhysicalProduct,
                    weight = Optional.present(it.shipping.weight),
                    length = Optional.present(it.shipping.length),
                    width = Optional.present(it.shipping.width),
                    height = Optional.present(it.shipping.height),
                ),
                variantCreateInputs = it.variants.map {
                    VariantCreateInput(
                        attrs = it.attrs.map {
                            VariantItemInput(
                                key = it.key,
                                value = it.value,
                            )
                        },
                        media = it.media,
                        quantity = it.quantity,
                        price = it.price,
                    )
                }
            )

            sideJob("handleCreateNewProduct") {
                postInput(AdminProductPageContract.Inputs.SetLoading(isLoading = true))
                productService.create(input).fold(
                    { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        state.localMedia.forEach { media ->
                            postInput(AdminProductPageContract.Inputs.UploadMedia(media.url))
                        }

                        postEvent(AdminProductPageContract.Events.GoToProduct(data.createProduct.product.id))
                    },
                )
                postInput(AdminProductPageContract.Inputs.SetLoading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleDeleteImage(imageId: String) {
        val state = getCurrentState()
        state.current.let { current ->
            sideJob("handleDeleteImage") {
                postInput(AdminProductPageContract.Inputs.SetImagesLoading(isImagesLoading = true))
                productService.deleteImage(current.id, imageId).fold(
                    { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                    {
                        val media = state.current.media.filter { it.id != imageId }
                        val updatedProduct = state.current.copy(media = media)
                        postInput(AdminProductPageContract.Inputs.SetOriginalProduct(updatedProduct))
                        postInput(AdminProductPageContract.Inputs.SetCurrentProduct(updatedProduct))
                    },
                )
                postInput(AdminProductPageContract.Inputs.SetImagesLoading(isImagesLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleUploadImage(imageString: String) {
        val state = getCurrentState()
        state.current.let { current ->
            sideJob("handleSaveDetailsUploadImage") {
                postInput(AdminProductPageContract.Inputs.SetImageDropError(error = null))
                postInput(AdminProductPageContract.Inputs.SetImagesLoading(isImagesLoading = true))
                val mediaType = MediaType.Image
                productService.uploadImage(current.id, BlobInput(imageString), mediaType).fold(
                    { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        val media = data.uploadProductMedia.media.map {
                            AdminProductGetByIdQuery.Medium(
                                id = it.id,
                                url = it.url,
                                mediaType = it.mediaType,
                                altText = it.altText,
                                updatedAt = it.updatedAt,
                            )
                        }
                        val updatedProduct = current.copy(media = media)
                        postInput(AdminProductPageContract.Inputs.SetOriginalProduct(updatedProduct))
                        postInput(AdminProductPageContract.Inputs.SetCurrentProduct(updatedProduct))
                    },
                )
                postInput(AdminProductPageContract.Inputs.SetImagesLoading(isImagesLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleUpdateProduct() {
        val state = getCurrentState()
        state.current.let { current ->
            state.original.let { original ->
                sideJob("handleUpdateDetails") {
                    productService.update(
                        id = current.id,
                        name = if (current.title != state.original.title) current.title else null,
                        description = if (current.description != state.original.description)
                            current.description else null,
                        isFeatured = if (current.isFeatured != state.original.isFeatured) {
                            current.isFeatured
                        } else null,
                        allowReviews = if (current.allowReviews != state.original.allowReviews) {
                            current.allowReviews
                        } else null,
                        categoryId = if (current.categoryId != state.original.categoryId) current.categoryId else null,
                        tags = if (current.tags.map { it } != state.original.tags.map { it }) {
                            current.tags.map { it }
                        } else null,
                        postStatus = if (current.postStatus != state.original.postStatus) {
                            current.postStatus
                        } else null,
                        backorderStatus = if (current.inventory.backorderStatus != state.original.inventory.backorderStatus) {
                            current.inventory.backorderStatus
                        } else null,
                        lowStockThreshold = if (current.inventory.lowStockThreshold != state.original.inventory.lowStockThreshold) {
                            current.inventory.lowStockThreshold
                        } else null,
                        remainingStock = if (current.inventory.remainingStock != state.original.inventory.remainingStock) {
                            current.inventory.remainingStock
                        } else null,
                        stockStatus = if (current.inventory.stockStatus != state.original.inventory.stockStatus) {
                            current.inventory.stockStatus
                        } else null,
                        trackQuantity = if (current.inventory.trackQuantity != state.original.inventory.trackQuantity) {
                            current.inventory.trackQuantity
                        } else null,
                        price = if (current.pricing.price != state.original.pricing.price) current.pricing.price else null,
                        regularPrice = if (current.pricing.regularPrice != state.original.pricing.regularPrice) {
                            current.pricing.regularPrice
                        } else null,
                        chargeTax = if (current.pricing.chargeTax != state.original.pricing.chargeTax) {
                            current.pricing.chargeTax
                        } else null,
                        presetId = if (current.shipping.presetId != state.original.shipping.presetId) current.shipping.presetId else null,
                        isPhysicalProduct = if (current.shipping.isPhysicalProduct != state.original.shipping.isPhysicalProduct) {
                            current.shipping.isPhysicalProduct
                        } else null,
                        height = if (current.shipping.height != state.original.shipping.height) current.shipping.height else null,
                        length = if (current.shipping.length != state.original.shipping.length) current.shipping.length else null,
                        weight = if (current.shipping.weight != state.original.shipping.weight) current.shipping.weight else null,
                        width = if (current.shipping.width != state.original.shipping.width) current.shipping.width else null,
                    ).fold(
                        { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                        { data ->
                            postInput(
                                AdminProductPageContract.Inputs.SetOriginalProduct(
                                    product = AdminProductGetByIdQuery.GetProductById(
                                        id = data.updateProduct.id,
                                        title = data.updateProduct.title,
                                        description = data.updateProduct.description,
                                        sold = data.updateProduct.sold,
                                        media = current.media, // Updating this elsewhere
                                        postStatus = data.updateProduct.postStatus,
                                        categoryId = data.updateProduct.categoryId,
                                        tags = data.updateProduct.tags,
                                        isFeatured = data.updateProduct.isFeatured,
                                        allowReviews = data.updateProduct.allowReviews,
                                        pricing = AdminProductGetByIdQuery.Pricing(
                                            price = data.updateProduct.pricing.price,
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
                                                media = it.media,
                                                price = it.price,
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
        }
    }

    private suspend fun InputScope.handleDelete() {
        getCurrentState().current.let {
            sideJob("handleDeleteUser") {
                productService.delete(it.id).fold(
                    { postEvent(AdminProductPageContract.Events.OnError(it.mapToUiMessage())) },
                    { postEvent(AdminProductPageContract.Events.GoBackToProducts) },
                )
            }
        }
    }

    private suspend fun InputScope.handleSetTitle(title: String) {
        updateState {
            val isValidated = inputValidator.validateText(title)
            it.copy(
                current = it.current.copy(title = title),
                titleError = if (!it.wasEdited) null else isValidated,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSave() {
        with(getCurrentState()) {
            val isTitleError = titleError != null
            val isDescriptionsError = descriptionError != null
            val isLowStockThresholdError = lowStockThresholdError != null
            val isRemainingStockError = remainingStockError != null
            val isPriceError = priceError != null
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
                when (screenState) {
                    AdminProductPageContract.ScreenState.New -> handleCreateNewProduct()
                    AdminProductPageContract.ScreenState.Existing -> handleUpdateProduct()
                }
            } else noOp()
        }
    }
}

private fun AdminProductPageContract.State.wasEdited(): AdminProductPageContract.State {
    println("wasEdited: ${current != original}")
    return copy(wasEdited = current != original)
}
