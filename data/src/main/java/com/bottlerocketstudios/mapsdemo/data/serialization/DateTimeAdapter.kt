package com.bottlerocketstudios.mapsdemo.data.serialization

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.time.Clock
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

class DateTimeAdapter(private val clock: Clock) {
    @ToJson
    fun toJson(zonedDateTime: ZonedDateTime) = zonedDateTime.toString()

    @FromJson
    fun fromJson(zonedDateTime: String): ZonedDateTime = run {

        try {
            // TODO: To verify if this needed
            ZonedDateTime.parse(zonedDateTime)
        } catch (exception: DateTimeParseException) {

            Timber.e(exception, "Failed to parse zonedDateTime")
            ZonedDateTime.now(clock)
        }
    }
}
