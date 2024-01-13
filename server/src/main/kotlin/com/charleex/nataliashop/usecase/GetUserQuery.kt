package com.charleex.nataliashop.usecase

import com.charleex.nataliashop.domain.util.OBJECT_ID
import com.charleex.nataliashop.domain.util.withCurrentUser
import com.charleex.nataliashop.model.user.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.flow.firstOrNull
import org.koin.java.KoinJavaComponent

object GetUserQuery : Query {
    private val database by KoinJavaComponent.inject<MongoDatabase>(MongoDatabase::class.java)
    private val userCollection = database.getCollection<User>(User::class.java.simpleName)

    @GraphQLDescription("Get current user from ID token")
    @Suppress("unused")
    suspend fun getUser(
        dfe: DataFetchingEnvironment,
    ): User = dfe.withCurrentUser { currentUserId ->
        userCollection.find(Filters.eq(OBJECT_ID, currentUserId)).firstOrNull()
            ?: error("User not found")
    }
}
