package com.hastakala.shop.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.components.BestSellerCard
import com.hastakala.shop.ui.components.CategoryRevenueSection
import com.hastakala.shop.ui.components.InsightCard
import com.hastakala.shop.ui.components.LowStockBanner
import com.hastakala.shop.ui.components.RevenueCard
import com.hastakala.shop.ui.components.RevenueCardRow
import com.hastakala.shop.ui.components.SalesBarChart
import com.hastakala.shop.ui.viewmodel.DashboardViewModel
import com.hastakala.shop.utils.WhatsAppExporter

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val context = LocalContext.current

    AnimatedVisibility(
        visible = uiState.isLoaded,
        enter = fadeIn()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }

            // Header
            item { DashboardHeader() }

            // Primary revenue card
            item {
                RevenueCard(
                    title = "Total Revenue",
                    value = "₹${String.format("%,.0f", uiState.totalRevenue)}",
                    subtitle = "Across ${uiState.totalSalesCount} orders",
                    icon = Icons.Default.TrendingUp,
                    isPrimary = true
                )
            }

            // Metric cards row
            item {
                RevenueCardRow(
                    card1Title = "Orders",
                    card1Value = "${uiState.totalSalesCount}",
                    card1Subtitle = "Total sales made",
                    card1Icon = Icons.Outlined.Receipt,
                    card2Title = "Products",
                    card2Value = "${uiState.categoryShares.size}",
                    card2Subtitle = "Active categories",
                    card2Icon = Icons.Outlined.Inventory2
                )
            }

            // Low stock warning
            if (lowStockProducts.isNotEmpty()) {
                item {
                    LowStockBanner(lowStockCount = lowStockProducts.size)
                }

                item {
                    LowStockDetailSection(lowStockProducts)
                }
            }

            // Best seller
            if (uiState.bestSeller != null) {
                item {
                    BestSellerCard(
                        productName = uiState.bestSeller!!.productName,
                        quantitySold = uiState.bestSeller!!.totalQuantitySold,
                        revenue = uiState.bestSeller!!.totalRevenue
                    )
                }
            }

            // Weekly sales bar chart
            if (uiState.dailySales.isNotEmpty()) {
                item {
                    SalesBarChart(dailySales = uiState.dailySales)
                }
            }

            // Top categories revenue
            if (uiState.categoryShares.isNotEmpty()) {
                item {
                    CategoryRevenueSection(categories = uiState.categoryShares)
                }
            }

            // Empty state when no sales data
            if (uiState.totalSalesCount == 0) {
                item {
                    EmptyDashboardState()
                }
            }

            // AI Insights section
            item {
                InsightsHeader(
                    isAiPowered = uiState.isAiPowered,
                    isLoading = uiState.isLoadingInsights,
                    onRefresh = { viewModel.refreshInsights() }
                )
            }

            if (uiState.insights.isEmpty() && !uiState.isLoadingInsights) {
                item { EmptyInsightsState() }
            } else {
                itemsIndexed(uiState.insights) { index, insight ->
                    InsightCard(insight = insight, index = index)
                }
            }

            // Share summary button
            item {
                ShareSummaryButton(
                    onClick = {
                        WhatsAppExporter.shareSummary(context, uiState.weeklySummary)
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }

    // Loading state
    if (!uiState.isLoaded) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = ArtisanOrange,
                strokeWidth = 3.dp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading dashboard…",
                fontSize = 15.sp,
                color = ArtisanTextLight
            )
        }
    }
}

@Composable
fun DashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Assessment,
            contentDescription = "Dashboard",
            tint = ArtisanOrange,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Business Dashboard",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            Text(
                text = "Your artisan intelligence hub",
                fontSize = 14.sp,
                color = ArtisanTextLight
            )
        }
    }
}

@Composable
fun InsightsHeader(
    isAiPowered: Boolean,
    isLoading: Boolean,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = "AI Insights",
                tint = ArtisanOrange,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAiPowered) "AI Business Insights" else "Business Insights",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = ArtisanOrange,
                strokeWidth = 2.dp
            )
        } else {
            IconButton(onClick = onRefresh, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh insights",
                    tint = ArtisanOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LowStockDetailSection(products: List<com.hastakala.shop.data.model.Product>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "Warning",
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Restock Needed",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            products.take(4).forEach { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = product.name,
                        fontSize = 13.sp,
                        color = ArtisanTextDark,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (product.stock == 0) "Out of stock" else "${product.stock} left",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (product.stock == 0) Color(0xFFC62828) else Color(0xFFE65100)
                    )
                }
            }
        }
    }
}

@Composable
fun ShareSummaryButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ArtisanTextDark,
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share",
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Share Weekly Summary",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EmptyDashboardState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Assessment,
                contentDescription = "No data",
                tint = ArtisanOrangeLight.copy(alpha = 0.4f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No sales yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Start billing to generate charts and analytics",
                fontSize = 14.sp,
                color = ArtisanTextLight
            )
        }
    }
}

@Composable
fun EmptyInsightsState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBF5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = "No insights",
                tint = ArtisanOrangeLight.copy(alpha = 0.5f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Start billing to generate insights",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = ArtisanTextLight
            )
        }
    }
}
