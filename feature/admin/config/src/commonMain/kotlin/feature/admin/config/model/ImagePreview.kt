package feature.admin.config.model

import data.GetConfigQuery

data class ImagePreview(
    val id: String,
    val url: String,
    val alt: String,
)

fun GetConfigQuery.CollageItem.toPreviewImage() = ImagePreview(
    id = id,
    url = imageUrl ?: "",
    alt = alt ?: "",
)

fun GetConfigQuery.Left.toPreviewImage() = ImagePreview(
    id = "",
    url = imageUrl ?: "",
    alt = alt ?: "",
)

fun GetConfigQuery.Right.toPreviewImage() = ImagePreview(
    id = "",
    url = imageUrl ?: "",
    alt = alt ?: "",
)
