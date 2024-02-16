package feature.admin.list

import component.localization.getString
import data.type.SortDirection

object AdminListContract {
    data class State(
        val dataType: DataType,

        val strings: ListStrings = ListStrings(
            title = when (dataType) {
                DataType.USER -> getString(component.localization.Strings.Users)
                DataType.PRODUCT -> getString(component.localization.Strings.Products)
                DataType.ORDER -> getString(component.localization.Strings.Orders)
                DataType.CATEGORY -> getString(component.localization.Strings.Categories)
                DataType.TAG -> getString(component.localization.Strings.Tags)
            },
        ),

        val isLoading: Boolean = false,

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

        data object OnCreateClick : Inputs
        data class OnItemClick(val id: String) : Inputs
        data class OnPageClick(val page: Int) : Inputs
        data object OnPreviousPageClick : Inputs
        data object OnNextPageClick : Inputs
        data class OnTopBarSlotClick(val slotName: String) : Inputs
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
        TAG;
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
        CreatedAt, Image, Title, Price, Sold, CatalogVisibility;

        fun asString() = when (this) {
            CreatedAt -> getString(component.localization.Strings.CreatedAt)
            Image -> getString(component.localization.Strings.Image)
            Title -> getString(component.localization.Strings.Title)
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
