package data.utils

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.files.File
import org.w3c.files.FileReader
import kotlin.js.Promise

actual typealias ImageFile = File

actual fun ImageFile.toByteArray(): ByteArray {
    return ByteArray(0)
//    readAsByteArray(this).await()
}

fun readAsByteArray(file: File): Promise<ByteArray> =
    Promise { resolve, reject ->
        val reader = FileReader()
        reader.onloadend = { _ ->
            resolve(Uint8Array(reader.result as ArrayBuffer).toByteArray())
        }
        reader.onerror = { _ ->
            reject(Throwable("Failed to read file"))
        }
        reader.readAsArrayBuffer(file)
    }

fun Uint8Array.toByteArray(): ByteArray {
    val byteArray = ByteArray(length)
    for (i in 0 until length) {
        byteArray[i] = this[i]
    }
    return byteArray
}
