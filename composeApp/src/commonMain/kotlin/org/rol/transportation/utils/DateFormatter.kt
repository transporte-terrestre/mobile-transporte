package org.rol.transportation.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateFormatter {

    fun formatDate(date: String): String {
        return try {
            val instant = Instant.parse(date)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

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

    fun formatRange(departure: String, arrival: String): String {
        return try {
            val start = Instant.parse(departure)
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val end = Instant.parse(arrival)
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val date =
                "${start.dayOfMonth.toString().padStart(2, '0')} " +
                        start.month.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() }
                            .take(3)

            val startHour = formatHour(start.hour)
            val endHour = formatHour(end.hour)

            "$date $startHour → $endHour"
        } catch (e: Exception) {
            departure
        }
    }

    private fun formatHour(hour24: Int): String {
        val amPm = if (hour24 >= 12) "PM" else "AM"
        val hour = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        return "$hour $amPm"
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
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            // Usamos tu lógica existente de formateo
            val amPm = if (dateTime.hour >= 12) "PM" else "AM"
            val hour = when {
                dateTime.hour == 0 -> 12
                dateTime.hour > 12 -> dateTime.hour - 12
                else -> dateTime.hour
            }
            "$hour:${dateTime.minute.toString().padStart(2, '0')}"
        } catch (e: Exception) {
            "--:--"
        }
    }

    fun formatOnlyDate(date: String): String {
        return try {
            val instant = Instant.parse(date)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val day = dateTime.dayOfMonth.toString().padStart(2, '0')
            val month = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
            val year = dateTime.year
            "$day $month $year"
        } catch (e: Exception) {
            date
        }
    }
}