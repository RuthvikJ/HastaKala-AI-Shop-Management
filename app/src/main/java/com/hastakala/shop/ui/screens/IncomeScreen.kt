package com.hastakala.shop.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.viewmodel.IncomeViewModel
import com.hastakala.shop.ui.viewmodel.RecentTransaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IncomeScreen(viewModel: IncomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

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
                text = "Loading income data…",
                fontSize = 15.sp,
                color = ArtisanTextLight
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        // Header
        item { IncomeHeader() }

        // Total Revenue Hero
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ArtisanOrange),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Income",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Income",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "₹${String.format("%,.0f", uiState.totalRevenue)}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${uiState.todayOrders + uiState.weeklyOrders + uiState.monthlyOrders} total transactions",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }
        }

        // Period breakdown cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IncomePeriodCard(
                    title = "Today",
                    amount = "₹${String.format("%,.0f", uiState.todayRevenue)}",
                    orders = "${uiState.todayOrders} orders",
                    icon = Icons.Outlined.Today,
                    modifier = Modifier.weight(1f)
                )
                IncomePeriodCard(
                    title = "This Week",
                    amount = "₹${String.format("%,.0f", uiState.weeklyRevenue)}",
                    orders = "${uiState.weeklyOrders} orders",
                    icon = Icons.Outlined.DateRange,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            IncomePeriodCardWide(
                title = "This Month",
                amount = "₹${String.format("%,.0f", uiState.monthlyRevenue)}",
                orders = "${uiState.monthlyOrders} orders",
                icon = Icons.Outlined.CalendarMonth
            )
        }

        // Recent Transactions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ReceiptLong,
                    contentDescription = "Transactions",
                    tint = ArtisanOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
            }
        }

        if (uiState.recentTransactions.isEmpty()) {
            item { EmptyTransactionsState() }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ArtisanCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        uiState.recentTransactions.forEachIndexed { index, transaction ->
                            TransactionRow(transaction = transaction)
                            if (index < uiState.recentTransactions.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = ArtisanBackground,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun IncomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountBalanceWallet,
            contentDescription = "Income",
            tint = ArtisanOrange,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Income Tracker",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            Text(
                text = "Track your earnings over time",
                fontSize = 14.sp,
                color = ArtisanTextLight
            )
        }
    }
}

@Composable
fun IncomePeriodCard(
    title: String,
    amount: String,
    orders: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ArtisanOrangeLight.copy(alpha = 0.2f),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = ArtisanOrange,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = amount,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ArtisanTextLight
            )
            Text(
                text = orders,
                fontSize = 12.sp,
                color = ArtisanTextLight.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun IncomePeriodCardWide(
    title: String,
    amount: String,
    orders: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ArtisanOrangeLight.copy(alpha = 0.2f),
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = ArtisanOrange,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ArtisanTextLight
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = orders,
                    fontSize = 12.sp,
                    color = ArtisanTextLight.copy(alpha = 0.7f)
                )
            }
            Text(
                text = amount,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
        }
    }
}

@Composable
fun TransactionRow(transaction: RecentTransaction) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateStr = dateFormat.format(Date(transaction.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Transaction",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.productName,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${transaction.quantity} × ${transaction.category} · $dateStr",
                fontSize = 12.sp,
                color = ArtisanTextLight
            )
        }
        Text(
            text = "+₹${String.format("%,.0f", transaction.totalPrice)}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun EmptyTransactionsState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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
                imageVector = Icons.Outlined.ReceiptLong,
                contentDescription = "No transactions",
                tint = ArtisanOrangeLight.copy(alpha = 0.5f),
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No transactions yet",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Start billing to track your income",
                fontSize = 14.sp,
                color = ArtisanTextLight
            )
        }
    }
}
