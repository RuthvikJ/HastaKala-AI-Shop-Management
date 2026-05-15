package com.hastakala.shop.utils

import com.hastakala.shop.data.model.Sale

object DemoSalesSeeder {

    fun getSales(): List<Sale> {
        val now = System.currentTimeMillis()
        val oneHour = 3_600_000L
        val oneDay = 86_400_000L

        return listOf(
            // Today's sales
            Sale(
                productId = 1,
                quantity = 3,
                totalPrice = 3600.0,
                timestamp = now - (2 * oneHour)
            ),
            Sale(
                productId = 3,
                quantity = 2,
                totalPrice = 1700.0,
                timestamp = now - (4 * oneHour)
            ),
            Sale(
                productId = 7,
                quantity = 10,
                totalPrice = 1800.0,
                timestamp = now - (5 * oneHour)
            ),
            Sale(
                productId = 10,
                quantity = 5,
                totalPrice = 1100.0,
                timestamp = now - (6 * oneHour)
            ),
            Sale(
                productId = 14,
                quantity = 2,
                totalPrice = 2700.0,
                timestamp = now - (3 * oneHour)
            ),
            // Yesterday's sales
            Sale(
                productId = 2,
                quantity = 1,
                totalPrice = 4500.0,
                timestamp = now - oneDay - (3 * oneHour)
            ),
            Sale(
                productId = 5,
                quantity = 4,
                totalPrice = 1800.0,
                timestamp = now - oneDay - (5 * oneHour)
            ),
            Sale(
                productId = 8,
                quantity = 6,
                totalPrice = 2100.0,
                timestamp = now - oneDay - (7 * oneHour)
            ),
            Sale(
                productId = 13,
                quantity = 3,
                totalPrice = 1650.0,
                timestamp = now - oneDay - (2 * oneHour)
            ),
            // 2 days ago
            Sale(
                productId = 4,
                quantity = 8,
                totalPrice = 5200.0,
                timestamp = now - (2 * oneDay) - (2 * oneHour)
            ),
            Sale(
                productId = 6,
                quantity = 2,
                totalPrice = 5600.0,
                timestamp = now - (2 * oneDay) - (4 * oneHour)
            ),
            Sale(
                productId = 11,
                quantity = 3,
                totalPrice = 5400.0,
                timestamp = now - (2 * oneDay) - (6 * oneHour)
            ),
            Sale(
                productId = 15,
                quantity = 4,
                totalPrice = 3120.0,
                timestamp = now - (2 * oneDay) - (1 * oneHour)
            ),
            // 3 days ago
            Sale(
                productId = 12,
                quantity = 4,
                totalPrice = 3000.0,
                timestamp = now - (3 * oneDay) - (1 * oneHour)
            ),
            Sale(
                productId = 9,
                quantity = 1,
                totalPrice = 1100.0,
                timestamp = now - (3 * oneDay) - (3 * oneHour)
            ),
            Sale(
                productId = 1,
                quantity = 5,
                totalPrice = 6000.0,
                timestamp = now - (3 * oneDay) - (5 * oneHour)
            ),
            Sale(
                productId = 16,
                quantity = 6,
                totalPrice = 1920.0,
                timestamp = now - (3 * oneDay) - (7 * oneHour)
            ),
            // 4 days ago
            Sale(
                productId = 3,
                quantity = 3,
                totalPrice = 2550.0,
                timestamp = now - (4 * oneDay) - (2 * oneHour)
            ),
            Sale(
                productId = 7,
                quantity = 12,
                totalPrice = 2160.0,
                timestamp = now - (4 * oneDay) - (6 * oneHour)
            ),
            Sale(
                productId = 18,
                quantity = 2,
                totalPrice = 2400.0,
                timestamp = now - (4 * oneDay) - (4 * oneHour)
            ),
            // 5 days ago
            Sale(
                productId = 14,
                quantity = 3,
                totalPrice = 4050.0,
                timestamp = now - (5 * oneDay) - (3 * oneHour)
            ),
            Sale(
                productId = 5,
                quantity = 5,
                totalPrice = 2250.0,
                timestamp = now - (5 * oneDay) - (6 * oneHour)
            ),
            // 6 days ago
            Sale(
                productId = 13,
                quantity = 2,
                totalPrice = 1100.0,
                timestamp = now - (6 * oneDay) - (2 * oneHour)
            ),
            Sale(
                productId = 17,
                quantity = 1,
                totalPrice = 1500.0,
                timestamp = now - (6 * oneDay) - (5 * oneHour)
            )
        )
    }
}
