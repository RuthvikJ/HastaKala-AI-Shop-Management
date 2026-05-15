package com.hastakala.shop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.data.model.Product
import com.hastakala.shop.ui.screens.ArtisanCard
import com.hastakala.shop.ui.screens.ArtisanOrange
import com.hastakala.shop.ui.screens.ArtisanOrangeLight
import com.hastakala.shop.ui.screens.ArtisanTextDark
import com.hastakala.shop.ui.screens.ArtisanTextLight

private val StockGreen = Color(0xFF2E7D32)
private val StockGreenBg = Color(0xFFE8F5E9)
private val StockAmber = Color(0xFFE65100)
private val StockAmberBg = Color(0xFFFFF3E0)
private val StockRed = Color(0xFFC62828)
private val StockRedBg = Color(0xFFFFEBEE)

@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
            // Product icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(ArtisanOrangeLight.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Category,
                    contentDescription = product.name,
                    tint = ArtisanOrange,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Product info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.category,
                    fontSize = 13.sp,
                    color = ArtisanTextLight
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${String.format("%,.0f", product.price)}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArtisanTextDark
                    )
                    StockBadge(stock = product.stock)
                }
            }

            if (trailing != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailing()
            }
        }
    }
}

@Composable
fun StockBadge(stock: Int) {
    val (bgColor, textColor, label) = when {
        stock == 0 -> Triple(StockRedBg, StockRed, "Out of stock")
        stock <= 5 -> Triple(StockAmberBg, StockAmber, "$stock left")
        else -> Triple(StockGreenBg, StockGreen, "$stock in stock")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
