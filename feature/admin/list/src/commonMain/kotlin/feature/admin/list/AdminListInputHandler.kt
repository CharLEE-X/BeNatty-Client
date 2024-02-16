package feature.admin.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.util.millisToDate
import data.service.CategoryService
import data.service.OrderService
import data.service.ProductService
import data.service.TagService
import data.service.UserService
import data.type.SortDirection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminListContract.Inputs, AdminListContract.Events, AdminListContract.State>

internal class AdminListInputHandler :
    KoinComponent,
    InputHandler<AdminListContract.Inputs, AdminListContract.Events, AdminListContract.State> {

    private val userService: UserService by inject()
    private val productService: ProductService by inject()
    private val orderService: OrderService by inject()
    private val categoryService: CategoryService by inject()
    private val tagService: TagService by inject()

    override suspend fun InputScope.handleInput(input: AdminListContract.Inputs) = when (input) {
        is AdminListContract.Inputs.Get.Page -> handleGetPage(input.page)

        is AdminListContract.Inputs.Set -> when (input) {
            is AdminListContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
            is AdminListContract.Inputs.Set.PerPage -> updateState { it.copy(perPage = input.perPage) }
            is AdminListContract.Inputs.Set.Items ->
                updateState {
                    it.copy(
                        items = input.items,
                        showList = input.items.isNotEmpty(),
                        showNoItems = input.items.isEmpty()
                    )
                }

            is AdminListContract.Inputs.Set.Info ->
                updateState {
                    it.copy(
                        info = input.info,
                        pagesNumbers = listOfNotNull(input.info.prev, input.info.count, input.info.next),
                        showPrevious = input.info.prev != null,
                        showNext = input.info.next != null
                    )
                }

            is AdminListContract.Inputs.Set.Search -> updateState { it.copy(searchValue = input.search) }
            is AdminListContract.Inputs.Set.SortBy -> updateState { it.copy(sortBy = input.sortBy) }
            is AdminListContract.Inputs.Set.SortDirection -> updateState { it.copy(sortDirection = input.sortDirection) }
        }

        is AdminListContract.Inputs.OnChange -> when (input) {
            is AdminListContract.Inputs.OnChange.Search -> updateState { it.copy(searchValue = input.search) }
            is AdminListContract.Inputs.OnChange.PerPage -> handleOnChangePerPage(input.perPage)
        }

        AdminListContract.Inputs.OnCreateClick -> postEvent(AdminListContract.Events.GoTo.Create)
        is AdminListContract.Inputs.OnItemClick -> postEvent(AdminListContract.Events.GoTo.Detail(input.id))
        is AdminListContract.Inputs.OnPageClick -> handleGetPage(input.page)
        AdminListContract.Inputs.OnNextPageClick -> getCurrentState().info.next?.let { handleGetPage(it) } ?: noOp()
        is AdminListContract.Inputs.OnTopBarSlotClick -> handleClickSlot(input.slotName)
        AdminListContract.Inputs.SendSearch -> handleGetPage(0)
        AdminListContract.Inputs.OnPreviousPageClick -> getCurrentState().info.prev?.let { handleGetPage(it) } ?: noOp()
    }

    private suspend fun InputScope.handleClickSlot(slotName: String) {
        val state = getCurrentState()
        sideJob("handleClickSlot") {
            if (state.sortBy == slotName) {
                when (state.sortDirection) {
                    SortDirection.ASC -> {
                        postInput(AdminListContract.Inputs.Set.SortDirection(SortDirection.DESC))
                    }

                    SortDirection.DESC -> {
                        postInput(AdminListContract.Inputs.Set.SortDirection(SortDirection.ASC))
                    }

                    SortDirection.UNKNOWN__ -> {
                        // Do nothing
                    }
                }
            } else {
                postInput(AdminListContract.Inputs.Set.SortDirection(SortDirection.DESC))
                postInput(AdminListContract.Inputs.Set.SortBy(slotName))
            }
            postInput(AdminListContract.Inputs.Get.Page(0))
        }
    }

    private suspend fun InputScope.handleOnChangePerPage(perPage: Int) {
        sideJob("handleOnChangePerPage") {
            postInput(AdminListContract.Inputs.Set.PerPage(perPage))
            postInput(AdminListContract.Inputs.Get.Page(0))
        }
    }

    private suspend fun InputScope.handleGetPage(page: Int) {
        val state = getCurrentState()
        sideJob("handleGetItemsPage") {
            postInput(AdminListContract.Inputs.Set.Loading(true))
            when (state.dataType) {
                AdminListContract.DataType.USER -> {
                    userService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection
                    ).fold(
                        onSuccess = {
                            val items = it.getAllUsersPage.users.map { user ->
                                ListItem(
                                    id = user.id.toString(),
                                    slot1 = millisToDate(user.createdAt.toLong()),
                                    slot2 = user.email,
                                    slot3 = user.name,
                                    slot4 = user.totalOrders.toString(),
                                    slot5 = null,
                                    slot6 = null,
                                )
                            }
                            postInput(AdminListContract.Inputs.Set.Items(items))

                            val info = PageInfo(
                                count = it.getAllUsersPage.info.count,
                                pages = it.getAllUsersPage.info.pages,
                                prev = it.getAllUsersPage.info.prev,
                                next = it.getAllUsersPage.info.next,
                            )
                            postInput(AdminListContract.Inputs.Set.Info(info))
                        },
                        onFailure = { postEvent(AdminListContract.Events.OnError(it.message ?: "")) }
                    )
                }

                AdminListContract.DataType.PRODUCT -> {
                    productService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection
                    ).fold(
                        onSuccess = {
                            val items = it.getAllProductsPage.products.map { product ->
                                ListItem(
                                    id = product.id.toString(),
                                    slot1 = millisToDate(product.createdAt.toLong()),
                                    slot2 = product.mediaUrl,
                                    slot3 = product.title,
                                    slot4 = product.price?.toString(),
                                    slot5 = product.sold.toString(),
                                    slot6 = null,
                                )
                            }
                            postInput(AdminListContract.Inputs.Set.Items(items))

                            val info = PageInfo(
                                count = it.getAllProductsPage.info.count,
                                pages = it.getAllProductsPage.info.pages,
                                prev = it.getAllProductsPage.info.prev,
                                next = it.getAllProductsPage.info.next,
                            )
                            postInput(AdminListContract.Inputs.Set.Info(info))
                        },
                        onFailure = { postEvent(AdminListContract.Events.OnError(it.message ?: "")) }
                    )
                }

                AdminListContract.DataType.ORDER -> {
                    // FIXME: Use orderService
                    orderService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection
                    ).fold(
                        onSuccess = {
                            val items = it.getCategoriesAsPage.categories.map { order ->
                                ListItem(
                                    id = order.id.toString(),
                                    slot1 = order.name, // millisToMonthYear(order.createdAt.toLong())
                                    slot2 = order.name,
                                    slot3 = order.name,
                                    slot4 = order.name,
                                    slot5 = null,
                                    slot6 = null,
                                )
                            }
                            postInput(AdminListContract.Inputs.Set.Items(items))

                            val info = PageInfo(
                                count = it.getCategoriesAsPage.info.count,
                                pages = it.getCategoriesAsPage.info.pages,
                                prev = it.getCategoriesAsPage.info.prev,
                                next = it.getCategoriesAsPage.info.next,
                            )
                            postInput(AdminListContract.Inputs.Set.Info(info))
                        },
                        onFailure = { postEvent(AdminListContract.Events.OnError(it.message ?: "")) }
                    )
                }

                AdminListContract.DataType.CATEGORY -> {
                    categoryService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection
                    ).fold(
                        onSuccess = {
                            val items = it.getCategoriesAsPage.categories.map { category ->
                                ListItem(
                                    id = category.id.toString(),
                                    slot1 = millisToDate(category.createdAt.toLong()),
                                    slot2 = category.name,
                                    slot3 = category.description,
                                    slot4 = category.display.toString(),
                                    slot5 = category.usedInProducts.toString(),
                                    slot6 = null,
                                )
                            }
                            postInput(AdminListContract.Inputs.Set.Items(items))

                            val info = PageInfo(
                                count = it.getCategoriesAsPage.info.count,
                                pages = it.getCategoriesAsPage.info.pages,
                                prev = it.getCategoriesAsPage.info.prev,
                                next = it.getCategoriesAsPage.info.next,
                            )
                            postInput(AdminListContract.Inputs.Set.Info(info))
                        },
                        onFailure = { postEvent(AdminListContract.Events.OnError(it.message ?: "")) }
                    )
                }

                AdminListContract.DataType.TAG -> {
                    // FIXME: Use tagService
                    tagService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection
                    ).fold(
                        onSuccess = {
                            val items = it.getTagsAsPage.tags.map { tag ->
                                ListItem(
                                    id = tag.id.toString(),
                                    slot1 = millisToDate(tag.createdAt.toLong()),
                                    slot2 = tag.name,
                                    slot3 = tag.usedInProducts.toString(),
                                    slot4 = null,
                                    slot5 = null,
                                    slot6 = null,
                                )
                            }
                            postInput(AdminListContract.Inputs.Set.Items(items))

                            val info = PageInfo(
                                count = it.getTagsAsPage.info.count,
                                pages = it.getTagsAsPage.info.pages,
                                prev = it.getTagsAsPage.info.prev,
                                next = it.getTagsAsPage.info.next,
                            )
                            postInput(AdminListContract.Inputs.Set.Info(info))
                        },
                        onFailure = { postEvent(AdminListContract.Events.OnError(it.message ?: "")) }
                    )
                }
            }
            postInput(AdminListContract.Inputs.Set.Loading(false))
        }
    }
}
