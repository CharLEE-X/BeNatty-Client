package feature.admin.list

import component.localization.getString
import data.type.SortDirection

object AdminListContract {
    data class State(
        val dataType: DataType,

        val isSlot1Sortable: Boolean,
        val showSlot2: Boolean,
        val isSlot2Sortable: Boolean,
        val showSlot3: Boolean,
        val isSlot3Sortable: Boolean,
        val showSlot4: Boolean,
        val isSlot4Sortable: Boolean,
        val showSlot5: Boolean,
        val isSlot5Sortable: Boolean,
        val showSlot6: Boolean,
        val isSlot6Sortable: Boolean,

        val strings: ListStrings = ListStrings(
            title = when (dataType) {
                DataType.USER -> getString(component.localization.Strings.Users)
                DataType.PRODUCT -> getString(component.localization.Strings.Products)
                DataType.ORDER -> getString(component.localization.Strings.Orders)
                DataType.CATEGORY -> getString(component.localization.Strings.Categories)
                DataType.TAG -> getString(component.localization.Strings.Tags)
            },
            slot1Text = when (dataType) {
                DataType.USER -> getString(component.localization.Strings.CreatedAt)
                DataType.PRODUCT -> getString(component.localization.Strings.CreatedAt)
                DataType.ORDER -> getString(component.localization.Strings.CreatedAt)
                DataType.CATEGORY -> getString(component.localization.Strings.CreatedAt)
                DataType.TAG -> getString(component.localization.Strings.CreatedAt)
            },
            slot2Text = when (dataType) {
                DataType.USER -> getString(component.localization.Strings.Email)
                DataType.PRODUCT -> getString(component.localization.Strings.Name)
                DataType.ORDER -> getString(component.localization.Strings.Name)
                DataType.CATEGORY -> getString(component.localization.Strings.Name)
                DataType.TAG -> getString(component.localization.Strings.Name)
            },
            slot3Text = when (dataType) {
                DataType.USER -> getString(component.localization.Strings.FullName)
                DataType.PRODUCT -> getString(component.localization.Strings.Price)
                DataType.ORDER -> getString(component.localization.Strings.Description)
                DataType.CATEGORY -> getString(component.localization.Strings.Description)
                DataType.TAG -> getString(component.localization.Strings.InProducts)
            },
            slot4Text = when (dataType) {
                DataType.USER -> getString(component.localization.Strings.Orders)
                DataType.PRODUCT -> getString(component.localization.Strings.Sold)
                DataType.ORDER -> getString(component.localization.Strings.StockStatus)
                DataType.CATEGORY -> getString(component.localization.Strings.Display)
                DataType.TAG -> ""
            },
            slot5Text = when (dataType) {
                DataType.USER -> ""
                DataType.PRODUCT -> getString(component.localization.Strings.CatalogVisibility)
                DataType.ORDER -> ""
                DataType.CATEGORY -> getString(component.localization.Strings.InProducts)
                DataType.TAG -> ""
            },
        ),

        val isLoading: Boolean = true,

        val items: List<ListItem> = emptyList(),
        val showList: Boolean = false,
        val showNoItems: Boolean = false,

        val searchValue: String = "",
        val sortBy: String = when (dataType) {
            DataType.USER -> UserSlot.CreatedAt.name
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

    data class ListStrings(
        val title: String,
        val slot1Text: String,
        val slot2Text: String,
        val slot3Text: String,
        val slot4Text: String,
        val slot5Text: String,
        val press: String = getString(component.localization.Strings.Press),
        val create: String = getString(component.localization.Strings.Create),
        val toStart: String = getString(component.localization.Strings.ToStart),
        val previous: String = getString(component.localization.Strings.Previous),
        val next: String = getString(component.localization.Strings.Next),
        val search: String = getString(component.localization.Strings.Search),
        val show: String = getString(component.localization.Strings.Show),
        val filter: String = getString(component.localization.Strings.Filter),
    )

    enum class DataType {
        USER,
        PRODUCT,
        ORDER,
        CATEGORY,
        TAG,
    }

    enum class UserSlot {
        CreatedAt, Email, FullName, Orders;

        fun asString(): String = when (this) {
            CreatedAt -> getString(component.localization.Strings.CreatedAt)
            Email -> getString(component.localization.Strings.Email)
            FullName -> getString(component.localization.Strings.FullName)
            Orders -> getString(component.localization.Strings.Orders)
        }
    }

    enum class ProductSlot {
        CreatedAt, Image, Name, Price, Sold, CatalogVisibility;

        fun asString() = when (this) {
            CreatedAt -> getString(component.localization.Strings.CreatedAt)
            Image -> getString(component.localization.Strings.Image)
            Name -> getString(component.localization.Strings.Name)
            Price -> getString(component.localization.Strings.Price)
            Sold -> getString(component.localization.Strings.Sold)
            CatalogVisibility -> getString(component.localization.Strings.CatalogVisibility)
        }
    }

    enum class OrderSlot {
        CreatedAt, Name, TotalPrice, Status;

        fun asString() = when (this) {
            CreatedAt -> getString(component.localization.Strings.CreatedAt)
            Name -> getString(component.localization.Strings.Name)
            TotalPrice -> getString(component.localization.Strings.Price)
            Status -> getString(component.localization.Strings.StockStatus)
        }
    }

    enum class CategorySlot {
        CreatedAt, Name, Description, Display, InProducts;

        fun asString() = when (this) {
            CreatedAt -> getString(component.localization.Strings.CreatedAt)
            Name -> getString(component.localization.Strings.Name)
            Description -> getString(component.localization.Strings.Description)
            Display -> getString(component.localization.Strings.Display)
            InProducts -> getString(component.localization.Strings.InProducts)
        }
    }

    enum class TagSlot {
        CreatedAt, Name, InProducts;

        fun asString() = when (this) {
            CreatedAt -> getString(component.localization.Strings.CreatedAt)
            Name -> getString(component.localization.Strings.Name)
            InProducts -> getString(component.localization.Strings.InProducts)
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
