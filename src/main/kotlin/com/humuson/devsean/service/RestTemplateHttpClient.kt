package com.humuson.devsean.service

import com.humuson.devsean.common.exception.HttpClientException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RestTemplateHttpClient(private val restTemplate: RestTemplate) : HttpClient {
    override fun <T> get(url: String, responseType: Class<T>): T {
        return restTemplate.getForObject(url, responseType)
            ?: throw HttpClientException("GET 요청 실패")
    }

    override fun <T> post(url: String, body: Any, responseType: Class<T>): ResponseEntity<T> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val request = HttpEntity(body, headers)
        val response = restTemplate.postForEntity(url, request, responseType)
        if (response.statusCode.is2xxSuccessful) {
            return response
        } else {
            throw HttpClientException("POST 요청 실패: ${response.statusCode}")
        }
    }
}
