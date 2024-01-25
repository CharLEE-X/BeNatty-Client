package data.service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.apolloStore

interface DebugService {
    //    suspend fun generateUsers(target: Int = 100): Result<GenerateUsersQuery.Data>
//    suspend fun deleteAllUsers(): Result<DeleteAllUsersQuery.Data>
//    suspend fun deleteGeneratedUsers(): Result<DeleteGeneratedUsersQuery.Data>
    suspend fun clearCache(): Result<Boolean>
}

internal class DebugServiceImpl(private val apolloClient: ApolloClient) : DebugService {
//    override suspend fun generateUsers(target: Int): Result<GenerateUsersQuery.Data> {
//        return apolloClient.query(GenerateUsersQuery(target)).handle()
//    }
//
//    override suspend fun deleteAllUsers(): Result<DeleteAllUsersQuery.Data> {
//        return apolloClient.query(DeleteAllUsersQuery()).handle()
//    }
//
//    override suspend fun deleteGeneratedUsers(): Result<DeleteGeneratedUsersQuery.Data> {
//        return apolloClient.query(DeleteGeneratedUsersQuery()).handle()
//    }

    override suspend fun clearCache(): Result<Boolean> = runCatching {
        apolloClient.apolloStore.clearAll()
    }
}
