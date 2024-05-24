package feature.admin.tag.edit

import data.GetTagByIdQuery
import data.TagsGetAllMinimalQuery

object AdminTagEditContract {
    data class State(
        val isLoading: Boolean = false,
        val wasEdited: Boolean = false,

        val allTags: List<TagsGetAllMinimalQuery.GetAllTagsAsMinimal> = emptyList(),

        val nameError: String? = null,
        val shakeName: Boolean = false,

        val original: GetTagByIdQuery.GetTagById = GetTagByIdQuery.GetTagById(
            id = "",
            name = "",
            creator = GetTagByIdQuery.Creator(
                id = "",
                name = "",
            ),
            createdAt = "",
            updatedAt = "",
        ),

        val current: GetTagByIdQuery.GetTagById = original,
    )

    sealed interface Inputs {
        data class Init(val id: String) : Inputs

        data class GetTagById(val id: String) : Inputs
        data class GetAllTags(val currentTagId: String?) : Inputs
        data object SaveEdit : Inputs
        data class ShakeErrors(val name: Boolean) : Inputs

        data object OnCreateClick : Inputs
        data object OnDeleteClick : Inputs
        data object OnSaveEditClick : Inputs
        data object OnCancelEditClick : Inputs
        data object OnGotToUserCreatorClick : Inputs
        data object OnImproveNameClick : Inputs
        data object OnUserCreatorClick : Inputs

        data class SetLoading(val isLoading: Boolean) : Inputs
        data class SetAllTags(val tags: List<TagsGetAllMinimalQuery.GetAllTagsAsMinimal>) : Inputs
        data class SetOriginalTag(val tag: GetTagByIdQuery.GetTagById) : Inputs
        data class SetCurrentTag(val tag: GetTagByIdQuery.GetTagById) : Inputs
        data class SetId(val id: String) : Inputs
        data class SetName(val fullName: String) : Inputs
        data class SetIsNameShake(val shake: Boolean) : Inputs
        data class SetCreator(val creator: GetTagByIdQuery.Creator) : Inputs
        data class SetCreatedAt(val createdAt: String) : Inputs
        data class SetUpdatedAt(val updatedAt: String) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoBack : Events
        data class GoToUser(val id: String) : Events
        data class GoToTag(val id: String) : Events
    }
}
