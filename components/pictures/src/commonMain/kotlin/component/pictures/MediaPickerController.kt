package component.pictures

import component.pictures.model.Bitmap
import component.pictures.model.MediaSource

interface MediaPickerController {
    suspend fun pickImage(source: MediaSource): Bitmap?
}
