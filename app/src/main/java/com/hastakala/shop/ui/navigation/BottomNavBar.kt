package com.hastakala.shop.ui.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hastakala.shop.ui.screens.ArtisanCard
import com.hastakala.shop.ui.screens.ArtisanOrange
import com.hastakala.shop.ui.screens.ArtisanOrangeLight
import com.hastakala.shop.ui.screens.ArtisanTextLight

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        BottomNavItem(Screen.Home, Icons.Filled.Home),
        BottomNavItem(Screen.Billing, Icons.Filled.ShoppingCart),
        BottomNavItem(Screen.Dashboard, Icons.Outlined.Assessment),
        BottomNavItem(Screen.Stock, Icons.Outlined.Inventory2),
        BottomNavItem(Screen.Income, Icons.Outlined.AccountBalanceWallet)
    )

    NavigationBar(
        containerColor = ArtisanCard,
        contentColor = ArtisanOrange,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.screen.label
                    )
                },
                label = { Text(item.screen.label) },
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ArtisanOrange,
                    selectedTextColor = ArtisanOrange,
                    indicatorColor = ArtisanOrangeLight.copy(alpha = 0.2f),
                    unselectedIconColor = ArtisanTextLight,
                    unselectedTextColor = ArtisanTextLight
                )
            )
        }
    }
}
