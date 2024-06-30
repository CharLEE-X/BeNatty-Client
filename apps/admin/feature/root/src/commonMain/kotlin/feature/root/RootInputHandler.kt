package feature.root

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import data.service.AuthService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import notification.NotificationService
import notification.NotificationType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias RootInputScope =
    InputHandlerScope<RootContract.Inputs, RootContract.Events, RootContract.State>

internal class RootInputHandler :
    KoinComponent,
    InputHandler<RootContract.Inputs, RootContract.Events, RootContract.State> {
    private val authService: AuthService by inject()
    private val notificationService: NotificationService by inject()

    override suspend fun InputHandlerScope<RootContract.Inputs, RootContract.Events, RootContract.State>.handleInput(
        input: RootContract.Inputs,
    ) = when (input) {
        is RootContract.Inputs.Init -> handleInit()
        RootContract.Inputs.ObserveAuthState -> observeAuthState()
        is RootContract.Inputs.SendNotification -> sendNotification(input.title, input.body)
        is RootContract.Inputs.SetIsAuthenticated -> updateState { it.copy(isAuthenticated = input.isAuthenticated) }
        is RootContract.Inputs.SetIsLoading -> updateState { it.copy(isLoading = input.isLoading) }
        RootContract.Inputs.LogOut -> logOut()
    }

    private suspend fun RootInputScope.handleInit() {
        sideJob("rootHandleInit") {
            postInput(RootContract.Inputs.ObserveAuthState)
        }
    }

    private suspend fun RootInputScope.observeAuthState() {
        sideJob("observeAuth") {
            authService.observeToken()
                .map { !it.isNullOrEmpty() }
                .onEach { isAuthenticated ->
                    postInput(RootContract.Inputs.SetIsAuthenticated(isAuthenticated))
                }.collect()
        }
    }

    private suspend fun RootInputScope.sendNotification(title: String, body: String) {
        sideJob("sendNotification") {
            notificationService.schedule(
                notificationType =
                NotificationType.Immediate(
                    title = title,
                    body = body,
                ),
            )
        }
    }

    private suspend fun RootInputScope.logOut() {
        sideJob("logOut") {
            authService.signOut()
        }
    }
}
