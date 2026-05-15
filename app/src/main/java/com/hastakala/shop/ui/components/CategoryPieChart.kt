package com.hastakala.shop.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.screens.ArtisanCard
import com.hastakala.shop.ui.screens.ArtisanTextDark
import com.hastakala.shop.ui.screens.ArtisanTextLight

data class CategoryShare(
    val category: String,
    val revenue: Double,
    val percentage: Float
)

private val categoryColors = listOf(
    Color(0xFFF26419),
    Color(0xFF2E7D32),
    Color(0xFF1565C0),
    Color(0xFF6A1B9A),
    Color(0xFFC62828),
    Color(0xFFEF6C00),
    Color(0xFF00838F),
    Color(0xFFAD1457)
)

@Composable
fun CategoryRevenueSection(
    categories: List<CategoryShare>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Top Categories",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
                Text(
                    text = "${categories.size} categories",
                    fontSize = 13.sp,
                    color = ArtisanTextLight
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            categories.forEachIndexed { index, category ->
                CategoryRevenueRow(
                    category = category,
                    color = categoryColors[index % categoryColors.size],
                    animDelay = index * 80
                )
                if (index < categories.size - 1) {
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun CategoryRevenueRow(
    category: CategoryShare,
    color: Color,
    animDelay: Int
) {
    var triggered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { triggered = true }

    val animatedProgress by animateFloatAsState(
        targetValue = if (triggered) (category.percentage / 100f).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 700, delayMillis = animDelay),
        label = "progress"
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = category.category,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ArtisanTextDark
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₹${String.format("%,.0f", category.revenue)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanTextDark
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${String.format("%.0f", category.percentage)}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = color
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round
        )
    }
}
