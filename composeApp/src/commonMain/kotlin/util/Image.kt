package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
internal expect fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap?
