package com.humuson.devsean.service

import com.humuson.devsean.common.exception.ExternalSystemException
import com.humuson.devsean.dto.ExternalOrderDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestClientException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Service
class ExternalSystemConnectorImpl(
    @Value("\${external.api.get-url}")
    private val externalApiGetUrl: String,
    @Value("\${external.api.post-url}")
    private val externalApiPostUrl: String,
    @Value("\${retry.times}")
    private val retryTimes: Int,
    @Value("\${retry.delay-millis}")
    private val delayMillis: Long,
    private val restTemplateHttpClient: RestTemplateHttpClient,
    private val dataConvertServiceImpl: ExternalDataConverterImpl,
    private val orderService: OrderService
) : ExternalSystemConnector {
    private val logger = LoggerFactory.getLogger(ExternalSystemConnectorImpl::class.java)

    override fun fetchExternalOrders() = runBlocking {
        retry(retryTimes, delayMillis) {
            try {
                val response = restTemplateHttpClient.get(
                    externalApiGetUrl,
                    Array<ExternalOrderDto>::class.java
                )
                val orders = response.map { dataConvertServiceImpl.convertToOrder(it) }
                orderService.saveOrders(orders)
                logger.info("주문이 성공적으로 수신되었습니다.")
            } catch (e: Exception) {
                handleCustomException(e, "주문 조회")
            }
        }
    }

    override fun sendOrdersToExternalSystem() = runBlocking {
        val orders = orderService.findAllOrders()
        val jsonOrder = dataConvertServiceImpl.convertToExternalJson(orders)
        retry(retryTimes, delayMillis) {
            try {
                val response = restTemplateHttpClient.post(externalApiPostUrl, jsonOrder, String::class.java)
                if (response.statusCode.is2xxSuccessful) {
                    logger.info("주문이 성공적으로 전송되었습니다.")
                } else {
                    logger.error("주문 전송 실패: HTTP 상태 코드 ${response.statusCode}")
                    throw ExternalSystemException("주문 전송 실패: HTTP 상태 코드 ${response.statusCode}")
                }
            } catch (e: Exception) {
                handleCustomException(e, "주문 전송")
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

    private fun handleCustomException(e: Exception, operation: String): Nothing {
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

        val fullErrorMessage = "$operation 중 오류 발생: $message"
        logger.error(fullErrorMessage, e)

        throw ExternalSystemException(fullErrorMessage, e)
    }
}
