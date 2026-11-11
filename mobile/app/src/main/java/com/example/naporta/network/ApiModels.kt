package com.example.naporta.network

// --- Guest ---
data class GuestResponse(
    val id: String,
    val name: String,
    val token: String,
    val active: Boolean,
    val room: RoomResponse
)

data class CreateGuestRequest(
    val name: String,
    val room_id: String
)

data class CreateGuestResponse(
    val id: String,
    val name: String,
    val token: String,
    val room_id: String,
    val active: Boolean,
    val checked_in: String
)

// --- Room ---
data class RoomResponse(
    val id: String,
    val number: String
)

data class CreateRoomRequest(val number: String)

// --- User ---
data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val created_at: String
)

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)

// --- MenuItem ---
data class MenuItemResponse(
    val id: String,
    val name: String,
    val description: String?,
    val price: String,
    val image_url: String?,
    val category: String,
    val available: Boolean
)

data class CreateMenuItemRequest(
    val name: String,
    val description: String,
    val price: Double,
    val image_url: String,
    val category: String
)

// --- Order ---
data class CreateOrderRequest(
    val guest_id: String,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val menu_item_id: String,
    val quantity: Int
)

// --- Auth ---
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)
