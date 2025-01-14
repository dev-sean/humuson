package com.humuson.devsean.dto

data class ExternalOrderDto(
    val orderId: String,
    val customerName: String,
    val orderDate: String,
    val orderStatus: String
)
