package com.humuson.devsean.service

import com.humuson.devsean.dto.OrderDto
import com.humuson.devsean.entity.Order

interface ExternalOrderService {
    fun fetchOrdersFromExternalSystem()
    fun sendOrdersToExternalSystem()
    fun convertToInternalOrder(orderDto: OrderDto): Order
}