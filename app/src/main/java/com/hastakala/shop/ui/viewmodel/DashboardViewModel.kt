package com.hastakala.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hastakala.shop.data.model.DailySales
import com.hastakala.shop.data.model.Product
import com.hastakala.shop.data.model.ProductSalesSummary
import com.hastakala.shop.data.model.Sale
import com.hastakala.shop.data.repository.HastaKalaRepository
import com.hastakala.shop.network.ClaudeApiService
import com.hastakala.shop.ui.components.CategoryShare
import com.hastakala.shop.utils.BusinessPromptBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DashboardUiState(
    val totalRevenue: Double = 0.0,
    val totalSalesCount: Int = 0,
    val bestSeller: ProductSalesSummary? = null,
    val dailySales: List<DailySales> = emptyList(),
    val categoryShares: List<CategoryShare> = emptyList(),
    val insights: List<String> = emptyList(),
    val weeklySummary: String = "",
    val isLoadingInsights: Boolean = false,
    val isAiPowered: Boolean = false,
    val isLoaded: Boolean = false
)

class DashboardViewModel(
    private val repository: HastaKalaRepository,
    private val claudeApi: ClaudeApiService = ClaudeApiService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    val lowStockProducts: StateFlow<List<Product>> = repository.getLowStockProducts(5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            val products = repository.getAllProductsOnce()
            val sales = repository.getAllSalesOnce()
            val bestSeller = repository.getBestSellingProduct()
            val totalRevenue = sales.sumOf { it.totalPrice }

            val dailySales = buildDailySales(sales)
            val categoryShares = buildCategoryShares(products, sales, totalRevenue)

            // Generate fallback insights immediately
            val fallbackInsights = BusinessPromptBuilder.getFallbackInsights(
                products, sales, totalRevenue
            )
            val fallbackSummary = BusinessPromptBuilder.getFallbackWeeklySummary(
                products, sales, totalRevenue
            )

            _uiState.value = _uiState.value.copy(
                totalRevenue = totalRevenue,
                totalSalesCount = sales.size,
                bestSeller = bestSeller,
                dailySales = dailySales,
                categoryShares = categoryShares,
                insights = fallbackInsights,
                weeklySummary = fallbackSummary,
                isLoaded = true
            )

            // Try AI insights in background
            fetchAiInsights(products, sales, totalRevenue)
        }
    }

    private fun fetchAiInsights(
        products: List<Product>,
        sales: List<Sale>,
        totalRevenue: Double
    ) {
        if (!claudeApi.isConfigured) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingInsights = true)

            // Fetch insight
            val insightPrompt = BusinessPromptBuilder.buildInsightPrompt(
                products, sales, totalRevenue
            )
            val insightResponse = claudeApi.getInsight(insightPrompt)

            if (!insightResponse.isError && insightResponse.text.isNotBlank()) {
                val aiInsights = insightResponse.text
                    .lines()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .take(4)

                if (aiInsights.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        insights = aiInsights,
                        isAiPowered = true
                    )
                }
            }

            // Fetch weekly summary
            val summaryPrompt = BusinessPromptBuilder.buildWeeklySummaryPrompt(
                products, sales, totalRevenue
            )
            val summaryResponse = claudeApi.getInsight(summaryPrompt)

            if (!summaryResponse.isError && summaryResponse.text.isNotBlank()) {
                _uiState.value = _uiState.value.copy(
                    weeklySummary = summaryResponse.text
                )
            }

            _uiState.value = _uiState.value.copy(isLoadingInsights = false)
        }
    }

    fun refreshInsights() {
        viewModelScope.launch {
            val products = repository.getAllProductsOnce()
            val sales = repository.getAllSalesOnce()
            val totalRevenue = sales.sumOf { it.totalPrice }
            fetchAiInsights(products, sales, totalRevenue)
        }
    }

    private fun buildDailySales(sales: List<Sale>): List<DailySales> {
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

        val grouped = sales.groupBy { dayFormat.format(Date(it.timestamp)) }
        val sortedKeys = grouped.keys.sorted().takeLast(7)

        return sortedKeys.map { dayKey ->
            val daySales = grouped[dayKey] ?: emptyList()
            val timestamp = daySales.firstOrNull()?.timestamp ?: 0L
            DailySales(
                date = dateFormat.format(Date(timestamp)),
                totalSales = daySales.sumOf { it.totalPrice },
                orderCount = daySales.size
            )
        }
    }

    private fun buildCategoryShares(
        products: List<Product>,
        sales: List<Sale>,
        totalRevenue: Double
    ): List<CategoryShare> {
        if (totalRevenue == 0.0) return emptyList()

        val categoryRevenue = sales.groupBy { sale ->
            products.find { it.id == sale.productId }?.category ?: "Other"
        }.map { (category, categorySales) ->
            val revenue = categorySales.sumOf { it.totalPrice }
            CategoryShare(
                category = category,
                revenue = revenue,
                percentage = (revenue / totalRevenue * 100).toFloat()
            )
        }.sortedByDescending { it.revenue }

        return categoryRevenue
    }

    class Factory(
        private val repository: HastaKalaRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                return DashboardViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
