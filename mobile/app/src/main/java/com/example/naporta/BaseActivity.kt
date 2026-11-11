package com.example.naporta

import android.content.Intent
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList

open class BaseActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        setupHeader()
        setupFooter()
    }

    private fun setupHeader() {
        val header = findViewById<android.view.View>(R.id.includeHeader) ?: return
        val room = SessionManager.getRoomNumber(this)
        header.findViewById<android.widget.TextView>(R.id.tvHeaderRoomNumber)?.text = room ?: ""
        header.findViewById<android.view.View>(R.id.headerLogo)?.setOnClickListener {
            if (this !is MainActivity) {
                startActivity(android.content.Intent(this, MainActivity::class.java)
                    .addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
    }

    private fun setupFooter() {
        findViewById<LinearLayout>(R.id.btnMenu)?.setOnClickListener {
            if (this !is MenuActivity) startActivity(Intent(this, MenuActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.btnOrders)?.setOnClickListener {
            if (this !is MyOrdersActivity) startActivity(Intent(this, MyOrdersActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.btnCarrinho)?.setOnClickListener {
            if (this !is CartActivity) startActivity(Intent(this, CartActivity::class.java))
        }

        val activeTab = when (this) {
            is MenuActivity -> "menu"
            is MyOrdersActivity -> "orders"
            is CartActivity -> "carrinho"
            else -> null
        }
        if (activeTab != null) activateTab(activeTab)
    }

    private fun activateTab(tab: String) {
        val blue = Color.parseColor("#2F6BFF")
        val gray = Color.parseColor("#737B89")
        val grayText = Color.parseColor("#687180")

        val tabs = listOf("menu", "orders", "carrinho")
        for (t in tabs) {
            val isActive = t == tab
            val color = if (isActive) blue else gray
            val textColor = if (isActive) blue else grayText

            val bgId = resources.getIdentifier("bg${t.replaceFirstChar { it.uppercase() }}", "id", packageName)
            val icId = resources.getIdentifier("ic${t.replaceFirstChar { it.uppercase() }}", "id", packageName)
            val lblId = resources.getIdentifier("lbl${t.replaceFirstChar { it.uppercase() }}", "id", packageName)

            findViewById<LinearLayout>(bgId)?.setBackgroundResource(
                if (isActive) R.drawable.bg_nav_active else android.R.color.transparent
            )
            findViewById<ImageView>(icId)?.let {
                ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(color))
            }
            findViewById<TextView>(lblId)?.apply {
                setTextColor(textColor)
                paint.isFakeBoldText = isActive
            }
        }
    }
}
