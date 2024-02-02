package feature.admin.category.list

import component.localization.getString
import data.GetCategoriesAsPageQuery

object AdminCategoryListContract {
    data class State(
        val isLoading: Boolean = false,
        val categories: List<GetCategoriesAsPageQuery.Category> = emptyList(),
        val info: GetCategoriesAsPageQuery.Info = GetCategoriesAsPageQuery.Info(
            count = 0,
            pages = 0,
            prev = null,
            next = null,
        ),
        val perPage: Int = 10,

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class GetPage(val page: Int) : Inputs
        data class SetCategories(val categories: List<GetCategoriesAsPageQuery.Category>) : Inputs
        data class SetPerPage(val perPage: Int) : Inputs
        data class SetInfo(val pageInfo: GetCategoriesAsPageQuery.Info) : Inputs
        data object OnCreateCategoryClicked : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToCreateCategory : Events
    }

    data class Strings(
        val categories: String = getString(component.localization.Strings.Categories),
        val newCategory: String = getString(component.localization.Strings.NewCategory),
        val pressCreateToStart: String = getString(component.localization.Strings.PressCreateToStart),
    )
}
