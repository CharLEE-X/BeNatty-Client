package feature.shop.home

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import data.service.AuthService
import data.service.UserService
import feature.shop.home.model.CollageItem
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>

internal class HomeInputHandler :
    KoinComponent,
    InputHandler<HomeContract.Inputs, HomeContract.Events, HomeContract.State> {

    private val inputValidator by inject<InputValidator>()
    private val authService: AuthService by inject()
    private val productService: UserService by inject()

    override suspend fun InputHandlerScope<HomeContract.Inputs, HomeContract.Events, HomeContract.State>.handleInput(
        input: HomeContract.Inputs,
    ) = when (input) {
        HomeContract.Inputs.Init -> handleInit()
        is HomeContract.Inputs.FetchHomeConfig -> handleFetchHomeConfig()
        is HomeContract.Inputs.FetchProducts -> handleFetchProducts()

        is HomeContract.Inputs.OnCollageItemClick -> handleCollageItemClick(input.item)

        is HomeContract.Inputs.SetCollageItems -> updateState { it.copy(collageItems = input.items) }
        is HomeContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is HomeContract.Inputs.SetProducts -> updateState { it.copy(products = input.products) }
        is HomeContract.Inputs.SetEmail -> handleSetEmail(input.email)
        HomeContract.Inputs.OnEmailSend -> handleOnEmailSend()
        is HomeContract.Inputs.OnEmailChange -> updateState { it.copy(email = input.email) }
    }

    private suspend fun InputScope.handleCollageItemClick(item: CollageItem) {
        noOp()
    }

    private suspend fun InputScope.handleFetchHomeConfig() {
        val items = listOf(
            CollageItem(
                id = "1",
                title = "Build your style",
                description = "Discover the latest trends",
                imageUrl = "https://media.istockphoto.com/id/1339264709/photo/flat-lay-with-womans-clothes-and-accessories.jpg?s=1024x1024&w=is&k=20&c=jEVAHmR8cL6tB7FTN3cNM1WnGb5fb9sd2f69Lbu3TAU=",
            ),
            CollageItem(
                id = "2",
                title = "Stay cozy and Stylish",
                description = "Grab your hoodies today",
                imageUrl = "https://media.istockphoto.com/id/1339264709/photo/flat-lay-with-womans-clothes-and-accessories.jpg?s=2048x2048&w=is&k=20&c=NKhylkVx_RfvuB5TzfVukUvY1Moc6TnF7JgnVoEi944=",
            ),
            CollageItem(
                id = "3",
                title = "Warm, comfortable and chic",
                description = "Tell more about your product, collection...",
                imageUrl = "https://media.istockphoto.com/id/1838033271/photo/stylish-young-smiling-hipster-woman-with-color-hair-wearing-trendy-peach-color-coat-and-hat.jpg?s=2048x2048&w=is&k=20&c=jqsLjiTqefUV1xHGck_849vXNsJUWT9z_wBB9IGG7Xg=",
            ),
            CollageItem(
                id = "4",
                title = "Woman",
                description = "Top, Pants, Dress, Shoes, Bags, Accessories",
                imageUrl = "https://media.istockphoto.com/id/1947951512/photo/young-man-enjoying-carnival-at-home.jpg?s=2048x2048&w=is&k=20&c=xVkwAcW6_37iq6tawognAUrfCee2dhB0giNyYzoxUeA=",
            ),
            CollageItem(
                id = "5",
                title = "Man",
                description = "Top, Pants, Hoodie, Tracksuit.",
                imageUrl = "https://media.istockphoto.com/id/1455095734/photo/portrait-fashion-or-stylish-young-gen-z-woman-stand-in-a-warehouse-with-green-clothing-trendy.jpg?s=1024x1024&w=is&k=20&c=6_wNgjOYO75ZJY_zAWADgc-LingZk-GTtdl0ePugyjs=",
            ),
            CollageItem(
                id = "6",
                title = "Sale",
                description = "New markdowns: up to 50% off",
                imageUrl = "https://media.istockphoto.com/id/877121628/photo/autumn-female-clothing-and-accessories-on-pastel-background.jpg?s=2048x2048&w=is&k=20&c=FAmYXO9Z-H5DzUPHP7675S136jgCSOuJClN5Dvuleu0=",
            ),
        )
        updateState { it.copy(collageItems = items) }
//        postInput(HomeContract.Inputs.SetCollageItems(items))
    }

    private suspend fun InputScope.handleFetchProducts() {
        noOp()
    }

    private suspend fun InputScope.handleInit() {
        sideJob("handleInit") {
            postInput(HomeContract.Inputs.SetIsLoading(isLoading = true))
            postInput(HomeContract.Inputs.FetchHomeConfig)
            postInput(HomeContract.Inputs.FetchProducts)
            postInput(HomeContract.Inputs.SetIsLoading(isLoading = false))
        }
    }
    private fun InputScope.handleOnEmailSend() {
        noOp()
    }

    private suspend fun InputScope.handleSetEmail(email: String) {
        inputValidator.validateEmail(email)?.let { error ->
            updateState {
                it.copy(
                    email = email,
                    emailError = error,
                )
            }
        } ?: updateState {
            it.copy(
                email = email,
                emailError = null,
            )
        }
    }
}
