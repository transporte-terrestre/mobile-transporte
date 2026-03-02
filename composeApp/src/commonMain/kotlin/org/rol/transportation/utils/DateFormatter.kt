package org.rol.transportation.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

object DateFormatter {

    private fun formatHourWithMinutes(hour24: Int, minute: Int): String {
        val amPm = if (hour24 >= 12) "PM" else "AM"
        val hour = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        return "$hour:${minute.toString().padStart(2, '0')} $amPm"
    }

    fun formatOnlyTime(date: String): String {
        return try {
            val instant = Instant.parse(date)

            val utcDateTime = instant.toLocalDateTime(TimeZone.UTC)

            val amPm = if (utcDateTime.hour >= 12) "PM" else "AM"
            val hour = when {
                utcDateTime.hour == 0 -> 12
                utcDateTime.hour > 12 -> utcDateTime.hour - 12
                else -> utcDateTime.hour
            }

            "$hour:${utcDateTime.minute.toString().padStart(2, '0')} $amPm"
        } catch (e: Exception) {
            "--:--"
        }
    }

    fun formatOnlyDate(date: String): String {
        return try {
            val instant = Instant.parse(date)
            val utcDateTime = instant.toLocalDateTime(TimeZone.UTC)

            val day = utcDateTime.dayOfMonth.toString().padStart(2, '0')
            val month = utcDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
            val year = utcDateTime.year
            "$day $month $year"
        } catch (e: Exception) {
            date
        }
    }

    fun getCurrentDateFormatted(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val month = now.monthNumber.toString().padStart(2, '0')
        val day = now.dayOfMonth.toString().padStart(2, '0')
        val year = now.year.toString()
        return "$year-$month-$day"
    }

    fun getCurrentTimeFormatted(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val h = now.hour.toString().padStart(2, '0')
        val m = now.minute.toString().padStart(2, '0')
        val s = now.second.toString().padStart(2, '0')
        return "$h:$m:$s"
    }

    fun getCurrentHourAndMinute(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val h = now.hour.toString().padStart(2, '0')
        val m = now.minute.toString().padStart(2, '0')
        return "$h:$m"
    }

    fun getCalculatedRestTimes(addedMinutes: Int): Pair<String, String> {
        val nowInstant = Clock.System.now()
        val now = nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        
        val endInstant = Instant.fromEpochMilliseconds(nowInstant.toEpochMilliseconds() + addedMinutes * 60000L)
        val endLocal = endInstant.toLocalDateTime(TimeZone.currentSystemDefault())

        val startHrFormat = if (now.hour > 12) now.hour - 12 else if (now.hour == 0) 12 else now.hour
        val startAmPm = if (now.hour >= 12) "PM" else "AM"
        val startTimeString = "${startHrFormat}:${now.minute.toString().padStart(2, '0')} $startAmPm"

        val endHrFormat = if (endLocal.hour > 12) endLocal.hour - 12 else if (endLocal.hour == 0) 12 else endLocal.hour
        val endAmPm = if (endLocal.hour >= 12) "PM" else "AM"
        val endTimeString = "${endHrFormat}:${endLocal.minute.toString().padStart(2, '0')} $endAmPm"

        return Pair(startTimeString, endTimeString)
    }

    fun getFullDateTimeStringWithHour(timeStringHHmm: String): String {
        val date = getCurrentDateFormatted()
        val formattedTime = if (timeStringHHmm.length == 5) "$timeStringHHmm:00" else timeStringHHmm
        return "${date}T${formattedTime}.000Z"
    }
}