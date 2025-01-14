package com.humuson.devsean.repository

import com.humuson.devsean.entity.Order

interface OrderRepository {
    fun save(orders: List<Order>)
    fun findById(orderId: String): Order?
    fun findAll(): List<Order>
}
