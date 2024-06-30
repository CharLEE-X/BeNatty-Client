package data.utils

import android.content.ContentResolver
import android.net.Uri

actual typealias ImageFile = ImageUri

actual suspend fun ImageFile.toByteArray() =
    contentResolver.openInputStream(uri)?.use {
        it.readBytes()
    } ?: throw IllegalStateException("Couldn't open inputStream $uri")

class ImageUri(val uri: Uri, val contentResolver: ContentResolver)

fun Uri.toImageFile(contentResolver: ContentResolver): ImageFile {
    return ImageFile(this, contentResolver)
}
