package com.hastakala.shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hastakala.shop.data.model.Product
import com.hastakala.shop.data.repository.HastaKalaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class InventoryUiState(
    val searchQuery: String = "",
    val showAddDialog: Boolean = false,
    val addProductSuccess: Boolean = false
)

class InventoryViewModel(private val repository: HastaKalaRepository) : ViewModel() {

    private val _allProducts: StateFlow<List<Product>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<Product>> = repository.getLowStockProducts(5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _allProducts,
        _uiState
    ) { products, state ->
        if (state.searchQuery.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.name.contains(state.searchQuery, ignoreCase = true) ||
                        product.category.contains(state.searchQuery, ignoreCase = true) ||
                        product.artisanName.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }

    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun addProduct(
        name: String,
        category: String,
        stock: Int,
        price: Double,
        artisanName: String,
        description: String
    ) {
        viewModelScope.launch {
            val product = Product(
                name = name,
                category = category,
                stock = stock,
                price = price,
                artisanName = artisanName,
                description = description
            )
            repository.insertProduct(product)
            _uiState.value = _uiState.value.copy(
                showAddDialog = false,
                addProductSuccess = true
            )
        }
    }

    fun consumeAddSuccess() {
        _uiState.value = _uiState.value.copy(addProductSuccess = false)
    }

    class Factory(private val repository: HastaKalaRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
                return InventoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
