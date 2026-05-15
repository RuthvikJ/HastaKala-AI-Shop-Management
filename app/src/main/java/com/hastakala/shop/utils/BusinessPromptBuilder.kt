package com.hastakala.shop.utils

import com.hastakala.shop.data.model.Product
import com.hastakala.shop.data.model.Sale
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BusinessPromptBuilder {

    fun buildInsightPrompt(
        products: List<Product>,
        sales: List<Sale>,
        totalRevenue: Double
    ): String {
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        // Aggregate sales by product
        val salesByProduct = sales.groupBy { it.productId }
        val productSalesSummary = salesByProduct.map { (productId, productSales) ->
            val product = products.find { it.id == productId }
            val totalQty = productSales.sumOf { it.quantity }
            val totalAmount = productSales.sumOf { it.totalPrice }
            "${product?.name ?: "Unknown"} (${product?.category ?: "N/A"}): $totalQty units sold, ₹${String.format("%,.0f", totalAmount)}"
        }.joinToString("\n")

        // Low stock items
        val lowStockItems = products.filter { it.stock <= 5 }
            .joinToString("\n") { "${it.name}: ${it.stock} left in stock" }

        // Category breakdown
        val categoryRevenue = sales.groupBy { sale ->
            products.find { it.id == sale.productId }?.category ?: "Other"
        }.map { (category, categorySales) ->
            val revenue = categorySales.sumOf { it.totalPrice }
            val percentage = if (totalRevenue > 0) (revenue / totalRevenue * 100) else 0.0
            "$category: ₹${String.format("%,.0f", revenue)} (${String.format("%.0f", percentage)}%)"
        }.joinToString("\n")

        // Recent sales dates
        val recentDates = sales.take(5).joinToString(", ") {
            dateFormat.format(Date(it.timestamp))
        }

        return """
You are a business intelligence advisor for a small artisan craft shop in India called HastaKala.
Analyze the following sales data and provide exactly 4 short, actionable business insights.

RULES:
- Each insight must be 1 sentence, maximum 15 words
- Use specific product names and numbers from the data
- Include one restock warning if applicable
- Include one revenue highlight
- Include one trend observation
- Include one actionable recommendation
- Use ₹ for currency
- Be encouraging and practical for a rural artisan

SALES DATA:
Total Revenue: ₹${String.format("%,.0f", totalRevenue)}
Total Orders: ${sales.size}
Recent Activity: $recentDates

PRODUCT PERFORMANCE:
$productSalesSummary

LOW STOCK ALERTS:
${lowStockItems.ifEmpty { "All products adequately stocked" }}

CATEGORY BREAKDOWN:
$categoryRevenue

Respond with exactly 4 insights, one per line, no numbering, no bullets.
        """.trimIndent()
    }

    fun buildWeeklySummaryPrompt(
        products: List<Product>,
        sales: List<Sale>,
        totalRevenue: Double
    ): String {
        val salesCount = sales.size
        val topProduct = sales.groupBy { it.productId }
            .maxByOrNull { it.value.sumOf { s -> s.quantity } }
        val topProductName = products.find { it.id == topProduct?.key }?.name ?: "N/A"
        val topProductQty = topProduct?.value?.sumOf { it.quantity } ?: 0
        val lowStockCount = products.count { it.stock <= 5 }

        return """
You are a business report writer for HastaKala, a small artisan craft shop in India.
Write a brief weekly WhatsApp-friendly business summary.

DATA:
- Revenue: ₹${String.format("%,.0f", totalRevenue)}
- Orders: $salesCount
- Top Product: $topProductName ($topProductQty units)
- Low Stock Items: $lowStockCount products
- Total Products: ${products.size}

RULES:
- Keep it under 200 words
- Use emojis sparingly (2-3 max)
- Include greeting "HastaKala Weekly Report"
- Mention key numbers
- End with one motivational line
- Format for WhatsApp readability
- Use ₹ for currency
        """.trimIndent()
    }

    fun getFallbackInsights(
        products: List<Product>,
        sales: List<Sale>,
        totalRevenue: Double
    ): List<String> {
        val insights = mutableListOf<String>()

        // Revenue insight
        insights.add("Total revenue reached ₹${String.format("%,.0f", totalRevenue)} across ${sales.size} orders")

        // Best seller
        val topProduct = sales.groupBy { it.productId }
            .maxByOrNull { it.value.sumOf { s -> s.quantity } }
        val topName = products.find { it.id == topProduct?.key }?.name
        val topQty = topProduct?.value?.sumOf { it.quantity } ?: 0
        if (topName != null) {
            insights.add("$topName is your best seller with $topQty units sold")
        }

        // Low stock
        val lowStock = products.filter { it.stock <= 5 }
        if (lowStock.isNotEmpty()) {
            insights.add("Restock alert: ${lowStock.first().name} has only ${lowStock.first().stock} units left")
        } else {
            insights.add("All products are well stocked — great inventory management!")
        }

        // Category leader
        val topCategory = sales.groupBy { sale ->
            products.find { it.id == sale.productId }?.category ?: "Other"
        }.maxByOrNull { it.value.sumOf { s -> s.totalPrice } }
        if (topCategory != null) {
            val catRevenue = topCategory.value.sumOf { it.totalPrice }
            insights.add("${topCategory.key} category leads with ₹${String.format("%,.0f", catRevenue)} revenue")
        }

        return insights.take(4)
    }

    fun getFallbackWeeklySummary(
        products: List<Product>,
        sales: List<Sale>,
        totalRevenue: Double
    ): String {
        val topProduct = sales.groupBy { it.productId }
            .maxByOrNull { it.value.sumOf { s -> s.quantity } }
        val topName = products.find { it.id == topProduct?.key }?.name ?: "N/A"
        val topQty = topProduct?.value?.sumOf { it.quantity } ?: 0
        val lowStockCount = products.count { it.stock <= 5 }

        return buildString {
            appendLine("📊 *HastaKala Weekly Report*")
            appendLine()
            appendLine("Revenue: ₹${String.format("%,.0f", totalRevenue)}")
            appendLine("Orders: ${sales.size}")
            appendLine("Top Product: $topName ($topQty units)")
            if (lowStockCount > 0) {
                appendLine("⚠️ $lowStockCount product${if (lowStockCount > 1) "s" else ""} need restocking")
            }
            appendLine()
            appendLine("Keep crafting, keep growing! 🙏")
        }
    }
}
