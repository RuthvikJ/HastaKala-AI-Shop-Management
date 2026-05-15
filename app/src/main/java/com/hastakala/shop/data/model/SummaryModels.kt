package com.hastakala.shop.data.model

data class RevenueSummary(
    val totalRevenue: Double,
    val totalOrders: Int,
    val totalCustomers: Int
)

data class ProductSalesSummary(
    val productId: Long,
    val productName: String,
    val totalQuantitySold: Int,
    val totalRevenue: Double
)

data class DailySales(
    val date: String,
    val totalSales: Double,
    val orderCount: Int
)
