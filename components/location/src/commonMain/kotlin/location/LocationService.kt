package location

import core.models.PermissionStatus
import location.models.LatLng
import location.models.Location

interface LocationService {
    fun checkPermission(): PermissionStatus
    suspend fun requestPermission()
    fun openSettingPage()
    suspend fun getCurrentLocation(): LatLng?
    suspend fun getPlaceDetails(latitude: Double, longitude: Double): Location?
}
