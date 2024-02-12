package feature.admin.product.page

import com.apollographql.apollo3.api.Optional
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.util.currentTimeMillis
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
import data.utils.ImageFile
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
        is AdminProductPageContract.Inputs.UploadImage -> handleUploadImage(input.imageFile)

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

        AdminProductPageContract.Inputs.OnClick.ImproveName -> handleImproveName()
        AdminProductPageContract.Inputs.OnClick.ImproveShortDescription -> handleImproveShortDescription()
        AdminProductPageContract.Inputs.OnClick.ImproveDescription -> handleImproveDescription()
        AdminProductPageContract.Inputs.OnClick.ImproveTags -> handleImproveTags()
        AdminProductPageContract.Inputs.OnClick.GoToCreateTag ->
            postEvent(AdminProductPageContract.Events.GoToCreateTag)


        is AdminProductPageContract.Inputs.OnClick.PresetSelected -> handlePresetClick(input.preset)
        AdminProductPageContract.Inputs.OnClick.GoToCreateCategory ->
            postEvent(AdminProductPageContract.Events.GoToCreateCategory)

        is AdminProductPageContract.Inputs.OnClick.AddImage -> handleAddImage(input.path)

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
        is AdminProductPageContract.Inputs.Set.LocalImages -> updateState { it.copy(localImages = input.localImages) }
    }

    private suspend fun InputScope.handleAddImage(path: String) {
        val time = currentTimeMillis().toString()
        val img = ProductGetByIdQuery.Image(
            id = "local$time",
            url = path,
            altText = "",
            name = "",
            created = time,
            modified = time,
        )
        updateState { it.copy(localImages = it.localImages + img) }
    }

    private suspend fun InputScope.handleOnClickCategory(name: String) {
        updateState { state ->
            state.allCategories
                .find { category -> category.name == name }
                ?.let { chosenCategory ->
                    val id = chosenCategory.id
                    val userCategories = state.current.product.common.categories
                    val newCategories = userCategories.toMutableList()

                    if (id in userCategories) newCategories.remove(id) else newCategories.add(id)

                    state.copy(
                        current = state.current.copy(
                            product = state.current.product.copy(
                                common = state.current.product.common.copy(categories = newCategories)
                            )
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
                    val userTags = state.current.product.common.tags
                    val newTags = userTags.toMutableList()

                    if (id in userTags) newTags.remove(id) else newTags.add(id)

                    state.copy(
                        current = state.current.copy(
                            product = state.current.product.copy(
                                common = state.current.product.common.copy(tags = newTags)
                            )
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
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            presetId = presetId
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPresetCategory(category: GetCategoryByIdQuery.GetCategoryById?) {
        updateState {
            it.copy(
                presetCategory = category,
                current = it.current.copy(
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            presetId = category?.id,
                            weight = category?.shippingPreset?.weight ?: it.current.product.shipping.weight,
                            length = category?.shippingPreset?.length ?: it.current.product.shipping.length,
                            width = category?.shippingPreset?.width ?: it.current.product.shipping.width,
                            height = category?.shippingPreset?.height ?: it.current.product.shipping.height,
                            requiresShipping = category?.shippingPreset?.requiresShipping
                                ?: it.current.product.shipping.requiresShipping,
                        )
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
            if (state.current.product.shipping.presetId == presetId) {
                postInput(AdminProductPageContract.Inputs.Set.PresetCategory(null))
            } else {
                postInput(AdminProductPageContract.Inputs.Get.PresetCategory(presetId))
            }
        }
    }

    private suspend fun InputScope.handleSetStatusOfBackorder(backorderStatus: BackorderStatus) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            backorderStatus = backorderStatus
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetStatusOfStock(stockStatus: StockStatus) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            stockStatus = stockStatus
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOnePerOrder(onePerOrder: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            onePerOrder = onePerOrder
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCanBackorder(canBackorder: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            canBackorder = canBackorder
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetIsOnBackorder(isOnBackorder: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            isOnBackorder = isOnBackorder
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetLowStockThreshold(lowStockThreshold: Int) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            lowStockThreshold = lowStockThreshold
                        )
                    )
                ),
                lowStockThresholdError = if (it.wasEdited) inputValidator.validateNumberPositive(lowStockThreshold) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetRemainingStock(remainingStock: Int) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            remainingStock = remainingStock
                        )
                    )
                ),
                remainingStockError = if (it.wasEdited) inputValidator.validateNumberPositive(remainingStock) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetTrackInventory(trackInventory: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        inventory = it.current.product.inventory.copy(
                            trackInventory = trackInventory
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetPrice(price: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        price = it.current.product.price.copy(
                            price = price
                        )
                    )
                ),
                priceError = if (it.wasEdited) inputValidator.validateNumberPositive(price.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetRegularPrice(regularPrice: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        price = it.current.product.price.copy(
                            regularPrice = regularPrice
                        )
                    )
                ),
                regularPriceError = if (it.wasEdited) inputValidator.validateNumberPositive(regularPrice.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetSalePrice(salePrice: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        price = it.current.product.price.copy(
                            salePrice = salePrice
                        )
                    )
                ),
                salePriceError = if (it.wasEdited) inputValidator.validateNumberPositive(salePrice.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetOnSale(onSale: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        price = it.current.product.price.copy(
                            onSale = onSale
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetSaleStart(saleStart: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        price = it.current.product.price.copy(
                            saleStart = saleStart
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetSaleEnd(saleEnd: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        price = it.current.product.price.copy(
                            saleEnd = saleEnd
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetHeight(height: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            height = height
                        )
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
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            length = length
                        )
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
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            weight = weight
                        )
                    )
                ),
                weightError = if (it.wasEdited) inputValidator.validateNumberPositive(weight.toInt()) else null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetWidth(width: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            width = width
                        )
                    )
                ),
                widthError = if (it.wasEdited) inputValidator.validateNumberPositive(width.toInt()) else null
            ).wasEdited()

        }
    }

    private suspend fun InputScope.handleSetRequiresShipping(requiresShipping: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        shipping = it.current.product.shipping.copy(
                            requiresShipping = requiresShipping
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetImages(images: List<ProductGetByIdQuery.Image>) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        data = it.current.product.data.copy(
                            images = images
                        )
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetId(id: String) {
        updateState {
            it.copy(
                current = it.current.copy(product = it.current.product.copy(id = id))
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetUpdatedAt(updatedAt: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(common = it.current.product.common.copy(updatedAt = updatedAt))
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCreator(creator: ProductGetByIdQuery.Creator) {
        updateState { it.copy(current = it.current.copy(creator = creator)).wasEdited() }
    }

    private suspend fun InputScope.handleSetCreatedAt(createdAt: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(common = it.current.product.common.copy(createdAt = createdAt))
                )
            ).wasEdited()
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
                        val createdAt = millisToTime(data.getProductById.product.common.createdAt.toLong())
                        val updatedAt = millisToTime(data.getProductById.product.common.updatedAt.toLong())
                        val originalProduct = data.getProductById.copy(
                            product = data.getProductById.product.copy(
                                common = data.getProductById.product.common.copy(
                                    shortDescription = data.getProductById.product.common.shortDescription ?: "",
                                    categories = data.getProductById.product.common.categories.map { it.toString() },
                                    createdAt = createdAt,
                                    updatedAt = updatedAt,
                                ),
                                data = data.getProductById.product.data.copy(
                                    description = data.getProductById.product.data.description ?: ""
                                ),
                                price = data.getProductById.product.price.copy(
                                    price = data.getProductById.product.price.price ?: "",
                                    regularPrice = data.getProductById.product.price.regularPrice ?: "",
                                    salePrice = data.getProductById.product.price.salePrice ?: "",
                                ),
                            )
                        )
                        postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(originalProduct))
                        postInput(AdminProductPageContract.Inputs.Set.CurrentProduct(originalProduct))
                        postInput(AdminProductPageContract.Inputs.Set.LocalImages(originalProduct.product.data.images))
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
        val input = ProductCreateInput(name = state.current.product.common.name)

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

    private suspend fun InputScope.handleUploadImage(file: ImageFile) {
        val state = getCurrentState()
        sideJob("handleSaveDetailsUploadImage") {
            productService.uploadImage(state.current.product.id.toString(), "", file).fold(
                onSuccess = { data ->
                    println("Image uploaded successfully. Message: $data")
                    postInput(AdminProductPageContract.Inputs.Get.ProductById(state.current.product.id.toString()))
                },
                onFailure = {
                    postEvent(AdminProductPageContract.Events.OnError(it.message ?: "Error while uploading image"))
                },
            )
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
                        id = current.product.id.toString(),
                        name = if (current.product.common.name != original.product.common.name) current.product.common.name else null,
                        shortDescription = if (current.product.common.shortDescription != original.product.common.shortDescription) {
                            current.product.common.shortDescription
                        } else null,
                        isFeatured = if (current.product.common.isFeatured != original.product.common.isFeatured) {
                            current.product.common.isFeatured
                        } else null,
                        allowReviews = if (current.product.common.allowReviews != original.product.common.allowReviews) {
                            current.product.common.allowReviews
                        } else null,
                        catalogVisibility = if (current.product.common.catalogVisibility != original.product.common.catalogVisibility) {
                            current.product.common.catalogVisibility
                        } else null,
                        categories = if (current.product.common.categories.map { it.toString() } != original.product.common.categories.map { it.toString() }) {
                            current.product.common.categories.map { it.toString() }
                        } else null,
                        tags = if (current.product.common.tags.map { it.toString() } != original.product.common.tags.map { it.toString() }) {
                            current.product.common.tags.map { it.toString() }
                        } else null,
                        description = if (current.product.data.description != original.product.data.description)
                            current.product.data.description else null,
                        isPurchasable = if (current.product.data.isPurchasable != original.product.data.isPurchasable) {
                            current.product.data.isPurchasable
                        } else null,
                        images = if (current.product.data.images.map { it.id } != original.product.data.images.map { it.id }) {
                            current.product.data.images.map {
                                ProductImageInput(
                                    id = it.id,
                                    name = Optional.absent(),
                                    url = it.url,
                                    altText = it.altText,
                                    created = it.created,
                                    modified = it.modified,
                                )
                            }
                        } else null,
                        postStatus = if (current.product.data.postStatus != original.product.data.postStatus) {
                            current.product.data.postStatus
                        } else null,
                        onePerOrder = if (current.product.inventory.onePerOrder != original.product.inventory.onePerOrder) {
                            current.product.inventory.onePerOrder
                        } else null,
                        backorderStatus = if (current.product.inventory.backorderStatus != original.product.inventory.backorderStatus) {
                            current.product.inventory.backorderStatus
                        } else null,
                        canBackorder = if (current.product.inventory.canBackorder != original.product.inventory.canBackorder) {
                            current.product.inventory.canBackorder
                        } else null,
                        isOnBackorder = if (current.product.inventory.isOnBackorder != original.product.inventory.isOnBackorder) {
                            current.product.inventory.isOnBackorder
                        } else null,
                        lowStockThreshold = if (current.product.inventory.lowStockThreshold != original.product.inventory.lowStockThreshold) {
                            current.product.inventory.lowStockThreshold
                        } else null,
                        remainingStock = if (current.product.inventory.remainingStock != original.product.inventory.remainingStock) {
                            current.product.inventory.remainingStock
                        } else null,
                        stockStatus = if (current.product.inventory.stockStatus != original.product.inventory.stockStatus) {
                            current.product.inventory.stockStatus
                        } else null,
                        trackInventory = if (current.product.inventory.trackInventory != original.product.inventory.trackInventory) {
                            current.product.inventory.trackInventory
                        } else null,
                        price = if (current.product.price.price != original.product.price.price) current.product.price.price else null,
                        regularPrice = if (current.product.price.regularPrice != original.product.price.regularPrice) {
                            current.product.price.regularPrice
                        } else null,
                        salePrice = if (current.product.price.salePrice != original.product.price.salePrice) current.product.price.salePrice else null,
                        onSale = if (current.product.price.onSale != original.product.price.onSale) current.product.price.onSale else null,
                        saleStart = if (current.product.price.saleStart != original.product.price.saleStart) current.product.price.saleStart else null,
                        saleEnd = if (current.product.price.saleEnd != original.product.price.saleEnd) current.product.price.saleEnd else null,
                        presetId = if (current.product.shipping.presetId != original.product.shipping.presetId) current.product.shipping.presetId?.toString() else null,
                        height = if (current.product.shipping.height != original.product.shipping.height) current.product.shipping.height else null,
                        length = if (current.product.shipping.length != original.product.shipping.length) current.product.shipping.length else null,
                        weight = if (current.product.shipping.weight != original.product.shipping.weight) current.product.shipping.weight else null,
                        width = if (current.product.shipping.width != original.product.shipping.width) current.product.shipping.width else null,
                        requiresShipping = if (current.product.shipping.requiresShipping != original.product.shipping.requiresShipping) {
                            current.product.shipping.requiresShipping
                        } else null,
                    ).fold(
                        onSuccess = { data ->
                            val images = data.updateProduct.data.images.map {
                                ProductGetByIdQuery.Image(
                                    id = it.id,
                                    name = it.name,
                                    url = it.url,
                                    altText = it.altText,
                                    created = it.created,
                                    modified = it.modified,
                                )
                            }
                            postInput(
                                AdminProductPageContract.Inputs.Set.OriginalProduct(
                                    product = ProductGetByIdQuery.GetProductById(
                                        product = ProductGetByIdQuery.Product(
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
                                                images = images,
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
        val id = getCurrentState().current.product.id.toString()
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
                current = it.current.copy(product = it.current.product.copy(common = it.current.product.common.copy(name = name))),
                nameError = if (!it.wasEdited) null else isValidated,
                isCreateDisabled = isValidated != null
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetShortDescription(shortDescription: String) {
        updateStateAndGet {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        common = it.current.product.common.copy(shortDescription = shortDescription)
                    )
                ),
                shortDescriptionError = if (it.wasEdited) null else inputValidator.validateText(shortDescription),
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetIsFeatured(featured: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(common = it.current.product.common.copy(isFeatured = featured))
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetAllowReviews(allowReviews: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        common = it.current.product.common.copy(allowReviews = allowReviews)
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCatalogVisibility(catalogVisibility: CatalogVisibility) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        common = it.current.product.common.copy(catalogVisibility = catalogVisibility)
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetStatusOfPost(postStatus: PostStatus) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(data = it.current.product.data.copy(postStatus = postStatus))
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetDescription(description: String) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(data = it.current.product.data.copy(description = description))
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetIsPurchasable(isPurchasable: Boolean) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(
                        data = it.current.product.data.copy(isPurchasable = isPurchasable)
                    )
                )
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleSetCategories(categories: List<String>) {
        updateState {
            it.copy(
                current = it.current.copy(
                    product = it.current.product.copy(common = it.current.product.common.copy(categories = categories))
                )
            ).wasEdited()
        }
    }

    private fun AdminProductPageContract.State.wasEdited(): AdminProductPageContract.State =
        copy(wasEdited = current != original)
}
