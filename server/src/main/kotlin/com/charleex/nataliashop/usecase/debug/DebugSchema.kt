package com.charleex.nataliashop.usecase.debug

import com.charleex.nataliashop.domain.util.OBJECT_ID
import com.charleex.nataliashop.domain.util.withCurrentUser
import com.charleex.nataliashop.model.user.User
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import graphql.schema.DataFetchingEnvironment
import org.koin.java.KoinJavaComponent.inject

class DebugSchema {
    class Queries : Query {
        private val database by inject<MongoDatabase>(MongoDatabase::class.java)
        private val userCollection = database.getCollection<User>(User::class.java.simpleName)

        @Suppress("unused")
        @GraphQLDescription("Generate users for testing purposes")
        suspend fun debugGenerateUsers(
            dfe: DataFetchingEnvironment,
            @GraphQLDescription("Target number of users to generate")
            target: Int = 200,
        ): GenerateUsersResult = dfe.withCurrentUser { currentUserId ->
            // Check how many users to generate
            val usersBefore = userCollection.countDocuments().toInt()
            val missingUsers = target - usersBefore
            if (missingUsers <= 0) {
                return@withCurrentUser GenerateUsersResult(
                    before = usersBefore,
                    inserted = missingUsers,
                    after = usersBefore,
                )
            }

            // generating users
            val usersToGenerate = getGeneratedUsers(missingUsers)
            val result = userCollection.insertMany(usersToGenerate)

            val totalInserted = result.insertedIds.size
            val usersAfter = userCollection.countDocuments().toInt()

            GenerateUsersResult(
                before = usersBefore,
                after = usersAfter,
                inserted = totalInserted,
            )
        }

        @Suppress("unused")
        @GraphQLDescription("Delete all users")
        suspend fun debugDeleteAllUsers(): Boolean {
            userCollection.drop()
            return userCollection.countDocuments() == 0L
        }

        @Suppress("unused")
        @GraphQLDescription("Delete all generated users")
        suspend fun debugDeleteGeneratedUsers(
            dfe: DataFetchingEnvironment,
        ): Boolean = dfe.withCurrentUser { currentUserId ->
            val result = userCollection.deleteMany(Filters.ne(OBJECT_ID, currentUserId))
            result.deletedCount > 0
        }
    }

    class Mutations : Mutation {
        private val database by inject<MongoDatabase>(MongoDatabase::class.java)
        private val userCollection = database.getCollection<User>(User::class.java.simpleName)
    }
}
