package com.humuson.devsean.common.support

import com.humuson.devsean.common.exception.DataValidationException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

const val DATE_FORMAT_YYYY_MM_DD_TIME = "yyyy-MM-dd HH:mm:ss"

fun isValidDateFormat(dateString: String): Boolean {
    return try {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYY_MM_DD_TIME)
        LocalDateTime.parse(dateString, formatter)
        true
    } catch (e: DateTimeParseException) {
        false
    }
}

fun convertStringToLocalDateTime(dateString: String): LocalDateTime {
    try {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYY_MM_DD_TIME)
        return LocalDateTime.parse(dateString, formatter)
    } catch (e: DateTimeParseException) {
        throw DataValidationException("지원하지 않는 날짜 형식입니다: $dateString")
    }
}
