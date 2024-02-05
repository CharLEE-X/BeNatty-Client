package component.localization

// TODO: Move it somewhere else

interface CommonListState {
    val listState: ListState

    fun setLoading(isLoading: Boolean): ListState {
        return listState.copy(
            isLoading = isLoading
        )
    }

    fun setItems(items: List<ListItem>): ListState {
        return listState.copy(
            items = items,
            showList = items.isNotEmpty(),
            showNoItems = items.isEmpty()
        )
    }

    fun setPerPage(perPage: Int): ListState {
        return listState.copy(
            perPage = perPage
        )
    }

    fun setInfo(info: PageInfo): ListState {
        val pageNumbers = listOfNotNull(listState.info.prev, listState.info.count, listState.info.next)
        return listState.copy(
            info = info,
            pagesNumbers = pageNumbers,
            showPrevious = info.prev != null,
            showNext = info.next != null
        )
    }

    suspend fun goToNextPage(block: suspend (Int) -> Unit): Unit? {
        return listState.info.next?.let { block(it) }
    }

    suspend fun goToPreviousPage(block: suspend (Int) -> Unit): Unit? {
        return listState.info.prev?.let { block(it) }
    }
}

data class ListState(
    val title: String,
    val strings: ListStrings = ListStrings(title),

    val isLoading: Boolean = true,

    val items: List<ListItem> = emptyList(),
    val showList: Boolean = items.isNotEmpty(),
    val showNoItems: Boolean = items.isEmpty(),

    val info: PageInfo = PageInfo(0, 0, null, null),
    val pagesNumbers: List<Int> = emptyList(),
    val perPage: Int = 1,
    val showPrevious: Boolean = info.prev != null,
    val showNext: Boolean = info.next != null,
)

data class ListItem(
    val id: String,
    val name: String
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val prev: Int?,
    val next: Int?,
)

data class PageInput(
    val page: Int,
    val size: Int
)

open class ListStrings(
    val title: String,
    val press: String = getString(Strings.Press),
    val create: String = getString(Strings.Create),
    val toStart: String = getString(Strings.ToStart),
    val previous: String = getString(Strings.Previous),
    val next: String = getString(Strings.Next),
)
