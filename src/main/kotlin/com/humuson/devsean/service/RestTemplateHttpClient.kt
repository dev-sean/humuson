package com.humuson.devsean.service

import com.humuson.devsean.common.exception.HttpClientException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RestTemplateHttpClient(private val restTemplate: RestTemplate) : HttpClient {
    private val logger = LoggerFactory.getLogger(RestTemplateHttpClient::class.java)

    override fun <T> get(url: String, responseType: Class<T>): T {
        try {
            return restTemplate.getForObject(url, responseType)
                ?: throw HttpClientException("GET 요청 실패")
        } catch (e: Exception) {
            logger.error("GET 요청 중 오류 발생: $url", e)
            throw HttpClientException("GET 요청 중 예기치 않은 오류 발생", e)
        }
    }

    override fun <T> post(url: String, body: Any, responseType: Class<T>): ResponseEntity<T> {
        try {
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }
            val request = HttpEntity(body, headers)
            val response = restTemplate.postForEntity(url, request, responseType)
            if (response.statusCode.is2xxSuccessful) {
                return response
            } else {
                logger.error("POST 요청 실패: $url, 상태 코드: ${response.statusCode}")
                throw HttpClientException("POST 요청 실패: ${response.statusCode}")
            }
        } catch (e: Exception) {
            logger.error("POST 요청 중 오류 발생: $url", e)
            throw HttpClientException("POST 요청 중 예기치 않은 오류 발생", e)
        }
    }
}
