package com.hastakala.shop.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hastakala.shop.data.model.Sale
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sale: Sale): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sales: List<Sale>)

    @Update
    suspend fun update(sale: Sale)

    @Delete
    suspend fun delete(sale: Sale)

    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getById(id: Long): Sale?

    @Query("SELECT * FROM sales ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 10): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE productId = :productId ORDER BY timestamp DESC")
    fun getByProductId(productId: Long): Flow<List<Sale>>

    @Query("SELECT SUM(totalPrice) FROM sales")
    fun getTotalRevenue(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM sales")
    fun getTotalSalesCount(): Flow<Int>

    @Query("""
        SELECT productId, SUM(quantity) as totalQty 
        FROM sales 
        GROUP BY productId 
        ORDER BY totalQty DESC 
        LIMIT 1
    """)
    suspend fun getBestSellingProductId(): BestSellerResult?

    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    suspend fun getAllOnce(): List<Sale>
}

data class BestSellerResult(
    val productId: Long,
    val totalQty: Int
)
