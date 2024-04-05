package feature.admin.product.create

import component.localization.getString

object AdminProductCreateContract {
    data class State(
        val isLoading: Boolean = false,

        val name: String = "",
        val nameError: String? = null,
        val nameShake: Boolean = false,
    )

    sealed interface Inputs {
        data object CreateProduct : Inputs
        data class ShakeErrors(val name: Boolean) : Inputs

        data object OnCreateClick : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetName(val name: String) : Inputs
        data class SetNameShake(val shake: Boolean) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBack : Events
        data class GoToProduct(val id: String) : Events
    }
}

val adminProductCreateStrings = AdminProductPageStrings

object AdminProductPageStrings {
    val save: String = getString(component.localization.Strings.Save)
    val title: String = getString(component.localization.Strings.Title)
    val newProduct: String = getString(component.localization.Strings.NewProduct)
    val create: String = getString(component.localization.Strings.Create)
    val product: String = getString(component.localization.Strings.Product)
    val name = getString(component.localization.Strings.Name)
}
