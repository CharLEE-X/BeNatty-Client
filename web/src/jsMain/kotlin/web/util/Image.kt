package web.util

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.coroutineScope
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.File
import org.w3c.files.FilePropertyBag
import org.w3c.files.FileReader

suspend fun convertImageToBase64(file: File): String = coroutineScope {
    val reader = FileReader()
    val promise = kotlin.js.Promise { resolve, reject ->
        reader.onload = { event ->
            resolve(event.target?.asDynamic().result as String)
        }
        reader.onerror = { event ->
            reject(Throwable("Error reading file: ${event.target?.asDynamic().error}"))
        }
    }

    reader.readAsDataURL(file)
    promise.await()
}

suspend fun convertBase64ToBlob(base64: String, contentType: String = "", sliceSize: Int = 512): Blob = coroutineScope {
    val byteCharacters = window.atob(base64.split(",")[1])
    val byteArrays = mutableListOf<ByteArray>()

    for (offset in byteCharacters.indices step sliceSize) {
        val slice = byteCharacters.substring(offset, minOf(offset + sliceSize, byteCharacters.length))

        val byteArray = ByteArray(slice.length) { pos ->
            slice[pos].code.toByte()
        }

        byteArrays.add(byteArray)
    }

    Blob(byteArrays.toTypedArray(), BlobPropertyBag(contentType))
}

suspend fun convertBase64ToFile(base64: String, fileName: String, contentType: String = ""): File = coroutineScope {
    val blob = convertBase64ToBlob(base64, contentType)
    // Adjust the property bag here for file creation
    File(arrayOf(blob), fileName, FilePropertyBag(type = contentType))
}
