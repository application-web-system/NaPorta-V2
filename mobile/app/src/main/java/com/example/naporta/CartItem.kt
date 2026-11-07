package com.example.naporta

data class CartItem(
    val productId: Long,
    val name: String,
    val unitPrice: Double,
    val quantity: Int = 1,
    val imageRes: Int? = null
) {
    val lineTotal: Double get() = unitPrice * quantity
}
