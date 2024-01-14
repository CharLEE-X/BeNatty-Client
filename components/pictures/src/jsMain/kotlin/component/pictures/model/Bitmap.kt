package component.pictures.model

actual class Bitmap {
    actual fun toByteArray(): ByteArray {
        return ByteArray(0)
    }

    actual fun toBase64(): String {
        return "error string"
    }

    actual fun toBase64WithCompress(maxSize: Int): String {
        return "error string"
    }
}
