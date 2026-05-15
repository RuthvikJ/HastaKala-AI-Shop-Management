package com.hastakala.shop.data.repository

import com.hastakala.shop.data.db.ProductDao
import com.hastakala.shop.data.db.SaleDao
import com.hastakala.shop.data.model.Product
import com.hastakala.shop.data.model.ProductSalesSummary
import com.hastakala.shop.data.model.Sale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HastaKalaRepository(
    private val productDao: ProductDao,
    private val saleDao: SaleDao
) {
    // ── Products ──────────────────────────────────────────────

    fun getAllProducts(): Flow<List<Product>> = productDao.getAll()

    fun getLowStockProducts(threshold: Int = 5): Flow<List<Product>> =
        productDao.getLowStock(threshold)

    fun getProductsByCategory(category: String): Flow<List<Product>> =
        productDao.getByCategory(category)

    suspend fun getProductById(id: Long): Product? = productDao.getById(id)

    suspend fun insertProduct(product: Product): Long = productDao.insert(product)

    suspend fun insertAllProducts(products: List<Product>) = productDao.insertAll(products)

    suspend fun updateProduct(product: Product) = productDao.update(product)

    suspend fun deleteProduct(product: Product) = productDao.delete(product)

    suspend fun getProductCount(): Int = productDao.getCount()

    // ── Sales ─────────────────────────────────────────────────

    fun getAllSales(): Flow<List<Sale>> = saleDao.getAll()

    fun getRecentSales(limit: Int = 10): Flow<List<Sale>> = saleDao.getRecent(limit)

    fun getSalesByProduct(productId: Long): Flow<List<Sale>> =
        saleDao.getByProductId(productId)

    fun getTotalRevenue(): Flow<Double> = saleDao.getTotalRevenue().map { it ?: 0.0 }

    fun getTotalSalesCount(): Flow<Int> = saleDao.getTotalSalesCount()

    suspend fun insertSale(sale: Sale): Long {
        val result = saleDao.insert(sale)
        // Decrement stock after sale
        val product = productDao.getById(sale.productId)
        if (product != null) {
            val updatedStock = (product.stock - sale.quantity).coerceAtLeast(0)
            productDao.update(product.copy(stock = updatedStock))
        }
        return result
    }

    suspend fun insertAllSales(sales: List<Sale>) = saleDao.insertAll(sales)

    suspend fun deleteSale(sale: Sale) = saleDao.delete(sale)

    // ── Analytics ─────────────────────────────────────────────

    suspend fun getBestSellingProduct(): ProductSalesSummary? {
        val bestSeller = saleDao.getBestSellingProductId() ?: return null
        val product = productDao.getById(bestSeller.productId) ?: return null
        return ProductSalesSummary(
            productId = product.id,
            productName = product.name,
            totalQuantitySold = bestSeller.totalQty,
            totalRevenue = product.price * bestSeller.totalQty
        )
    }

    // ── Snapshots (one-shot for analytics) ────────────────────

    suspend fun getAllProductsOnce(): List<Product> = productDao.getAllOnce()

    suspend fun getAllSalesOnce(): List<Sale> = saleDao.getAllOnce()
}
