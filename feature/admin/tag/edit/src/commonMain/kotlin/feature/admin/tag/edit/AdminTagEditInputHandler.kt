package feature.admin.tag.edit

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import component.localization.InputValidator
import core.Constants
import core.mapToUiMessage
import core.util.millisToTime
import data.GetTagByIdQuery
import data.service.TagService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private typealias InputScope = InputHandlerScope<AdminTagEditContract.Inputs, AdminTagEditContract.Events, AdminTagEditContract.State>

internal class AdminTagEditInputHandler :
    KoinComponent,
    InputHandler<AdminTagEditContract.Inputs, AdminTagEditContract.Events, AdminTagEditContract.State> {

    private val tagService: TagService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputScope.handleInput(input: AdminTagEditContract.Inputs) = when (input) {
        is AdminTagEditContract.Inputs.Init -> handleInit(input.id)

        is AdminTagEditContract.Inputs.GetTagById -> handleGetById(input.id)
        is AdminTagEditContract.Inputs.GetAllTags -> handleGetAllTags(input.currentTagId)
        AdminTagEditContract.Inputs.SaveEdit -> handleSaveEdit()
        is AdminTagEditContract.Inputs.ShakeErrors -> handleShakeErrors(name = input.name)

        AdminTagEditContract.Inputs.OnCreateClick -> handleCreateNew()
        AdminTagEditContract.Inputs.OnSaveEditClick -> handleOnSaveEditClick()
        AdminTagEditContract.Inputs.OnDeleteClick -> handleDelete()
        AdminTagEditContract.Inputs.OnCancelEditClick -> handleCancel()
        AdminTagEditContract.Inputs.OnGotToUserCreatorClick -> handleGoToCreator()
        AdminTagEditContract.Inputs.OnImproveNameClick -> handleImproveName()

        is AdminTagEditContract.Inputs.SetLoading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminTagEditContract.Inputs.SetAllTags -> updateState { it.copy(allTags = input.tags) }
        is AdminTagEditContract.Inputs.SetOriginalTag ->
            updateState { it.copy(original = input.tag).wasEdited() }

        is AdminTagEditContract.Inputs.SetCurrentTag ->
            updateState { it.copy(current = input.tag).wasEdited() }

        is AdminTagEditContract.Inputs.SetId -> updateState { it.copy(current = it.current.copy(id = input.id)) }
        is AdminTagEditContract.Inputs.SetName -> handleSetName(input.fullName)
        is AdminTagEditContract.Inputs.SetIsNameShake -> updateState { it.copy(shakeName = input.shake) }

        is AdminTagEditContract.Inputs.SetCreatedAt ->
            updateState { it.copy(current = it.current.copy(createdAt = input.createdAt)) }

        is AdminTagEditContract.Inputs.SetCreator ->
            updateState { it.copy(current = it.current.copy(creator = input.creator)) }

        is AdminTagEditContract.Inputs.SetUpdatedAt ->
            updateState { it.copy(current = it.current.copy(updatedAt = input.updatedAt)) }
    }

    private suspend fun InputScope.handleShakeErrors(name: Boolean) {
        sideJob("ShakeErrors") {
            if (name) postInput(AdminTagEditContract.Inputs.SetIsNameShake(shake = true))

            delay(Constants.shakeAnimantionDuration)

            if (name) postInput(AdminTagEditContract.Inputs.SetIsNameShake(shake = false))
        }
    }

    private suspend fun InputScope.handleSaveEdit() {
        val current = getCurrentState().current
        val original = getCurrentState().original
        sideJob("handleSavePersonalDetails") {
            tagService.update(
                id = current.id,
                name = if (current.name != original.name) current.name else null,
            ).fold(
                { postEvent(AdminTagEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val tag = GetTagByIdQuery.GetTagById(
                        id = data.updateTag.id,
                        name = data.updateTag.name,
                        creator = original.creator,
                        createdAt = data.updateTag.createdAt,
                        updatedAt = data.updateTag.updatedAt,
                    )
                    postInput(AdminTagEditContract.Inputs.SetOriginalTag(tag))
                    postInput(AdminTagEditContract.Inputs.SetCurrentTag(tag))
                },
            )
        }
    }

    private fun InputScope.handleImproveName() {
        noOp()
    }

    private suspend fun InputScope.handleCancel() {
        updateState {
            it.copy(
                wasEdited = false,
                current = it.original,
            ).wasEdited()
        }
    }

    private suspend fun InputScope.handleGoToCreator() {
        val state = getCurrentState()
        postEvent(AdminTagEditContract.Events.GoToUser(state.current.creator.id))
    }

    private suspend fun InputScope.handleGetAllTags(currentTagId: String?) {
        sideJob("handleGetAllTags") {
            tagService.getTagsAllMinimal().fold(
                { postEvent(AdminTagEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val allTags = data.getAllTagsAsMinimal
                        .filter { currentTagId?.let { categoryId -> it.id != categoryId } ?: true }
                    postInput(AdminTagEditContract.Inputs.SetAllTags(allTags))
                },
            )
        }
    }

    private suspend fun InputScope.handleDelete() {
        val state = getCurrentState()
        sideJob("handleDeleteTag") {
            tagService.deleteById(state.current.id).fold(
                { postEvent(AdminTagEditContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminTagEditContract.Events.GoBack) },
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String) {
        sideJob("handleInit") {
            postInput(AdminTagEditContract.Inputs.SetLoading(isLoading = true))
            postInput(AdminTagEditContract.Inputs.SetId(id))
            postInput(AdminTagEditContract.Inputs.GetTagById(id))
            postInput(AdminTagEditContract.Inputs.SetLoading(isLoading = false))
        }
    }

    private suspend fun InputScope.handleCreateNew() {
        val state = getCurrentState()
        sideJob("handleCreateNewUser") {
            tagService.create(name = state.current.name).fold(
                { postEvent(AdminTagEditContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminTagEditContract.Events.GoToTag(it.createTag.id)) },
            )
        }
    }

    private suspend fun InputScope.handleSetName(name: String) {
        with(name) {
            updateState {
                it.copy(
                    current = it.current.copy(name = this),
                    nameError = if (it.wasEdited) inputValidator.validateText(this) else null,
                ).wasEdited()
            }
        }
    }

    private suspend fun InputScope.handleOnSaveEditClick() {
        val state = getCurrentState()

        val isNameError = state.nameError != null

        if (isNameError) {
            postInput(AdminTagEditContract.Inputs.ShakeErrors(name = isNameError))
            return
        } else {
            inputValidator.validateText(state.current.name, 1)?.let {
                postInput(AdminTagEditContract.Inputs.SetName(state.current.name))
                return
            }
        }

        postInput(AdminTagEditContract.Inputs.SaveEdit)
    }

    private suspend fun InputScope.handleGetById(id: String) {
        sideJob("handleGetUserProfile") {
            tagService.getById(id).fold(
                { postEvent(AdminTagEditContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val createdAt = millisToTime(data.getTagById.createdAt.toLong())
                    val updatedAt = millisToTime(data.getTagById.updatedAt.toLong())
                    val category = data.getTagById.copy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                    )
                    postInput(AdminTagEditContract.Inputs.SetOriginalTag(category))
                    postInput(AdminTagEditContract.Inputs.SetCurrentTag(category))
                },
            )
        }
    }

    private fun AdminTagEditContract.State.wasEdited(): AdminTagEditContract.State = copy(
        wasEdited = current.name != original.name
    )
}
