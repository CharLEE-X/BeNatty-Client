package feature.shop.home

import component.localization.getString
import feature.shop.home.model.CollageItem

object HomeContract {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = false,
        val collageItems: List<CollageItem> = emptyList(),
        val products: List<String> = emptyList(),

        val email: String = "",
        val emailError: String? = null,
    )

    sealed interface Inputs {
        data object Init : Inputs
        data object FetchHomeConfig : Inputs
        data object FetchProducts : Inputs

        data class OnCollageItemClick(val item: CollageItem) : Inputs

        data class OnEmailChange(val email: String) : Inputs
        data object OnEmailSend : Inputs

        data class SetCollageItems(val items: List<CollageItem>) : Inputs
        data class SetEmail(val email: String) : Inputs
        data class SetIsLoading(val isLoading: Boolean) : Inputs
        data class SetProducts(val products: List<String>) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val title: String = getString(component.localization.Strings.Home),
        val subscribe: String = getString(component.localization.Strings.Subscribe),
        val email: String = getString(component.localization.Strings.Email),
        val shopNow: String = getString(component.localization.Strings.ShopNow),
    )
}
