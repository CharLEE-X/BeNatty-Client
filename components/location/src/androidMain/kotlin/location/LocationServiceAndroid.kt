package location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.os.Build
import co.touchlab.kermit.Logger
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import core.models.Permission
import core.models.PermissionStatus
import core.util.checkPermissions
import core.util.openAppSettingsPage
import core.util.providePermissions
import kotlinx.coroutines.CompletableDeferred
import location.models.LatLng
import location.models.Location
import java.util.Locale

internal class LocationServiceAndroid(
    private val logger: Logger,
    private val context: Context,
    private val activity: Lazy<Activity>,
) : LocationService {
    private val locationClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val fineLocationPermissions: List<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        } else {
            listOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng? {
        logger.v { "Getting location" }
        return when (checkPermission()) {
            PermissionStatus.GRANTED -> {
                collectLocation()
            }

            PermissionStatus.NOT_DETERMINED -> {
                requestPermission()
                null
            }

            PermissionStatus.DENIED -> {
                openSettingPage()
                null
            }
        }
    }

    override fun checkPermission(): PermissionStatus {
        return checkPermissions(
            context = context,
            activity = activity,
            logger = logger,
            permissions = fineLocationPermissions,
        )
    }

    override suspend fun requestPermission() {
        activity.value.providePermissions(fineLocationPermissions) {
            error(it.localizedMessage ?: "Failed to request foreground location permission")
        }
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.LOCATION_FOREGROUND)
    }

    @SuppressLint("MissingPermission")
    private suspend fun collectLocation(): LatLng? {
        val deferred = CompletableDeferred<LatLng?>()

        try {
            val priority = Priority.PRIORITY_HIGH_ACCURACY
            val locationResult = locationClient.getCurrentLocation(priority, CancellationTokenSource().token)
            locationResult.addOnCompleteListener {
                if (it.isSuccessful) {
                    deferred.complete(LatLng(it.result.latitude, it.result.longitude))
                } else {
                    deferred.complete(null)
                }
            }
        } catch (e: Exception) {
            deferred.complete(null)
        }

        val location = deferred.await()
        logger.v { "Location: $location" }
        return location
    }

    override suspend fun getPlaceDetails(latitude: Double, longitude: Double): Location? {
        logger.v { "Getting place details for $latitude, $longitude" }

        val geocoder = Geocoder(context, Locale.ENGLISH)
        return geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.let {
            val city = it.locality ?: ""
            val state = it.adminArea ?: ""
            val country = it.countryName ?: ""

            Location(
                latLng = LatLng(latitude, longitude),
                city = city,
                state = state,
                country = country,
            )
        }
    }
}
