package com.example.myapplication.model

import com.example.myapplication.R

//defining Inventory data types for stock
data class InventoryItem(
    val name: String,
    val have: Int,
    val need: Int
)

//attaching icons to navigation bar options
enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.ic_home),
    INVENTORY("Inventory", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}
