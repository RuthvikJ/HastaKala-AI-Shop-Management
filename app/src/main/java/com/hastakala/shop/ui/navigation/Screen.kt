package com.hastakala.shop.ui.navigation

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Billing : Screen("billing", "Billing")
    data object Dashboard : Screen("dashboard", "Dashboard")
    data object Stock : Screen("stock", "Stock")
    data object Income : Screen("income", "Income")
}
