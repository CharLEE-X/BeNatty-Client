package data.service

import arrow.core.Either
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import core.RemoteError
import data.DeleteGeneratedDataMutation
import data.GenerateDataMutation
import data.GetStatsQuery
import data.type.DeleteGenerateDataInput
import data.type.GenerateDataInput
import data.utils.handle

interface AdminService {
    suspend fun getStats(): Either<RemoteError, GetStatsQuery.Data>
    suspend fun generateData(input: GenerateDataInput): Either<RemoteError, GenerateDataMutation.Data>
    suspend fun deleteGeneratedData(input: DeleteGenerateDataInput): Either<RemoteError, DeleteGeneratedDataMutation.Data>
}

internal class AdminServiceImpl(private val apolloClient: ApolloClient) : AdminService {
    override suspend fun getStats(): Either<RemoteError, GetStatsQuery.Data> {
        return apolloClient.query(GetStatsQuery())
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
    }

    override suspend fun generateData(input: GenerateDataInput): Either<RemoteError, GenerateDataMutation.Data> =
        apolloClient.mutation(GenerateDataMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()

    override suspend fun deleteGeneratedData(
        input: DeleteGenerateDataInput
    ): Either<RemoteError, DeleteGeneratedDataMutation.Data> =
        apolloClient.mutation(DeleteGeneratedDataMutation(input))
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .handle()
}
