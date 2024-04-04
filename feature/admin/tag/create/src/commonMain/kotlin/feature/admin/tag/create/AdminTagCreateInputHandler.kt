package feature.admin.tag.create

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import data.service.TagService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminTagCreateContract.Inputs, AdminTagCreateContract.Events, AdminTagCreateContract.State>

internal class AdminTagPageInputHandler :
    KoinComponent,
    InputHandler<AdminTagCreateContract.Inputs, AdminTagCreateContract.Events, AdminTagCreateContract.State> {

    private val tagService: TagService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputScope.handleInput(input: AdminTagCreateContract.Inputs) = when (input) {
        AdminTagCreateContract.Inputs.CreateTag -> handleCreateTag()
        is AdminTagCreateContract.Inputs.ShakeErrors -> handleShakeErrors(name = input.name)

        AdminTagCreateContract.Inputs.OnCreateClick -> handleOnCreateClick()

        is AdminTagCreateContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminTagCreateContract.Inputs.SetName -> handleSetName(input.fullName)
        is AdminTagCreateContract.Inputs.SetIsNameShake -> updateState { it.copy(shakeName = input.shake) }
    }

    private suspend fun InputScope.handleOnCreateClick() {
        val state = getCurrentState()

        val isNameError = state.nameError != null

        if (isNameError) {
            postInput(AdminTagCreateContract.Inputs.ShakeErrors(name = isNameError))
            return
        } else {
            inputValidator.validateText(state.name, 1)?.let {
                postInput(AdminTagCreateContract.Inputs.SetName(state.name))
                return
            }
        }

        postInput(AdminTagCreateContract.Inputs.CreateTag)
    }

    private suspend fun InputScope.handleCreateTag() {
        val state = getCurrentState()
        sideJob("CreateTag") {
            tagService.create(name = state.name).fold(
                { postEvent(AdminTagCreateContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminTagCreateContract.Events.GoToTag(it.createTag.id)) },
            )
        }
    }

    private suspend fun InputScope.handleShakeErrors(name: Boolean) {
        sideJob("ShakeErrors") {
            if (name) postInput(AdminTagCreateContract.Inputs.SetIsNameShake(shake = true))

            delay(Constants.shakeAnimantionDuration)

            if (name) postInput(AdminTagCreateContract.Inputs.SetIsNameShake(shake = false))
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        updateState { it.copy(name = name, nameError = inputValidator.validateText(name)) }
    }
}
