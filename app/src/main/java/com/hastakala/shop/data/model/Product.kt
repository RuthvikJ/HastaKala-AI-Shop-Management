package com.hastakala.shop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val stock: Int,
    val price: Double,
    val artisanName: String,
    val description: String
)
