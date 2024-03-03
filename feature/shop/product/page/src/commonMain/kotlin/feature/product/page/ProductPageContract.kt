package feature.product.page

import component.localization.getString

object ProductPageContract {
    data class State(
        val strings: Strings = Strings(),
        val isLoading: Boolean = false,
    )

    sealed interface Inputs {
        data class Init(val productId: String?) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val productPage: String = getString(component.localization.Strings.ProductPage),
    )
}
