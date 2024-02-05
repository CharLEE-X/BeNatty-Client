package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.CreateUserMutation
import data.DeleteUserByIdMutation
import data.UserCheckPasswordMatchQuery
import data.UserGetByIdQuery
import data.UserGetQuery
import data.UserUpdateMutation
import data.UsersGetAllPageQuery
import data.type.AddressInput
import data.type.CreateUserInput
import data.type.PageInput
import data.type.PersonalDetailsInput
import data.type.Role
import data.type.SortDirection
import data.type.UserUpdateInput
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserService {
    suspend fun create(input: CreateUserInput): Result<CreateUserMutation.Data>
    suspend fun get(): Flow<Result<UserGetQuery.Data>>
    suspend fun getById(id: String): Flow<Result<UserGetByIdQuery.Data>>
    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<UsersGetAllPageQuery.Data>

    suspend fun deleteById(id: String): Result<DeleteUserByIdMutation.Data>

    suspend fun update(
        id: String,
        email: String? = null,
        password: String? = null,
        role: Role? = null,
        name: String? = null,
        phone: String? = null,
        address: String? = null,
        additionalInfo: String? = null,
        postcode: String? = null,
        city: String? = null,
        state: String? = null,
        country: String? = null,
    ): Result<UserUpdateMutation.Data>

    suspend fun checkPasswordMatch(oldPassword: String, newPassword: String): Result<UserCheckPasswordMatchQuery.Data>
}

internal class UserServiceImpl(private val apolloClient: ApolloClient) : UserService {

    override suspend fun create(input: CreateUserInput): Result<CreateUserMutation.Data> {
        return apolloClient.mutation(CreateUserMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun get(): Flow<Result<UserGetQuery.Data>> {
        return apolloClient.query(UserGetQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
            .map { it.handle() }
    }

    override suspend fun getById(id: String): Flow<Result<UserGetByIdQuery.Data>> {
        return apolloClient.query(UserGetByIdQuery(id))
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
            .map { it.handle() }
    }

    override suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<UsersGetAllPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(UsersGetAllPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun deleteById(id: String): Result<DeleteUserByIdMutation.Data> {
        return apolloClient.mutation(DeleteUserByIdMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun update(
        id: String,
        email: String?,
        password: String?,
        role: Role?,
        name: String?,
        phone: String?,
        address: String?,
        additionalInfo: String?,
        postcode: String?,
        city: String?,
        state: String?,
        country: String?,
    ): Result<UserUpdateMutation.Data> {
        val detailsInput = if (name == null && phone == null) {
            Optional.absent()
        } else {
            Optional.present(
                PersonalDetailsInput(
                    name = name.skipIfNull(),
                    phone = phone.skipIfNull(),
                )
            )
        }
        val addressInput = if (
            address == null && additionalInfo == null && postcode == null &&
            city == null && state == null && country == null
        ) {
            Optional.absent()
        } else {
            Optional.present(
                AddressInput(
                    address = address.skipIfNull(),
                    additionalInfo = additionalInfo.skipIfNull(),
                    postcode = postcode.skipIfNull(),
                    city = city.skipIfNull(),
                    state = state.skipIfNull(),
                    country = country.skipIfNull(),
                )
            )
        }
        val input = UserUpdateInput(
            id = id,
            email = email.skipIfNull(),
            password = password.skipIfNull(),
            role = role.skipIfNull(),
            details = detailsInput,
            address = addressInput,
        )

        return apolloClient.mutation(UserUpdateMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun checkPasswordMatch(
        oldPassword: String,
        newPassword: String
    ): Result<UserCheckPasswordMatchQuery.Data> {
        return apolloClient.query(UserCheckPasswordMatchQuery(oldPassword, newPassword))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
