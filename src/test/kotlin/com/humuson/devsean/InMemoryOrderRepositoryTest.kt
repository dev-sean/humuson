package com.humuson.devsean

import com.humuson.devsean.entity.Order
import com.humuson.devsean.entity.OrderStatus
import com.humuson.devsean.repository.InMemoryOrderRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.time.LocalDateTime
import kotlin.test.Test

class InMemoryOrderRepositoryTest {
    private val inMemoryOrderRepository = InMemoryOrderRepository()

    private val testOrder = Order(
        orderId = "test1",
        customerName = "테스트 고객",
        orderDate = LocalDateTime.parse("2025-01-14T14:00:00"),
        orderStatus = OrderStatus.PREPARING
    )

    @Test
    fun `단일 주문 저장 테스트`() {
        // given
        val order = testOrder

        // when
        inMemoryOrderRepository.save(listOf(order))

        // then
        val savedOrder = inMemoryOrderRepository.findById(order.orderId)
        assertEquals(order, savedOrder)
    }

    @Test
    fun `다중 주문 저장 테스트`() {
        // given
        val orders = listOf(
            testOrder,
            testOrder.copy(orderId = "test2", orderStatus = OrderStatus.DELIVERING),
            testOrder.copy(orderId = "test3", orderStatus = OrderStatus.COMPLETED)
        )

        // when
        inMemoryOrderRepository.save(orders)

        // then
        val savedOrders = inMemoryOrderRepository.findAll()
        assertEquals(3, savedOrders.size)
        assertTrue(savedOrders.containsAll(orders))
    }

    @Test
    fun `ID로 주문 조회 테스트`() {
        // given
        inMemoryOrderRepository.save(listOf(testOrder))

        // when
        val foundOrder = inMemoryOrderRepository.findById("test1")
        val notFoundOrder = inMemoryOrderRepository.findById("non-exist")

        // then
        assertNotNull(foundOrder)
        assertEquals(testOrder, foundOrder)
        assertNull(notFoundOrder)
    }

    @Test
    fun `전체 주문 목록 조회 테스트`() {
        // given
        val orders = listOf(
            testOrder,
            testOrder.copy(orderId = "test2")
        )
        inMemoryOrderRepository.save(orders)

        // when
        val allOrders = inMemoryOrderRepository.findAll()

        // then
        assertEquals(2, allOrders.size)
        assertTrue(allOrders.containsAll(orders))
    }

    @Test
    fun `중복 주문 저장 시 덮어쓰기 테스트`() {
        // given
        val originalOrder = testOrder
        val updatedOrder = testOrder.copy(orderStatus = OrderStatus.COMPLETED)

        // when
        inMemoryOrderRepository.save(listOf(originalOrder))
        inMemoryOrderRepository.save(listOf(updatedOrder))

        // then
        val savedOrder = inMemoryOrderRepository.findById(testOrder.orderId)
        assertNotNull(savedOrder)
        assertEquals(OrderStatus.COMPLETED, savedOrder?.orderStatus)
        assertEquals(1, inMemoryOrderRepository.findAll().size)
    }
}
