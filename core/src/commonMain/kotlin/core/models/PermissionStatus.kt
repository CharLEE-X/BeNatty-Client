package core.models

enum class PermissionStatus {
    NOT_DETERMINED,
    GRANTED,
    DENIED;

    fun isGranted(): Boolean = this == GRANTED
    fun isNotGranted(): Boolean = this != GRANTED
}
