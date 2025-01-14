package com.humuson.devsean.service

import com.humuson.devsean.entity.Order
import com.humuson.devsean.repository.InMemoryOrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val inMemoryOrderRepository: InMemoryOrderRepository
) {

    fun saveOrders(newOrders: List<Order>) {
        inMemoryOrderRepository.save(newOrders)
    }

    fun findAllOrders(): List<Order> = inMemoryOrderRepository.findAll()

    fun findOrderById(orderId: String): Order? = inMemoryOrderRepository.findById(orderId)
}
