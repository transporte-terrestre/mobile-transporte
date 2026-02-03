package org.rol.transportation.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateFormatter {

    fun formatDate(date: String): String {
        return try {
            val instant = Instant.parse(date)
            val dateTime = instant.toLocalDateTime(TimeZone.UTC)

            val day = dateTime.dayOfMonth.toString().padStart(2, '0')

            val month = dateTime.month.name
                .lowercase()
                .replaceFirstChar { it.uppercase() }
                .take(3)

            val year = dateTime.year

            val hourFormatted = formatHourWithMinutes(
                dateTime.hour,
                dateTime.minute
            )

            "$day $month $year $hourFormatted"
        } catch (e: Exception) {
            date
        }
    }

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
}