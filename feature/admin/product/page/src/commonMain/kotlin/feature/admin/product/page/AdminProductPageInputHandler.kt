package feature.admin.product.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.util.millisToTime
import data.GetCategoryByIdQuery
import data.ProductGetByIdQuery
import data.service.CategoryService
import data.service.ProductService
import data.service.TagService
import data.type.BackorderStatus
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.ProductCreateInput
import data.type.ProductImageInput
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
        is AdminProductPageContract.Inputs.UploadImage -> handleUploadImage(input.imageString)

        is AdminProductPageContract.Inputs.Get.ProductById -> handleGetProductById(input.id)
        AdminProductPageContract.Inputs.Get.AllCategories -> handleGetAllCategories()
        AdminProductPageContract.Inputs.Get.AllTags -> handleGetAllTags()
        is AdminProductPageContract.Inputs.Get.PresetCategory -> handleGetPresetCategory(input.categoryId)

        AdminProductPageContract.Inputs.OnClick.Create -> handleCreateNewProduct()
        AdminProductPageContract.Inputs.OnClick.Delete -> handleDeleteProduct()
        AdminProductPageContract.Inputs.OnClick.SaveEdit -> handleSaveDetails()
        AdminProductPageContract.Inputs.OnClick.CancelEdit -> handleCancelEdit()
        is AdminProductPageContract.Inputs.OnClick.CategorySelected -> handleOnClickCategory(input.categoryName)
        is AdminProductPageContract.Inputs.OnClick.TagSelected -> handleTagClick(input.tagName)
        is AdminProductPageContract.Inputs.OnClick.GoToUserCreator -> {
            val userId = getCurrentState().original.creator.id.toString()
            postEvent(AdminProductPageContract.Events.GoToUserDetails(userId))
        }

        is AdminProductPageContract.Inputs.OnClick.DeleteImage -> handleDeleteImage(input.imageId)

        AdminProductPageContract.Inputs.OnClick.ImproveName -> handleImproveName()
        AdminProductPageContract.Inputs.OnClick.ImproveShortDescription -> handleImproveShortDescription()
        AdminProductPageContract.Inputs.OnClick.ImproveDescription -> handleImproveDescription()
        AdminProductPageContract.Inputs.OnClick.ImproveTags -> handleImproveTags()
        AdminProductPageContract.Inputs.OnClick.GoToCreateTag ->
            postEvent(AdminProductPageContract.Events.GoToCreateTag)


        is AdminProductPageContract.Inputs.OnClick.PresetSelected -> handlePresetClick(input.preset)
        AdminProductPageContract.Inputs.OnClick.GoToCreateCategory ->
            postEvent(AdminProductPageContract.Events.GoToCreateCategory)

        is AdminProductPageContract.Inputs.Set.AllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminProductPageContract.Inputs.Set.AllTags -> updateState { it.copy(allTags = input.tags) }
        is AdminProductPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductPageContract.Inputs.Set.OriginalProduct ->
            updateState { it.copy(original = input.product).wasEdited() }

        is AdminProductPageContract.Inputs.Set.CurrentProduct ->
            updateState { it.copy(current = input.product).wasEdited() }

        is AdminProductPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }
        is AdminProductPageContract.Inputs.Set.Id -> handleSetId(input.id)
        is AdminProductPageContract.Inputs.Set.Name -> handleSetName(input.name)
        is AdminProductPageContract.Inputs.Set.NameShake -> updateState { it.copy(shakeName = input.shake) }
        is AdminProductPageContract.Inputs.Set.ShortDescription -> handleSetShortDescription(input.shortDescription)
        is AdminProductPageContract.Inputs.Set.ShortDescriptionShake -> updateState { it.copy(shakeShortDescription = input.shake) }
        is AdminProductPageContract.Inputs.Set.IsFeatured -> handleSetIsFeatured(input.isFeatured)
        is AdminProductPageContract.Inputs.Set.AllowReviews -> handleSetAllowReviews(input.allowReviews)
        is AdminProductPageContract.Inputs.Set.VisibilityInCatalog -> handleSetCatalogVisibility(input.catalogVisibility)
        is AdminProductPageContract.Inputs.Set.StatusOfPost -> handleSetStatusOfPost(input.postStatus)
        is AdminProductPageContract.Inputs.Set.Description -> handleSetDescription(input.description)
        is AdminProductPageContract.Inputs.Set.DescriptionShake -> updateState { it.copy(shakeDescription = input.shake) }
        is AdminProductPageContract.Inputs.Set.IsPurchasable -> handleSetIsPurchasable(input.isPurchasable)
        is AdminProductPageContract.Inputs.Set.Categories -> handleSetCategories(input.categories)
        is AdminProductPageContract.Inputs.Set.CreatedAt -> handleSetCreatedAt(input.createdAt)
        is AdminProductPageContract.Inputs.Set.Creator -> handleSetCreator(input.creator)
        is AdminProductPageContract.Inputs.Set.UpdatedAt -> handleSetUpdatedAt(input.updatedAt)
        is AdminProductPageContract.Inputs.Set.StatusOfBackorder -> handleSetStatusOfBackorder(input.backorderStatus)
        is AdminProductPageContract.Inputs.Set.OnePerOrder -> handleSetOnePerOrder(input.onePerOrder)
        is AdminProductPageContract.Inputs.Set.CanBackorder -> handleSetCanBackorder(input.canBackorder)
        is AdminProductPageContract.Inputs.Set.Images -> handleSetImages(input.images)
        is AdminProductPageContract.Inputs.Set.IsOnBackorder -> handleSetIsOnBackorder(input.isOnBackorder)
        is AdminProductPageContract.Inputs.Set.LowStockThreshold -> handleSetLowStockThreshold(input.lowStockThreshold)
        is AdminProductPageContract.Inputs.Set.LowStockThresholdShake -> updateState { it.copy(shakeLowStockThreshold = input.shake) }
        is AdminProductPageContract.Inputs.Set.OnSale -> handleSetOnSale(input.onSale)
        is AdminProductPageContract.Inputs.Set.Price -> handleSetPrice(input.price)
        is AdminProductPageContract.Inputs.Set.PriceShake -> updateState { it.copy(shakePrice = input.shake) }
        is AdminProductPageContract.Inputs.Set.RegularPrice -> handleSetRegularPrice(input.regularPrice)
        is AdminProductPageContract.Inputs.Set.RegularPriceShake -> updateState { it.copy(shakeRegularPrice = input.shake) }
        is AdminProductPageContract.Inputs.Set.RemainingStock -> handleSetRemainingStock(input.remainingStock)
        is AdminProductPageContract.Inputs.Set.RemainingStockShake -> updateState { it.copy(shakeRemainingStock = input.shake) }
        is AdminProductPageContract.Inputs.Set.RequiresShipping -> handleSetRequiresShipping(input.requiresShipping)
        is AdminProductPageContract.Inputs.Set.SaleEnd -> handleSetSaleEnd(input.saleEnd)
        is AdminProductPageContract.Inputs.Set.SalePrice -> handleSetSalePrice(input.salePrice)
        is AdminProductPageContract.Inputs.Set.SalePriceShake -> updateState { it.copy(shakeSalePrice = input.shake) }
        is AdminProductPageContract.Inputs.Set.SaleStart -> handleSetSaleStart(input.saleStart)
        is AdminProductPageContract.Inputs.Set.StatusOfStock -> handleSetStatusOfStock(input.stockStatus)
        is AdminProductPageContract.Inputs.Set.TrackInventory -> handleSetTrackInventory(input.trackInventory)
        is AdminProductPageContract.Inputs.Set.Weight -> handleSetWeight(input.weight)
        is AdminProductPageContract.Inputs.Set.WeightShake -> updateState { it.copy(shakeWeight = input.shake) }
        is AdminProductPageContract.Inputs.Set.Length -> handleSetLength(input.length)
        is AdminProductPageContract.Inputs.Set.LengthShake -> updateState { it.copy(shakeLength = input.shake) }
        is AdminProductPageContract.Inputs.Set.Width -> handleSetWidth(input.width)
        is AdminProductPageContract.Inputs.Set.WidthShake -> updateState { it.copy(shakeWidth = input.shake) }
        is AdminProductPageContract.Inputs.Set.Height -> handleSetHeight(input.height)
        is AdminProductPageContract.Inputs.Set.HeightShake -> updateState { it.copy(shakeHeight = input.shake) }
        is AdminProductPageContract.Inputs.Set.PresetCategory -> handleSetPresetCategory(input.category)
        is AdminProductPageContract.Inputs.Set.ShippingPresetId -> handleSetShippingPresetId(input.presetId)
    }

    private suspend fun InputScope.handleOnClickCategory(name: String) {
        updateState { state ->
            state.allCategories
                .find { category -> category.name == name }
                ?.let { chosenCategory ->
                    val id = chosenCategory.id
                    val userCategories = state.current.common.categories
                    val newCategories = userCategories.toMutableList()

                    if (id in userCategories) newCategories.remove(id) else newCategories.add(id)

                    state.copy(
                        current = state.current.copy(
                            common = state.current.common.copy(categories = newCategories)
                        )
                    ).wasEdited()
                } ?: state
        }
    }

    private suspend fun InputScope.handleTagClick(name: String) {
        updateState { state ->
            state.allTags
                .find { tag -> tag.name == name }
                ?.let { chosenTag ->
                    val id = chosenTag.id
                    val userTags = state.current.common.tags
                    val newTags = userTags.toMutableList()

                    if (id in userTags) newTags.remove(id) else newTags.add(id)

                    state.copy(
                        current = state.current.copy(
                            common = state.current.common.copy(tags = newTags)
                        )
                    ).wasEdited()
                } ?: state
        }
    }

    private fun InputScope.handleImproveName() {
        noOp()
    }

    private fun InputScope.handleImproveShortDescription() {
        noOp()
    }

    private fun InputScope.handleImproveDescription() {
        noOp()
    }

    private fun InputScope.handleImproveTags() {
        noOp()
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
                        requiresShipping = category?.shippingPreset?.requiresShipping
                            ?: it.current.shipping.requiresShipping,
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
                postInput(AdminProductPageContract.Inputs.Get.PresetCategory(presetId))
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

    private suspend fun InputScope.handleSetOnePerOrder(onePerOrder: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(onePerOrder = onePerOrder))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCanBackorder(canBackorder: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(canBackorder = canBackorder))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetIsOnBackorder(isOnBackorder: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(isOnBackorder = isOnBackorder))
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

    private suspend fun InputScope.handleSetTrackInventory(trackInventory: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(inventory = it.current.inventory.copy(trackInventory = trackInventory))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPrice(price: String) {
        updateState {
            it.copy(
                current = it.current.copy(price = it.current.price.copy(price = price)),
                priceError = if (it.wasEdited) inputValidator.validateNumberPositive(price.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetRegularPrice(regularPrice: String) {
        updateState {
            it.copy(
                current = it.current.copy(price = it.current.price.copy(regularPrice = regularPrice)),
                regularPriceError = if (it.wasEdited) inputValidator.validateNumberPositive(regularPrice.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetSalePrice(salePrice: String) {
        updateState {
            it.copy(
                current = it.current.copy(price = it.current.price.copy(salePrice = salePrice)),
                salePriceError = if (it.wasEdited) inputValidator.validateNumberPositive(salePrice.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOnSale(onSale: Boolean) {
        updateState { it.copy(current = it.current.copy(price = it.current.price.copy(onSale = onSale))).wasEdited() }
    }

    private suspend fun InputScope.handleSetSaleStart(saleStart: String) {
        updateState {
            it.copy(current = it.current.copy(price = it.current.price.copy(saleStart = saleStart))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetSaleEnd(saleEnd: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    price = it.current.price.copy(
                        saleEnd = saleEnd
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetHeight(height: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shipping = it.current.shipping.copy(
                        height = height
                    )
                ),
                heightError = if (it.wasEdited) inputValidator.validateNumberPositive(height.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetLength(length: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shipping = it.current.shipping.copy(
                        length = length
                    )
                ),
                lengthError = if (it.wasEdited) inputValidator.validateNumberPositive(length.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetWeight(weight: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    shipping = it.current.shipping.copy(
                        weight = weight
                    )
                ),
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

    private suspend fun InputScope.handleSetRequiresShipping(requiresShipping: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(shipping = it.current.shipping.copy(requiresShipping = requiresShipping))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetImages(images: List<ProductGetByIdQuery.Image>) {
        updateState { it.copy(current = it.current.copy(data = it.current.data.copy(images = images))).wasEdited() }
    }

    private suspend fun InputScope.handleSetId(id: String) {
        updateState { it.copy(current = it.current.copy(id = id)).wasEdited() }
    }

    private suspend fun InputScope.handleSetUpdatedAt(updatedAt: String) {
        updateState {
            it.copy(current = it.current.copy(common = it.current.common.copy(updatedAt = updatedAt))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCreator(creator: ProductGetByIdQuery.Creator) {
        updateState { it.copy(current = it.current.copy(creator = creator)).wasEdited() }
    }

    private suspend fun InputScope.handleSetCreatedAt(createdAt: String) {
        updateState {
            it.copy(current = it.current.copy(common = it.current.common.copy(createdAt = createdAt))).wasEdited()
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminProductPageContract.Inputs.Set.StateOfScreen(AdminProductPageContract.ScreenState.New))
            } else {
                postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminProductPageContract.Inputs.Get.ProductById(id))
                postInput(AdminProductPageContract.Inputs.Set.StateOfScreen(AdminProductPageContract.ScreenState.Existing))
                postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = false))
            }
            postInput(AdminProductPageContract.Inputs.Get.AllCategories)
            postInput(AdminProductPageContract.Inputs.Get.AllTags)
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
                        val createdAt = millisToTime(data.getProductById.common.createdAt.toLong())
                        val updatedAt = millisToTime(data.getProductById.common.updatedAt.toLong())
                        val originalProduct = data.getProductById.copy(
                            common = data.getProductById.common.copy(
                                shortDescription = data.getProductById.common.shortDescription ?: "",
                                categories = data.getProductById.common.categories.map { it.toString() },
                                createdAt = createdAt,
                                updatedAt = updatedAt,
                            ),
                            data = data.getProductById.data.copy(
                                description = data.getProductById.data.description ?: ""
                            ),
                            price = data.getProductById.price.copy(
                                price = data.getProductById.price.price ?: "",
                                regularPrice = data.getProductById.price.regularPrice ?: "",
                                salePrice = data.getProductById.price.salePrice ?: "",
                            ),
                        )
                        postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(originalProduct))
                        postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(originalProduct))
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
        val input = ProductCreateInput(name = state.current.common.name)

        sideJob("handleCreateNewUser") {
            productService.create(input).fold(
                onSuccess = { data ->
                    postEvent(AdminProductPageContract.Events.GoToProduct(data.createProduct.id))
                },
                onFailure = {
                    postEvent(
                        AdminProductPageContract.Events.OnError(
                            it.message ?: "Error while creating new user"
                        )
                    )
                },
            )
        }
    }

    private suspend fun InputScope.handleCancelEdit() {
        updateState {
            it.copy(
                wasEdited = false,
                current = it.original,
            )
        }
    }

    private suspend fun InputScope.handleDeleteImage(imageId: String) {
        val state = getCurrentState()
        sideJob("handleDeleteImage") {
            postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = true))
            productService.deleteImage(state.current.id.toString(), imageId).fold(
                onSuccess = {
                    val images = state.current.data.images.filter { it.id != imageId }
                    val original =
                        state.original.copy(data = state.original.data.copy(images = images))
                    val current =
                        state.current.copy(data = state.current.data.copy(images = images))

                    postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(original))
                    postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(current))
                },
                onFailure = {
                    postEvent(AdminProductPageContract.Events.OnError(it.message ?: "Error while deleting image"))
                },
            )
            postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleUploadImage(imageString: String) {
        val state = getCurrentState()
        sideJob("handleSaveDetailsUploadImage") {
            postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = true))
            productService.uploadImage(state.current.id.toString(), imageString).fold(
                onSuccess = { data ->
                    println("Image uploaded successfully.\nMessage: ${data.uploadImageToProduct}")
                    val images = data.uploadImageToProduct.images.map {
                        ProductGetByIdQuery.Image(
                            id = it.id,
                            blob = it.blob,
                            altText = it.altText,
                            created = it.created,
                            modified = it.modified,
                        )
                    }
                    val original = state.original.copy(
                        data = state.original.data.copy(images = images)
                    )
                    val current = state.current.copy(
                        data = state.current.data.copy(images = images)
                    )
                    postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(original))
                    postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(current))
                },
                onFailure = {
                    postEvent(AdminProductPageContract.Events.OnError(it.message ?: "Error while uploading image"))
                },
            )
            postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleSaveDetails() {
        with(getCurrentState()) {
            val isNameError = nameError != null
            val isShortDescriptionError = shortDescriptionError != null
            val isDescriptionsError = descriptionError != null
            val isLowStockThresholdError = lowStockThresholdError != null
            val isRemainingStockError = remainingStockError != null
            val isPriceError = priceError != null
            val isRegularPriceError = regularPriceError != null
            val isSalePriceError = salePriceError != null
            val isWeightError = weightError != null
            val isLengthError = lengthError != null
            val isWidthError = widthError != null
            val isHeightError = heightError != null
            val isNoError = !isNameError && !isShortDescriptionError && !isDescriptionsError &&
                !isLowStockThresholdError && !isRemainingStockError && !isPriceError &&
                !isRegularPriceError && !isSalePriceError && !isWeightError && !isLengthError &&
                !isWidthError && !isHeightError

            if (!isNoError) {
                sideJob("handleSaveDetailsShakes") {
                    if (isNameError) postInput(AdminProductPageContract.Inputs.Set.NameShake(shake = true))
                    if (isShortDescriptionError) {
                        postInput(AdminProductPageContract.Inputs.Set.ShortDescriptionShake(shake = true))
                    }
                    if (isDescriptionsError) postInput(AdminProductPageContract.Inputs.Set.DescriptionShake(shake = true))
                    if (isLowStockThresholdError) postInput(
                        AdminProductPageContract.Inputs.Set.LowStockThresholdShake(
                            shake = true
                        )
                    )
                    if (isRemainingStockError) postInput(AdminProductPageContract.Inputs.Set.RemainingStockShake(shake = true))
                    if (isPriceError) postInput(AdminProductPageContract.Inputs.Set.PriceShake(shake = true))
                    if (isRegularPriceError) postInput(AdminProductPageContract.Inputs.Set.RegularPriceShake(shake = true))
                    if (isSalePriceError) postInput(AdminProductPageContract.Inputs.Set.SalePriceShake(shake = true))
                    if (isWeightError) postInput(AdminProductPageContract.Inputs.Set.WeightShake(shake = true))
                    if (isLengthError) postInput(AdminProductPageContract.Inputs.Set.LengthShake(shake = true))
                    if (isWidthError) postInput(AdminProductPageContract.Inputs.Set.WidthShake(shake = true))
                    if (isHeightError) postInput(AdminProductPageContract.Inputs.Set.HeightShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isNameError) postInput(AdminProductPageContract.Inputs.Set.NameShake(shake = false))
                    if (isShortDescriptionError) {
                        postInput(AdminProductPageContract.Inputs.Set.ShortDescriptionShake(shake = false))
                    }
                    if (isDescriptionsError) postInput(AdminProductPageContract.Inputs.Set.DescriptionShake(shake = false))
                    if (isLowStockThresholdError) postInput(
                        AdminProductPageContract.Inputs.Set.LowStockThresholdShake(
                            shake = false
                        )
                    )
                    if (isRemainingStockError) postInput(AdminProductPageContract.Inputs.Set.RemainingStockShake(shake = false))
                    if (isPriceError) postInput(AdminProductPageContract.Inputs.Set.PriceShake(shake = false))
                    if (isRegularPriceError) postInput(AdminProductPageContract.Inputs.Set.RegularPriceShake(shake = false))
                    if (isSalePriceError) postInput(AdminProductPageContract.Inputs.Set.SalePriceShake(shake = false))
                    if (isWeightError) postInput(AdminProductPageContract.Inputs.Set.WeightShake(shake = false))
                    if (isLengthError) postInput(AdminProductPageContract.Inputs.Set.LengthShake(shake = false))
                    if (isWidthError) postInput(AdminProductPageContract.Inputs.Set.WidthShake(shake = false))
                    if (isHeightError) postInput(AdminProductPageContract.Inputs.Set.HeightShake(shake = false))
                }
            } else {
                sideJob("handleSaveDetails") {
                    productService.update(
                        id = current.id.toString(),
                        name = if (current.common.name != original.common.name) current.common.name else null,
                        shortDescription = if (current.common.shortDescription != original.common.shortDescription) {
                            current.common.shortDescription
                        } else null,
                        isFeatured = if (current.common.isFeatured != original.common.isFeatured) {
                            current.common.isFeatured
                        } else null,
                        allowReviews = if (current.common.allowReviews != original.common.allowReviews) {
                            current.common.allowReviews
                        } else null,
                        catalogVisibility = if (current.common.catalogVisibility != original.common.catalogVisibility) {
                            current.common.catalogVisibility
                        } else null,
                        categories = if (current.common.categories.map { it.toString() } != original.common.categories.map { it.toString() }) {
                            current.common.categories.map { it.toString() }
                        } else null,
                        tags = if (current.common.tags.map { it.toString() } != original.common.tags.map { it.toString() }) {
                            current.common.tags.map { it.toString() }
                        } else null,
                        description = if (current.data.description != original.data.description)
                            current.data.description else null,
                        isPurchasable = if (current.data.isPurchasable != original.data.isPurchasable) {
                            current.data.isPurchasable
                        } else null,
                        images = if (current.data.images.map { it.id } != original.data.images.map { it.id }) {
                            current.data.images.map {
                                ProductImageInput(
                                    id = it.id,
                                    blob = it.blob,
                                    altText = it.altText,
                                    created = it.created,
                                    modified = it.modified,
                                )
                            }
                        } else null,
                        postStatus = if (current.data.postStatus != original.data.postStatus) {
                            current.data.postStatus
                        } else null,
                        onePerOrder = if (current.inventory.onePerOrder != original.inventory.onePerOrder) {
                            current.inventory.onePerOrder
                        } else null,
                        backorderStatus = if (current.inventory.backorderStatus != original.inventory.backorderStatus) {
                            current.inventory.backorderStatus
                        } else null,
                        canBackorder = if (current.inventory.canBackorder != original.inventory.canBackorder) {
                            current.inventory.canBackorder
                        } else null,
                        isOnBackorder = if (current.inventory.isOnBackorder != original.inventory.isOnBackorder) {
                            current.inventory.isOnBackorder
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
                        trackInventory = if (current.inventory.trackInventory != original.inventory.trackInventory) {
                            current.inventory.trackInventory
                        } else null,
                        price = if (current.price.price != original.price.price) current.price.price else null,
                        regularPrice = if (current.price.regularPrice != original.price.regularPrice) {
                            current.price.regularPrice
                        } else null,
                        salePrice = if (current.price.salePrice != original.price.salePrice) current.price.salePrice else null,
                        onSale = if (current.price.onSale != original.price.onSale) current.price.onSale else null,
                        saleStart = if (current.price.saleStart != original.price.saleStart) current.price.saleStart else null,
                        saleEnd = if (current.price.saleEnd != original.price.saleEnd) current.price.saleEnd else null,
                        presetId = if (current.shipping.presetId != original.shipping.presetId) current.shipping.presetId?.toString() else null,
                        height = if (current.shipping.height != original.shipping.height) current.shipping.height else null,
                        length = if (current.shipping.length != original.shipping.length) current.shipping.length else null,
                        weight = if (current.shipping.weight != original.shipping.weight) current.shipping.weight else null,
                        width = if (current.shipping.width != original.shipping.width) current.shipping.width else null,
                        requiresShipping = if (current.shipping.requiresShipping != original.shipping.requiresShipping) {
                            current.shipping.requiresShipping
                        } else null,
                    ).fold(
                        onSuccess = { data ->
                            postInput(
                                AdminProductPageContract.Inputs.Set.OriginalProduct(
                                    product = ProductGetByIdQuery.GetProductById(
                                        id = data.updateProduct.id,
                                        common = ProductGetByIdQuery.Common(
                                            name = data.updateProduct.common.name,
                                            shortDescription = data.updateProduct.common.shortDescription,
                                            isFeatured = data.updateProduct.common.isFeatured,
                                            allowReviews = data.updateProduct.common.allowReviews,
                                            catalogVisibility = data.updateProduct.common.catalogVisibility,
                                            categories = data.updateProduct.common.categories.map { it.toString() },
                                            createdBy = data.updateProduct.common.createdBy,
                                            createdAt = data.updateProduct.common.createdAt,
                                            updatedAt = data.updateProduct.common.updatedAt,
                                            relatedIds = data.updateProduct.common.relatedIds,
                                            tags = data.updateProduct.common.tags,
                                        ),
                                        data = ProductGetByIdQuery.Data1(
                                            postStatus = data.updateProduct.data.postStatus,
                                            description = data.updateProduct.data.description,
                                            isPurchasable = data.updateProduct.data.isPurchasable,
                                            images = current.data.images, // Not updating this actually
                                            parentId = data.updateProduct.data.parentId,
                                        ),
                                        inventory = ProductGetByIdQuery.Inventory(
                                            onePerOrder = data.updateProduct.inventory.onePerOrder,
                                            backorderStatus = data.updateProduct.inventory.backorderStatus,
                                            canBackorder = data.updateProduct.inventory.canBackorder,
                                            isOnBackorder = data.updateProduct.inventory.isOnBackorder,
                                            lowStockThreshold = data.updateProduct.inventory.lowStockThreshold,
                                            remainingStock = data.updateProduct.inventory.remainingStock,
                                            stockStatus = data.updateProduct.inventory.stockStatus,
                                            trackInventory = data.updateProduct.inventory.trackInventory,
                                        ),
                                        price = ProductGetByIdQuery.Price(
                                            price = data.updateProduct.price.price,
                                            regularPrice = data.updateProduct.price.regularPrice,
                                            salePrice = data.updateProduct.price.salePrice,
                                            onSale = data.updateProduct.price.onSale,
                                            saleStart = data.updateProduct.price.saleStart,
                                            saleEnd = data.updateProduct.price.saleEnd,
                                        ),
                                        shipping = ProductGetByIdQuery.Shipping(
                                            presetId = data.updateProduct.shipping.presetId,
                                            height = data.updateProduct.shipping.height,
                                            length = data.updateProduct.shipping.length,
                                            weight = data.updateProduct.shipping.weight,
                                            width = data.updateProduct.shipping.width,
                                            requiresShipping = data.updateProduct.shipping.requiresShipping,
                                        ),
                                        creator = original.creator,
                                        reviews = original.reviews,
                                        totalInWishlist = original.totalInWishlist,
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
    }

    private suspend fun InputScope.handleDeleteProduct() {
        val id = getCurrentState().current.id.toString()
        sideJob("handleDeleteUser") {
            productService.deleteById(id).fold(
                onSuccess = {
                    postEvent(AdminProductPageContract.Events.GoToProductList)
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

    private suspend fun InputScope.handleSetName(name: String) {
        updateState {
            val isValidated = inputValidator.validateText(name)
            it.copy(
                current = it.current.copy(common = it.current.common.copy(name = name)),
                nameError = if (!it.wasEdited) null else isValidated,
                isCreateDisabled = isValidated != null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetShortDescription(shortDescription: String) {
        updateStateAndGet {
            it.copy(
                current = it.current.copy(common = it.current.common.copy(shortDescription = shortDescription)),
                shortDescriptionError = if (it.wasEdited) null else inputValidator.validateText(shortDescription),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetIsFeatured(featured: Boolean) {
        updateState {
            it.copy(current = it.current.copy(common = it.current.common.copy(isFeatured = featured))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetAllowReviews(allowReviews: Boolean) {
        updateState {
            it.copy(current = it.current.copy(common = it.current.common.copy(allowReviews = allowReviews))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCatalogVisibility(catalogVisibility: CatalogVisibility) {
        updateState {
            it.copy(
                current = it.current.copy(common = it.current.common.copy(catalogVisibility = catalogVisibility))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetStatusOfPost(postStatus: PostStatus) {
        updateState {
            it.copy(current = it.current.copy(data = it.current.data.copy(postStatus = postStatus))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetDescription(description: String) {
        updateState {
            it.copy(current = it.current.copy(data = it.current.data.copy(description = description))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetIsPurchasable(isPurchasable: Boolean) {
        updateState {
            it.copy(current = it.current.copy(data = it.current.data.copy(isPurchasable = isPurchasable))).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCategories(categories: List<String>) {
        updateState {
            it.copy(current = it.current.copy(common = it.current.common.copy(categories = categories))).wasEdited()
        }
    }

    private fun AdminProductPageContract.State.wasEdited(): AdminProductPageContract.State =
        copy(wasEdited = current != original)
}
