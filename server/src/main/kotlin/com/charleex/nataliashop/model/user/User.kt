package com.charleex.nataliashop.model.user

import com.charleex.nataliashop.domain.InputValidator
import com.charleex.nataliashop.domain.util.ErrorMessage
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@GraphQLDescription(UserDesc.MODEL)
data class User(
    @BsonId
    @GraphQLDescription(UserDesc.ID)
    val id: ObjectId,
    @GraphQLDescription(UserDesc.EMAIL)
    val email: String,
    @GraphQLIgnore
    var passwordHash: ByteArray,
    @GraphQLIgnore
    var passwordSalt: ByteArray,
    @GraphQLDescription(UserDesc.PROFILE)
    var profile: Profile? = null,
    @GraphQLDescription(UserDesc.UPDATED_AT)
    var updatedAt: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (!passwordHash.contentEquals(other.passwordHash)) return false
        if (!passwordSalt.contentEquals(other.passwordSalt)) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + passwordHash.contentHashCode()
        result = 31 * result + passwordSalt.contentHashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }
}

@GraphQLDescription(UserDesc.INPUT_UPDATE)
data class UserUpdateInput(
    @GraphQLDescription(UserDesc.EMAIL)
    val email: String? = null,
    @GraphQLDescription(UserDesc.PASSWORD)
    val password: String? = null,
)

object UserDesc {
    const val MODEL = "Represents a user object"
    const val INPUT_UPDATE = "Represents a user update input object"
    const val ID = "Unique identifier of the user."
    const val EMAIL = "Email of the user."
    const val PASSWORD = """
User password. Special characters prohibited @£€#¢∞§¶•ªº\${'$'}%^&*()_+="
Invalid format will throw error: '${ErrorMessage.INVALID_PASSWORD}'"

Regex: ${InputValidator.REGEX_PASSWORD}
"""
    const val PROFILE = "User profile information"
    const val UPDATED_AT = "Last modification date in 'Long' as a String"
}
