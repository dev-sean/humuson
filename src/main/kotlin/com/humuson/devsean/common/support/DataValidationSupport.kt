package com.humuson.devsean.common.support

import com.humuson.devsean.common.CUSTOMER_NAME_MAX_LENGTH
import com.humuson.devsean.common.ORDER_ID_MAX_LENGTH
import com.humuson.devsean.common.exception.DataValidationException
import com.humuson.devsean.dto.OrderDto

fun validateExternalOrderData(orderDto: OrderDto) {
    when {
        orderDto.orderId.isBlank() -> throw DataValidationException("주문 ID는 비어 있을 수 없습니다.")
        orderDto.orderId.length > ORDER_ID_MAX_LENGTH -> throw DataValidationException("주문 ID는 10자리를 초과할 수 없습니다.")
        orderDto.customerName.isBlank() -> throw DataValidationException("고객 이름은 비어 있을 수 없습니다.")
        orderDto.customerName.length > CUSTOMER_NAME_MAX_LENGTH -> throw DataValidationException("고객 이름은 20자를 초과할 수 없습니다.")
        !isValidDateFormat(orderDto.orderDate) -> throw DataValidationException("주문 날짜는 yyyy-MM-dd HH:mm:ss 형식이어야 합니다.")
        orderDto.orderStatus !in listOf("NONE", "PREPARING", "DELIVERING", "COMPLETED") ->
            throw DataValidationException("유효하지 않은 주문 상태입니다: ${orderDto.orderStatus}")
    }
}
