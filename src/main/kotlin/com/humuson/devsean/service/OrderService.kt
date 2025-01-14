package com.humuson.devsean.service

import com.humuson.devsean.entity.Order
import com.humuson.devsean.repository.MemoryOrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val memoryOrderRepository: MemoryOrderRepository
) {
    
    fun saveOrders(newOrders: List<Order>) {
        memoryOrderRepository.save(newOrders)
    }

    fun getAllOrders(): List<Order> = memoryOrderRepository.findAll()


    fun getOrderById(orderId: String): Order? = memoryOrderRepository.findById(orderId)
}
