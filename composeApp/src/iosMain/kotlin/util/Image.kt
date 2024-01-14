package util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

@Composable
internal actual fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? {
    return remember(bytes) {
        if (bytes != null) {
            val image = Image.makeFromEncoded(bytes)
            Bitmap.makeFromImage(image).asComposeImageBitmap()
        } else {
            null
        }
    }
}
