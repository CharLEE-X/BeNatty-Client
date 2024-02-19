package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.CreateCustomerMutation
import data.CustomerCheckPasswordMatchQuery
import data.CustomerGetAllPageQuery
import data.DeleteCustomerByIdMutation
import data.GetCustomerQuery
import data.UpdateCustomerMutation
import data.type.PageInput
import data.type.SortDirection
import data.type.UserCreateInput
import data.type.UserUpdateInput
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserService {
    suspend fun create(
        email: String,
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
        collectTax: Boolean,
        marketingEmails: Boolean,
        marketingSms: Boolean,
    ): Result<CreateCustomerMutation.Data>

    suspend fun getById(id: String): Flow<Result<GetCustomerQuery.Data>>
    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<CustomerGetAllPageQuery.Data>

    suspend fun deleteById(id: String): Result<DeleteCustomerByIdMutation.Data>

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
    ): Result<UpdateCustomerMutation.Data>

    suspend fun checkPasswordMatch(
        oldPassword: String,
        newPassword: String
    ): Result<CustomerCheckPasswordMatchQuery.Data>
}

internal class UserServiceImpl(private val apolloClient: ApolloClient) : UserService {
    override suspend fun create(
        email: String,
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
        collectTax: Boolean,
        marketingEmails: Boolean,
        marketingSms: Boolean,
    ): Result<CreateCustomerMutation.Data> {
        val input = UserCreateInput(
            email = email,
            detailFirstName = detailsFirstName.skipIfNull(),
            detailLastName = detailsLastName.skipIfNull(),
            language = language.skipIfNull(),
            detailPhone = detailPhone.skipIfNull(),
            country = country.skipIfNull(),
            addressFirstName = addressFirstName.skipIfNull(),
            addressLastName = addressLastName.skipIfNull(),
            addressPhone = addressPhone.skipIfNull(),
            company = company.skipIfNull(),
            address = address.skipIfNull(),
            apartment = apartment.skipIfNull(),
            city = city.skipIfNull(),
            postcode = postcode.skipIfNull(),
            collectTax = collectTax,
            marketingEmails = marketingEmails,
            marketingSms = marketingSms,
        )
        return apolloClient.mutation(CreateCustomerMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Flow<Result<GetCustomerQuery.Data>> {
        return apolloClient.query(GetCustomerQuery(id))
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
    ): Result<CustomerGetAllPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(CustomerGetAllPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun deleteById(id: String): Result<DeleteCustomerByIdMutation.Data> {
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
    ): Result<UpdateCustomerMutation.Data> {
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

    override suspend fun checkPasswordMatch(
        oldPassword: String,
        newPassword: String
    ): Result<CustomerCheckPasswordMatchQuery.Data> {
        return apolloClient.query(CustomerCheckPasswordMatchQuery(oldPassword, newPassword))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
