package com.hastakala.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hastakala.shop.data.model.Product
import com.hastakala.shop.data.model.Sale
import com.hastakala.shop.data.repository.HastaKalaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class RecentTransaction(
    val productName: String,
    val category: String,
    val quantity: Int,
    val totalPrice: Double,
    val timestamp: Long
)

data class IncomeUiState(
    val totalRevenue: Double = 0.0,
    val todayRevenue: Double = 0.0,
    val weeklyRevenue: Double = 0.0,
    val monthlyRevenue: Double = 0.0,
    val todayOrders: Int = 0,
    val weeklyOrders: Int = 0,
    val monthlyOrders: Int = 0,
    val recentTransactions: List<RecentTransaction> = emptyList(),
    val isLoaded: Boolean = false
)

class IncomeViewModel(private val repository: HastaKalaRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeUiState())
    val uiState: StateFlow<IncomeUiState> = _uiState.asStateFlow()

    val totalRevenue: StateFlow<Double> = repository.getTotalRevenue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        loadIncomeData()
    }

    private fun loadIncomeData() {
        viewModelScope.launch {
            val products = repository.getAllProductsOnce()
            val sales = repository.getAllSalesOnce()
            val productMap = products.associateBy { it.id }

            val now = System.currentTimeMillis()
            val todayStart = getTodayStart()
            val weekStart = getWeekStart()
            val monthStart = getMonthStart()

            val todaySales = sales.filter { it.timestamp >= todayStart }
            val weeklySales = sales.filter { it.timestamp >= weekStart }
            val monthlySales = sales.filter { it.timestamp >= monthStart }

            val recentTransactions = sales.take(15).mapNotNull { sale ->
                val product = productMap[sale.productId] ?: return@mapNotNull null
                RecentTransaction(
                    productName = product.name,
                    category = product.category,
                    quantity = sale.quantity,
                    totalPrice = sale.totalPrice,
                    timestamp = sale.timestamp
                )
            }

            _uiState.value = IncomeUiState(
                totalRevenue = sales.sumOf { it.totalPrice },
                todayRevenue = todaySales.sumOf { it.totalPrice },
                weeklyRevenue = weeklySales.sumOf { it.totalPrice },
                monthlyRevenue = monthlySales.sumOf { it.totalPrice },
                todayOrders = todaySales.size,
                weeklyOrders = weeklySales.size,
                monthlyOrders = monthlySales.size,
                recentTransactions = recentTransactions,
                isLoaded = true
            )
        }
    }

    private fun getTodayStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getWeekStart(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -7)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getMonthStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    class Factory(private val repository: HastaKalaRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
                return IncomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
