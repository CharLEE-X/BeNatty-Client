package location.models

data class LatLng(
    val latitude: Double,
    val longitude: Double,
)

data class Location(
    val latLng: LatLng,
    val city: String,
    val state: String,
    val country: String,
)
