package location

import co.touchlab.kermit.Logger
import core.models.PermissionStatus
import location.models.LatLng
import location.models.Location

internal class LocationServiceJs(private val logger: Logger) : LocationService {
    override suspend fun getCurrentLocation(): LatLng? {
        logger.v { "Getting location" }
        if (checkPermission() != PermissionStatus.GRANTED) {
            logger.e { "Missing permission" }
            return null
        }

        return null
    }

    override suspend fun getPlaceDetails(
        latitude: Double,
        longitude: Double,
    ): Location? {
        return null
    }

    override fun checkPermission(): PermissionStatus {
        return PermissionStatus.NOT_DETERMINED
    }

    override suspend fun requestPermission() {
        // no-op
    }

    override fun openSettingPage() {
        // no-op
    }
}
