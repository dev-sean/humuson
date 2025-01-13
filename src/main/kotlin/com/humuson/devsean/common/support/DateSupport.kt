package com.humuson.devsean.common.support

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"

fun isValidDateFormat(dateString: String): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYY_MM_DD)
        LocalDate.parse(dateString, formatter)
        true
    } catch (e: DateTimeParseException) {
        false
    }
}


