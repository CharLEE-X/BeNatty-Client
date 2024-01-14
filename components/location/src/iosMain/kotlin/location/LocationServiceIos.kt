package location

import co.touchlab.kermit.Logger
import core.models.PermissionStatus
import core.util.openAppSettingsPage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import location.models.LatLng
import location.models.Location
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLDistanceFilterNone
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class LocationServiceIos(private val logger: Logger) : LocationService {
    private var latLngContinuation: Continuation<LatLng?>? = null

    @OptIn(ExperimentalForeignApi::class)
    private val clDelegate = object : CLLocationManagerDelegateProtocol, NSObject() {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val updatedLatLng = didUpdateLocations.filterIsInstance<CLLocation>()
                .takeIf { it.isNotEmpty() }
                ?.let {
                    it.first().coordinate.useContents {
                        LatLng(latitude, longitude)
                    }
                }

            if (updatedLatLng != null) {
                logger.v { "Updated locations $updatedLatLng" }
                latLngContinuation?.resume(updatedLatLng)
                latLngContinuation = null
                manager.stopUpdatingLocation()
            }
        }

        override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
            logger.e { "Failed $didFailWithError" }
            latLngContinuation?.resumeWithException(Throwable(didFailWithError.description))
            latLngContinuation = null
        }
    }

    private val clLocationManager = CLLocationManager().apply {
        desiredAccuracy = kCLLocationAccuracyBest
        distanceFilter = kCLDistanceFilterNone
        delegate = clDelegate
    }

    override suspend fun getCurrentLocation(): LatLng? {
        logger.v { "Getting location" }
        if (checkPermission() != PermissionStatus.GRANTED) {
            logger.e { "Missing permission" }
            return null
        }

        return suspendCoroutine { continuation ->
            latLngContinuation = continuation
            clLocationManager.requestLocation()
        }
    }

    override suspend fun getPlaceDetails(latitude: Double, longitude: Double): Location? {
        val geocoder = CLGeocoder()
        val location = CLLocation(latitude = latitude, longitude = longitude)

        logger.v { "Reverse geocoding $latitude, $longitude" }
        val locationDeferred = CompletableDeferred<Location?>()

        geocoder.reverseGeocodeLocation(location) { clPlacemarks, error ->
            if (error != null) {
                locationDeferred.completeExceptionally(Throwable("Failed to reverse geocode $error"))
                return@reverseGeocodeLocation
            }

            val placemarks = clPlacemarks?.filterIsInstance<CLPlacemark>()
            if (placemarks.isNullOrEmpty()) {
                locationDeferred.completeExceptionally(Throwable("No placemarks found"))
                return@reverseGeocodeLocation
            }

            val firstPlacemark = placemarks.first()
            val city = firstPlacemark.locality ?: ""
            val county = firstPlacemark.subAdministrativeArea ?: ""
            val country = firstPlacemark.country ?: ""

            val newLocation =
                Location(latLng = LatLng(latitude, longitude), city = city, state = county, country = country)
            locationDeferred.complete(newLocation)
        }

        return locationDeferred.await()
    }

    override fun checkPermission(): PermissionStatus {
        return when (clLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusRestricted,
            -> PermissionStatus.GRANTED

            kCLAuthorizationStatusNotDetermined -> PermissionStatus.NOT_DETERMINED
            kCLAuthorizationStatusDenied -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
    }

    override suspend fun requestPermission() {
        MainScope().launch {
            clLocationManager.requestWhenInUseAuthorization()
        }
    }

    override fun openSettingPage() {
        openAppSettingsPage()
    }
}
