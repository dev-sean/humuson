package com.humuson.devsean.entity

import java.time.LocalDateTime

data class Order(
    val orderId: String,
    val customerName: String,
    val orderDate: LocalDateTime,
    val orderStatus: OrderStatus
)

enum class OrderStatus {
    NONE,    // 알수없음
    PREPARING,     // 준비중
    DELIVERING,    // 배송중
    COMPLETED      // 완료
}