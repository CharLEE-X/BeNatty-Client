package feature.account.wishlist

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias WishlistInputScope = InputHandlerScope<WishlistContract.Inputs, WishlistContract.Events, WishlistContract.State>

internal class WishlistInputHandler :
    KoinComponent,
    InputHandler<WishlistContract.Inputs, WishlistContract.Events, WishlistContract.State> {

    private val authService: AuthService by inject()

    override suspend fun InputHandlerScope<WishlistContract.Inputs, WishlistContract.Events, WishlistContract.State>.handleInput(
        input: WishlistContract.Inputs,
    ) = when (input) {
        is WishlistContract.Inputs.SetEmail -> {}
    }
}
