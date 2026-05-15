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

data class BillingUiState(
    val selectedProduct: Product? = null,
    val quantity: Int = 1,
    val saleSuccess: Boolean = false,
    val errorMessage: String? = null
)

class BillingViewModel(private val repository: HastaKalaRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(BillingUiState())
    val uiState: StateFlow<BillingUiState> = _uiState.asStateFlow()

    fun selectProduct(product: Product) {
        _uiState.value = _uiState.value.copy(
            selectedProduct = product,
            quantity = 1,
            saleSuccess = false,
            errorMessage = null
        )
    }

    fun clearSelection() {
        _uiState.value = BillingUiState()
    }

    fun incrementQuantity() {
        val current = _uiState.value
        val maxStock = current.selectedProduct?.stock ?: 0
        if (current.quantity < maxStock) {
            _uiState.value = current.copy(quantity = current.quantity + 1)
        }
    }

    fun decrementQuantity() {
        val current = _uiState.value
        if (current.quantity > 1) {
            _uiState.value = current.copy(quantity = current.quantity - 1)
        }
    }

    fun processSale() {
        val current = _uiState.value
        val product = current.selectedProduct ?: return

        if (product.stock <= 0) {
            _uiState.value = current.copy(errorMessage = "Product is out of stock")
            return
        }

        if (current.quantity > product.stock) {
            _uiState.value = current.copy(errorMessage = "Not enough stock available")
            return
        }

        viewModelScope.launch {
            val sale = Sale(
                productId = product.id,
                quantity = current.quantity,
                totalPrice = product.price * current.quantity
            )
            repository.insertSale(sale)
            _uiState.value = BillingUiState(saleSuccess = true)
        }
    }

    fun consumeSaleSuccess() {
        _uiState.value = _uiState.value.copy(saleSuccess = false)
    }

    fun consumeError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    class Factory(private val repository: HastaKalaRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BillingViewModel::class.java)) {
                return BillingViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
