package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import core.RemoteError
import data.AddCategoryImageMutation
import data.CreateCategoryMutation
import data.DeleteCategoryMutation
import data.GetAllCategoriesAsMinimalQuery
import data.GetCategoriesAsPageQuery
import data.GetCategoryByIdQuery
import data.UpdateCategoryMutation
import data.type.AddMediaToCategoryInput
import data.type.BlobInput
import data.type.CategoryCreateInput
import data.type.CategoryUpdateInput
import data.type.MediaType
import data.type.PageInput
import data.type.ShippingPresetInput
import data.type.SortDirection
import data.utils.handle
import data.utils.skipIfNull

interface CategoryService {
    suspend fun create(name: String): Either<RemoteError, CreateCategoryMutation.Data>

    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, GetCategoriesAsPageQuery.Data>

    suspend fun getById(id: String): Either<RemoteError, GetCategoryByIdQuery.Data>
    suspend fun getCategoriesAllMinimal(): Either<RemoteError, GetAllCategoriesAsMinimalQuery.Data>
    suspend fun deleteById(id: String): Either<RemoteError, DeleteCategoryMutation.Data>
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
    ): Either<RemoteError, UpdateCategoryMutation.Data>

    suspend fun addCategoryImage(
        categoryId: String,
        blob: String,
        type: MediaType,
    ): Either<RemoteError, AddCategoryImageMutation.Data>
}

internal class CategoryServiceImpl(private val apolloClient: ApolloClient) : CategoryService {
    override suspend fun create(name: String): Either<RemoteError, CreateCategoryMutation.Data> {
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
    ): Either<RemoteError, GetCategoriesAsPageQuery.Data> {
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

    override suspend fun getById(id: String): Either<RemoteError, GetCategoryByIdQuery.Data> {
        return apolloClient.query(GetCategoryByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getCategoriesAllMinimal(): Either<RemoteError, GetAllCategoriesAsMinimalQuery.Data> {
        return apolloClient.query(GetAllCategoriesAsMinimalQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun deleteById(id: String): Either<RemoteError, DeleteCategoryMutation.Data> {
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
    ): Either<RemoteError, UpdateCategoryMutation.Data> {
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

    override suspend fun addCategoryImage(
        categoryId: String,
        blob: String,
        type: MediaType,
    ): Either<RemoteError, AddCategoryImageMutation.Data> {
        val input = AddMediaToCategoryInput(
            categoryId = categoryId,
            blob = BlobInput(blob),
            type = type,
        )
        return apolloClient.mutation(AddCategoryImageMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
