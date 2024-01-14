package component.pictures

import co.touchlab.kermit.Logger
import component.pictures.model.Bitmap
import component.pictures.model.MediaSource
import core.models.PermissionStatus

internal class PicturesServiceIos(
    private val logger: Logger,
) : PicturesService {
    override fun checkGalleryPermission(): PermissionStatus {
        return PermissionStatus.NOT_DETERMINED
    }

    override fun checkCameraPermission(): PermissionStatus {
        return PermissionStatus.NOT_DETERMINED
    }

    override suspend fun requestGalleryPermission() {
        logger.d { "Requesting Gallery permission." }
    }

    override suspend fun requestCameraPermission() {
        logger.d { "Requesting Camera permission." }
    }

    override fun openSettings() {
        logger.d { "Opening Settings." }
    }

    override suspend fun pickImage(source: MediaSource): Bitmap? {
        logger.d { "Picking image from $source." }
        return null
    }
}
