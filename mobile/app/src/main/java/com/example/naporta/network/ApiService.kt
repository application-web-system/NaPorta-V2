package com.example.naporta.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("sessions")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    // Guest
    @GET("guests/{token}")
    suspend fun getGuestByToken(@Path("token") token: String): Response<GuestResponse>

    @POST("guests")
    suspend fun createGuest(
        @Header("Authorization") bearer: String,
        @Body body: CreateGuestRequest
    ): Response<CreateGuestResponse>

    // Users
    @GET("users")
    suspend fun getUsers(
        @Header("Authorization") bearer: String
    ): Response<List<UserResponse>>

    @POST("users")
    suspend fun createUser(
        @Header("Authorization") bearer: String,
        @Body body: CreateUserRequest
    ): Response<UserResponse>

    // Rooms
    @POST("rooms")
    suspend fun createRoom(
        @Header("Authorization") bearer: String,
        @Body body: CreateRoomRequest
    ): Response<RoomResponse>

    // Menu (guest - só disponíveis, sem auth)
    @GET("menu")
    suspend fun getMenuGuest(): Response<List<MenuItemResponse>>

    // Menu (admin - todos)
    @GET("menu/all")
    suspend fun getMenuItems(
        @Header("Authorization") bearer: String
    ): Response<List<MenuItemResponse>>

    @POST("menu")
    suspend fun createMenuItem(
        @Header("Authorization") bearer: String,
        @Body body: CreateMenuItemRequest
    ): Response<MenuItemResponse>

    // Orders
    @POST("orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): Response<Unit>
}
