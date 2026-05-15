package com.hastakala.shop.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hastakala.shop.HastaKalaApplication
import com.hastakala.shop.ui.screens.ArtisanBackground
import com.hastakala.shop.ui.screens.ArtisanTextDark
import com.hastakala.shop.ui.screens.BillingScreen
import com.hastakala.shop.ui.screens.DashboardScreen
import com.hastakala.shop.ui.screens.HomeScreen
import com.hastakala.shop.ui.screens.IncomeScreen
import com.hastakala.shop.ui.screens.InventoryScreen
import com.hastakala.shop.ui.viewmodel.BillingViewModel
import com.hastakala.shop.ui.viewmodel.DashboardViewModel
import com.hastakala.shop.ui.viewmodel.IncomeViewModel
import com.hastakala.shop.ui.viewmodel.InventoryViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val context = LocalContext.current
    val application = context.applicationContext as HastaKalaApplication
    val repository = application.repository

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor = ArtisanBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    showBottomBar = false,
                    onNavigateToBilling = {
                        navController.navigate(Screen.Billing.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(Screen.Billing.route) {
                val billingViewModel: BillingViewModel = viewModel(
                    factory = BillingViewModel.Factory(repository)
                )
                BillingScreen(viewModel = billingViewModel)
            }
            composable(Screen.Dashboard.route) {
                val dashboardViewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModel.Factory(repository)
                )
                DashboardScreen(viewModel = dashboardViewModel)
            }
            composable(Screen.Stock.route) {
                val inventoryViewModel: InventoryViewModel = viewModel(
                    factory = InventoryViewModel.Factory(repository)
                )
                InventoryScreen(viewModel = inventoryViewModel)
            }
            composable(Screen.Income.route) {
                val incomeViewModel: IncomeViewModel = viewModel(
                    factory = IncomeViewModel.Factory(repository)
                )
                IncomeScreen(viewModel = incomeViewModel)
            }
        }
    }
}
