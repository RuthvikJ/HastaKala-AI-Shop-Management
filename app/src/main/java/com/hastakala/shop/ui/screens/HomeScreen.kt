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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Artisan Orange Theme Colors
val ArtisanOrange = Color(0xFFF26419)
val ArtisanOrangeLight = Color(0xFFF6A376)
val ArtisanBackground = Color(0xFFFFF8F4)
val ArtisanCard = Color(0xFFFFFFFF)
val ArtisanTextDark = Color(0xFF2F1D11)
val ArtisanTextLight = Color(0xFF7A5C4A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    showBottomBar: Boolean = true,
    onNavigateToBilling: (() -> Unit)? = null
) {
    Scaffold(
        bottomBar = { if (showBottomBar) HomeBottomNavigation() },
        containerColor = ArtisanBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // 1. Greeting Header
            item { GreetingHeader() }

            // 2. Revenue Summary Card
            item { RevenueSummaryCard() }

            // 3. Quick Bill Action
            item { QuickBillAction(onNavigateToBilling = onNavigateToBilling) }

            // 6. Analytics Overview Cards
            item { AnalyticsOverview() }

            // 4. Best-selling Product Card
            item { BestSellingProductCard() }

            // 5. Recent Sales List
            item { RecentSalesSection() }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun GreetingHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Namaste Artisan 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Let's grow your business today",
                fontSize = 16.sp,
                color = ArtisanTextLight
            )
        }
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ArtisanOrangeLight.copy(alpha = 0.2f),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = ArtisanOrange,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun RevenueSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Revenue",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Trending Up",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "₹ 45,250",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "+12% from last week",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun QuickBillAction(onNavigateToBilling: (() -> Unit)? = null) {
    Button(
        onClick = { onNavigateToBilling?.invoke() },
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ArtisanTextDark)
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = "Add Bill",
            modifier = Modifier.size(28.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Create Quick Bill",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun AnalyticsOverview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnalyticsCard(
            title = "Orders",
            value = "128",
            icon = Icons.Default.ShoppingCart,
            modifier = Modifier.weight(1f)
        )
        AnalyticsCard(
            title = "Customers",
            value = "84",
            icon = Icons.Default.Face,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AnalyticsCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ArtisanOrangeLight.copy(alpha = 0.2f),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = ArtisanOrange,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = ArtisanTextLight
            )
        }
    }
}

@Composable
fun BestSellingProductCard() {
    Column {
        Text(
            text = "Best Selling Product",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ArtisanTextDark,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ArtisanCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ArtisanOrangeLight.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Handwoven Basket",
                        tint = ArtisanOrange,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Terracotta Pot Set",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArtisanTextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "45 Units Sold",
                        fontSize = 14.sp,
                        color = ArtisanOrange,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "₹ 1,200",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
            }
        }
    }
}

data class SaleItem(val id: String, val itemName: String, val amount: String, val date: String)

@Composable
fun RecentSalesSection() {
    val mockSales = listOf(
        SaleItem("1", "Wooden Elephant", "₹ 850", "Today, 10:30 AM"),
        SaleItem("2", "Handwoven Shawl", "₹ 2,400", "Yesterday, 4:15 PM"),
        SaleItem("3", "Clay Lamps (Set of 5)", "₹ 350", "Yesterday, 1:00 PM")
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Sales",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
            TextButton(onClick = { }) {
                Text(text = "See All", color = ArtisanOrange)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ArtisanCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                mockSales.forEachIndexed { index, sale ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ArtisanBackground,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Sale",
                                tint = ArtisanOrange,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sale.itemName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ArtisanTextDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sale.date,
                                fontSize = 12.sp,
                                color = ArtisanTextLight
                            )
                        }
                        Text(
                            text = "+ ${sale.amount}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    if (index < mockSales.size - 1) {
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

@Composable
fun HomeBottomNavigation() {
    NavigationBar(
        containerColor = ArtisanCard,
        contentColor = ArtisanOrange,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ArtisanOrange,
                selectedTextColor = ArtisanOrange,
                indicatorColor = ArtisanOrangeLight.copy(alpha = 0.2f),
                unselectedIconColor = ArtisanTextLight,
                unselectedTextColor = ArtisanTextLight
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Products") },
            label = { Text("Products") },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = ArtisanTextLight,
                unselectedTextColor = ArtisanTextLight
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Sales") },
            label = { Text("Sales") },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = ArtisanTextLight,
                unselectedTextColor = ArtisanTextLight
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = ArtisanTextLight,
                unselectedTextColor = ArtisanTextLight
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
