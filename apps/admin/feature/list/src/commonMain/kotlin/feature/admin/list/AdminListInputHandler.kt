package feature.admin.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import core.mapToUiMessage
import core.util.millisToDate
import data.service.CategoryService
import data.service.OrderService
import data.service.ProductService
import data.service.TagService
import data.service.UserService
import data.type.SortDirection
import feature.admin.list.AdminListContract.Events
import feature.admin.list.AdminListContract.Inputs
import feature.admin.list.AdminListContract.State
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<Inputs, Events, State>

internal class AdminListInputHandler : KoinComponent, InputHandler<Inputs, Events, State> {
    private val userService: UserService by inject()
    private val productService: ProductService by inject()
    private val orderService: OrderService by inject()
    private val categoryService: CategoryService by inject()
    private val tagService: TagService by inject()

    override suspend fun InputScope.handleInput(input: Inputs) =
        when (input) {
            is Inputs.Get.Page -> handleGetPage(input.page)

            is Inputs.Set ->
                when (input) {
                    is Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
                    is Inputs.Set.PerPage -> updateState { it.copy(perPage = input.perPage) }
                    is Inputs.Set.Items ->
                        updateState {
                            it.copy(
                                items = input.items,
                                showList = input.items.isNotEmpty(),
                                showNoItems = input.items.isEmpty(),
                            )
                        }

                    is Inputs.Set.Info ->
                        updateState {
                            it.copy(
                                info = input.info,
                                pagesNumbers = listOfNotNull(input.info.prev, input.info.count, input.info.next),
                                showPrevious = input.info.prev != null,
                                showNext = input.info.next != null,
                            )
                        }

                    is Inputs.Set.Search -> updateState { it.copy(searchValue = input.search) }
                    is Inputs.Set.SortBy -> updateState { it.copy(sortBy = input.sortBy) }
                    is Inputs.Set.SortDirection -> updateState { it.copy(sortDirection = input.sortDirection) }
                }

            is Inputs.OnChange ->
                when (input) {
                    is Inputs.OnChange.Search -> updateState { it.copy(searchValue = input.search) }
                    is Inputs.OnChange.PerPage -> handleOnChangePerPage(input.perPage)
                }

            is Inputs.Click ->
                when (input) {
                    Inputs.Click.Create ->
                        postEvent(Events.GoTo.Create)

                    is Inputs.Click.Item ->
                        postEvent(Events.GoTo.Detail(input.id))

                    is Inputs.Click.Page -> handleGetPage(input.page)
                    Inputs.Click.NextPage ->
                        getCurrentState().info.next?.let { handleGetPage(it) } ?: noOp()

                    Inputs.Click.PreviousPage ->
                        getCurrentState().info.prev?.let { handleGetPage(it) } ?: noOp()

                    is Inputs.Click.Slot -> handleClickSlot(input.slotName)
                }

            Inputs.SendSearch -> handleGetPage(0)
        }

    private suspend fun InputScope.handleClickSlot(slotName: String) {
        val state = getCurrentState()
        sideJob("handleClickSlot") {
            if (state.sortBy == slotName) {
                when (state.sortDirection) {
                    SortDirection.ASC -> {
                        postInput(Inputs.Set.SortDirection(SortDirection.DESC))
                    }

                    SortDirection.DESC -> {
                        postInput(Inputs.Set.SortDirection(SortDirection.ASC))
                    }

                    SortDirection.UNKNOWN__ -> {
                        // Do nothing
                    }
                }
            } else {
                postInput(Inputs.Set.SortDirection(SortDirection.DESC))
                postInput(Inputs.Set.SortBy(slotName))
            }
            postInput(Inputs.Get.Page(0))
        }
    }

    private suspend fun InputScope.handleOnChangePerPage(perPage: Int) {
        sideJob("handleOnChangePerPage") {
            postInput(Inputs.Set.PerPage(perPage))
            postInput(Inputs.Get.Page(0))
        }
    }

