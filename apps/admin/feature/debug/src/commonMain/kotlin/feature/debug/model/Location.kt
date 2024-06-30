package feature.debug.model

import location.models.LatLng as CoreLatLng
import location.models.Location as CoreLocation

data class LatLng(
    val latitude: Double,
    val longitude: Double,
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val state: String,
    val country: String,
)

internal fun CoreLatLng.toLatLng() =
    LatLng(
        latitude = latitude,
        longitude = longitude,
    )

internal fun CoreLocation.toLocation() =
    Location(
        latitude = latLng.latitude,
        longitude = latLng.longitude,
        city = city,
        state = state,
        country = country,
    )
