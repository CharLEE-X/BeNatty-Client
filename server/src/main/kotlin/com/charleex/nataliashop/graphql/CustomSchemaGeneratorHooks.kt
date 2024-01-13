package com.charleex.nataliashop.graphql

import com.charleex.nataliashop.graphql.scalar.GraphQLLongAsString
import com.charleex.nataliashop.graphql.scalar.GraphQLUpload
import com.charleex.nataliashop.graphql.scalar.graphqlObjectIdType
import com.charleex.nataliashop.graphql.scalar.graphqlPointType
import com.expediagroup.graphql.generator.hooks.FlowSubscriptionSchemaGeneratorHooks
import com.mongodb.client.model.geojson.Point
import graphql.schema.GraphQLType
import io.javalin.http.UploadedFile
import org.bson.types.ObjectId
import kotlin.reflect.KClass
import kotlin.reflect.KType

class CustomSchemaGeneratorHooks : FlowSubscriptionSchemaGeneratorHooks() {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? =
        when (type.classifier as? KClass<*>) {
            Long::class -> GraphQLLongAsString
            ObjectId::class -> graphqlObjectIdType
            Point::class -> graphqlPointType
            UploadedFile::class -> GraphQLUpload
            else -> super.willGenerateGraphQLType(type)
        }
}
