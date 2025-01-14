package com.humuson.devsean.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.humuson.devsean.common.support.convertStringToLocalDateTime
import com.humuson.devsean.common.support.validateExternalOrderData
import com.humuson.devsean.dto.ExternalOrderDto
import com.humuson.devsean.entity.Order
import com.humuson.devsean.entity.OrderStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ExternalDataConverterImpl : ExternalDataConverter {
    private val logger = LoggerFactory.getLogger(ExternalSystemConnectorImpl::class.java)

    override fun convertToOrder(externalOrderDto: ExternalOrderDto): Order {
        logger.info("외부 주문 데이터 변환 시작: $externalOrderDto")

        try {
            // 데이터 검증
            validateExternalOrderData(externalOrderDto)
            logger.debug("외부 주문 데이터 검증 완료")

            // 데이터 변환
            val order = Order(
                orderId = externalOrderDto.orderId,
                customerName = externalOrderDto.customerName,
                orderDate = convertStringToLocalDateTime(externalOrderDto.orderDate),
                orderStatus = convertStatus(externalOrderDto.orderStatus)
            )

            logger.info("외부 주문 데이터 변환 완료: $order")
            return order
        } catch (e: Exception) {
            logger.error("외부 주문 데이터 변환 중 오류 발생", e)
            throw e
        }
    }

    private fun convertStatus(externalStatus: String): OrderStatus {
        return when (externalStatus) {
            "PREPARING" -> OrderStatus.PREPARING
            "DELIVERING" -> OrderStatus.DELIVERING
            "COMPLETED" -> OrderStatus.COMPLETED
            else -> OrderStatus.NONE
        }
    }

    override fun convertToExternalJson(orders: List<Order>): String {
        logger.info("내부 주문 데이터를 외부 시스템 형식으로 변환 시작: ${orders.size}개 주문")

        try {
            val objectMapper = jacksonObjectMapper()
            val result = objectMapper.writeValueAsString(orders)
            logger.debug("변환된 JSON 데이터: $result")
            logger.info("내부 주문 데이터 변환 완료")
            return result
        } catch (e: Exception) {
            logger.error("내부 주문 데이터 변환 중 오류 발생", e)
            throw e
        }
    }
}
