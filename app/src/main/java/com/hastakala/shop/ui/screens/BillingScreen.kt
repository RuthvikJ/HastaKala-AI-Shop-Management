package com.hastakala.shop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.data.model.Product
import com.hastakala.shop.ui.components.QuantitySelector
import com.hastakala.shop.ui.viewmodel.BillingViewModel

@Composable
fun BillingScreen(viewModel: BillingViewModel) {
    val products by viewModel.products.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saleSuccess) {
        if (uiState.saleSuccess) {
            snackbarHostState.showSnackbar("Sale recorded successfully! ✅")
            viewModel.consumeSaleSuccess()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.consumeError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Receipt,
                    contentDescription = "Billing",
                    tint = ArtisanOrange,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Quick Billing",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Select a product to create a sale",
                fontSize = 14.sp,
                color = ArtisanTextLight
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Selected product billing card
            AnimatedVisibility(
                visible = uiState.selectedProduct != null,
                enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it / 2 }) + fadeOut()
            ) {
                uiState.selectedProduct?.let { product ->
                    BillingSummaryCard(
                        product = product,
                        quantity = uiState.quantity,
                        onIncrement = { viewModel.incrementQuantity() },
                        onDecrement = { viewModel.decrementQuantity() },
                        onClear = { viewModel.clearSelection() },
                        onConfirmSale = { viewModel.processSale() }
                    )
                }
            }

            if (uiState.selectedProduct != null) {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Product list header
            Text(
                text = if (uiState.selectedProduct != null) "Change Product" else "Available Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArtisanTextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Product list
            if (products.isEmpty()) {
                Spacer(modifier = Modifier.height(40.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ReceiptLong,
                        contentDescription = "No products",
                        tint = ArtisanOrangeLight.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No products available",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArtisanTextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Add products in Inventory to start billing",
                        fontSize = 14.sp,
                        color = ArtisanTextLight
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        BillingProductItem(
                            product = product,
                            isSelected = uiState.selectedProduct?.id == product.id,
                            onClick = {
                                if (product.stock > 0) {
                                    viewModel.selectProduct(product)
                                }
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BillingSummaryCard(
    product: Product,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onClear: () -> Unit,
    onConfirmSale: () -> Unit
) {
    val total = product.price * quantity

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New Sale",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = onClear, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear selection",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product name
            Text(
                text = product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹${String.format("%,.0f", product.price)} each · ${product.stock} in stock",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity + Total row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Quantity",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    QuantitySelector(
                        quantity = quantity,
                        maxQuantity = product.stock,
                        onIncrement = onIncrement,
                        onDecrement = onDecrement
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "₹${String.format("%,.0f", total)}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Confirm button
            Button(
                onClick = onConfirmSale,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = ArtisanOrange
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Confirm sale",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm Sale",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BillingProductItem(
    product: Product,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isOutOfStock = product.stock <= 0
    val cardColor = when {
        isSelected -> ArtisanOrangeLight.copy(alpha = 0.15f)
        isOutOfStock -> Color(0xFFF5F5F5)
        else -> ArtisanCard
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (!isOutOfStock) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isOutOfStock) Color.Gray.copy(alpha = 0.1f)
                        else ArtisanOrangeLight.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Category,
                    contentDescription = product.name,
                    tint = if (isOutOfStock) Color.Gray else ArtisanOrange,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isOutOfStock) Color.Gray else ArtisanTextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.category,
                    fontSize = 12.sp,
                    color = if (isOutOfStock) Color.Gray.copy(alpha = 0.6f) else ArtisanTextLight
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${String.format("%,.0f", product.price)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOutOfStock) Color.Gray else ArtisanTextDark
                )
                Text(
                    text = if (isOutOfStock) "Out of stock" else "${product.stock} left",
                    fontSize = 11.sp,
                    color = if (isOutOfStock) Color(0xFFC62828) else ArtisanTextLight
                )
            }
        }
    }
}
