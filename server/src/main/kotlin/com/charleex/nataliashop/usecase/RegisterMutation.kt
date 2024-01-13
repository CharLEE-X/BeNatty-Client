package com.charleex.nataliashop.usecase

import com.charleex.nataliashop.domain.InputValidator
import com.charleex.nataliashop.domain.JwtService
import com.charleex.nataliashop.domain.util.ErrorMessage
import com.charleex.nataliashop.domain.util.encrypt
import com.charleex.nataliashop.domain.util.generateSalt
import com.charleex.nataliashop.model.auth.AuthInput
import com.charleex.nataliashop.model.auth.AuthInputDesc
import com.charleex.nataliashop.model.auth.AuthResponse
import com.charleex.nataliashop.model.user.Profile
import com.charleex.nataliashop.model.user.User
import com.charleex.nataliashop.model.user.toMinimal
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import graphql.GraphQLException
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import org.koin.java.KoinJavaComponent

object RegisterMutation : Mutation {
    private val jwtService: JwtService by KoinJavaComponent.inject(JwtService::class.java)
    private val inputValidator: InputValidator by KoinJavaComponent.inject(InputValidator::class.java)
    private val database: MongoDatabase by KoinJavaComponent.inject(MongoDatabase::class.java)
    private val userCollection: MongoCollection<User> =
        database.getCollection<User>(User::class.java.simpleName)

    @GraphQLDescription("Signs up user with email and password, no header needed.")
    @Suppress("unused")
    suspend fun register(
        @GraphQLDescription(AuthInputDesc.MODEL)
        authInput: AuthInput,
    ): AuthResponse {
        if (!inputValidator.isValidEmail(authInput.email)) {
            throw GraphQLException(ErrorMessage.INVALID_EMAIL)
        }
        if (!inputValidator.isValidPassword(authInput.password)) {
            throw GraphQLException(ErrorMessage.INVALID_PASSWORD)
        }

        userCollection.find(Filters.eq(User::email.name, authInput.email)).firstOrNull()?.let {
            error(ErrorMessage.EMAIL_IN_USE)
        }

        val salt = generateSalt()
        val newUser = User(
            id = ObjectId(),
            email = authInput.email,
            passwordHash = authInput.password.encrypt(salt = salt),
            passwordSalt = salt,
            profile = Profile(
                bio = "",
                pictures = listOf("https://picsum.photos/200/300"), // TODO: Give user default picture
            ),
            updatedAt = System.currentTimeMillis().toString(),
        )

        val result = userCollection.insertOne(newUser)

        return if (result.wasAcknowledged()) {
            val token = jwtService.generateToken(newUser.id)
            AuthResponse(token, newUser.toMinimal())
        } else {
            throw GraphQLException(ErrorMessage.NOT_CREATED)
        }
    }
}
