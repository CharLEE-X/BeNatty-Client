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

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data object GetStats : Inputs
        data class SetStats(val stats: GetStatsQuery.GetStats) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val users: String = getString(component.localization.Strings.Users),
        val dashboard: String = getString(component.localization.Strings.Dashboard),
        val stats: String = getString(component.localization.Strings.Stats),
        val products: String = getString(component.localization.Strings.Products),
        val orders: String = getString(component.localization.Strings.Orders),
    )
}
