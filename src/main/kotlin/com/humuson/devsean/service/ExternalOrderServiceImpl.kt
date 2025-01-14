package com.humuson.devsean.service

import com.humuson.devsean.common.exception.ExternalSystemException
import com.humuson.devsean.common.support.convertStringToLocalDateTime
import com.humuson.devsean.common.support.validateExternalOrderData
import com.humuson.devsean.dto.OrderDto
import com.humuson.devsean.entity.Order
import com.humuson.devsean.entity.OrderStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Service
class ExternalOrderServiceImpl(
    @Value("\${external.api.get-url}")
    private val externalApiGetUrl: String,
    @Value("\${external.api.post-url}")
    private val externalApiPostUrl: String,
    @Value("\${retry.times}")
    private val retryTimes: Int,
    @Value("\${retry.delay-millis}")
    private val delayMillis: Long,
    private val restTemplate: RestTemplate,
    private val orderService: OrderService
) : ExternalOrderService {
    private val logger = LoggerFactory.getLogger(ExternalOrderServiceImpl::class.java)

    override fun fetchOrdersFromExternalSystem() = runBlocking {
        retry(retryTimes, delayMillis) {
            try {
                val response = restTemplate.getForObject(
                    externalApiGetUrl,
                    Array<OrderDto>::class.java
                )
                // TODO
                logger.info(response?.toList().toString())

                val orders = response?.map { convertToInternalOrder(it) } ?: emptyList()
                orderService.saveOrders(orders)
            } catch (e: Exception) {
                handleException(e, "주문 조회")
            }
        }
    }

    override fun sendOrdersToExternalSystem() = runBlocking {
        val orders = orderService.getAllOrders()
        retry(retryTimes, delayMillis) {
            try {
                val headers = HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_JSON
                }
                val request = HttpEntity(orders, headers)
                val response = restTemplate.postForEntity(externalApiPostUrl, request, String::class.java)
                if (response.statusCode.is2xxSuccessful) {
                    logger.info("주문이 성공적으로 전송되었습니다.")
                } else {
                    throw ExternalSystemException("주문 전송 실패: ${response.statusCode}")
                }
            } catch (e: Exception) {
                handleException(e, "주문 전송")
            }
        }
    }

    private suspend fun <T> retry(times: Int, delayMillis: Long, block: suspend () -> T): T {
        repeat(times) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                logger.warn("시도 ${attempt + 1} 실패| ${e.message}. 재시도 중...")
                delay(delayMillis)
            }
        }
        return block() // 마지막 시도
    }

    private fun handleException(e: Exception, operation: String): Nothing {
        val message = when (e) {
            is HttpClientErrorException -> "클라이언트 오류: ${e.statusCode} - ${e.message}"
            is HttpServerErrorException -> "서버 오류: ${e.statusCode} - ${e.message}"
            is ResourceAccessException -> when (e.cause) {
                is ConnectException -> "외부 API 서버에 연결할 수 없습니다: ${e.message}"
                is SocketTimeoutException -> "요청 시간이 초과되었습니다: ${e.message}"
                is UnknownHostException -> "호스트를 찾을 수 없습니다: ${e.message}"
                else -> "리소스 접근 중 오류 발생: ${e.message}"
            }

            is RestClientException -> "REST 클라이언트 오류: ${e.message}"
            else -> "예기치 않은 오류 발생: ${e.message}"
        }
        throw ExternalSystemException("$operation 중 오류 발생: $message")
    }

    override fun convertToInternalOrder(orderDto: OrderDto): Order {
        // 데이터 검증
        validateExternalOrderData(orderDto)

        // 데이터 변환
        return Order(
            orderId = orderDto.orderId,
            customerName = orderDto.customerName,
            orderDate = convertStringToLocalDateTime(orderDto.orderDate),
            orderStatus = convertStatus(orderDto.orderStatus)
        )
    }

    private fun convertStatus(externalStatus: String): OrderStatus {
        return when (externalStatus) {
            "PREPARING" -> OrderStatus.PREPARING
            "DELIVERING" -> OrderStatus.DELIVERING
            "COMPLETED" -> OrderStatus.COMPLETED
            else -> OrderStatus.NONE
        }
    }

}
