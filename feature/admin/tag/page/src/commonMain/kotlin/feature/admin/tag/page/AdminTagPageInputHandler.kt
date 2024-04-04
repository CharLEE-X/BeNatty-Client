package feature.admin.tag.page

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import component.localization.InputValidator
import core.mapToUiMessage
import core.models.PageScreenState
import core.util.millisToTime
import data.service.TagService
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

val SHAKE_ANIM_DURATION = (.25).seconds
private typealias InputScope = InputHandlerScope<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State>

internal class AdminTagPageInputHandler :
    KoinComponent,
    InputHandler<AdminTagPageContract.Inputs, AdminTagPageContract.Events, AdminTagPageContract.State> {

    private val tagService: TagService by inject()
    private val inputValidator: InputValidator by inject()

    override suspend fun InputScope.handleInput(input: AdminTagPageContract.Inputs) = when (input) {
        is AdminTagPageContract.Inputs.Init -> handleInit(input.id)

        is AdminTagPageContract.Inputs.Get.TagById -> handleGetById(input.id)
        is AdminTagPageContract.Inputs.Get.AllTags -> handleGetAllTags(input.currentTagId)

        AdminTagPageContract.Inputs.OnClick.Create -> handleCreateNew()
        AdminTagPageContract.Inputs.OnClick.SaveEdit -> handleSaveDetails()
        AdminTagPageContract.Inputs.OnClick.Delete -> handleDelete()
        AdminTagPageContract.Inputs.OnClick.CancelEdit -> handleCancel()
        AdminTagPageContract.Inputs.OnClick.GotToUserCreator -> handleGoToCreator()
        AdminTagPageContract.Inputs.OnClick.ImproveName -> handleImproveName()

        is AdminTagPageContract.Inputs.Set.Loading -> updateState { it.copy(isLoading = input.isLoading) }
        is AdminTagPageContract.Inputs.Set.StateOfScreen -> updateState { it.copy(pageScreenState = input.screenState) }
        is AdminTagPageContract.Inputs.Set.AllTags -> updateState { it.copy(allTags = input.categories) }
        is AdminTagPageContract.Inputs.Set.OriginalTag ->
            updateState { it.copy(original = input.category).wasEdited() }

        is AdminTagPageContract.Inputs.Set.CurrentTag ->
            updateState { it.copy(current = input.category).wasEdited() }

        is AdminTagPageContract.Inputs.Set.Id -> updateState { it.copy(current = it.current.copy(id = input.id)) }
        is AdminTagPageContract.Inputs.Set.Name -> handleSetName(input.fullName)
        is AdminTagPageContract.Inputs.Set.IsNameShake -> updateState { it.copy(shakeName = input.shake) }

        is AdminTagPageContract.Inputs.Set.CreatedAt ->
            updateState { it.copy(current = it.current.copy(createdAt = input.createdAt)) }

        is AdminTagPageContract.Inputs.Set.Creator ->
            updateState { it.copy(current = it.current.copy(creator = input.creator)) }

        is AdminTagPageContract.Inputs.Set.UpdatedAt ->
            updateState { it.copy(current = it.current.copy(updatedAt = input.updatedAt)) }
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
        postEvent(AdminTagPageContract.Events.GoToUser(state.current.creator.id.toString()))
    }

    private suspend fun InputScope.handleGetAllTags(currentTagId: String?) {
        sideJob("handleGetAllTags") {
            tagService.getTagsAllMinimal().fold(
                { postEvent(AdminTagPageContract.Events.OnError(it.mapToUiMessage())) },
                { data ->
                    val allTags = data.getAllTagsAsMinimal
                        .filter { currentTagId?.let { categoryId -> it.id != categoryId } ?: true }
                    postInput(AdminTagPageContract.Inputs.Set.AllTags(allTags))
                },
            )
        }
    }

    private suspend fun InputScope.handleDelete() {
        val state = getCurrentState()
        sideJob("handleDeleteTag") {
            tagService.deleteById(state.current.id).fold(
                { postEvent(AdminTagPageContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminTagPageContract.Events.GoToTagList) },
            )
        }
    }

    private suspend fun InputScope.handleInit(id: String?) {
        sideJob("handleInit") {
            postInput(AdminTagPageContract.Inputs.Get.AllTags(id))
            if (id == null) {
                postInput(AdminTagPageContract.Inputs.Set.StateOfScreen(PageScreenState.New))
            } else {
                postInput(AdminTagPageContract.Inputs.Set.Loading(isLoading = true))
                postInput(AdminTagPageContract.Inputs.Set.Id(id))
                postInput(AdminTagPageContract.Inputs.Get.TagById(id))
                postInput(AdminTagPageContract.Inputs.Set.StateOfScreen(PageScreenState.Existing))
                postInput(AdminTagPageContract.Inputs.Set.Loading(isLoading = false))
            }
        }
    }

    private suspend fun InputScope.handleCreateNew() {
        val state = getCurrentState()
        sideJob("handleCreateNewUser") {
            tagService.create(name = state.current.name).fold(
                { postEvent(AdminTagPageContract.Events.OnError(it.mapToUiMessage())) },
                { postEvent(AdminTagPageContract.Events.GoToTag(it.createTag.id)) },
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

    private suspend fun InputScope.handleSaveDetails() {
        with(getCurrentState()) {
            val isFullNameError = nameError != null
            val isNoError = !isFullNameError

            if (!isNoError) {
                sideJob("handleSavePersonalDetailsShakes") {
                    if (isFullNameError) postInput(AdminTagPageContract.Inputs.Set.IsNameShake(shake = true))

                    delay(SHAKE_ANIM_DURATION)

                    if (isFullNameError) postInput(AdminTagPageContract.Inputs.Set.IsNameShake(shake = false))
                }
            } else {
                sideJob("handleSavePersonalDetails") {
                    tagService.update(
                        id = current.id,
                        name = if (current.name != original.name) current.name else null,
                    ).fold(
                        { postEvent(AdminTagPageContract.Events.OnError(it.mapToUiMessage())) },
                        { data ->
                            postInput(
                                AdminTagPageContract.Inputs.Set.OriginalTag(
                                    category = this@with.original.copy(
                                        name = data.updateTag.name,
                                        updatedAt = data.updateTag.updatedAt,
                                    )
                                )
                            )
                            postInput(AdminTagPageContract.Inputs.OnClick.CancelEdit)
                        },
                    )
                }
            }
        }
    }

    private suspend fun InputScope.handleGetById(id: String) {
        sideJob("handleGetUserProfile") {
            tagService.getById(id).collect { result ->
                result.fold(
                    { postEvent(AdminTagPageContract.Events.OnError(it.mapToUiMessage())) },
                    { data ->
                        val createdAt = millisToTime(data.getTagById.createdAt.toLong())
                        val updatedAt = millisToTime(data.getTagById.updatedAt.toLong())
                        val category = data.getTagById.copy(
                            createdAt = createdAt,
                            updatedAt = updatedAt,
                        )
                        postInput(AdminTagPageContract.Inputs.Set.OriginalTag(category))
                        postInput(AdminTagPageContract.Inputs.Set.CurrentTag(category))
                    },
                )
            }
        }
    }

    private fun AdminTagPageContract.State.wasEdited(): AdminTagPageContract.State = copy(
        wasEdited = current.name != original.name
    )
}
