package com.humuson.devsean.service

interface ExternalSystemConnector {
    fun fetchExternalOrders() // 외부에서 데이터 가져오기
    fun sendOrdersToExternalSystem() // 외부로 데이터 내보내기
}
