package com.charleex.nataliashop.model.user

import com.expediagroup.graphql.generator.annotations.GraphQLDescription

internal const val INTERESTS_COMPLETED_SIZE_MIN = 2

@GraphQLDescription(ProfileDesc.MODEL)
data class Profile(
    @GraphQLDescription(ProfileDesc.BIO)
    val bio: String? = null,
    @GraphQLDescription(ProfileDesc.PICTURES)
    val pictures: List<String> = emptyList(),
)

data class ProfileUpdateInput(
    @GraphQLDescription(ProfileDesc.PICTURES)
    val pictures: List<String>? = null,
    @GraphQLDescription(ProfileDesc.BIO)
    val bio: String? = null,
)

object ProfileDesc {
    const val MODEL = "Represents a user's profile."
    const val PICTURES = "List of profile pictures."
    const val BIO = "Short bio or description of the user."
}
