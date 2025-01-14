package com.humuson.devsean.dto

data class OrderDto(
    val orderId: String,
    val customerName: String,
    val orderDate: String,
    val orderStatus: String
)