package com.example.naporta

data class CartItem(
    val productId: Long,       // hash do UUID, usado como chave local
    val menuItemId: String,    // UUID real do MenuItem para enviar à API
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val quantity: Int = 1
) {
    val lineTotal: Double get() = price * quantity
}
