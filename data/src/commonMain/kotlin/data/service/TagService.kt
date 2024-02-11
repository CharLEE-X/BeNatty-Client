package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import data.CreateCategoryMutation
import data.DeleteCategoryMutation
import data.GetCategoriesAsPageQuery
import data.GetCategoryByIdQuery
import data.UpdateCategoryMutation
import data.type.CategoryCreateInput
import data.type.CategoryUpdateInput
import data.type.PageInput
import data.type.SortDirection
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TagService {
    suspend fun create(name: String): Result<CreateCategoryMutation.Data>

    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Result<GetCategoriesAsPageQuery.Data>

    suspend fun getById(id: String): Flow<Result<GetCategoryByIdQuery.Data>>
    suspend fun deleteById(id: String): Result<DeleteCategoryMutation.Data>
    suspend fun update(
        id: String,
        name: String?,
        description: String?,
        parentId: String?,
        display: Boolean?,
    ): Result<UpdateCategoryMutation.Data>
}

internal class TagServiceImpl(private val apolloClient: ApolloClient) : TagService {
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
    ): Result<UpdateCategoryMutation.Data> {
        val input = CategoryUpdateInput(
            id = id,
            name = name.skipIfNull(),
            description = description.skipIfNull(),
            parentId = parentId.skipIfNull(),
            display = display.skipIfNull(),
        )
        return apolloClient.mutation(UpdateCategoryMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}