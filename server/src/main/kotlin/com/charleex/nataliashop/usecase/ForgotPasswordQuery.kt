package com.charleex.nataliashop.usecase

import com.charleex.nataliashop.model.auth.ForgotPasswordResponse
import com.charleex.nataliashop.model.user.UserDesc
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query

object ForgotPasswordQuery : Query {
    @GraphQLDescription("Reset password for userId")
    @Suppress("unused")
    fun forgotPassword(
        @GraphQLDescription(UserDesc.EMAIL)
        email: String,
    ): ForgotPasswordResponse {
        val result = true
        return ForgotPasswordResponse(
            email = email,
            isForgotPasswordEmailSent = result,
        )
    }
}
