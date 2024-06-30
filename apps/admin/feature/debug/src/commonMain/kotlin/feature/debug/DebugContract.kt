package feature.debug

import component.pictures.model.Bitmap
import core.Platform
import core.currentPlatform
import core.models.PermissionStatus
import feature.debug.model.LatLng
import feature.debug.model.Location

private const val APPLE_LATITUDE = 37.334606
private const val APPLE_LONGITUDE = -122.009102

private const val GOOGLE_LATITUDE = 37.422160
private const val GOOGLE_LONGITUDE = -122.084270

val defaultLocation =
    when (currentPlatform) {
        Platform.IOS -> LatLng(APPLE_LATITUDE, APPLE_LONGITUDE)
        Platform.ANDROID -> LatLng(GOOGLE_LATITUDE, GOOGLE_LONGITUDE)
        else -> LatLng(0.0, 0.0)
    }

object DebugContract {
    data class State(
        val isLoading: Boolean = false,
        val isPremium: Boolean = false,
        val generateUsersTarget: Int = 100,
        val undoCount: Int = 10,
        val showMap: Boolean = false,
        val latLng: LatLng? = null,
        val location: Location? = null,
        val locationPermissionStatus: PermissionStatus = PermissionStatus.NOT_DETERMINED,
        val notificationPermissionStatus: PermissionStatus = PermissionStatus.NOT_DETERMINED,
        val cameraPermissionStatus: PermissionStatus = PermissionStatus.NOT_DETERMINED,
        val galleryPermissionStatus: PermissionStatus = PermissionStatus.NOT_DETERMINED,
        val notificationText: String = "Forevely notification",
        val pickedBitmaps: List<Bitmap> = emptyList(),
    )

    sealed interface Inputs {
        data class SetIsLoading(val isLoading: Boolean) : Inputs

        data object Init : Inputs

        data object OnClearCacheClick : Inputs

        data class SetGenerateUsersTarget(val target: String) : Inputs

        data class SetUndoCount(val count: String) : Inputs

        //        data object GenerateUsers : Inputs
//        data object DeleteAllUsers : Inputs
//        data object DeleteGeneratedUsers : Inputs
        data class SetIsPremium(val isPremium: Boolean) : Inputs

        data object OpenAppSettings : Inputs

        // Location
        data object ObserveLocationPermission : Inputs

        data class SetLocationPermissionStatus(val status: PermissionStatus) : Inputs

        data object RequestLocationPermission : Inputs

        data object GetLatLng : Inputs

        data class SetLatLng(val latLng: LatLng, val showMap: Boolean = true) : Inputs

        data object GetLocation : Inputs

        data class SetLocation(val location: Location) : Inputs

        // Notification
        data object ObserveNotificationPermission : Inputs

        data object RequestNotificationPermission : Inputs

        data class SetNotificationPermission(val status: PermissionStatus) : Inputs

        data class SetNotificationText(val text: String) : Inputs

        data object SendNotificationNow : Inputs

        // Pictures
        data object ObserveCameraPermission : Inputs

        data object ObserveGalleryPermission : Inputs

        data object RequestCameraPermission : Inputs

        data class SetCameraPermissionStatus(val status: PermissionStatus) : Inputs

        data object RequestGalleryPermission : Inputs

        data class SetGalleryPermission(val status: PermissionStatus) : Inputs

        data object PickImageFromGallery : Inputs

        data object PickImageFromCamera : Inputs

        data class SetPickedBitmaps(val bitmaps: List<Bitmap>) : Inputs

        data object ClearPickedBitmaps : Inputs
    }

    sealed interface Events {
        data class OnError(val message: String) : Events

        data class OnDeleteUsersSuccess(val message: String) : Events

        data class OnGenerateUsersSuccess(val message: String) : Events
    }
}
