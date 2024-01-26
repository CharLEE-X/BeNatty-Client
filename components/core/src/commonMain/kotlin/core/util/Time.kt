package core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentTimeMillis(): Long {
    return Clock.System.now().epochSeconds
}

fun currentYear(): Int {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
}
