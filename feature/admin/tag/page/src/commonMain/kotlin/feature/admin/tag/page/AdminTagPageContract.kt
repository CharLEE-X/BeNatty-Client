package feature.admin.tag.page

import component.localization.getString
import data.GetTagByIdQuery
import data.TagsGetAllMinimalQuery

object AdminTagPageContract {
    data class State(
        val isLoading: Boolean = false,
        val screenState: ScreenState,

        val wasEdited: Boolean = false,

        val allTags: List<TagsGetAllMinimalQuery.GetAllTagsAsMinimal> = emptyList(),

        val nameError: String? = null,
        val shakeName: Boolean = false,

        val original: GetTagByIdQuery.GetTagById = GetTagByIdQuery.GetTagById(
            id = "",
            name = "",
            creator = GetTagByIdQuery.Creator(
                id = "",
                firstName = "",
                lastName = "",
            ),
            createdAt = "",
            updatedAt = "",
        ),

        val current: GetTagByIdQuery.GetTagById = original,
        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class Init(val id: String?) : Inputs

        sealed interface Get : Inputs {
            data class TagById(val id: String) : Inputs
            data class AllTags(val currentTagId: String?) : Inputs
        }

        sealed interface OnClick : Inputs {
            data object Create : Inputs
            data object Delete : Inputs
            data object SaveEdit : Inputs
            data object CancelEdit : Inputs
            data object GotToUserCreator : Inputs
            data object ImproveName : Inputs
        }

        sealed interface Set : Inputs {
            data class Loading(val isLoading: Boolean) : Inputs
            data class StateOfScreen(val screenState: ScreenState) : Inputs

            data class AllTags(val categories: List<TagsGetAllMinimalQuery.GetAllTagsAsMinimal>) : Inputs

            data class OriginalTag(val category: GetTagByIdQuery.GetTagById) : Inputs
            data class CurrentTag(val category: GetTagByIdQuery.GetTagById) : Inputs

            data class Id(val id: String) : Inputs
            data class Name(val fullName: String) : Inputs
            data class IsNameShake(val shake: Boolean) : Inputs
            data class Creator(val creator: GetTagByIdQuery.Creator) : Inputs
            data class CreatedAt(val createdAt: String) : Inputs
            data class UpdatedAt(val updatedAt: String) : Inputs
        }
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
        data object GoToList : Events
        data class GoToUser(val id: String) : Events
        data class GoToTag(val id: String) : Events
    }

    data class Strings(
        val save: String = getString(component.localization.Strings.Save),
        val edit: String = getString(component.localization.Strings.Edit),
        val discard: String = getString(component.localization.Strings.Discard),
        val delete: String = getString(component.localization.Strings.Delete),
        val createdBy: String = getString(component.localization.Strings.CreatedBy),
        val createdAt: String = getString(component.localization.Strings.CreatedAt),
        val lastUpdatedAt: String = getString(component.localization.Strings.LastUpdatedAt),
        val createTag: String = getString(component.localization.Strings.CreateTag),
        val category: String = getString(component.localization.Strings.Tag),
        val details: String = getString(component.localization.Strings.Details),
        val name: String = getString(component.localization.Strings.Name),
        val create: String = getString(component.localization.Strings.Create),
        val description: String = getString(component.localization.Strings.Description),
        val none: String = getString(component.localization.Strings.None),
        val noOtherTagsToChooseFrom: String = getString(component.localization.Strings.NoOtherTagsToChooseFrom),
        val unsavedChanges: String = getString(component.localization.Strings.UnsavedChanges),
        val saveChanges: String = getString(component.localization.Strings.SaveChanges),
        val dismiss: String = getString(component.localization.Strings.Dismiss),
        val improveWithAi: String = getString(component.localization.Strings.ImproveWithAi),
    )

    sealed interface ScreenState {
        data object New : ScreenState
        data object Existing : ScreenState
    }
}
