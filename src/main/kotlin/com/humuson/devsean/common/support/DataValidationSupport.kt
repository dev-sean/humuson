package com.humuson.devsean.common.support

import com.humuson.devsean.common.CUSTOMER_NAME_MAX_LENGTH
import com.humuson.devsean.common.ORDER_ID_MAX_LENGTH
import com.humuson.devsean.common.exception.DataValidationException
import com.humuson.devsean.dto.ExternalOrderDto

fun validateExternalOrderData(externalOrderDto: ExternalOrderDto) {
    when {
        externalOrderDto.orderId.isBlank() -> throw DataValidationException("주문 ID는 비어 있을 수 없습니다.")
        externalOrderDto.orderId.length > ORDER_ID_MAX_LENGTH -> throw DataValidationException("주문 ID는 ${ORDER_ID_MAX_LENGTH}자리를 초과할 수 없습니다.")
        externalOrderDto.customerName.isBlank() -> throw DataValidationException("고객 이름은 비어 있을 수 없습니다.")
        externalOrderDto.customerName.length > CUSTOMER_NAME_MAX_LENGTH -> throw DataValidationException("고객 이름은 ${CUSTOMER_NAME_MAX_LENGTH}자를 초과할 수 없습니다.")
        !isValidDateFormat(externalOrderDto.orderDate) -> throw DataValidationException("주문 날짜는 $DATE_FORMAT_YYYY_MM_DD_TIME 형식이어야 합니다.")
    }
}
