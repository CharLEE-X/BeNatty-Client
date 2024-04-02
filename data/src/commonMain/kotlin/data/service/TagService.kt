package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import core.RemoteError
import data.CreateTagMutation
import data.DeleteTagMutation
import data.GetTagByIdQuery
import data.GetTagsAsPageQuery
import data.TagsGetAllMinimalQuery
import data.UpdateTagMutation
import data.type.PageInput
import data.type.SortDirection
import data.type.TagCreateInput
import data.type.TagUpdateInput
import data.utils.handle
import data.utils.skipIfNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TagService {
    suspend fun create(name: String): Either<RemoteError, CreateTagMutation.Data>

    suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, GetTagsAsPageQuery.Data>

    suspend fun getById(id: String): Flow<Either<RemoteError, GetTagByIdQuery.Data>>
    suspend fun getTagsAllMinimal(): Either<RemoteError, TagsGetAllMinimalQuery.Data>
    suspend fun deleteById(id: String): Either<RemoteError, DeleteTagMutation.Data>
    suspend fun update(
        id: String,
        name: String?,
    ): Either<RemoteError, UpdateTagMutation.Data>
}

internal class TagServiceImpl(private val apolloClient: ApolloClient) : TagService {
    override suspend fun create(name: String): Either<RemoteError, CreateTagMutation.Data> {
        val input = TagCreateInput(name = name)
        return apolloClient.mutation(CreateTagMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getAsPage(
        page: Int,
        size: Int,
        query: String?,
        sortBy: String?,
        sortDirection: SortDirection?,
    ): Either<RemoteError, GetTagsAsPageQuery.Data> {
        val pageInput = PageInput(
            page = page,
            size = size,
            query = query.skipIfNull(),
            sortBy = sortBy.skipIfNull(),
            sortDirection = sortDirection.skipIfNull(),
        )
        return apolloClient.query(GetTagsAsPageQuery(pageInput))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun getById(id: String): Flow<Either<RemoteError, GetTagByIdQuery.Data>> {
        return apolloClient.query(GetTagByIdQuery(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .watch()
            .map { it.handle() }
    }

    override suspend fun getTagsAllMinimal(): Either<RemoteError, TagsGetAllMinimalQuery.Data> {
        return apolloClient.query(TagsGetAllMinimalQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun deleteById(id: String): Either<RemoteError, DeleteTagMutation.Data> {
        return apolloClient.mutation(DeleteTagMutation(id))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun update(
        id: String,
        name: String?,
    ): Either<RemoteError, UpdateTagMutation.Data> {
        val input = TagUpdateInput(
            id = id,
            name = name.skipIfNull(),
        )
        return apolloClient.mutation(UpdateTagMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }
}
