package core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

fun currentTimeMillis(): Long {
    return Clock.System.now().epochSeconds
}

fun currentYear(): Int {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
}

fun millisToLocalDateTime(millis: Long): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(millis)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun millisToTime(millis: Long): String {
    val local = millisToLocalDateTime(millis)
    return with(local) {
        val day = if (date.dayOfMonth < 10) "0${date.dayOfMonth}" else date.dayOfMonth
        val month = if (month.number < 10) "0${month.number}" else month.number
        val hours = if (hour < 10) "0$hour" else hour
        val minutes = if (minute < 10) "0$minute" else minute
        "$day-$month-$year $hours:$minutes"
    }
}

fun millisToDate(millis: Long): String {
    val local = millisToLocalDateTime(millis)
    return with(local.date) {
        val day = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
        val month = if (month.number < 10) "0${month.number}" else month.number
        "${day}-${month}-${year}"
    }
}
