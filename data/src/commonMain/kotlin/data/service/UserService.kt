package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.GetUserQuery
import data.UpdateUserMutation
import data.type.AboutMeInput
import data.type.Gender
import data.type.InterestsInput
import data.type.OpenQuestion
import data.type.ProfileUpdateInput
import data.type.UserUpdateInput
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserService {
    suspend fun getUser(): Flow<Result<GetUserQuery.Data>>

    suspend fun updateUser(
        email: String? = null,
        name: String? = null,
        username: String? = null,
        password: String? = null,
        profilePicture: String? = null,
        bio: String? = null,
        aboutMe: AboutMeInput? = null,
        interests: InterestsInput? = null,
        gender: Gender? = null,
        openQuestion: OpenQuestion? = null,
        pictures: List<String>? = null,
    ): Result<UpdateUserMutation.Data>
}

internal class UserServiceImpl(private val apolloClient: ApolloClient) : UserService {
    override suspend fun getUser(): Flow<Result<GetUserQuery.Data>> {
        return apolloClient.query(GetUserQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
            .map { it.handle() }
    }

    override suspend fun updateUser(
        email: String?,
        name: String?,
        username: String?,
        password: String?,
        profilePicture: String?,
        bio: String?,
        aboutMe: AboutMeInput?,
        interests: InterestsInput?,
        gender: Gender?,
        openQuestion: OpenQuestion?,
        pictures: List<String>?,
    ): Result<UpdateUserMutation.Data> {
        val userUpdateInput = UserUpdateInput(
            email = email.skipIfNull(),
            password = password.skipIfNull(),
        )

        val profileUpdateInput = ProfileUpdateInput(
            aboutMe = aboutMe.skipIfNull(),
            bio = bio.skipIfNull(),
            interests = interests.skipIfNull(),
            openQuestion = openQuestion.skipIfNull(),
            pictures = pictures.skipIfNull(),

            )
        return apolloClient.mutation(
            UpdateUserMutation(
                Optional.present(userUpdateInput),
                Optional.present(profileUpdateInput),
            ),
        )
            .fetchPolicy(FetchPolicy.CacheOnly)
            .handle()
    }
}
