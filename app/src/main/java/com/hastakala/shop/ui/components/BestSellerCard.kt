package com.hastakala.shop.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.screens.ArtisanCard
import com.hastakala.shop.ui.screens.ArtisanOrange
import com.hastakala.shop.ui.screens.ArtisanOrangeLight
import com.hastakala.shop.ui.screens.ArtisanTextDark
import com.hastakala.shop.ui.screens.ArtisanTextLight

@Composable
fun BestSellerCard(
    productName: String,
    quantitySold: Int,
    revenue: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ArtisanOrangeLight.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Best Seller",
                    tint = ArtisanOrange,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Best Seller",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ArtisanOrange
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = productName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$quantitySold units sold",
                    fontSize = 13.sp,
                    color = ArtisanTextLight
                )
            }
            Text(
                text = "₹${String.format("%,.0f", revenue)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisanTextDark
            )
        }
    }
}
