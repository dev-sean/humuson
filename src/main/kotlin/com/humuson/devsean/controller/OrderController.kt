package com.humuson.devsean.controller

import com.humuson.devsean.entity.Order
import com.humuson.devsean.service.ExternalOrderServiceImpl
import com.humuson.devsean.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
    private val orderServiceImpl: ExternalOrderServiceImpl
) {

    // 외부 시스템으로 부터 데이터를 가져온다.
    @GetMapping("external-data")
    fun getExternalOrderData(): ResponseEntity<Any> {
        return try {
            orderServiceImpl.fetchOrdersFromExternalSystem()
            ResponseEntity.ok(mapOf("message" to "주문이 성공적으로 수신되었습니다."))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // 외부 시스템으로 데이터를 내보낸다.
    @PostMapping("/external-data/send")
    fun sendOrderData(): ResponseEntity<Any> {
        return try {
            orderServiceImpl.sendOrdersToExternalSystem()
            ResponseEntity.ok(mapOf("message" to "주문이 성공적으로 수신되었습니다."))

        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    // orderId를 통해 주문 내역을 조회한다.
    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: String): ResponseEntity<Order> {
        val order = orderService.getOrderById(orderId)
        return if (order != null) {
            ResponseEntity.ok(order)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // 모든 주문 내역을 조회한다.
    @GetMapping
    fun getAllOrders(): ResponseEntity<List<Order>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders)
    }
}
