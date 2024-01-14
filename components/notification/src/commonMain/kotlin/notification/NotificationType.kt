package notification

import kotlinx.datetime.Instant

sealed interface NotificationType {
    data class Immediate(
        val title: String,
        val body: String,
        val soundType: SoundType = SoundType.DEFAULT,
    ) : NotificationType

    data class Scheduled(
        val title: String,
        val body: String,
        val delivery: Instant,
        val soundType: SoundType = SoundType.DEFAULT,
    ) : NotificationType
}

enum class SoundType {
    DEFAULT,
    CRITICAL,
    NONE
}
