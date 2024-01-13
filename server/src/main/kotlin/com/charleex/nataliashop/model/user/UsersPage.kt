package com.charleex.nataliashop.model.user

import com.charleex.nataliashop.model.Page
import com.charleex.nataliashop.model.PagingInfo
import com.charleex.nataliashop.model.PagingInfoDesc
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

@GraphQLDescription(UserPageDesc.MODEL)
data class UsersPage(
    @GraphQLDescription(UserPageDesc.USERS)
    val users: List<User>,
    @GraphQLDescription(UserPageDesc.INFO)
    val info: PagingInfo,
)

fun Page<User>.toUsersPage() = UsersPage(
    users = results,
    info = info,
)

object UserPageDesc {
    const val MODEL = "Represents a paginated list of user objects"
    const val USERS = "The list of users on this page"
    const val INFO = PagingInfoDesc.MODEL
}
