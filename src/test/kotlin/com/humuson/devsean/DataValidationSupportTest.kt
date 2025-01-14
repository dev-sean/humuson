package com.humuson.devsean

import com.humuson.devsean.common.CUSTOMER_NAME_MAX_LENGTH
import com.humuson.devsean.common.ORDER_ID_MAX_LENGTH
import com.humuson.devsean.common.exception.DataValidationException
import com.humuson.devsean.common.support.DATE_FORMAT_YYYY_MM_DD_TIME
import com.humuson.devsean.common.support.validateExternalOrderData
import com.humuson.devsean.dto.ExternalOrderDto
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DataValidationSupportTest {
    @Test
    fun `주문 ID가 비어있는 경우 예외 발생 테스트`() {
        // given
        val orderDto = ExternalOrderDto(
            orderId = "",
            customerName = "테스트 고객",
            orderDate = "2025-01-14 14:00:00",
            orderStatus = "PREPARING"
        )

        // when & then
        assertFailsWith<DataValidationException>("주문 ID는 비어 있을 수 없습니다.") {
            validateExternalOrderData(orderDto)
        }
    }

    @Test
    fun `주문 ID가 최대 길이를 초과하는 경우 예외 발생 테스트`() {
        // given
        val orderDto = ExternalOrderDto(
            orderId = "test-order-id-too-long",
            customerName = "테스트 고객",
            orderDate = "2025-01-14T 4:00:00",
            orderStatus = "PREPARING"
        )

        // when & then
        assertFailsWith<DataValidationException>("주문 ID는 ${ORDER_ID_MAX_LENGTH}자리를 초과할 수 없습니다.") {
            validateExternalOrderData(orderDto)
        }
    }

    @Test
    fun `고객 이름이 비어있는 경우 예외 발생 테스트`() {
        // given
        val orderDto = ExternalOrderDto(
            orderId = "test1",
            customerName = "",
            orderDate = "2025-01-14 14:00:00",
            orderStatus = "PREPARING"
        )

        // when & then
        assertFailsWith<DataValidationException>("고객 이름은 비어 있을 수 없습니다.") {
            validateExternalOrderData(orderDto)
        }
    }

    @Test
    fun `고객 이름이 최대 길이를 초과하는 경우 예외 발생 테스트`() {
        // given
        val orderDto = ExternalOrderDto(
            orderId = "test1",
            customerName = "너무 긴 고객 이름입니다".repeat(10),
            orderDate = "2025-01-14 14:00:00",
            orderStatus = "PREPARING"
        )

        // when & then
        assertFailsWith<DataValidationException>("고객 이름은 ${CUSTOMER_NAME_MAX_LENGTH}자를 초과할 수 없습니다.") {
            validateExternalOrderData(orderDto)
        }
    }

    @Test
    fun `잘못된 날짜 형식인 경우 예외 발생 테스트`() {
        // given
        val orderDto = ExternalOrderDto(
            orderId = "test1",
            customerName = "테스트 고객",
            orderDate = "2025-13-40",
            orderStatus = "PREPARING"
        )

        // when & then
        assertFailsWith<DataValidationException>("주문 날짜는 $DATE_FORMAT_YYYY_MM_DD_TIME 형식이어야 합니다.") {
            validateExternalOrderData(orderDto)
        }
    }

    @Test
    fun `올바른 데이터 형식인 경우 검증 통과 테스트`() {
        // given
        val orderDto = ExternalOrderDto(
            orderId = "test1",
            customerName = "테스트 고객",
            orderDate = "2025-01-14 14:00:00",
            orderStatus = "PREPARING"
        )

        // when & then
        assertDoesNotThrow {
            validateExternalOrderData(orderDto)
        }
    }
}
