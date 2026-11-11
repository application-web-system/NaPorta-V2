package com.example.naporta

import android.content.Context

object SessionManager {
    private const val PREFS = "naporta_session"
    private const val KEY_GUEST_TOKEN = "guest_token"
    private const val KEY_GUEST_ID = "guest_id"
    private const val KEY_GUEST_NAME = "guest_name"
    private const val KEY_ROOM_NUMBER = "room_number"
    private const val KEY_ADMIN_JWT = "admin_jwt"

    fun saveGuest(ctx: Context, token: String, id: String, name: String, room: String) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_GUEST_TOKEN, token)
            .putString(KEY_GUEST_ID, id)
            .putString(KEY_GUEST_NAME, name)
            .putString(KEY_ROOM_NUMBER, room)
            .apply()
    }

    fun saveAdminJwt(ctx: Context, jwt: String) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_ADMIN_JWT, jwt)
            .apply()
    }

    fun getGuestToken(ctx: Context) =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_GUEST_TOKEN, null)

    fun getGuestId(ctx: Context) =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_GUEST_ID, null)

    fun getGuestName(ctx: Context) =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_GUEST_NAME, null)

    fun getRoomNumber(ctx: Context) =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_ROOM_NUMBER, null)

    fun getAdminJwt(ctx: Context) =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_ADMIN_JWT, null)

    fun adminBearer(ctx: Context) = "Bearer ${getAdminJwt(ctx) ?: ""}"

    fun clearGuest(ctx: Context) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .remove(KEY_GUEST_TOKEN)
            .remove(KEY_GUEST_ID)
            .remove(KEY_GUEST_NAME)
            .remove(KEY_ROOM_NUMBER)
            .apply()
    }
}
