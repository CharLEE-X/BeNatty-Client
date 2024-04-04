package feature.admin.dashboard

import component.localization.getString
import data.GetStatsQuery

object AdminDashboardContract {
    data class State(
        val isLoading: Boolean = false,
        val stats: GetStatsQuery.GetStats = GetStatsQuery.GetStats(
            totalUsers = 0,
            totalProducts = 0,
            totalOrders = 0,
        ),
    )

    sealed interface Inputs {
        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data object GetStats : Inputs
        data class SetStats(val stats: GetStatsQuery.GetStats) : Inputs
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
