package com.hastakala.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hastakala.shop.ui.navigation.AppNavigation
import com.hastakala.shop.ui.theme.HastaKalaShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HastaKalaShopTheme {
                AppNavigation()
            }
        }
    }
}