package com.humuson.devsean.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

data class Order(
    val orderId: String,
    val customerName: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val orderDate: LocalDateTime,
    val orderStatus: OrderStatus
)

enum class OrderStatus {
    NONE, // 알수없음
    PREPARING, // 준비중
    DELIVERING, // 배송중
    COMPLETED // 완료
}
