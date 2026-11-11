package com.example.naporta

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList
import android.os.Bundle

open class BaseAdminActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        setupAdminHeader()
        setupSidebar()
    }

    private fun setupAdminHeader() {
        val header = findViewById<android.view.View>(R.id.includeHeader) ?: return

        // Decodifica nome do JWT (payload base64) ou usa fallback
        val jwt = SessionManager.getAdminJwt(this)
        if (jwt != null) {
            try {
                val payload = jwt.split(".")[1]
                val decoded = String(android.util.Base64.decode(payload, android.util.Base64.URL_SAFE))
                val name = org.json.JSONObject(decoded).optString("name", "Admin")
                header.findViewById<TextView>(R.id.tvAdminName)?.text = name
            } catch (_: Exception) {
                header.findViewById<TextView>(R.id.tvAdminName)?.text = "Admin"
            }
        }

        header.findViewById<android.view.View>(R.id.headerLogo)?.setOnClickListener {
            if (this !is AdminDashboardActivity) {
                startActivity(Intent(this, AdminDashboardActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        header.findViewById<android.view.View>(R.id.btnHamburger)?.setOnClickListener { view ->
            val popup = android.widget.PopupMenu(this, view)
            popup.menu.add(0, R.id.itemDashboard, 0, "Dashboard")
            popup.menu.add(0, R.id.itemPedidos, 1, "Pedidos")
            popup.menu.add(0, R.id.itemUsuarios, 2, "Usuários")
            popup.menu.add(0, R.id.itemCardapio, 3, "Cardápio")
            popup.menu.add(0, R.id.itemCategorias, 4, "Categorias")
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.itemDashboard -> { if (this !is AdminDashboardActivity) startActivity(Intent(this, AdminDashboardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); true }
                    R.id.itemPedidos -> { if (this !is AdminOrdersActivity) startActivity(Intent(this, AdminOrdersActivity::class.java)); true }
                    R.id.itemUsuarios -> { if (this !is AdminUsersActivity) startActivity(Intent(this, AdminUsersActivity::class.java)); true }
                    R.id.itemCardapio -> { if (this !is AdminMenuActivity) startActivity(Intent(this, AdminMenuActivity::class.java)); true }
                    R.id.itemCategorias -> { if (this !is AdminCategoriesActivity) startActivity(Intent(this, AdminCategoriesActivity::class.java)); true }
                    else -> false
                }
            }
            popup.show()
        }
    }

    private fun setupSidebar() {
        findViewById<LinearLayout>(R.id.itemDashboard)?.setOnClickListener {
            if (this !is AdminDashboardActivity) startActivity(Intent(this, AdminDashboardActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.itemPedidos)?.setOnClickListener {
            if (this !is AdminOrdersActivity) startActivity(Intent(this, AdminOrdersActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.itemUsuarios)?.setOnClickListener {
            if (this !is AdminUsersActivity) startActivity(Intent(this, AdminUsersActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.itemCardapio)?.setOnClickListener {
            if (this !is AdminMenuActivity) startActivity(Intent(this, AdminMenuActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.itemCategorias)?.setOnClickListener {
            if (this !is AdminCategoriesActivity) startActivity(Intent(this, AdminCategoriesActivity::class.java))
        }

        val activeItem = when (this) {
            is AdminDashboardActivity -> R.id.itemDashboard
            is AdminOrdersActivity -> R.id.itemPedidos
            is AdminUsersActivity -> R.id.itemUsuarios
            is AdminMenuActivity -> R.id.itemCardapio
            is AdminCategoriesActivity -> R.id.itemCategorias
            else -> null
        }
        activeItem?.let { activateSidebarItem(it) }
    }

    private fun activateSidebarItem(activeId: Int) {
        val blue = Color.parseColor("#2F6BFF")
        val gray = Color.parseColor("#7C8492")
        val textDark = Color.parseColor("#172033")

        val items = listOf(R.id.itemDashboard, R.id.itemPedidos, R.id.itemUsuarios, R.id.itemCardapio, R.id.itemCategorias)
        for (id in items) {
            val item = findViewById<LinearLayout>(id) ?: continue
            val isActive = id == activeId
            val color = if (isActive) blue else gray

            item.setBackgroundResource(if (isActive) R.drawable.bg_menu_item_active else android.R.color.transparent)

            val icon = item.getChildAt(0) as? ImageView
            val label = item.getChildAt(1) as? TextView
            val arrow = item.getChildAt(2) as? ImageView

            icon?.let { ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(color)) }
            label?.apply {
                setTextColor(if (isActive) blue else textDark)
                setTypeface(null, if (isActive) Typeface.BOLD else Typeface.NORMAL)
            }
            arrow?.let { ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(color)) }
        }
    }
}