    private suspend fun InputScope.handleGetPage(page: Int) {
        val state = getCurrentState()
        sideJob("handleGetPage") {
            println("DEBUG Getting page: $page")

            postInput(Inputs.Set.Loading(true))
            when (state.dataType) {
                AdminListContract.DataType.Customer -> {
                    userService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection,
                    ).fold(
                        { postEvent(Events.OnError(it.mapToUiMessage())) },
                        {
                            val items =
                                it.getAllUsersAsPage.users.map { user ->
                                    ListItem(
                                        id = user.id,
                                        slot1 = millisToDate(user.createdAt.toLong()),
                                        slot2 = user.email,
                                        slot3 = user.name,
                                        slot4 = user.totalOrders.toString(),
                                        slot5 = null,
                                        slot6 = null,
                                    )
                                }
                            postInput(Inputs.Set.Items(items))

                            val info =
                                PageInfo(
                                    count = it.getAllUsersAsPage.info.count,
                                    pages = it.getAllUsersAsPage.info.pages,
                                    prev = it.getAllUsersAsPage.info.prev,
                                    next = it.getAllUsersAsPage.info.next,
                                )
                            postInput(Inputs.Set.Info(info))
                        },
                    )
                }

                AdminListContract.DataType.PRODUCT -> {
                    println("DEBUG Getting Products page")

                    productService.getAdminProductsAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection,
                    ).fold(
                        { postEvent(Events.OnError(it.mapToUiMessage())) },
                        {
                            with(it.getAllProductsAsPage) {
                                val items =
                                    products.map { product ->
                                        ListItem(
                                            id = product.id,
                                            slot1 = millisToDate(product.createdAt.toLong()),
                                            slot2 = product.mediaUrl,
                                            slot3 = product.name,
                                            slot4 = product.regularPrice.toString(),
                                            slot5 = product.sold.toString(),
                                            slot6 = null,
                                        )
                                    }
                                postInput(Inputs.Set.Items(items))

                                val info =
                                    PageInfo(
                                        count = info.count,
                                        pages = info.pages,
                                        prev = info.prev,
                                        next = info.next,
                                    )
                                postInput(Inputs.Set.Info(info))
                            }
                        },
                    )
                }

                AdminListContract.DataType.ORDER -> {
                    // FIXME: Use orderService
                    orderService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection,
                    ).fold(
                        { postEvent(Events.OnError(it.mapToUiMessage())) },
                        {
                            val items =
                                it.getAllCategoriesAsPage.categories.map { order ->
                                    ListItem(
                                        id = order.id,
                                        slot1 = order.name, // millisToMonthYear(order.createdAt.toLong())
                                        slot2 = order.name,
                                        slot3 = order.name,
                                        slot4 = order.name,
                                        slot5 = null,
                                        slot6 = null,
                                    )
                                }
                            postInput(Inputs.Set.Items(items))

                            val info =
                                PageInfo(
                                    count = it.getAllCategoriesAsPage.info.count,
                                    pages = it.getAllCategoriesAsPage.info.pages,
                                    prev = it.getAllCategoriesAsPage.info.prev,
                                    next = it.getAllCategoriesAsPage.info.next,
                                )
                            postInput(Inputs.Set.Info(info))
                        },
                    )
                }

                AdminListContract.DataType.CATEGORY -> {
                    categoryService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection,
                    ).fold(
                        { postEvent(Events.OnError(it.mapToUiMessage())) },
                        {
                            val items =
                                it.getAllCategoriesAsPage.categories.map { category ->
                                    ListItem(
                                        id = category.id,
                                        slot1 = millisToDate(category.createdAt.toLong()),
                                        slot2 = category.mediaUrl,
                                        slot3 = category.name,
                                        slot4 = category.display.toString(),
                                        slot5 = category.usedInProducts.toString(),
                                        slot6 = null,
                                    )
                                }
                            postInput(Inputs.Set.Items(items))

                            val info =
                                PageInfo(
                                    count = it.getAllCategoriesAsPage.info.count,
                                    pages = it.getAllCategoriesAsPage.info.pages,
                                    prev = it.getAllCategoriesAsPage.info.prev,
                                    next = it.getAllCategoriesAsPage.info.next,
                                )
                            postInput(Inputs.Set.Info(info))
                        },
                    )
                }

                AdminListContract.DataType.TAG -> {
                    // FIXME: Use tagService
                    tagService.getAsPage(
                        page = page,
                        size = state.perPage,
                        query = state.searchValue.ifBlank { null },
                        sortBy = state.sortBy,
                        sortDirection = state.sortDirection,
                    ).fold(
                        { postEvent(Events.OnError(it.mapToUiMessage())) },
                        {
                            val items =
                                it.getAllTagsAsPage.tags.map { tag ->
                                    ListItem(
                                        id = tag.id,
                                        slot1 = millisToDate(tag.createdAt.toLong()),
                                        slot2 = tag.name,
                                        slot3 = tag.usedInProducts.toString(),
                                        slot4 = null,
                                        slot5 = null,
                                        slot6 = null,
                                    )
                                }
                            postInput(Inputs.Set.Items(items))

                            val info =
                                PageInfo(
                                    count = it.getAllTagsAsPage.info.count,
                                    pages = it.getAllTagsAsPage.info.pages,
                                    prev = it.getAllTagsAsPage.info.prev,
                                    next = it.getAllTagsAsPage.info.next,
                                )
                            postInput(Inputs.Set.Info(info))
                        },
                    )
                }
            }
            postInput(Inputs.Set.Loading(false))
        }
    }
}
