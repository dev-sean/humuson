package com.humuson.devsean

import com.humuson.devsean.common.exception.DataValidationException
import com.humuson.devsean.common.support.convertStringToLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SupportTest {

    @Test
    fun `문자열 날짜를 LocalDateTime으로 변환 테스트`() {
        // given
        val dateString = "2025-01-14 14:00:00"

        // when
        val result = convertStringToLocalDateTime(dateString)

        // then
        assertEquals(2025, result.year)
        assertEquals(1, result.monthValue)
        assertEquals(14, result.dayOfMonth)
        assertEquals(14, result.hour)
        assertEquals(0, result.minute)
        assertEquals(0, result.second)
    }

    @Test
    fun `잘못된 날짜 형식 예외 처리 테스트`() {
        // given
        val invalidDateStrings = listOf(
            "2025-13-40",  // 잘못된 월/일
            "2025/01/14",  // 잘못된 구분자
            "invalid-date", // 잘못된 형식
            ""            // 빈 문자열
        )

        // when & then
        invalidDateStrings.forEach { dateString ->
            assertFailsWith<DataValidationException> {
                convertStringToLocalDateTime(dateString)
            }
        }
    }

}