package com.hastakala.shop

import android.app.Application
import com.hastakala.shop.data.db.AppDatabase
import com.hastakala.shop.data.repository.HastaKalaRepository
import com.hastakala.shop.utils.DemoSalesSeeder
import com.hastakala.shop.utils.ProductSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HastaKalaApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    val repository: HastaKalaRepository by lazy {
        HastaKalaRepository(database.productDao(), database.saleDao())
    }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        seedDemoDataIfNeeded()
    }

    private fun seedDemoDataIfNeeded() {
        applicationScope.launch {
            val productCount = repository.getProductCount()
            if (productCount == 0) {
                val products = ProductSeeder.getProducts()
                repository.insertAllProducts(products)
                val sales = DemoSalesSeeder.getSales()
                repository.insertAllSales(sales)
            }
        }
    }
}
