package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import core.RemoteError
import data.CreateCustomerMutation
import data.DeleteCustomerByIdMutation
import data.GetAllCustomersAsPageQuery
import data.GetCustomerByIdQuery
import data.UpdateCustomerMutation
import data.type.PageInput
import data.type.SortDirection
import data.type.UserCreateInput
import data.type.UserUpdateInput
import data.utils.handle
import data.utils.skipIfNull

interface UserService {
    suspend fun create(
        email: String,
        firstName: String,
        lastName: String,
    ): Either<RemoteError, CreateCustomerMutation.Data>

    suspend fun getById(id: String): Either<RemoteError, GetCustomerByIdQuery.Data>
    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, GetAllCustomersAsPageQuery.Data>

    suspend fun deleteById(id: String): Either<RemoteError, DeleteCustomerByIdMutation.Data>

    suspend fun update(
        id: String,
        email: String?,
        detailsFirstName: String?,
        detailsLastName: String?,
        language: String?,
        detailPhone: String?,
        country: String?,
        addressFirstName: String?,
        addressLastName: String?,
        addressPhone: String?,
        company: String?,
        address: String?,
        apartment: String?,
        city: String?,
        postcode: String?,
        collectTax: Boolean?,
        marketingEmails: Boolean?,
        marketingSms: Boolean?,
        password: String?,
    ): Either<RemoteError, UpdateCustomerMutation.Data>
}

internal class UserServiceImpl(private val apolloClient: ApolloClient) : UserService {
    override suspend fun create(
        email: String,
        firstName: String,
        lastName: String,
    ): Either<RemoteError, CreateCustomerMutation.Data> {
        val input = UserCreateInput(
            email = email,
            firstName = Optional.present(firstName),
            lastName = Optional.present(lastName),
        )
        return apolloClient.mutation(CreateCustomerMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Either<RemoteError, GetCustomerByIdQuery.Data> {
        return apolloClient.query(GetCustomerByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, GetAllCustomersAsPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(GetAllCustomersAsPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun deleteById(id: String): Either<RemoteError, DeleteCustomerByIdMutation.Data> {
        return apolloClient.mutation(DeleteCustomerByIdMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun update(
        id: String,
        email: String?,
        detailsFirstName: String?,
        detailsLastName: String?,
        language: String?,
        detailPhone: String?,
        country: String?,
        addressFirstName: String?,
        addressLastName: String?,
        addressPhone: String?,
        company: String?,
        address: String?,
        apartment: String?,
        city: String?,
        postcode: String?,
        collectTax: Boolean?,
        marketingEmails: Boolean?,
        marketingSms: Boolean?,
        password: String?,
    ): Either<RemoteError, UpdateCustomerMutation.Data> {
        val input = UserUpdateInput(
            id = id,
            email = email.skipIfNull(),
            password = password.skipIfNull(),
            address = address.skipIfNull(),
            addressFirstName = addressFirstName.skipIfNull(),
            addressLastName = addressLastName.skipIfNull(),
            apartment = apartment.skipIfNull(),
            city = city.skipIfNull(),
            collectTax = collectTax.skipIfNull(),
            company = company.skipIfNull(),
            country = country.skipIfNull(),
            detailsFirstName = detailsFirstName.skipIfNull(),
            detailsLastName = detailsLastName.skipIfNull(),
            marketingEmails = marketingEmails.skipIfNull(),
            marketingSms = marketingSms.skipIfNull(),
            detailsPhone = detailPhone.skipIfNull(),
            postcode = postcode.skipIfNull(),
            addressPhone = addressPhone.skipIfNull(),
        )

        return apolloClient.mutation(UpdateCustomerMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
