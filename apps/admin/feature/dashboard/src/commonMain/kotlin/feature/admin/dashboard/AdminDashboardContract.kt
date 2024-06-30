package feature.admin.dashboard

import component.localization.getString
import data.GetStatsQuery

object AdminDashboardContract {
    data class State(
        val isStatsLoading: Boolean = true,
        val stats: GetStatsQuery.GetStats =
            GetStatsQuery.GetStats(
                totalCustomers = 0,
                totalProducts = 0,
                totalOrders = 0,
                totalCategories = 0,
                totalTags = 0,
            ),
        // Generator
        val productsToGenerate: Int = 100,
        val customersToGenerate: Int = 0,
        val ordersToGenerate: Int = 0,
        val isGenerating: Boolean = false,
        // Deleter
        val deleteProducts: Boolean = false,
        val deleteCustomers: Boolean = false,
        val deleteOrders: Boolean = false,
        val isDeleting: Boolean = false,
    )

    sealed interface Inputs {
        data object Init : Inputs

        data object GetStats : Inputs

        data object OnGenerateClicked : Inputs

        data object OnDeleteGeneratedClicked : Inputs

        data object OnDeleteProductsClicked : Inputs

        data object OnDeleteCustomersClicked : Inputs

        data object OnDeleteOrdersClicked : Inputs

        data class SetIsStatsLoading(val isLoading: Boolean) : Inputs

        data class SetIsGenerating(val isGenerating: Boolean) : Inputs

        data class SetIsDeleting(val isDeleting: Boolean) : Inputs

        data class SetStats(val stats: GetStatsQuery.GetStats) : Inputs

        data class SetProductsToGenerate(val products: String) : Inputs

        data class SetCustomersToGenerate(val customers: String) : Inputs

        data class SetOrdersToGenerate(val orders: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }
}

val adminDashboardStrings: AdminDashboardStrings = AdminDashboardStrings()

data class AdminDashboardStrings(
    val customers: String = getString(component.localization.Strings.Customers),
    val home: String = getString(component.localization.Strings.Home),
    val stats: String = getString(component.localization.Strings.Stats),
    val products: String = getString(component.localization.Strings.Products),
    val orders: String = getString(component.localization.Strings.Orders),
)
