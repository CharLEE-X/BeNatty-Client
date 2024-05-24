package util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

internal val currentYear = Clock.System.todayIn(TimeZone.currentSystemDefault()).year

internal fun Long.toTime(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = localDateTime.month.name
    val day = localDateTime.dayOfMonth
    val year = localDateTime.year
    val hour = localDateTime.hour
    val minute = localDateTime.minute
    val pmOrAm = if (localDateTime.hour > 12) "PM" else "AM"

    return if (year == currentYear) {
        "$month $day $hour:$minute $pmOrAm"
    } else {
        "$month $day $year $hour:$minute $pmOrAm"
    }
}

internal fun Long.toDateOrTime(): String {
    val instantBefore = Instant.fromEpochMilliseconds(this)
    val localBefore = instantBefore.toLocalDateTime(TimeZone.currentSystemDefault())

    val now = Clock.System.now()
    val localNow = now.toLocalDateTime(TimeZone.currentSystemDefault())

    val isToday = localBefore.date == localNow.date

    return if (isToday) {
        val hour = localBefore.hour
        val minute = localBefore.minute
        "$hour:$minute"
    } else {
        val month = localBefore.month.name
        val day = localBefore.dayOfMonth
        "$month $day"
    }
}

internal fun Long.date(): LocalDate {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.date
}
