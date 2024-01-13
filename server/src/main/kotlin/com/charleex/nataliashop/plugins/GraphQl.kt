package com.charleex.nataliashop.plugins

import com.charleex.nataliashop.graphql.CustomGraphQLContextFactory
import com.charleex.nataliashop.graphql.CustomSchemaGeneratorHooks
import com.charleex.nataliashop.usecase.ForgotPasswordQuery
import com.charleex.nataliashop.usecase.GetUserQuery
import com.charleex.nataliashop.usecase.LoginMutation
import com.charleex.nataliashop.usecase.RegisterMutation
import com.charleex.nataliashop.usecase.UpdateUserMutation
import com.charleex.nataliashop.usecase.debug.DebugSchema
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.federation.directives.ContactDirective
import com.expediagroup.graphql.server.Schema
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.KtorGraphQLRequestParser
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.application.Application
import io.ktor.server.application.install

/**
 *  Docs: [graphql-kotlin](https://opensource.expediagroup.com/graphql-kotlin/docs/server/ktor-server/ktor-overview)
 */

private const val PACKAGE_NAME = "com.charleex.nataliashop"

fun Application.configureGraphQL() {
    install(GraphQL) {
        schema {
            packages = listOf(
                "$PACKAGE_NAME.model",
                "$PACKAGE_NAME.usecase",
            )
            queries = listOf(
                DebugSchema.Queries(),
                ForgotPasswordQuery,
                GetUserQuery,
            )

            mutations = listOf(
                DebugSchema.Mutations(),
                LoginMutation,
                RegisterMutation,
                UpdateUserMutation,
            )

            subscriptions = listOf()

            schemaObject = ForevelySchema()
            hooks = CustomSchemaGeneratorHooks()
        }
        server {
            contextFactory = CustomGraphQLContextFactory()
            jacksonConfiguration = {
                enable(SerializationFeature.INDENT_OUTPUT)
                setDefaultLeniency(true)
                registerModules(
                    KotlinModule(
                        nullToEmptyMap = true,
                        nullToEmptyCollection = true,
                    ),
                )
            }
            requestParser =
                KtorGraphQLRequestParser(jacksonObjectMapper().apply(jacksonConfiguration))
        }
    }
}

@ContactDirective(
    name = "CharLEE-X",
    url = "https://github.com/CharLEE-X",
    description = "Send urgent issues to [#discussions](https://github.com/orgs/CharLEE-X/discussions).",
)
@GraphQLDescription("My schema description")
class ForevelySchema : Schema
