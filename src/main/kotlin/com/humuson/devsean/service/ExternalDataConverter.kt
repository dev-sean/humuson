package com.humuson.devsean.service

import com.humuson.devsean.dto.ExternalOrderDto
import com.humuson.devsean.entity.Order

interface ExternalDataConverter {
    fun convertToOrder(externalOrderDto: ExternalOrderDto): Order // 외부에서 가져온 데이터 변환
    fun convertToExternalJson(orders: List<Order>): String // 외부로 내보낼 데이터 변환
}
