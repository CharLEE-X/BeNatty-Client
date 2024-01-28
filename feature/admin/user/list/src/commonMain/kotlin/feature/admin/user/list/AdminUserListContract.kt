package feature.admin.user.list

import component.localization.getString
import data.UsersGetAllPageQuery

object AdminUserListContract {
    data class State(
        val users: List<UsersGetAllPageQuery.User> = emptyList(),
        val info: UsersGetAllPageQuery.Info = UsersGetAllPageQuery.Info(
            count = 0,
            pages = 0,
            prev = null,
            next = null,
        ),

        val strings: Strings = Strings()
    )

    sealed interface Inputs {
        data class GetUsersPage(val page: Int) : Inputs
        data class SetUsersPage(val users: List<UsersGetAllPageQuery.User>, val info: UsersGetAllPageQuery.Info) :
            Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events
    }

    data class Strings(
        val users: String = getString(component.localization.Strings.Users),
        val newUser: String = getString(component.localization.Strings.NewUser),
    )
}
