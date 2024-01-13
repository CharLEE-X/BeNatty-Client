package com.charleex.nataliashop.usecase

import com.charleex.nataliashop.domain.InputValidator
import com.charleex.nataliashop.domain.util.ErrorMessage
import com.charleex.nataliashop.domain.util.OBJECT_ID
import com.charleex.nataliashop.domain.util.encrypt
import com.charleex.nataliashop.domain.util.generateSalt
import com.charleex.nataliashop.domain.util.withCurrentUser
import com.charleex.nataliashop.model.user.ProfileUpdateInput
import com.charleex.nataliashop.model.user.User
import com.charleex.nataliashop.model.user.UserDesc
import com.charleex.nataliashop.model.user.UserUpdateInput
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import graphql.GraphQLException
import graphql.schema.DataFetchingEnvironment
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.koin.java.KoinJavaComponent
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object UpdateUserMutation : Mutation {
    private val database by KoinJavaComponent.inject<MongoDatabase>(MongoDatabase::class.java)
    private val userCollection = database.getCollection<User>(User::class.java.simpleName)
    private val inputValidator by KoinJavaComponent.inject<InputValidator>(InputValidator::class.java)

    @GraphQLDescription("Mutation to update a user's profile by ID (for use in an admin panel)")
    @Suppress("unused")
    suspend fun updateUser(
        dfe: DataFetchingEnvironment,
        @GraphQLDescription(UserDesc.INPUT_UPDATE)
        userUpdateInput: UserUpdateInput? = null,
        @GraphQLDescription(UserDesc.PROFILE)
        profileUpdateInput: ProfileUpdateInput? = null,
    ): User = dfe.withCurrentUser { executorId ->
        userUpdateInput?.email?.let {
            val user = userCollection.find(Filters.eq(User::email.name, it)).firstOrNull()
            if (user != null) error(ErrorMessage.EMAIL_IN_USE)

            if (!inputValidator.isValidEmail(it)) throw GraphQLException(ErrorMessage.INVALID_EMAIL)
        }

        var passwordSalt: ByteArray? = null
        val hashedPass = userUpdateInput?.password?.let { password ->
            if (!inputValidator.isValidPassword(password)) throw GraphQLException(ErrorMessage.INVALID_PASSWORD)
            passwordSalt = generateSalt()
            passwordSalt?.let { password.encrypt(it) }
        }

        updateUser(
            id = executorId,
            email = userUpdateInput?.email,
            hashedPass = hashedPass,
            passwordSalt = passwordSalt,
            profileUpdateInput = profileUpdateInput,
        )
    }

    private suspend fun updateUser(
        id: ObjectId,
        email: String?,
        hashedPass: ByteArray?,
        passwordSalt: ByteArray?,
        profileUpdateInput: ProfileUpdateInput?,
    ): User {
        val bson = mutableListOf<Bson>().apply {
            email?.let { add(Updates.set(User::email.name, email)) }

            hashedPass?.let { pass ->
                passwordSalt?.let { salt ->
                    add(Updates.set(User::passwordHash.name, pass))
                    add(Updates.set(User::passwordSalt.name, salt))
                }
            }

            profileUpdateInput?.let {
                updateValuesIfNotNull(it, ProfileUpdateInput::class, User::profile.name)
            }
        }
        if (bson.isEmpty()) {
            throw GraphQLException("Nothing to update.")
        }
        bson.add(Updates.set(User::updatedAt.name, getTimeMillis().toString()))

        val updates = Updates.combine(bson)

        val filter = Filters.eq(OBJECT_ID, id)
        userCollection.updateOne(filter, updates)

        return userCollection.find(Filters.eq(OBJECT_ID, id)).firstOrNull()
            ?: throw GraphQLException(ErrorMessage.NOT_FOUND + " $id")
    }

    private fun <T : Any> MutableList<Bson>.updateValuesIfNotNull(
        newValue: T,
        kClass: KClass<T>,
        fieldPrefix: String,
    ) {
        kClass.memberProperties.forEach { property ->
            val value = property.get(newValue)
            if (value != null) {
                add(Updates.set("$fieldPrefix.${property.name}", value))
            }
        }
    }
}
