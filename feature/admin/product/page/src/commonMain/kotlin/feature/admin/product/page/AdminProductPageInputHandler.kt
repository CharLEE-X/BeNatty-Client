package feature.admin.product.page

import com.apollographql.apollo3.api.Optional
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.util.millisToTime
import data.AdminProductGetByIdQuery
import data.GetCategoryByIdQuery
import data.service.CategoryService
import data.service.ProductService
import data.service.TagService
import data.type.BackorderStatus
import data.type.InventoryInput
import data.type.MediaType
import data.type.PostStatus
import data.type.PricingCreateInput
import data.type.ProductCreateInput
import data.type.ShippingInput
import data.type.StockStatus
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
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
        is AdminProductPageContract.Inputs.OnUserCreatorClick -> {
            val userId = getCurrentState().original.creator.id
            postEvent(AdminProductPageContract.Events.GoToUserDetails(userId))
        }

        AdminProductPageContract.Inputs.OnCreateTagClick ->
            postEvent(AdminProductPageContract.Events.GoToCreateTag)


        is AdminProductPageContract.Inputs.OnPresetSelected -> handlePresetClick(input.preset)
        AdminProductPageContract.Inputs.OnCreateCategoryClick ->
            postEvent(AdminProductPageContract.Events.GoToCreateCategory)

        is AdminProductPageContract.Inputs.Set.AllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminProductPageContract.Inputs.Set.AllTags -> updateState { it.copy(allTags = input.tags) }
        is AdminProductPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductPageContract.Inputs.Set.ImagesLoading ->
            updateState { it.copy(isImagesLoading = input.isImagesLoading) }

        is AdminProductPageContract.Inputs.Set.OriginalProduct ->
            updateState { it.copy(original = input.product).wasEdited() }

        is AdminProductPageContract.Inputs.Set.CurrentProduct ->
            updateState { it.copy(current = input.product).wasEdited() }

        is AdminProductPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }
        is AdminProductPageContract.Inputs.Set.Id -> handleSetId(input.id)
        is AdminProductPageContract.Inputs.Set.Title -> handleSetTitle(input.title)
        is AdminProductPageContract.Inputs.Set.TitleShake -> updateState { it.copy(titleShake = input.shake) }

        is AdminProductPageContract.Inputs.Set.IsFeatured -> handleSetIsFeatured(input.isFeatured)
        is AdminProductPageContract.Inputs.Set.AllowReviews -> handleSetAllowReviews(input.allowReviews)
        is AdminProductPageContract.Inputs.Set.StatusOfPost -> handleSetStatusOfPost(input.postStatus)
        is AdminProductPageContract.Inputs.Set.Description -> handleSetDescription(input.description)
        is AdminProductPageContract.Inputs.Set.DescriptionShake -> updateState { it.copy(descriptionShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.CategoryId -> handleSetCategoryId(input.categoryId)
        is AdminProductPageContract.Inputs.Set.CreatedAt -> handleSetCreatedAt(input.createdAt)
        is AdminProductPageContract.Inputs.Set.Creator -> handleSetCreator(input.creator)
        is AdminProductPageContract.Inputs.Set.UpdatedAt -> handleSetUpdatedAt(input.updatedAt)
        is AdminProductPageContract.Inputs.Set.StatusOfBackorder -> handleSetStatusOfBackorder(input.backorderStatus)
        is AdminProductPageContract.Inputs.Set.Media -> handleSetMedia(input.media)
        is AdminProductPageContract.Inputs.Set.LowStockThreshold -> handleSetLowStockThreshold(input.lowStockThreshold)
        is AdminProductPageContract.Inputs.Set.LowStockThresholdShake -> updateState { it.copy(lowStockThresholdShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.Price -> handleSetPrice(input.price)
        is AdminProductPageContract.Inputs.Set.PriceShake -> updateState { it.copy(priceShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.RegularPrice -> handleSetRegularPrice(input.regularPrice)
        is AdminProductPageContract.Inputs.Set.RegularPriceShake -> updateState { it.copy(regularPriceShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.RemainingStock -> handleSetRemainingStock(input.remainingStock)
        is AdminProductPageContract.Inputs.Set.RemainingStockShake -> updateState { it.copy(remainingStockShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.IsPhysicalProduct -> handleSetIsPhysicalProduct(input.isPhysicalProduct)
        is AdminProductPageContract.Inputs.Set.StatusOfStock -> handleSetStatusOfStock(input.stockStatus)
        is AdminProductPageContract.Inputs.Set.TrackQuantity -> handleSetTrackQuantity(input.trackQuantity)
        is AdminProductPageContract.Inputs.Set.Weight -> handleSetWeight(input.weight)
        is AdminProductPageContract.Inputs.Set.WeightShake -> updateState { it.copy(weightShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.Length -> handleSetLength(input.length)
        is AdminProductPageContract.Inputs.Set.LengthShake -> updateState { it.copy(lengthShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.Width -> handleSetWidth(input.width)
        is AdminProductPageContract.Inputs.Set.WidthShake -> updateState { it.copy(widthShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.Height -> handleSetHeight(input.height)
        is AdminProductPageContract.Inputs.Set.HeightShake -> updateState { it.copy(heightShake = input.shake) }
        is AdminProductPageContract.Inputs.Set.PresetCategory -> handleSetPresetCategory(input.category)
        is AdminProductPageContract.Inputs.Set.ShippingPresetId -> handleSetShippingPresetId(input.presetId)
        is AdminProductPageContract.Inputs.Set.ImageDropError -> updateState { it.copy(imageDropError = input.error) }
        is AdminProductPageContract.Inputs.Set.ChargeTax -> handleSetChargeTax(input.chargeTax)
        is AdminProductPageContract.Inputs.Set.UseGlobalTracking -> handleSetUseGlobalTracking(input.useGlobalTracking)
        is AdminProductPageContract.Inputs.Set.LocalMedia -> updateState { it.copy(localMedia = input.media) }
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
                        createdAt = "0",
                        updatedAt = "0",
                    )
                    postInput(AdminProductPageContract.Inputs.Set.LocalMedia(currentLocalMedia + newMedia))
                }
            }
        }
    }

    private suspend fun InputScope.handleDeleteMedia(index: Int) {
        val state = getCurrentState()
        sideJob("handleDeleteMedia") {
            when (state.screenState) {
                AdminProductPageContract.ScreenState.Existing -> {
                    val id = state.current.media[index].id.toString()
                    postInput(AdminProductPageContract.Inputs.OnDeleteImageClick(id))
                }

                AdminProductPageContract.ScreenState.New -> {
                    val newMedia = state.localMedia.toMutableList().drop(index)
                    postInput(AdminProductPageContract.Inputs.Set.LocalMedia(newMedia))
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
                    val userTags = state.current.tags
                    val newTags = userTags.toMutableList()

                    if (id in userTags) newTags.remove(id) else newTags.add(id)

                    state.copy(current = state.current.copy(tags = newTags)).wasEdited()
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
                    onSuccess = { data ->
                        postInput(AdminProductPageContract.Inputs.Set.ShippingPresetId(categoryId))
                        postInput(AdminProductPageContract.Inputs.Set.PresetCategory(data.getCategoryById))
                    },
                    onFailure = {
                        postEvent(
                            AdminProductPageContract.Events.OnError(it.message ?: "Error while getting category")
                        )
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handlePresetClick(presetId: String) {
        val state = getCurrentState()
        sideJob("handlePresetClick") {
            if (state.current.shipping.presetId == presetId) {
                postInput(AdminProductPageContract.Inputs.Set.PresetCategory(null))
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
                postInput(AdminProductPageContract.Inputs.Set.StateOfScreen(AdminProductPageContract.ScreenState.New))
            } else {
                postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminProductPageContract.Inputs.GetProductById(id))
                postInput(AdminProductPageContract.Inputs.Set.StateOfScreen(AdminProductPageContract.ScreenState.Existing))
                postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = false))
            }
            postInput(AdminProductPageContract.Inputs.GetAllCategories)
            postInput(AdminProductPageContract.Inputs.GetAllTags)
        }
    }

    private suspend fun InputScope.handleGetAllCategories() {
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                onSuccess = { data ->
                    val categories = data.getCategoriesAllMinimal
                    postInput(AdminProductPageContract.Inputs.Set.AllCategories(categories))
                },
                onFailure = {
                    postEvent(
                        AdminProductPageContract.Events.OnError(it.message ?: "Error while getting all categories")
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleGetAllTags() {
        sideJob("handleGetAllTags") {
            tagService.getTagsAllMinimal().fold(
                onSuccess = { data ->
                    postInput(AdminProductPageContract.Inputs.Set.AllTags(data.getTagsAllMinimal))
                },
                onFailure = {
                    postEvent(
                        AdminProductPageContract.Events.OnError(it.message ?: "Error while getting all tags")
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleGetProductById(id: String) {
        sideJob("handleGetProduct") {
            productService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        with(data.getAdminProductById) {
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
                            postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(originalProduct))
                            postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(originalProduct))
                        }
                    },
                    onFailure = {
                        postEvent(
                            AdminProductPageContract.Events.OnError(it.message ?: "Error while getting user profile")
                        )
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleCreateNewProduct() {
        val state = getCurrentState()
        val input = ProductCreateInput(
            title = state.current.title,
            description = state.current.description,
            postStatus = state.current.postStatus,
            media = state.current.media.map { it.id },
            categoryId = Optional.present(state.current.categoryId),
            tags = state.current.tags,
            isFeatured = state.current.isFeatured,
            allowReviews = state.current.allowReviews,
            pricing = PricingCreateInput(
                price = state.current.pricing.price ?: "",
                regularPrice = state.current.pricing.regularPrice ?: "",
                chargeTax = state.current.pricing.chargeTax,
            ),
            inventory = InventoryInput(
                trackQuantity = state.current.inventory.trackQuantity,
                useGlobalTracking = state.current.inventory.useGlobalTracking,
                remainingStock = state.current.inventory.remainingStock,
                stockStatus = state.current.inventory.stockStatus,
                backorderStatus = state.current.inventory.backorderStatus,
                lowStockThreshold = state.current.inventory.lowStockThreshold,
            ),
            shipping = ShippingInput(
                isPhysicalProduct = state.current.shipping.isPhysicalProduct,
                weight = Optional.present(state.current.shipping.weight),
                length = Optional.present(state.current.shipping.length),
                width = Optional.present(state.current.shipping.width),
                height = Optional.present(state.current.shipping.height),
            ),
        )

        sideJob("handleCreateNewProduct") {
            postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = true))
            productService.create(input).fold(
                onSuccess = { data ->
                    state.localMedia.forEach {
                        postInput(AdminProductPageContract.Inputs.UploadMedia(it.url))
                    }

                    postEvent(AdminProductPageContract.Events.GoToProduct(data.createProduct.id.toString()))
                },
                onFailure = {
                    postEvent(
                        AdminProductPageContract.Events.OnError(
                            it.message ?: "Error while creating new user"
                        )
                    )
                },
            )
            postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleDeleteImage(imageId: String) {
        val state = getCurrentState()
        sideJob("handleDeleteImage") {
            postInput(AdminProductPageContract.Inputs.Set.ImagesLoading(isImagesLoading = true))
            productService.deleteImage(state.current.id.toString(), imageId).fold(
                onSuccess = {
                    val media = state.current.media.filter { it.id != imageId }
                    val original = state.original.copy(media = media)
                    val current = state.current.copy(media = media)

                    postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(original))
                    postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(current))
                },
                onFailure = {
                    postEvent(AdminProductPageContract.Events.OnError(it.message ?: "Error while deleting image"))
                },
            )
            postInput(AdminProductPageContract.Inputs.Set.ImagesLoading(isImagesLoading = false))
        }
    }

    private suspend fun InputScope.handleUploadImage(imageString: String) {
        val state = getCurrentState()
        sideJob("handleSaveDetailsUploadImage") {
            postInput(AdminProductPageContract.Inputs.Set.ImageDropError(error = null))
            postInput(AdminProductPageContract.Inputs.Set.ImagesLoading(isImagesLoading = true))
            val mediaType = MediaType.Image
            productService.uploadImage(state.current.id.toString(), imageString, mediaType).fold(
                onSuccess = { data ->
                    val media = data.uploadMediaToProduct.media.map {
                        AdminProductGetByIdQuery.Medium(
                            id = it.id,
                            url = it.url,
                            mediaType = it.mediaType,
                            altText = it.altText,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt,
                        )
                    }
                    postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(state.original.copy(media = media)))
                    postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(state.current.copy(media = media)))
                },
                onFailure = {
                    postEvent(AdminProductPageContract.Events.OnError(it.message ?: "Error while uploading image"))
                },
            )
            postInput(AdminProductPageContract.Inputs.Set.ImagesLoading(isImagesLoading = false))
        }
    }

    private suspend fun InputScope.handleUpdateProduct() {
        with(getCurrentState()) {
            sideJob("handleUpdateDetails") {
                productService.update(
                    id = current.id.toString(),
                    name = if (current.title != original.title) current.title else null,
                    description = if (current.description != original.description)
                        current.description else null,
                    isFeatured = if (current.isFeatured != original.isFeatured) {
                        current.isFeatured
                    } else null,
                    allowReviews = if (current.allowReviews != original.allowReviews) {
                        current.allowReviews
                    } else null,
                    categoryId = if (current.categoryId != original.categoryId) current.categoryId?.toString() else null,
                    tags = if (current.tags.map { it.toString() } != original.tags.map { it.toString() }) {
                        current.tags.map { it.toString() }
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
                    price = if (current.pricing.price != original.pricing.price) current.pricing.price else null,
                    regularPrice = if (current.pricing.regularPrice != original.pricing.regularPrice) {
                        current.pricing.regularPrice
                    } else null,
                    chargeTax = if (current.pricing.chargeTax != original.pricing.chargeTax) {
                        current.pricing.chargeTax
                    } else null,
                    presetId = if (current.shipping.presetId != original.shipping.presetId) current.shipping.presetId?.toString() else null,
                    isPhysicalProduct = if (current.shipping.isPhysicalProduct != original.shipping.isPhysicalProduct) {
                        current.shipping.isPhysicalProduct
                    } else null,
                    height = if (current.shipping.height != original.shipping.height) current.shipping.height else null,
                    length = if (current.shipping.length != original.shipping.length) current.shipping.length else null,
                    weight = if (current.shipping.weight != original.shipping.weight) current.shipping.weight else null,
                    width = if (current.shipping.width != original.shipping.width) current.shipping.width else null,
                ).fold(
                    onSuccess = { data ->
                        postInput(
                            AdminProductPageContract.Inputs.Set.OriginalProduct(
                                product = AdminProductGetByIdQuery.GetAdminProductById(
                                    id = data.updateProduct.id,
                                    title = data.updateProduct.title,
                                    description = data.updateProduct.description,
                                    media = current.media, // Updating this alsewhere
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
                                    creator = original.creator,
                                    reviews = original.reviews,
                                    totalInWishlist = original.totalInWishlist,
                                    createdAt = original.createdAt,
                                    updatedAt = original.updatedAt,
                                ),
                            )
                        )
                    },
                    onFailure = {
                        postEvent(
                            AdminProductPageContract.Events.OnError(
                                it.message ?: "Error while updating product details"
                            )
                        )
                    },
                )
            }
        }
    }

    private suspend fun InputScope.handleDelete() {
        val id = getCurrentState().current.id.toString()
        sideJob("handleDeleteUser") {
            productService.delete(id).fold(
                onSuccess = {
                    postEvent(AdminProductPageContract.Events.GoBackToProducts)
                },
                onFailure = {
                    postEvent(
                        AdminProductPageContract.Events.OnError(
                            it.message ?: "Error while deleting product"
                        )
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleSetTitle(title: String) {
        updateState {
            val isValidated = inputValidator.validateText(title)
            it.copy(
                current = it.current.copy(title = title),
                titleError = if (!it.wasEdited) null else isValidated,
                isCreateDisabled = isValidated != null
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

            if (!isNoError) {
                shakeErrors(
                    isTitleError,
                    isDescriptionsError,
                    isLowStockThresholdError,
                    isRemainingStockError,
                    isPriceError,
                    isRegularPriceError,
                    isWeightError,
                    isLengthError,
                    isWidthError,
                    isHeightError
                )
            } else {
                when (screenState) {
                    AdminProductPageContract.ScreenState.New -> handleCreateNewProduct()
                    AdminProductPageContract.ScreenState.Existing -> handleUpdateProduct()
                }
            }
        }
    }
}

private suspend fun InputScope.handleDiscard() {
    postEvent(AdminProductPageContract.Events.GoBackToProducts)
}

private suspend fun InputScope.handleSetIsFeatured(featured: Boolean) {
    updateState { it.copy(current = it.current.copy(isFeatured = featured)).wasEdited() }
}

private suspend fun InputScope.handleSetAllowReviews(allowReviews: Boolean) {
    updateState { it.copy(current = it.current.copy(allowReviews = allowReviews)).wasEdited() }
}

private suspend fun InputScope.handleSetStatusOfPost(postStatus: PostStatus) {
    updateState { it.copy(current = it.current.copy(postStatus = postStatus)).wasEdited() }
}

private suspend fun InputScope.handleSetDescription(description: String) {
    updateState { it.copy(current = it.current.copy(description = description)).wasEdited() }
}

private suspend fun InputScope.handleSetCategoryId(categoryId: String) {
    updateState { it.copy(current = it.current.copy(categoryId = categoryId)).wasEdited() }
}

private fun AdminProductPageContract.State.wasEdited(): AdminProductPageContract.State =
    copy(wasEdited = current != original)

private fun InputScope.shakeErrors(
    isTitleError: Boolean,
    isDescriptionsError: Boolean,
    isLowStockThresholdError: Boolean,
    isRemainingStockError: Boolean,
    isPriceError: Boolean,
    isRegularPriceError: Boolean,
    isWeightError: Boolean,
    isLengthError: Boolean,
    isWidthError: Boolean,
    isHeightError: Boolean
) {
    sideJob("handleSaveDetailsShakes") {
        if (isTitleError) postInput(AdminProductPageContract.Inputs.Set.TitleShake(shake = true))
        if (isDescriptionsError) postInput(AdminProductPageContract.Inputs.Set.DescriptionShake(shake = true))
        if (isLowStockThresholdError) postInput(
            AdminProductPageContract.Inputs.Set.LowStockThresholdShake(
                shake = true
            )
        )
        if (isRemainingStockError) postInput(AdminProductPageContract.Inputs.Set.RemainingStockShake(shake = true))
        if (isPriceError) postInput(AdminProductPageContract.Inputs.Set.PriceShake(shake = true))
        if (isRegularPriceError) postInput(AdminProductPageContract.Inputs.Set.RegularPriceShake(shake = true))
        if (isWeightError) postInput(AdminProductPageContract.Inputs.Set.WeightShake(shake = true))
        if (isLengthError) postInput(AdminProductPageContract.Inputs.Set.LengthShake(shake = true))
        if (isWidthError) postInput(AdminProductPageContract.Inputs.Set.WidthShake(shake = true))
        if (isHeightError) postInput(AdminProductPageContract.Inputs.Set.HeightShake(shake = true))

        delay(SHAKE_ANIM_DURATION)

        if (isTitleError) postInput(AdminProductPageContract.Inputs.Set.TitleShake(shake = false))
        if (isDescriptionsError) postInput(AdminProductPageContract.Inputs.Set.DescriptionShake(shake = false))
        if (isLowStockThresholdError) postInput(
            AdminProductPageContract.Inputs.Set.LowStockThresholdShake(
                shake = false
            )
        )
        if (isRemainingStockError) postInput(AdminProductPageContract.Inputs.Set.RemainingStockShake(shake = false))
        if (isPriceError) postInput(AdminProductPageContract.Inputs.Set.PriceShake(shake = false))
        if (isRegularPriceError) postInput(AdminProductPageContract.Inputs.Set.RegularPriceShake(shake = false))
        if (isWeightError) postInput(AdminProductPageContract.Inputs.Set.WeightShake(shake = false))
        if (isLengthError) postInput(AdminProductPageContract.Inputs.Set.LengthShake(shake = false))
        if (isWidthError) postInput(AdminProductPageContract.Inputs.Set.WidthShake(shake = false))
        if (isHeightError) postInput(AdminProductPageContract.Inputs.Set.HeightShake(shake = false))
    }
}
