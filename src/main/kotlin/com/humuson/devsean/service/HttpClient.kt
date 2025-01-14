package com.humuson.devsean.service

import org.springframework.http.ResponseEntity

interface HttpClient {
    fun <T> get(url: String, responseType: Class<T>): T
    fun <T> post(url: String, body: Any, responseType: Class<T>): ResponseEntity<T>
}
