package core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentTimeMillis(): Long {
    return Clock.System.now().epochSeconds
}

fun currentYear(): Int {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
}

fun millisToTime(millis: Long): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return with(local) {
        "$date ${time.hour}:${time.minute}"
    }
}
