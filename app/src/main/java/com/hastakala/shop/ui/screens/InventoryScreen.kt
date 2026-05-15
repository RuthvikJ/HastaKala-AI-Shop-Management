package com.hastakala.shop.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.components.LowStockBanner
import com.hastakala.shop.ui.components.ProductCard
import com.hastakala.shop.ui.viewmodel.InventoryViewModel

@Composable
fun InventoryScreen(viewModel: InventoryViewModel) {
    val products by viewModel.filteredProducts.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.addProductSuccess) {
        if (uiState.addProductSuccess) {
            snackbarHostState.showSnackbar("Product added successfully! 🎉")
            viewModel.consumeAddSuccess()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = ArtisanOrange,
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = ArtisanBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = "Inventory",
                    tint = ArtisanOrange,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Inventory",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${products.size} products in catalog",
                fontSize = 14.sp,
                color = ArtisanTextLight
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search products, categories, artisans…",
                        color = ArtisanTextLight.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ArtisanOrange
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ArtisanOrange,
                    unfocusedBorderColor = ArtisanOrangeLight.copy(alpha = 0.3f),
                    focusedContainerColor = ArtisanCard,
                    unfocusedContainerColor = ArtisanCard,
                    cursorColor = ArtisanOrange
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Low stock banner
            LowStockBanner(lowStockCount = lowStockProducts.size)

            if (lowStockProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Product list
            if (products.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Inventory2,
                        contentDescription = "No products",
                        tint = ArtisanOrangeLight.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (uiState.searchQuery.isNotBlank()) "No products found" else "No products yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArtisanTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (uiState.searchQuery.isNotBlank()) "Try a different search term" else "Tap + to add your first product",
                        fontSize = 14.sp,
                        color = ArtisanTextLight
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(product = product)
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // Add Product Dialog
    if (uiState.showAddDialog) {
        AddProductDialog(
            onDismiss = { viewModel.hideAddDialog() },
            onConfirm = { name, category, stock, price, artisan, desc ->
                viewModel.addProduct(name, category, stock, price, artisan, desc)
            }
        )
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, Double, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var artisan by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isValid = name.isNotBlank() && category.isNotBlank() &&
            stock.toIntOrNull() != null && stock.toInt() >= 0 &&
            price.toDoubleOrNull() != null && price.toDouble() > 0 &&
            artisan.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ArtisanCard,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Add New Product",
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DialogTextField(value = name, onValueChange = { name = it }, label = "Product Name")
                DialogTextField(value = category, onValueChange = { category = it }, label = "Category")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DialogTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = "Stock",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    DialogTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = "Price (₹)",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                }
                DialogTextField(value = artisan, onValueChange = { artisan = it }, label = "Artisan Name")
                DialogTextField(value = description, onValueChange = { description = it }, label = "Description (optional)")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        name.trim(),
                        category.trim(),
                        stock.toIntOrNull() ?: 0,
                        price.toDoubleOrNull() ?: 0.0,
                        artisan.trim(),
                        description.trim().ifEmpty { "No description" }
                    )
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArtisanOrange,
                    disabledContainerColor = ArtisanOrangeLight.copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Product", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = ArtisanTextLight)
            }
        }
    )
}

@Composable
fun DialogTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label, fontSize = 13.sp) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ArtisanOrange,
            unfocusedBorderColor = ArtisanOrangeLight.copy(alpha = 0.4f),
            focusedLabelColor = ArtisanOrange,
            cursorColor = ArtisanOrange
        )
    )
}
