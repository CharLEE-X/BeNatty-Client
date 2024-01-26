package feature.admin.users

import component.localization.getString
import data.GetUsersPageQuery

object AdminUsersContract {
    data class State(
        val users: List<GetUsersPageQuery.User> = emptyList(),
        val info: GetUsersPageQuery.Info = GetUsersPageQuery.Info(
            count = 0,
            pages = 0,
            prev = null,
            next = null,
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class GetUsersPage(val page: Int) : Inputs
        data class SetUsersPage(val users: List<GetUsersPageQuery.User>, val info: GetUsersPageQuery.Info) : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val users: String = getString(component.localization.Strings.Users),
    )
}
