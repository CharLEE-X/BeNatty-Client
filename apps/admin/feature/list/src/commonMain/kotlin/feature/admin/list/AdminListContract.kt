package feature.admin.list

import component.localization.Strings
import component.localization.getString
import data.type.SortDirection
import feature.admin.list.AdminListContract.DataType

object AdminListContract {
    data class State(
        val dataType: DataType,
        val isLoading: Boolean = false,
        val items: List<ListItem> = emptyList(),
        val showList: Boolean = false,
        val showNoItems: Boolean = false,
        val searchValue: String = "",
        val sortBy: String =
            when (dataType) {
                DataType.Customer -> CustomerSlot.CreatedAt.name
                DataType.PRODUCT -> ProductSlot.CreatedAt.name
                DataType.ORDER -> OrderSlot.CreatedAt.name
                DataType.CATEGORY -> CategorySlot.CreatedAt.name
                DataType.TAG -> TagSlot.CreatedAt.name
            },
        val sortDirection: SortDirection = SortDirection.DESC,
        val info: PageInfo = PageInfo(0, 0, null, null),
        val pagesNumbers: List<Int> = emptyList(),
        val perPageOptions: List<Int> = listOf(10, 25, 50, 100),
        val perPage: Int = perPageOptions.first(),
        val showPrevious: Boolean = info.prev != null,
        val showNext: Boolean = info.next != null,
    )

    sealed interface Inputs {
        data object SendSearch : Inputs

        sealed interface Get : Inputs {
            data class Page(val page: Int) : Get
        }

        sealed interface Set : Inputs {
            data class Loading(val isLoading: Boolean) : Set

            data class Items(val items: List<ListItem>) : Set

            data class PerPage(val perPage: Int) : Set

            data class Info(val info: PageInfo) : Set

            data class Search(val search: String) : Set

            data class SortBy(val sortBy: String) : Set

            data class SortDirection(val sortDirection: data.type.SortDirection) : Set
        }

        sealed interface OnChange : Inputs {
            data class Search(val search: String) : OnChange

            data class PerPage(val perPage: Int) : OnChange
        }

        sealed interface Click : Inputs {
            data object Create : Click

            data class Item(val id: String) : Click

            data class Page(val page: Int) : Click

            data object PreviousPage : Click

            data object NextPage : Click

            data class Slot(val slotName: String) : Click
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events

        sealed interface GoTo : Events {
            data object Create : Events

            data class Detail(val id: String) : Events
        }
    }

    enum class DataType {
        Customer,
        PRODUCT,
        ORDER,
        CATEGORY,
        TAG,
    }

    enum class CustomerSlot {
        CreatedAt,
        Email,
        FullName,
        Orders,
        ;

        fun asString(): String =
            when (this) {
                CreatedAt -> getString(Strings.CreatedAt)
                Email -> getString(Strings.Email)
                FullName -> getString(Strings.FirstName)
                Orders -> getString(Strings.Orders)
            }
    }

    enum class ProductSlot {
        CreatedAt,
        Image,
        Name,
        Price,
        Sold,
        ;

        fun asString() =
            when (this) {
                CreatedAt -> getString(Strings.CreatedAt)
                Image -> getString(Strings.Image)
                Name -> getString(Strings.Name)
                Price -> getString(Strings.Price)
                Sold -> getString(Strings.Sold)
            }
    }

    enum class OrderSlot {
        CreatedAt,
        Name,
        TotalPrice,
        Status,
        ;

        fun asString() =
            when (this) {
                CreatedAt -> getString(Strings.CreatedAt)
                Name -> getString(Strings.Name)
                TotalPrice -> getString(Strings.Price)
                Status -> getString(Strings.StockStatus)
            }
    }

    enum class CategorySlot {
        CreatedAt,
        Image,
        Name,
        Display,
        InProducts,
        ;

        fun asString() =
            when (this) {
                CreatedAt -> getString(Strings.CreatedAt)
                Image -> getString(Strings.Image)
                Name -> getString(Strings.Name)
                Display -> getString(Strings.Display)
                InProducts -> getString(Strings.InProducts)
            }
    }

    enum class TagSlot {
        CreatedAt,
        Name,
        InProducts,
        ;

        fun asString() =
            when (this) {
                CreatedAt -> getString(Strings.CreatedAt)
                Name -> getString(Strings.Name)
                InProducts -> getString(Strings.InProducts)
            }
    }
}

data class ListItem(
    /**
     * Id of the item.
     */
    val id: String,
    /**
     * Usually date: millisToDate(tag.createdAt.toLong())
     */
    val slot1: String,
    /**
     * Usually email, name, or other short string
     */
    val slot2: String?,
    val slot3: String?,
    val slot4: String?,
    val slot5: String?,
    val slot6: String?,
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val prev: Int?,
    val next: Int?,
)

fun adminListStrings(dataType: DataType): AdminListStrings =
    AdminListStrings(
        title =
        when (dataType) {
            DataType.Customer -> getString(Strings.Customers)
            DataType.PRODUCT -> getString(Strings.Products)
            DataType.ORDER -> getString(Strings.Orders)
            DataType.CATEGORY -> getString(Strings.Categories)
            DataType.TAG -> getString(Strings.Tags)
        },
        slot1Text =
        when (dataType) {
            DataType.Customer -> getString(Strings.CreatedAt)
            DataType.PRODUCT -> getString(Strings.CreatedAt)
            DataType.ORDER -> getString(Strings.CreatedAt)
            DataType.CATEGORY -> getString(Strings.CreatedAt)
            DataType.TAG -> getString(Strings.CreatedAt)
        },
        slot2Text =
        when (dataType) {
            DataType.Customer -> getString(Strings.Email)
            DataType.PRODUCT -> getString(Strings.Name)
            DataType.ORDER -> getString(Strings.Name)
            DataType.CATEGORY -> getString(Strings.Name)
            DataType.TAG -> getString(Strings.Name)
        },
        slot3Text =
        when (dataType) {
            DataType.Customer -> getString(Strings.FirstName)
            DataType.PRODUCT -> getString(Strings.Price)
            DataType.ORDER -> getString(Strings.Description)
            DataType.CATEGORY -> getString(Strings.Description)
            DataType.TAG -> getString(Strings.InProducts)
        },
        slot4Text =
        when (dataType) {
            DataType.Customer -> getString(Strings.Orders)
            DataType.PRODUCT -> getString(Strings.Sold)
            DataType.ORDER -> getString(Strings.StockStatus)
            DataType.CATEGORY -> getString(Strings.Display)
            DataType.TAG -> ""
        },
        slot5Text =
        when (dataType) {
            DataType.Customer -> ""
            DataType.PRODUCT -> getString(Strings.CatalogVisibility)
            DataType.ORDER -> ""
            DataType.CATEGORY -> getString(Strings.InProducts)
            DataType.TAG -> ""
        },
    )

data class AdminListStrings(
    val title: String,
    val slot1Text: String,
    val slot2Text: String,
    val slot3Text: String,
    val slot4Text: String,
    val slot5Text: String,
)
