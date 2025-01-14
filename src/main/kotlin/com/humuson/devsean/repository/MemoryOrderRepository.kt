package com.humuson.devsean.repository

import com.humuson.devsean.entity.Order
import org.springframework.stereotype.Repository

@Repository
class MemoryOrderRepository : OrderRepository {
    private val orders = mutableMapOf<String, Order>()

    override fun save(newOrders: List<Order>) {
        newOrders.forEach { order ->
            orders[order.orderId] = order
        }
    }

    override fun findById(orderId: String): Order? = orders[orderId]

    override fun findAll(): List<Order> = orders.values.toList()
}