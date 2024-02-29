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
