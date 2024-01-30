package feature.admin.product.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.ProductService
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

    override suspend fun InputHandlerScope<AdminProductPageContract.Inputs, AdminProductPageContract.Events, AdminProductPageContract.State>.handleInput(
        input: AdminProductPageContract.Inputs,
    ) = when (input) {
        is AdminProductPageContract.Inputs.Init -> handleInit(input.productId)
        AdminProductPageContract.Inputs.CreateNewProduct -> handleCreateNewProduct()
        AdminProductPageContract.Inputs.DeleteProduct -> handleDeleteProduct()
        is AdminProductPageContract.Inputs.GetProductById -> handleGetProductById(input.id)

        is AdminProductPageContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminProductPageContract.Inputs.SetProduct -> updateState { it.copy(original = input.product) }
        is AdminProductPageContract.Inputs.SetScreenState -> updateState { it.copy(screenState = input.screenState) }

        // Common
        is AdminProductPageContract.Inputs.SetId -> updateState { it.copy(id = input.id) }
        is AdminProductPageContract.Inputs.SetName -> handleSetName(input.name)
        is AdminProductPageContract.Inputs.SetNameShake -> updateState { it.copy(shakeName = input.shake) }
        is AdminProductPageContract.Inputs.SetShortDescription -> handleSetShortDescription(input.shortDescription)

        is AdminProductPageContract.Inputs.SetShortDescriptionShake ->
            updateState { it.copy(shakeShortDescription = input.shake) }

        is AdminProductPageContract.Inputs.SetCommonDetailsButtonDisabled ->
            updateState { it.copy(isSaveCommonDetailsButtonDisabled = input.isDisabled) }

        is AdminProductPageContract.Inputs.SetCommonDetailsEditable ->
            updateState { it.copy(isCommonDetailsEditing = input.isEditable) }

        AdminProductPageContract.Inputs.SaveCommonDetails -> handleSaveCommonDetails()

        is AdminProductPageContract.Inputs.SetCreatedAt -> updateState { it.copy(createdAt = input.createdAt) }
        is AdminProductPageContract.Inputs.SetCreatedBy -> updateState { it.copy(createdBy = input.createdBy) }
        is AdminProductPageContract.Inputs.SetUpdatedAt -> updateState { it.copy(updatedAt = input.updatedAt) }
    }

    private suspend fun InputScope.handleSaveCommonDetails() {
        with(getCurrentState()) {
            val isNameError = nameError != null
            val isShortDescriptionError = shortDescriptionError != null
            val isNoError = !isNameError && !isShortDescriptionError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isNameError) postInput(AdminProductPageContract.Inputs.SetNameShake(shake = true))
                    if (isShortDescriptionError) {
                        postInput(AdminProductPageContract.Inputs.SetShortDescriptionShake(shake = true))
                    }

                    delay(SHAKE_ANIM_DURATION)

                    if (isNameError) postInput(AdminProductPageContract.Inputs.SetNameShake(shake = false))
                    if (isShortDescriptionError) {
                        postInput(AdminProductPageContract.Inputs.SetShortDescriptionShake(shake = false))
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
                        ).fold(
                            onSuccess = { data ->
                                postInput(
                                    AdminProductPageContract.Inputs.SetProduct(
                                        product = this@with.original.copy(
                                            product = this@with.original.product.copy(
                                                common = this@with.original.product.common.copy(
                                                    name = data.updateProduct.common.name,
                                                    shortDescription = data.updateProduct.common.shortDescription,
                                                    createdBy = data.updateProduct.common.createdBy,
                                                    createdAt = data.updateProduct.common.createdAt,
                                                    updatedAt = data.updateProduct.common.updatedAt,
                                                ),
                                            ),
                                        )
                                    )
                                )
                                postInput(AdminProductPageContract.Inputs.SetCommonDetailsButtonDisabled(isDisabled = true))
                                postInput(AdminProductPageContract.Inputs.SetCommonDetailsEditable(isEditable = false))
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

    private suspend fun InputScope.handleSetShortDescription(shortDescription: String) {
        with(shortDescription) {
            updateState {
                val hasChanged =
                    shortDescription != it.original.product.common.shortDescription ||
                        it.name != it.original.product.common.name
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

    private suspend fun InputScope.handleGetProductById(id: String) {
        sideJob("handleGetProduct") {
            productService.getById(id).collect { result ->
                result.fold(
                    onSuccess = { data ->
                        println("data.getProductById ${data.getProductById}")

                        postInput(AdminProductPageContract.Inputs.SetProduct(data.getProductById))

                        postInput(AdminProductPageContract.Inputs.SetName(data.getProductById.product.common.name))
                        postInput(
                            AdminProductPageContract.Inputs.SetShortDescription(
                                data.getProductById.product.common.shortDescription ?: ""
                            )
                        )
                        postInput(AdminProductPageContract.Inputs.SetCommonDetailsButtonDisabled(isDisabled = true))

                        try {
                            postInput(
                                AdminProductPageContract.Inputs.SetCreatedAt(
                                    data.getProductById.product.common.createdAt.toLong()
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        postInput(
                            AdminProductPageContract.Inputs.SetCreatedBy(
                                data.getProductById.product.common.createdBy.toString()
                            )
                        )
                        try {
                            postInput(
                                AdminProductPageContract.Inputs.SetUpdatedAt(
                                    data.getProductById.product.common.updatedAt.toLong()
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
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

    private suspend fun InputScope.handleSetName(name: String) {
        with(name) {
            updateState {
                val hasChanged = if (it.screenState is AdminProductPageContract.ScreenState.New) {
                    name != it.original.product.common.name
                } else {
                    name != it.original.product.common.name ||
                        it.shortDescription != it.original.product.common.shortDescription
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

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            if (id == null) {
                postInput(AdminProductPageContract.Inputs.SetScreenState(AdminProductPageContract.ScreenState.New))
            } else {
                postInput(AdminProductPageContract.Inputs.SetLoading(isLoading = true))
                postInput(AdminProductPageContract.Inputs.SetId(id))
                postInput(AdminProductPageContract.Inputs.SetScreenState(AdminProductPageContract.ScreenState.Existing.Read))
                postInput(AdminProductPageContract.Inputs.GetProductById(id))
                postInput(AdminProductPageContract.Inputs.SetLoading(isLoading = false))
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

    private suspend fun InputScope.handleCreateNewProduct() {
        val state = getCurrentState()
        val input = ProductCreateInput(
            name = state.name,
        )
        sideJob("handleCreateNewUser") {
            productService.create(input).fold(
                onSuccess = { data ->
                    postInput(
                        AdminProductPageContract.Inputs.SetProduct(
                            product = state.original.copy(
                                product = state.original.product.copy(
                                    common = state.original.product.common.copy(
                                        name = data.createProduct.name,
                                        createdBy = data.createProduct.createdBy,
                                        createdAt = data.createProduct.createdAt,
                                        updatedAt = data.createProduct.createdAt,
                                    ),
                                )
                            )
                        )
                    )
                    postInput(AdminProductPageContract.Inputs.SetId(data.createProduct.id))
                    postInput(
                        AdminProductPageContract.Inputs.SetScreenState(
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
}
