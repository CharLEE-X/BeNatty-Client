package feature.admin.product.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.util.millisToTime
import data.ProductGetByIdQuery
import data.service.CategoryService
import data.service.ProductService
import data.type.CatalogVisibility
import data.type.PostStatus
import data.type.ProductCreateInput
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

    override suspend fun InputHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>.handleInput(
        input: AdminProductPageContract.Inputs,
    ) = when (input) {
        is AdminProductPageContract.Inputs.Init -> handleInit(input.productId)

        is AdminProductPageContract.Inputs.Get.ProductById -> handleGetProductById(input.id)
        AdminProductPageContract.Inputs.Get.AllCategories -> handleGetAllCategories()

        AdminProductPageContract.Inputs.OnClick.Create -> handleCreateNewProduct()
        AdminProductPageContract.Inputs.OnClick.Delete -> handleDeleteProduct()
        AdminProductPageContract.Inputs.OnClick.SaveCommonDetails -> handleSaveCommonDetails()
        AdminProductPageContract.Inputs.OnClick.EditDetails -> handleEditDetails()
        AdminProductPageContract.Inputs.OnClick.EditData -> handleEditData()
        AdminProductPageContract.Inputs.OnClick.CancelEditDetails -> handleCancelEditDetails()
        AdminProductPageContract.Inputs.OnClick.CancelEditData -> handleCancelEditData()
        is AdminProductPageContract.Inputs.OnClick.Category -> handleOnClickCategory(input.category)
        is AdminProductPageContract.Inputs.OnClick.Creator -> {
            val userId = getCurrentState().creator.id.toString()
            postEvent(AdminProductPageContract.Events.GoToUserDetails(userId))
        }

        is AdminProductPageContract.Inputs.Set.AllCategories -> updateState { it.copy(allCategories = input.categories) }
        is AdminProductPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductPageContract.Inputs.Set.OriginalProduct -> updateState { it.copy(original = input.product) }
        is AdminProductPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(screenState = input.screenState) }
        is AdminProductPageContract.Inputs.Set.Id -> updateState { it.copy(id = input.id) }
        is AdminProductPageContract.Inputs.Set.Name -> handleSetName(input.name)
        is AdminProductPageContract.Inputs.Set.NameShake -> updateState { it.copy(shakeName = input.shake) }
        is AdminProductPageContract.Inputs.Set.ShortDescription -> handleSetShortDescription(input.shortDescription)
        is AdminProductPageContract.Inputs.Set.ShortDescriptionShake -> updateState { it.copy(shakeShortDescription = input.shake) }
        is AdminProductPageContract.Inputs.Set.IsFeatured -> handleSetIsFeatured(input.isFeatured)
        is AdminProductPageContract.Inputs.Set.AllowReviews -> handleSetAllowReviews(input.allowReviews)
        is AdminProductPageContract.Inputs.Set.VisibilityInCatalog -> handleSetCatalogVisibility(input.catalogVisibility)
        is AdminProductPageContract.Inputs.Set.IsCommonDetailsButtonDisabled -> updateState {
            it.copy(isSaveCommonDetailsButtonDisabled = input.isDisabled)
        }

        is AdminProductPageContract.Inputs.Set.IsCommonDetailsEditable ->
            updateState { it.copy(isCommonDetailsEditing = input.isEditable) }

        is AdminProductPageContract.Inputs.Set.CreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminProductPageContract.Inputs.Set.UpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
        is AdminProductPageContract.Inputs.Set.Creator -> updateState { it.copy(creator = input.creator) }
        is AdminProductPageContract.Inputs.Set.Categories -> updateState { it.copy(categories = input.categories) }
        is AdminProductPageContract.Inputs.Set.StatusOfPost -> handleSetStatusOfPost(input.postStatus)
        is AdminProductPageContract.Inputs.Set.Description -> handleSetDescription(input.description)
        is AdminProductPageContract.Inputs.Set.DescriptionShake -> updateState { it.copy(shakeDescription = input.shake) }
        is AdminProductPageContract.Inputs.Set.IsPurchasable -> handleSetIsPurchasable(input.isPurchasable)

        is AdminProductPageContract.Inputs.Set.IsDataButtonDisabled ->
            updateState { it.copy(isSaveDataButtonDisabled = input.isDisabled) }

        is AdminProductPageContract.Inputs.Set.IsDataEditable -> updateState { it.copy(isDataEditing = input.isEditable) }
        AdminProductPageContract.Inputs.OnClick.SaveDataDetails -> handleSaveDataDetails()
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminProductPageContract.Inputs.Set.StateOfScreen(AdminProductPageContract.ScreenState.New))
            } else {
                postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminProductPageContract.Inputs.Get.ProductById(id))
                postInput(AdminProductPageContract.Inputs.Set.StateOfScreen(AdminProductPageContract.ScreenState.Existing.Read))
                postInput(AdminProductPageContract.Inputs.Set.Loading(isLoading = false))
                postInput(AdminProductPageContract.Inputs.Set.IsCommonDetailsEditable(isEditable = true))
                postInput(AdminProductPageContract.Inputs.Set.IsCommonDetailsEditable(isEditable = false))
            }
            postInput(AdminProductPageContract.Inputs.Get.AllCategories)
        }
    }

    private suspend fun InputScope.handleGetAllCategories() {
        val state = getCurrentState()
        sideJob("handleGetAllCategories") {
            categoryService.getCategoriesAllMinimal().fold(
                onSuccess = { data ->
                    val categories = data.getCategoriesAllMinimal
                        .filter { it.id != state.id }
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

    private suspend fun InputScope.handleGetProductById(id: String) {
        sideJob("handleGetProduct") {
            productService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        // COMMON
                        val originalProduct = data.getProductById.copy(
                            product = data.getProductById.product.copy(
                                data = data.getProductById.product.data.copy(
                                    description = data.getProductById.product.data.description ?: ""
                                )
                            )
                        )
                        postInput(AdminProductPageContract.Inputs.Set.OriginalProduct(originalProduct))

                        postInput(AdminProductPageContract.Inputs.Set.Name(data.getProductById.product.common.name))
                        postInput(
                            AdminProductPageContract.Inputs.Set.ShortDescription(
                                data.getProductById.product.common.shortDescription ?: ""
                            )
                        )
                        postInput(
                            AdminProductPageContract.Inputs.Set.IsFeatured(data.getProductById.product.common.isFeatured)
                        )
                        postInput(
                            AdminProductPageContract.Inputs.Set.AllowReviews(
                                data.getProductById.product.common.allowReviews
                            )
                        )
                        postInput(
                            AdminProductPageContract.Inputs.Set.VisibilityInCatalog(
                                data.getProductById.product.common.catalogVisibility
                            )
                        )
                        postInput(AdminProductPageContract.Inputs.Set.IsCommonDetailsButtonDisabled(isDisabled = true))
                        postInput(AdminProductPageContract.Inputs.Set.Creator(data.getProductById.creator))
                        postInput(AdminProductPageContract.Inputs.Set.Categories(
                            data.getProductById.product.common.categories.map { it.toString() }
                        ))

                        try {
                            val createdAt = millisToTime(data.getProductById.product.common.createdAt.toLong())
                            postInput(AdminProductPageContract.Inputs.Set.CreatedAt(createdAt))

                            val updatedAt = millisToTime(data.getProductById.product.common.updatedAt.toLong())
                            postInput(AdminProductPageContract.Inputs.Set.UpdatedAt(updatedAt))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        // DATA
                        postInput(
                            AdminProductPageContract.Inputs.Set.StatusOfPost(data.getProductById.product.data.postStatus)
                        )
                        postInput(
                            AdminProductPageContract.Inputs.Set.Description(
                                data.getProductById.product.data.description ?: ""
                            )
                        )
                        postInput(
                            AdminProductPageContract.Inputs.Set.IsPurchasable(data.getProductById.product.data.isPurchasable)
                        )
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
            name = state.name,
        )
        sideJob("handleCreateNewUser") {
            productService.create(input).fold(
                onSuccess = { data ->
                    val createdAt = millisToTime(data.createProduct.createdAt.toLong())
                    postInput(
                        AdminProductPageContract.Inputs.Set.OriginalProduct(
                            product = state.original.copy(
                                product = state.original.product.copy(
                                    common = state.original.product.common.copy(
                                        name = data.createProduct.name,
                                        createdBy = data.createProduct.createdBy,
                                        createdAt = createdAt,
                                        updatedAt = createdAt,
                                    ),
                                )
                            )
                        )
                    )

                    postInput(AdminProductPageContract.Inputs.Set.Id(data.createProduct.id))
                    postInput(AdminProductPageContract.Inputs.Set.CreatedAt(createdAt))
                    postInput(AdminProductPageContract.Inputs.Set.UpdatedAt(createdAt))
                    postInput(
                        AdminProductPageContract.Inputs.Set.StateOfScreen(
                            AdminProductPageContract.ScreenState.Existing.Edit
                        )
                    )
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

    private suspend fun InputScope.handleEditDetails() {
        updateState { it.copy(isCommonDetailsEditing = true) }
    }

    private suspend fun InputScope.handleEditData() {
        updateState { it.copy(isDataEditing = true) }
    }

    private suspend fun InputScope.handleCancelEditData() {
        updateState {
            it.copy(
                description = it.original.product.data.description ?: "",
                isDataEditing = false,
            )
        }
    }

    private suspend fun InputScope.handleCancelEditDetails() {
        updateState {
            it.copy(
                name = it.original.product.common.name,
                shortDescription = it.original.product.common.shortDescription ?: "",
                isFeatured = it.original.product.common.isFeatured,
                allowReviews = it.original.product.common.allowReviews,
                catalogVisibility = it.original.product.common.catalogVisibility,
                categories = it.original.product.common.categories.map { it.toString() },
                isCommonDetailsEditing = false,
            )
        }
    }


    private suspend fun InputScope.handleSaveCommonDetails() {
        with(getCurrentState()) {
            val isNameError = nameError != null
            val isShortDescriptionError = shortDescriptionError != null
            val isNoError = !isNameError && !isShortDescriptionError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isNameError) postInput(AdminProductPageContract.Inputs.Set.NameShake(shake = true))
                    if (isShortDescriptionError) {
                        postInput(AdminProductPageContract.Inputs.Set.ShortDescriptionShake(shake = true))
                    }

                    delay(SHAKE_ANIM_DURATION)

                    if (isNameError) postInput(AdminProductPageContract.Inputs.Set.NameShake(shake = false))
                    if (isShortDescriptionError) {
                        postInput(AdminProductPageContract.Inputs.Set.ShortDescriptionShake(shake = false))
                    }
                }
            } else {
                id?.let { id ->
                    sideJob("handleSavePersonalDetails") {
                        productService.update(
                            id = id,
                            name = if (name != original.product.common.name) name else null,
                            shortDescription = if (shortDescription != original.product.common.shortDescription) {
                                shortDescription
                            } else null,
                            isFeatured = if (isFeatured != original.product.common.isFeatured) isFeatured else null,
                            allowReviews = if (allowReviews != original.product.common.allowReviews) {
                                allowReviews
                            } else null,
                            catalogVisibility = if (catalogVisibility != original.product.common.catalogVisibility) {
                                catalogVisibility
                            } else null,
                            categories = if (categories != original.product.common.categories.map { it.toString() }) {
                                categories
                            } else null,
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminProductPageContract.Inputs.Set.OriginalProduct(
                                        product = this@with.original.copy(
                                            product = this@with.original.product.copy(
                                                common = this@with.original.product.common.copy(
                                                    name = data.updateProduct.common.name,
                                                    shortDescription = data.updateProduct.common.shortDescription,
                                                    isFeatured = data.updateProduct.common.isFeatured,
                                                    allowReviews = data.updateProduct.common.allowReviews,
                                                    catalogVisibility = data.updateProduct.common.catalogVisibility,
                                                    categories = data.updateProduct.common.categories.map { it.toString() },
                                                    createdBy = data.updateProduct.common.createdBy,
                                                    createdAt = data.updateProduct.common.createdAt,
                                                    updatedAt = data.updateProduct.common.updatedAt,
                                                ),
                                            ),
                                        ),
                                    )
                                )
                                postInput(AdminProductPageContract.Inputs.Set.IsCommonDetailsButtonDisabled(isDisabled = true))
                                postInput(AdminProductPageContract.Inputs.Set.IsCommonDetailsEditable(isEditable = false))
                            },
                            onFailure = {
                                postEvent(
                                    AdminProductPageContract.Events.OnError(
                                        it.message ?: "Error while updating personal details"
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    private suspend fun InputScope.handleSaveDataDetails() {
        with(getCurrentState()) {
            val isDescriptionsError = descriptionError != null
            val isNoError = !isDescriptionsError

            if (!isNoError) {
                sideJob("handleSaveDataShakes") {
                    if (isDescriptionsError) postInput(AdminProductPageContract.Inputs.Set.DescriptionShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isDescriptionsError) postInput(AdminProductPageContract.Inputs.Set.DescriptionShake(shake = false))
                }
            } else {
                id?.let { id ->
                    sideJob("handleSaveProductData") {
                        productService.update(
                            id = id,
                            description = if (description != original.product.data.description) description else null,
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
                                        product = this@with.original.copy(
                                            product = this@with.original.product.copy(
                                                data = this@with.original.product.data.copy(
                                                    postStatus = data.updateProduct.data.postStatus,
                                                    description = data.updateProduct.data.description,
                                                    isPurchasable = data.updateProduct.data.isPurchasable,
                                                    images = images,
                                                    parentId = data.updateProduct.data.parentId,
                                                ),
                                            ),
                                        ),
                                    )
                                )
                                postInput(AdminProductPageContract.Inputs.Set.IsDataButtonDisabled(isDisabled = true))
                                postInput(AdminProductPageContract.Inputs.Set.IsDataEditable(isEditable = false))
                            },
                            onFailure = {
                                postEvent(
                                    AdminProductPageContract.Events.OnError(
                                        it.message ?: "Error while updating personal details"
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }

    private suspend fun InputScope.handleDeleteProduct() {
        getCurrentState().id?.let { id ->
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
    }

    private suspend fun InputScope.handleSetName(name: String) {
        with(name) {
            updateState {
                val hasChanged = if (it.screenState is AdminProductPageContract.ScreenState.New) {
                    name != it.original.product.common.name
                } else {
                    name != it.original.product.common.name ||
                        it.shortDescription != it.original.product.common.shortDescription ||
                        it.isFeatured != it.original.product.common.isFeatured ||
                        it.allowReviews != it.original.product.common.allowReviews ||
                        it.catalogVisibility != it.original.product.common.catalogVisibility ||
                        it.categories != it.original.product.common.categories.map { it.toString() } ||
                        it.postStatus != it.original.product.data.postStatus
                }
                it.copy(
                    name = this,
                    nameError = if (it.isCreateProductButtonDisabled) null else {
                        inputValidator.validateText(this)
                    },
                    isCreateProductButtonDisabled = !hasChanged,
                    isSaveCommonDetailsButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun InputScope.handleSetShortDescription(shortDescription: String) {
        with(shortDescription) {
            updateState {
                val hasChanged = it.name != it.original.product.common.name ||
                    shortDescription != it.original.product.common.shortDescription ||
                    it.isFeatured != it.original.product.common.isFeatured ||
                    it.allowReviews != it.original.product.common.allowReviews ||
                    it.catalogVisibility != it.original.product.common.catalogVisibility ||
                    it.categories != it.original.product.common.categories.map { it.toString() } ||
                    it.postStatus != it.original.product.data.postStatus
                it.copy(
                    shortDescription = this,
                    shortDescriptionError = if (it.isSaveCommonDetailsButtonDisabled) null else {
                        inputValidator.validateText(this)
                    },
                    isSaveCommonDetailsButtonDisabled = !hasChanged,
                )
            }
        }
    }

    private suspend fun InputScope.handleSetIsFeatured(featured: Boolean) {
        updateState {
            val hasChanged = it.name != it.original.product.common.name ||
                it.shortDescription != it.original.product.common.shortDescription ||
                featured != it.original.product.common.isFeatured ||
                it.allowReviews != it.original.product.common.allowReviews ||
                it.catalogVisibility != it.original.product.common.catalogVisibility ||
                it.categories != it.original.product.common.categories.map { it.toString() } ||
                it.postStatus != it.original.product.data.postStatus
            it.copy(
                isFeatured = featured,
                isSaveCommonDetailsButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleSetAllowReviews(allowReviews: Boolean) {
        updateState {
            val hasChanged = it.name != it.original.product.common.name ||
                it.shortDescription != it.original.product.common.shortDescription ||
                it.isFeatured != it.original.product.common.isFeatured ||
                allowReviews != it.original.product.common.allowReviews ||
                it.catalogVisibility != it.original.product.common.catalogVisibility ||
                it.categories != it.original.product.common.categories.map { it.toString() } ||
                it.postStatus != it.original.product.data.postStatus
            it.copy(
                allowReviews = allowReviews,
                isSaveCommonDetailsButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleSetCatalogVisibility(catalogVisibility: CatalogVisibility) {
        updateState {
            val hasChanged = it.name != it.original.product.common.name ||
                it.shortDescription != it.original.product.common.shortDescription ||
                it.isFeatured != it.original.product.common.isFeatured ||
                it.allowReviews != it.original.product.common.allowReviews ||
                catalogVisibility != it.original.product.common.catalogVisibility ||
                it.categories != it.original.product.common.categories.map { it.toString() } ||
                it.postStatus != it.original.product.data.postStatus
            it.copy(
                catalogVisibility = catalogVisibility,
                isSaveCommonDetailsButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleOnClickCategory(category: String) {
        updateState {
            val categories = it.categories.toMutableList()
            if (categories.contains(category)) categories.remove(category) else categories.add(category)

            val hasChanged = it.name != it.original.product.common.name ||
                it.shortDescription != it.original.product.common.shortDescription ||
                it.isFeatured != it.original.product.common.isFeatured ||
                it.allowReviews != it.original.product.common.allowReviews ||
                it.catalogVisibility != it.original.product.common.catalogVisibility ||
                categories != it.original.product.common.categories.map { it.toString() } ||
                it.postStatus != it.original.product.data.postStatus

            it.copy(
                categories = categories,
                isSaveCommonDetailsButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleSetStatusOfPost(postStatus: PostStatus) {
        updateState {
            val hasChanged = it.name != it.original.product.common.name ||
                it.shortDescription != it.original.product.common.shortDescription ||
                it.isFeatured != it.original.product.common.isFeatured ||
                it.allowReviews != it.original.product.common.allowReviews ||
                it.catalogVisibility != it.original.product.common.catalogVisibility ||
                it.categories != it.original.product.common.categories.map { it.toString() } ||
                postStatus != it.original.product.data.postStatus
            it.copy(
                postStatus = postStatus,
                isSaveCommonDetailsButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleSetDescription(description: String) {
        updateState {
            val hasChanged = description != it.original.product.data.description ||
                it.isPurchasable != it.original.product.data.isPurchasable
            it.copy(
                description = description,
                isSaveDataButtonDisabled = !hasChanged,
            )
        }
    }

    private suspend fun InputScope.handleSetIsPurchasable(isPurchasable: Boolean) {
        updateState {
            val hasChanged = it.description != it.original.product.data.description ||
                isPurchasable != it.original.product.data.isPurchasable
            it.copy(
                isPurchasable = isPurchasable,
                isSaveDataButtonDisabled = !hasChanged,
            )
        }
    }
}
