package com.hastakala.shop.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.screens.ArtisanCard
import com.hastakala.shop.ui.screens.ArtisanOrange
import com.hastakala.shop.ui.screens.ArtisanTextDark
import com.hastakala.shop.ui.screens.ArtisanTextLight

@Composable
fun RevenueCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector = Icons.Default.TrendingUp,
    isPrimary: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) ArtisanOrange else ArtisanCard
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPrimary) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isPrimary) Color.White.copy(alpha = 0.85f) else ArtisanTextLight
                )
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isPrimary) Color.White.copy(alpha = 0.8f) else ArtisanOrange,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = if (isPrimary) 36.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPrimary) Color.White else ArtisanTextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = if (isPrimary) Color.White.copy(alpha = 0.75f) else ArtisanTextLight
            )
        }
    }
}

@Composable
fun RevenueCardRow(
    card1Title: String,
    card1Value: String,
    card1Subtitle: String,
    card1Icon: ImageVector,
    card2Title: String,
    card2Value: String,
    card2Subtitle: String,
    card2Icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        RevenueCard(
            title = card1Title,
            value = card1Value,
            subtitle = card1Subtitle,
            icon = card1Icon,
            modifier = Modifier.weight(1f)
        )
        RevenueCard(
            title = card2Title,
            value = card2Value,
            subtitle = card2Subtitle,
            icon = card2Icon,
            modifier = Modifier.weight(1f)
        )
    }
}
