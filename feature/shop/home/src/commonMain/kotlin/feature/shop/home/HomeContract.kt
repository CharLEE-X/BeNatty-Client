package feature.shop.home

import component.localization.getString

object HomeContract {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = false,
        val products: List<String> = emptyList(),
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchProducts : Inputs

        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetProducts(val products: List<String>) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val title: String = getString(component.localization.Strings.Home),
    )
}
