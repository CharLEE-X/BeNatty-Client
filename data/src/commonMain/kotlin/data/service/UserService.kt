package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.GetUserProfileQuery
import data.UpdateUserMutation
import data.type.AddressUpdateInput
import data.type.PersonalDetailsUpdateInput
import data.type.UserUpdateInput
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserService {
    suspend fun getUser(): Flow<Result<GetUserProfileQuery.Data>>

    suspend fun updateUser(
        email: String? = null,
        password: String? = null,
        name: String? = null,
        phone: String? = null,
        address: String? = null,
        additionalInfo: String? = null,
        postcode: String? = null,
        city: String? = null,
        state: String? = null,
        country: String? = null,
    ): Result<UpdateUserMutation.Data>
}

internal class UserServiceImpl(private val apolloClient: ApolloClient) : UserService {
    override suspend fun getUser(): Flow<Result<GetUserProfileQuery.Data>> {
        return apolloClient.query(GetUserProfileQuery())
            .fetchPolicy(FetchPolicy.CacheAndNetwork)
            .watch()
            .map { it.handle() }
    }

    override suspend fun updateUser(
        email: String?,
        password: String?,
        name: String?,
        phone: String?,
        address: String?,
        additionalInfo: String?,
        postcode: String?,
        city: String?,
        state: String?,
        country: String?,
    ): Result<UpdateUserMutation.Data> {
        val optionalUserUpdateInput = if (email == null && password == null) {
            Optional.absent()
        } else {
            Optional.present(
                UserUpdateInput(
                    email = email.skipIfNull(),
                    password = password.skipIfNull(),
                )
            )
        }
        val personalDetailsUpdateInput = if (name == null && phone == null) {
            Optional.absent()
        } else {
            Optional.present(
                PersonalDetailsUpdateInput(
                    name = name.skipIfNull(),
                    phone = phone.skipIfNull(),
                )
            )
        }
        val addressUpdateInput = if (
            address == null && additionalInfo == null && postcode == null &&
            city == null && state == null && country == null
        ) {
            Optional.absent()
        } else {
            Optional.present(
                AddressUpdateInput(
                    address = address.skipIfNull(),
                    additionalInfo = additionalInfo.skipIfNull(),
                    postcode = postcode.skipIfNull(),
                    city = city.skipIfNull(),
                    state = state.skipIfNull(),
                    country = country.skipIfNull(),
                )
            )
        }

        return apolloClient.mutation(
            UpdateUserMutation(optionalUserUpdateInput, personalDetailsUpdateInput, addressUpdateInput)
        )
            .fetchPolicy(FetchPolicy.CacheOnly)
            .handle()
    }
}
