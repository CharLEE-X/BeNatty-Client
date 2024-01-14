package component.pictures

import co.touchlab.kermit.Logger
import component.pictures.model.Bitmap
import component.pictures.model.MediaSource
import core.models.PermissionStatus
import core.util.mainContinuation
import core.util.openAppSettingsPage
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class PicturesServiceIos(
    private val logger: Logger,
    private val mediaPickerController: MediaPickerController,
) : PicturesService {
    private val avMediaType = AVMediaTypeVideo

    override fun checkGalleryPermission(): PermissionStatus {
        return when (val status: PHAuthorizationStatus = PHPhotoLibrary.authorizationStatus()) {
            PHAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
            PHAuthorizationStatusNotDetermined -> PermissionStatus.NOT_DETERMINED
            PHAuthorizationStatusDenied -> PermissionStatus.DENIED
            else -> error("Unknown Gallery authorization status $status")
        }
    }

    override fun checkCameraPermission(): PermissionStatus {
        return when (val status: AVAuthorizationStatus = currentCameraAuthorizationStatus()) {
            AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
            AVAuthorizationStatusNotDetermined -> PermissionStatus.NOT_DETERMINED
            AVAuthorizationStatusDenied -> PermissionStatus.DENIED
            AVAuthorizationStatusRestricted -> PermissionStatus.GRANTED
            else -> error("unknown authorization status $status")
        }
    }

    override suspend fun requestGalleryPermission() {
        logger.d { "Requesting Gallery permission." }
        provideGalleryPermission(PHPhotoLibrary.authorizationStatus())
    }

    override suspend fun requestCameraPermission() {
        logger.d { "Requesting Camera permission." }
        when (val status: AVAuthorizationStatus = currentCameraAuthorizationStatus()) {
            AVAuthorizationStatusAuthorized -> return
            AVAuthorizationStatusNotDetermined -> {
                val isGranted: Boolean = suspendCoroutine { continuation ->
                    AVCaptureDevice.requestAccessForMediaType(avMediaType) { continuation.resume(it) }
                }
                if (isGranted) return
                else throw Exception("Permission Camera denied")
            }

            AVAuthorizationStatusDenied -> throw Exception("Permission Camera denied /';[p-=0-ol, l")
            else -> error("unknown authorization status $status")
        }
    }

    override fun openSettings() {
        logger.d { "Opening Settings." }
        openAppSettingsPage()
    }

    override suspend fun pickImage(source: MediaSource): Bitmap? {
        logger.d { "Picking image from $source." }
        when (source) {
            MediaSource.GALLERY -> {
                provideGalleryPermission(PHPhotoLibrary.authorizationStatus())
                if (checkGalleryPermission() != PermissionStatus.GRANTED) return null
            }

            MediaSource.CAMERA -> {
                requestCameraPermission()
                if (checkCameraPermission() != PermissionStatus.GRANTED) return null
            }
        }
        return mediaPickerController.pickImage(source)
    }

    private suspend fun provideGalleryPermission(status: PHAuthorizationStatus) {
        return when (status) {
            PHAuthorizationStatusAuthorized -> return
            PHAuthorizationStatusNotDetermined -> {
                val newStatus = suspendCoroutine { continuation ->
                    requestGalleryAccess { continuation.resume(it) }
                }
                provideGalleryPermission(newStatus)
            }

            PHAuthorizationStatusDenied -> throw Exception("Permission Gallery denied")
            else -> error("unknown gallery authorization status $status")
        }
    }

    private fun requestGalleryAccess(callback: (PHAuthorizationStatus) -> Unit) {
        PHPhotoLibrary.requestAuthorization(
            mainContinuation { status: PHAuthorizationStatus -> callback(status) }
        )
    }

    private fun currentCameraAuthorizationStatus(): AVAuthorizationStatus {
        return AVCaptureDevice.authorizationStatusForMediaType(avMediaType)
    }
}
