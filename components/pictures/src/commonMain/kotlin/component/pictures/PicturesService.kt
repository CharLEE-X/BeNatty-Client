package component.pictures

import component.pictures.model.Bitmap
import component.pictures.model.MediaSource
import core.models.PermissionStatus

interface PicturesService {
    fun checkGalleryPermission(): PermissionStatus
    fun checkCameraPermission(): PermissionStatus
    suspend fun requestGalleryPermission()
    suspend fun requestCameraPermission()
    fun openSettings()
    suspend fun pickImage(source: MediaSource): Bitmap?
}
