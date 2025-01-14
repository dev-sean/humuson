package com.humuson.devsean.controller

import com.humuson.devsean.entity.Order
import com.humuson.devsean.service.ExternalSystemConnectorImpl
import com.humuson.devsean.service.OrderService
import org.slf4j.LoggerFactory
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
    private val orderServiceImpl: ExternalSystemConnectorImpl
) {
    private val logger = LoggerFactory.getLogger(OrderController::class.java)

    @GetMapping("/external")
    fun fetchExternalOrders(): ResponseEntity<Any> {
        logger.info("외부 시스템으로부터 주문 데이터 수신 시작")
        return try {
            orderServiceImpl.fetchExternalOrders()
            logger.info("외부 시스템으로부터 주문 데이터 수신 완료")
            ResponseEntity.ok(mapOf("message" to "주문이 성공적으로 수신되었습니다."))
        } catch (e: Exception) {
            logger.error("외부 시스템으로부터 주문 데이터 수신 실패: ${e.message}", e)
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/external")
    fun sendOrdersToExternalSystem(): ResponseEntity<Any> {
        logger.info("외부 시스템으로 주문 데이터 전송 시작")
        return try {
            orderServiceImpl.sendOrdersToExternalSystem()
            logger.info("외부 시스템으로 주문 데이터 전송 완료")
            ResponseEntity.ok(mapOf("message" to "주문이 성공적으로 수신되었습니다."))
        } catch (e: Exception) {
            logger.error("외부 시스템으로 주문 데이터 전송 실패: ${e.message}", e)
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/{orderId}")
    fun findOrderById(@PathVariable orderId: String): ResponseEntity<Order> {
        logger.info("주문 조회 시작 - orderId: $orderId")
        val order = orderService.findOrderById(orderId)
        return if (order != null) {
            logger.info("주문 조회 완료 - orderId: $orderId")
            ResponseEntity.ok(order)
        } else {
            logger.warn("주문 조회 실패 - orderId: $orderId (주문을 찾을 수 없음)")
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/list")
    fun findAllOrders(): ResponseEntity<List<Order>> {
        logger.info("전체 주문 목록 조회 시작")
        val orders = orderService.findAllOrders()
        logger.info("전체 주문 목록 조회 완료 - 조회된 주문 수: ${orders.size}")
        return ResponseEntity.ok(orders)
    }
}
