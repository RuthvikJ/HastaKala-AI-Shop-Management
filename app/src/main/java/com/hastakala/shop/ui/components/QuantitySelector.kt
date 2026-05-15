package com.hastakala.shop.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hastakala.shop.ui.screens.ArtisanOrange
import com.hastakala.shop.ui.screens.ArtisanOrangeLight
import com.hastakala.shop.ui.screens.ArtisanTextDark

@Composable
fun QuantitySelector(
    quantity: Int,
    maxQuantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledIconButton(
            onClick = onDecrement,
            enabled = quantity > 1,
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = ArtisanOrangeLight.copy(alpha = 0.25f),
                contentColor = ArtisanOrange,
                disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
                disabledContentColor = Color.Gray.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease quantity",
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = quantity.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ArtisanTextDark
        )

        FilledIconButton(
            onClick = onIncrement,
            enabled = quantity < maxQuantity,
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = ArtisanOrangeLight.copy(alpha = 0.25f),
                contentColor = ArtisanOrange,
                disabledContainerColor = Color.Gray.copy(alpha = 0.1f),
                disabledContentColor = Color.Gray.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase quantity",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
