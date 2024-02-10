package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.CreateCategoryMutation
import data.DeleteCategoryMutation
import data.GetCategoriesAllMinimalQuery
import data.GetCategoriesAsPageQuery
import data.GetCategoryByIdQuery
import data.UpdateCategoryMutation
import data.type.CategoryCreateInput
import data.type.CategoryUpdateInput
import data.type.PageInput
import data.type.ShippingPresetInput
import data.type.SortDirection
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CategoryService {
    suspend fun create(name: String): Result<CreateCategoryMutation.Data>

    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<GetCategoriesAsPageQuery.Data>

    suspend fun getById(id: String): Flow<Result<GetCategoryByIdQuery.Data>>
    suspend fun getCategoriesAllMinimal(): Result<GetCategoriesAllMinimalQuery.Data>
    suspend fun deleteById(id: String): Result<DeleteCategoryMutation.Data>
    suspend fun update(
        id: String,
        name: String?,
        description: String?,
        parentId: String?,
        display: Boolean?,
        weight: String?,
        length: String?,
        width: String?,
        height: String?,
        requiresShipping: Boolean?,
    ): Result<UpdateCategoryMutation.Data>
}

internal class CategoryServiceImpl(private val apolloClient: ApolloClient) : CategoryService {
    override suspend fun create(name: String): Result<CreateCategoryMutation.Data> {
        val input = CategoryCreateInput(name = name)
        return apolloClient.mutation(CreateCategoryMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<GetCategoriesAsPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(GetCategoriesAsPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Flow<Result<GetCategoryByIdQuery.Data>> {
        return apolloClient.query(GetCategoryByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .watch()
            .map { it.handle() }
    }

    override suspend fun getCategoriesAllMinimal(): Result<GetCategoriesAllMinimalQuery.Data> {
        return apolloClient.query(GetCategoriesAllMinimalQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun deleteById(id: String): Result<DeleteCategoryMutation.Data> {
        return apolloClient.mutation(DeleteCategoryMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun update(
        id: String,
        name: String?,
        description: String?,
        parentId: String?,
        display: Boolean?,
        weight: String?,
        length: String?,
        width: String?,
        height: String?,
        requiresShipping: Boolean?,
    ): Result<UpdateCategoryMutation.Data> {
        val shippingPreset = if (
            weight != null || length != null || width != null || height != null || requiresShipping != null
        ) {
            Optional.present(
                ShippingPresetInput(
                    weight = weight.skipIfNull(),
                    length = length.skipIfNull(),
                    width = width.skipIfNull(),
                    height = height.skipIfNull(),
                    requiresShipping = requiresShipping.skipIfNull(),
                )
            )
        } else Optional.absent()

        val input = CategoryUpdateInput(
            id = id,
            name = name.skipIfNull(),
            description = description.skipIfNull(),
            parentId = parentId.skipIfNull(),
            display = display.skipIfNull(),
            shippingPreset = shippingPreset,
        )
        return apolloClient.mutation(UpdateCategoryMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
